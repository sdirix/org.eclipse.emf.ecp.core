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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
public class ViewValidationCachedTree extends AbstractCachedTree<VDiagnostic> {

	private final ValidationRegistry validationRegistry;

	/**
	 * Default constructor.
	 * 
	 * @param callback the {@link IExcludedObjectsCallback} to use when checking when to stop
	 * @param validationRegistry the validation registry for the view validation
	 */
	public ViewValidationCachedTree(IExcludedObjectsCallback callback, ValidationRegistry validationRegistry) {
		super(callback);
		this.validationRegistry = validationRegistry;

		// readPropagators();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#getDefaultValue()
	 */
	@Override
	public VDiagnostic getDefaultValue() {
		final VDiagnostic result = ViewFactory.eINSTANCE.createVDiagnostic();
		result.getDiagnostics().add(Diagnostic.OK_INSTANCE);
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#createdCachedTreeNode(java.lang.Object)
	 */
	@Override
	protected CachedTreeNode<VDiagnostic> createdCachedTreeNode(VDiagnostic value) {
		return new ViewValidationTreeNode(value);
	}

	/**
	 * Validate the given eObject.
	 * 
	 * @param eObject the eObject to validate
	 */
	public void validate(EObject eObject) {
		final Diagnostic diagnostic = getDiagnosticForEObject(eObject);
		for (final AbstractControl control : validationRegistry.getRenderablesForEObject(eObject)) {
			if (diagnostic.getSeverity() == Diagnostic.OK) {
				update(control, getDefaultValue());
			} else {
				final VDiagnostic vDiagnostic = ViewFactory.eINSTANCE.createVDiagnostic();

				for (final Diagnostic childDiagnostic : diagnostic.getChildren()) {
					if (childDiagnostic.getData().size() != 2) {
						continue;
					}

					if (control.getTargetFeatures().contains(childDiagnostic.getData().get(1))) {
						vDiagnostic.getDiagnostics().add(childDiagnostic);
					}
				}
				update(control, vDiagnostic);
			}
		}
	}

	/**
	 * Validates all given eObjects.
	 * 
	 * @param eObjects the eObjects to validate
	 */
	public void validate(Collection<EObject> eObjects) {

		for (final EObject eObject : eObjects) {
			validate(eObject);
		}

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
		final Map<Object, Object> context = new LinkedHashMap<Object, Object>();
		context.put(EValidator.SubstitutionLabelProvider.class, Diagnostician.INSTANCE);
		context.put(EValidator.class, validator);

		validator.validate(object, diagnostics, context);

		return diagnostics;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.common.cachetree.AbstractCachedTree#updateNode(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateNodeObject(Object object) {
		final Renderable renderable = (Renderable) object;

		final VDiagnostic displayValue = getNodes().get(object).getDisplayValue();
		if (!VDiagnosticHelper.isEqual(renderable.getDiagnostic(), displayValue))
		{
			renderable.setDiagnostic(EcoreUtil.copy(displayValue));
		}
	}

	/**
	 * Tree node that caches the diagnostics of its children.
	 */
	private class ViewValidationTreeNode extends CachedTreeNode<VDiagnostic> {

		/**
		 * Constructor.
		 * 
		 * @param diagnostic
		 *            the initial diagnostic containing the severity and validation message
		 */
		public ViewValidationTreeNode(VDiagnostic diagnostic) {
			super(diagnostic);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update() {
			final Collection<VDiagnostic> severities = values();

			if (severities.size() > 0) {
				VDiagnostic mostSevereDiagnostic = values().iterator().next();
				for (final VDiagnostic diagnostic : severities) {
					if (diagnostic.getHighestSeverity() > mostSevereDiagnostic.getHighestSeverity()) {
						mostSevereDiagnostic = diagnostic;
					}
				}
				setChildValue(mostSevereDiagnostic);
				return;
			}
			setChildValue(getDefaultValue());
		}

		@Override
		public VDiagnostic getDisplayValue() {
			if (getChildValue() != null) {
				return getChildValue();
			}
			return getOwnValue();
		}
	}

}
