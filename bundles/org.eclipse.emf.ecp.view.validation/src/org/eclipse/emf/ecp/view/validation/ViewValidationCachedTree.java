/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree;
import org.eclipse.emf.ecp.common.cachetree.CachedTreeNode;
import org.eclipse.emf.ecp.common.cachetree.IExcludedObjectsCallback;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.ViewFactory;

/**
 * {@link AbstractCachedTree} for view validation.
 * 
 * @author jfaltermeier
 * 
 */
public class ViewValidationCachedTree extends AbstractCachedTree<Diagnostic> {

	private final ValidationRegistry validationRegistry;
	private final ECPValidationPropagator propagator;

	/**
	 * Default constructor.
	 * 
	 * @param callback the {@link IExcludedObjectsCallback} to use when checking when to stop
	 * @param validationRegistry the validation registry for the view validation
	 */
	public ViewValidationCachedTree(IExcludedObjectsCallback callback, ValidationRegistry validationRegistry) {
		super(callback);
		this.validationRegistry = validationRegistry;
		propagator = getPropagator();
	}

	private ECPValidationPropagator getPropagator() {
		final IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.view.validation.propagator");
		try {
			for (final IConfigurationElement e : config) {
				final Object o =
					e.createExecutableExtension("class");
				if (o instanceof ECPValidationPropagator) {
					return (ECPValidationPropagator) o;
				}
			}
		} catch (final CoreException ex) {
			return null;
		}
		return new DefaultValidationPropagator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#getDefaultValue()
	 */
	@Override
	public Diagnostic getDefaultValue() {
		return Diagnostic.OK_INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#createdCachedTreeNode(java.lang.Object)
	 */
	@Override
	protected CachedTreeNode<Diagnostic> createdCachedTreeNode(Diagnostic value) {
		return new ViewValidationTreeNode(value);
	}

	/**
	 * Validate the given eObject.
	 * 
	 * @param eObject the eObject to validate
	 * @return the set of affected elements
	 */
	public Set<EObject> validate(EObject eObject) {
		return update(eObject, getDiagnosticForEObject(eObject));
	}

	/**
	 * Validates all given eObjects.
	 * 
	 * @param eObjects the eObjects to validate
	 * @return the set of affected elements
	 */
	public Set<EObject> validate(Collection<EObject> eObjects) {
		final Set<EObject> allAffected = new HashSet<EObject>();
		for (final EObject eObject : eObjects) {
			final Set<EObject> affected = validate(eObject);
			allAffected.addAll(affected);
		}
		return allAffected;
	}

	/**
	 * Computes the {@link Diagnostic} for the given eObject.
	 * 
	 * @param object the eObject to validate
	 * @return the diagnostic
	 */
	public Diagnostic getDiagnosticForEObject(EObject object) {
		EValidator validator = EValidator.Registry.INSTANCE.getEValidator(object.eClass().getEPackage());
		final BasicDiagnostic diagnostics = Diagnostician.INSTANCE.createDefaultDiagnostic(object);

		if (validator == null) {
			validator = new EObjectValidator();
		}
		final Map<Object, Object> context = new HashMap<Object, Object>();
		context.put(EValidator.SubstitutionLabelProvider.class, Diagnostician.INSTANCE);
		context.put(EValidator.class, validator);

		validator.validate(object, diagnostics, context);
		return diagnostics;
	}

	private void updateAssociatedRenderables(EObject object, Diagnostic value) {
		final List<Renderable> renderables = validationRegistry.getRenderablesForEObject(object);
		for (final Renderable renderable : renderables) {
			if (renderable instanceof AbstractControl) {
				final AbstractControl control = (AbstractControl) renderable;
				final VDiagnostic vDiagnostic = ViewFactory.eINSTANCE.createVDiagnostic();
				final List<EStructuralFeature> targetFeatures = control.getTargetFeatures();

				final List<EObject> associatedEObjects = validationRegistry.getEObjectsForControl(control);
				final Map<Object, CachedTreeNode<Diagnostic>> nodes = getNodes();

				for (final EObject o : associatedEObjects) {
					if (nodes.containsKey(o)) {
						final Diagnostic diagnostic = nodes.get(o).getDisplayValue();
						for (final Diagnostic d : extractRelevantDiagnostics(diagnostic, targetFeatures)) {
							vDiagnostic.getDiagnostics().add(d);
						}
					}
				}
				control.setDiagnostic(vDiagnostic);

			}
			// non controls
			else {
				if (propagator != null && propagator.canHandle(renderable)) {
					propagator.propagate(renderable);
				}
			}
		}
	}

	/**
	 * Checks if this diagnostic contains a validation result for an element in the given list.
	 * 
	 * @param diagnostic
	 * @param features
	 * @return
	 */
	private Collection<Diagnostic> extractRelevantDiagnostics(Diagnostic diagnostic,
		List<EStructuralFeature> features) {

		final Collection<Diagnostic> result = new ArrayList<Diagnostic>();

		if (diagnostic.getSeverity() == Diagnostic.OK) {
			result.add(diagnostic);
			return result;
		}

		for (final EStructuralFeature feature : features) {
			for (final Diagnostic childDiagnostic : diagnostic.getChildren()) {
				for (final Object o : childDiagnostic.getData()) {
					if (feature.getClass().isInstance(o)) {
						result.add(childDiagnostic);
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#updateNode(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateNodeObject(Object object) {
		updateAssociatedRenderables((EObject) object, getNodes().get(object).getDisplayValue());
	}

	/**
	 * Tree node that caches the diagnostics of its children.
	 */
	private class ViewValidationTreeNode extends CachedTreeNode<Diagnostic> {

		/**
		 * Constructor.
		 * 
		 * @param diagnostic
		 *            the initial diagnostic containing the severity and validation message
		 */
		public ViewValidationTreeNode(Diagnostic diagnostic) {
			super(diagnostic);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update() {
			final Collection<Diagnostic> severities = values();

			if (severities.size() > 0) {
				Diagnostic mostSevereDiagnostic = values().iterator().next();
				for (final Diagnostic diagnostic : severities) {
					if (diagnostic.getSeverity() > mostSevereDiagnostic.getSeverity()) {
						mostSevereDiagnostic = diagnostic;
					}
				}
				setChildValue(mostSevereDiagnostic);
				return;
			}
			setChildValue(getDefaultValue());
		}

		@Override
		public Diagnostic getDisplayValue() {
			if (getChildValue() == null) {
				return getOwnValue();
			}
			return getOwnValue().getSeverity() >= getChildValue().getSeverity() ? getOwnValue() : getChildValue();
		}
	}

}
