/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.diffmerge.internal.context;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;

/**
 * Class to compare Controls for equality.
 *
 * @author Eugen Neufeld
 *
 */
public final class CompareControls {

	private CompareControls() {
	}

	/**
	 * Compares to {@link VControl VControls}. This is just a convenience method which delegates to
	 * {@link #areEqual(VDomainModelReference, VDomainModelReference)}.
	 *
	 * @param left the first VControl to compare
	 * @param leftDomainModel The domain model of the left VControl
	 * @param right the second VControl to compare
	 * @param rightDomainModel The domain model of the right VControl
	 * @return true if all values of the {@link VDomainModelReference VDomainModelReferences} are equal
	 */
	public static boolean areEqual(VControl left, EObject leftDomainModel, VControl right, EObject rightDomainModel) {
		return areEqual(left.getDomainModelReference(), leftDomainModel, right.getDomainModelReference(),
			rightDomainModel);
	}

	/**
	 * Compares to {@link VDomainModelReference VDomainModelReferences} by comparing all values.
	 *
	 * @param left the first {@link VDomainModelReference}
	 * @param leftDomainModel The domain model of the left VControl
	 * @param right the second {@link VDomainModelReference}
	 * @param rightDomainModel The domain model of the right VControl
	 * @return true if all values of the {@link VDomainModelReference VDomainModelReferences} are equal
	 */
	public static boolean areEqual(VDomainModelReference left, EObject leftDomainModel, VDomainModelReference right,
		EObject rightDomainModel) {
		IObservableValue leftObservableValue;
		IObservableValue rightObservableValue;
		try {
			leftObservableValue = Activator.getDefault().getEMFFormsDatabinding()
				.getObservableValue(left, leftDomainModel);
			rightObservableValue = Activator.getDefault().getEMFFormsDatabinding()
				.getObservableValue(right, rightDomainModel);
		} catch (final DatabindingFailedException ex) {
			Activator.getDefault().getReportService().report(new DatabindingFailedReport(ex));
			return false;
		}
		final EObject leftEObject = (EObject) ((IObserving) leftObservableValue).getObserved();
		final EStructuralFeature leftStructuralFeature = (EStructuralFeature) leftObservableValue.getValueType();
		final EObject rightEObject = (EObject) ((IObserving) rightObservableValue).getObserved();
		final EStructuralFeature rightStructuralFeature = (EStructuralFeature) rightObservableValue.getValueType();

		final Object leftValue = leftEObject.eGet(leftStructuralFeature, true);
		final Object rightValue = rightEObject.eGet(rightStructuralFeature, true);

		if (leftValue == null && rightValue == null) {
			return true;
		}
		// TODO handle EReference (single and many)
		if (EcorePackage.eINSTANCE.getEReference().isInstance(leftStructuralFeature)
			|| EcorePackage.eINSTANCE.getEReference().isInstance(rightStructuralFeature)) {
			return true;
		}

		if (leftValue == null && rightValue != null) {
			return false;
		}
		if (leftValue != null && rightValue == null) {
			return false;
		}
		if (leftValue.equals(rightValue)) {
			return true;
		}
		return false;
	}
}
