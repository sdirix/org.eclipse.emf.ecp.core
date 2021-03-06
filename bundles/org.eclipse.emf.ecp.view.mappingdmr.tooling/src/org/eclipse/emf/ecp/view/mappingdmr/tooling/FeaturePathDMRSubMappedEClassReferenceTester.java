/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.mappingdmr.tooling;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.mappingdmr.model.VMappingDomainModelReference;
import org.eclipse.emf.ecp.view.spi.mappingdmr.model.VMappingdmrPackage;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;

/**
 * A Tester for the FeaturePathControl which is added as a child of a {@link VMappingDomainModelReference}.
 *
 * @author Eugen Neufeld
 *
 */
public class FeaturePathDMRSubMappedEClassReferenceTester implements
	ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {

		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}

		final VControl control = (VControl) vElement;
		if (control.getDomainModelReference() == null) {
			return NOT_APPLICABLE;
		}
		IObservableValue observableValue;
		try {
			observableValue = Activator.getDefault().getEMFFormsDatabinding()
				.getObservableValue(control.getDomainModelReference(), viewModelContext.getDomainModel());
		} catch (final DatabindingFailedException ex) {
			Activator.getDefault().getReportService().report(new DatabindingFailedReport(ex));
			return NOT_APPLICABLE;
		}
		final EStructuralFeature feature = (EStructuralFeature) observableValue.getValueType();
		final EObject eObject = (EObject) ((IObserving) observableValue).getObserved();
		observableValue.dispose();

		if (VMappingDomainModelReference.class.isInstance(eObject.eContainer())
			&& VViewPackage.eINSTANCE
				.getFeaturePathDomainModelReference_DomainModelEFeature() == feature) {
			return 6;
		}
		if (VMappingDomainModelReference.class.isInstance(eObject)
			&& VMappingdmrPackage.eINSTANCE.getMappingDomainModelReference_DomainModelReference() == feature) {
			return 6;
		}

		return NOT_APPLICABLE;
	}
}
