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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * {@link AbstractCachedTree} for view validation.
 * 
 * @author jfaltermeier
 * 
 */
@SuppressWarnings("restriction")
public class ViewValidationCachedTree extends AbstractCachedTree<Diagnostic> {
	// TODO restrict warnings -> x-friends?

	private final IExcludedObjectsCallback excludedCallback;
	private final ValidationRegistry validationRegistry;

	/**
	 * Default constructor.
	 * 
	 * @param callback the {@link IExcludedObjectsCallback} to use when checking when to stop
	 * @param validationRegistry the validation registry for the view validation
	 */
	public ViewValidationCachedTree(IExcludedObjectsCallback callback, ValidationRegistry validationRegistry) {
		super(callback);
		excludedCallback = callback;
		this.validationRegistry = validationRegistry;
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
	 * Updates the cached entry for the given {@link EObject} with the given value.<br/>
	 * If the cached entry does not yet exist, it will be created.
	 * 
	 * @param eObject
	 *            the {@link EObject}
	 * @param value
	 *            the value associated with the {@link EObject}
	 * @return set of affected eobjects
	 */
	@Override
	public Set<EObject> update(EObject eObject, Diagnostic value) {

		if (excludedCallback.isExcluded(eObject)) {
			return Collections.emptySet();
		}

		final List<EStructuralFeature> affectedFeatures = new ArrayList<EStructuralFeature>();

		if (!value.getChildren().isEmpty()) {
			final List<?> diagnosticData = value.getChildren().get(0).getData();
			for (int i = 1; i < diagnosticData.size(); i++) {
				affectedFeatures.add((EStructuralFeature) diagnosticData.get(i));
			}
		}

		final List<Renderable> renderables = validationRegistry.getRenderablesForEObject(eObject);
		for (final Renderable renderable : renderables) {
			if (renderable instanceof AbstractControl) {
				final AbstractControl control = (AbstractControl) renderable;
				for (final EStructuralFeature targetFeature : control.getTargetFeatures()) {
					if (affectedFeatures.contains(targetFeature)) {
						// needed?
						control.getDiagnostic().getDiagnostics().add(value);
					}
				}
			}
		}

		return super.update(eObject, value);
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
		 * Puts a value into the cache and updates the diagnostics in the view model.
		 * 
		 * @param key
		 *            the (child) object that contains the given value
		 * @param value
		 *            an additional value that will be considered for the computation of the
		 *            actual value that results to a {@link #update()} call
		 */
		@Override
		public void putIntoCache(Object key, Diagnostic value) {
			super.putIntoCache(key, value);

			// update renderables
			final Set<EStructuralFeature> affectedFeatures = new HashSet<EStructuralFeature>();
			for (final Diagnostic diagnostic : value.getChildren()) {
				for (final Object o : diagnostic.getData()) {
					if (o instanceof EStructuralFeature) {
						affectedFeatures.add((EStructuralFeature) o);
					}
				}
			}

			final List<Renderable> renderables = validationRegistry.getRenderablesForEObject((EObject) key);
			for (final Renderable renderable : renderables) {
				renderable.getDiagnostic().getDiagnostics().clear();
				if (renderable instanceof AbstractControl) {
					final AbstractControl control = (AbstractControl) renderable;
					for (final EStructuralFeature targetFeature : control.getTargetFeatures()) {
						if (affectedFeatures.contains(targetFeature)) {
							for (final Diagnostic childDiagnostic : value.getChildren()) {
								if (isFeatureAffected(childDiagnostic, targetFeature)) {
									control.getDiagnostic().getDiagnostics().add(childDiagnostic);
								}
							}
						}
					}
				} else {
					renderable.getDiagnostic().getDiagnostics().add(getDisplayValue());
				}
			}
		}

		private boolean isFeatureAffected(Diagnostic diagnostic, EStructuralFeature feature) {
			for (final Object o : diagnostic.getData()) {
				if (feature.getClass().isInstance(o)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Removes a (child) object from the cache and updates the diagnostics in the view model.
		 * 
		 * @param key
		 *            the object to be removed
		 */
		@Override
		public void removeFromCache(Object key) {
			super.removeFromCache(key);
			final List<Renderable> renderables = validationRegistry.getRenderablesForEObject((EObject) key);
			for (final Renderable renderable : renderables) {
				renderable.getDiagnostic().getDiagnostics().clear();
			}
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
			return getOwnValue().getSeverity() > getChildValue().getSeverity() ? getOwnValue() : getChildValue();
		}
	}

}
