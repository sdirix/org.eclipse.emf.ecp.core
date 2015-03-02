/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.editsupport;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.emfforms.spi.core.services.emfspecificservice.EMFSpecificService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport;

/**
 * EMF implementation of {@link EMFFormsEditSupport}.
 *
 * @author Lucas Koehler
 *
 */
public class EMFFormsEditSupportImpl implements EMFFormsEditSupport {
	private EMFFormsDatabinding emfFormsDatabinding;
	private EMFSpecificService emfSpecificService;
	private ReportService reportService;

	/**
	 * Sets the {@link EMFFormsDatabinding} service.
	 *
	 * @param emfFormsDatabinding The databinding service.
	 */
	protected void setEMFFormsDatabinding(EMFFormsDatabinding emfFormsDatabinding) {
		this.emfFormsDatabinding = emfFormsDatabinding;
	}

	/**
	 * Unsets the {@link EMFFormsDatabinding} service.
	 *
	 * @param emfFormsDatabinding The databinding service.
	 */
	protected void unsetEMFFormsDatabinding(EMFFormsDatabinding emfFormsDatabinding) {
		this.emfFormsDatabinding = null;
	}

	/**
	 * Sets the {@link EMFSpecificService}.
	 *
	 * @param emfSpecificService The {@link EMFSpecificService}
	 */
	protected void setEMFSpecificService(EMFSpecificService emfSpecificService) {
		this.emfSpecificService = emfSpecificService;
	}

	/**
	 * Unsets the {@link EMFSpecificService}.
	 *
	 * @param emfSpecificService The {@link EMFSpecificService}
	 */
	protected void unsetEMFSpecificService(EMFSpecificService emfSpecificService) {
		this.emfSpecificService = null;
	}

	/**
	 * Sets the {@link ReportService}.
	 *
	 * @param reportService The {@link ReportService}
	 */
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Unsets the {@link ReportService}.
	 *
	 * @param reportService The {@link ReportService}
	 */
	protected void unsetReportService(ReportService reportService) {
		this.reportService = null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport#isMultiLine(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean isMultiLine(VDomainModelReference domainModelReference, EObject rootObject) {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return false;
		}
		final IObserving observing = (IObserving) observableValue;
		final EObject value = (EObject) observing.getObserved();
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final IItemPropertyDescriptor itemPropertyDescriptor = emfSpecificService.getAdapterFactoryItemDelegator()
			.getPropertyDescriptor(value, structuralFeature);

		return itemPropertyDescriptor.isMultiLine(value);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport#canSetProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean canSetProperty(VDomainModelReference domainModelReference, EObject rootObject) {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return false;
		}
		final IObserving observing = (IObserving) observableValue;
		final EObject value = (EObject) observing.getObserved();
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final IItemPropertyDescriptor itemPropertyDescriptor = emfSpecificService.getAdapterFactoryItemDelegator()
			.getPropertyDescriptor(value, structuralFeature);

		return itemPropertyDescriptor.canSetProperty(value);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport#getText(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	public String getText(VDomainModelReference domainModelReference, EObject rootObject, Object element) {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();
		Assert.create(element).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return ex.getMessage();
		}
		final IObserving observing = (IObserving) observableValue;
		final EObject value = (EObject) observing.getObserved();
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final IItemPropertyDescriptor itemPropertyDescriptor = emfSpecificService.getAdapterFactoryItemDelegator()
			.getPropertyDescriptor(value, structuralFeature);
		final IItemLabelProvider labelProvider = itemPropertyDescriptor.getLabelProvider(rootObject);

		return labelProvider.getText(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport#getImage(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	public Object getImage(VDomainModelReference domainModelReference, EObject rootObject, Object element) {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();
		Assert.create(element).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return ex.getMessage();
		}
		final IObserving observing = (IObserving) observableValue;
		final EObject value = (EObject) observing.getObserved();
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final IItemPropertyDescriptor itemPropertyDescriptor = emfSpecificService.getAdapterFactoryItemDelegator()
			.getPropertyDescriptor(value, structuralFeature);
		final IItemLabelProvider labelProvider = itemPropertyDescriptor.getLabelProvider(rootObject);

		return labelProvider.getImage(element);
	}
}