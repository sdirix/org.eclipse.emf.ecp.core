/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.mappingprovider.table;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.spi.UniqueSetting;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelPropertiesHelper;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.emf.EMFFormsDatabindingEMF;
import org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProvider;
import org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProviderManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * An {@link EMFFormsMappingProvider} implementation for {@link VTableControl VTableControls}.
 *
 * @author Lucas Koehler
 *
 */
@Component(name = "EMFFormsMappingProviderTable")
public class EMFFormsMappingProviderTable implements EMFFormsMappingProvider {

	private EMFFormsDatabindingEMF emfFormsDatabinding;
	private ReportService reportService;

	/**
	 * Sets the {@link EMFFormsDatabindingEMF} service.
	 *
	 * @param emfFormsDatabinding The databinding service
	 */
	@Reference
	protected void setEMFFormsDatabinding(EMFFormsDatabindingEMF emfFormsDatabinding) {
		this.emfFormsDatabinding = emfFormsDatabinding;
	}

	/**
	 * Sets the {@link ReportService}.
	 *
	 * @param reportService The {@link ReportService}
	 */
	@Reference
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProvider#getMappingFor(org.eclipse.emf.ecp.view.spi.model.VControl,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<UniqueSetting> getMappingFor(VControl vControl, EObject domainObject) {
		Assert.create(vControl).notNull();
		Assert.create(domainObject).notNull();

		final VTableControl tableControl = (VTableControl) vControl;
		final VTableDomainModelReference tableDMR = (VTableDomainModelReference) tableControl.getDomainModelReference();
		Setting tableSetting;
		try {
			tableSetting = emfFormsDatabinding.getSetting(tableControl.getDomainModelReference(), domainObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return Collections.<UniqueSetting> emptySet();
		}

		final Set<UniqueSetting> settingsMap = new LinkedHashSet<UniqueSetting>();
		settingsMap.add(UniqueSetting.createSetting(tableSetting));
		final Bundle bundle = FrameworkUtil.getBundle(getClass());
		ServiceReference<EMFFormsMappingProviderManager> serviceReference = null;
		EMFFormsMappingProviderManager manager = null;
		BundleContext bundleContext = null;
		if (bundle != null) {
			bundleContext = bundle.getBundleContext();
			serviceReference = bundleContext.getServiceReference(EMFFormsMappingProviderManager.class);
			manager = bundleContext.getService(serviceReference);
		}

		for (final EObject eObject : (List<EObject>) tableSetting.get(true)) {
			if (tableControl.getDetailEditing() == DetailEditing.NONE) {
				for (final VDomainModelReference domainModelReference : tableDMR.getColumnDomainModelReferences()) {
					try {
						final Setting columnSetting = emfFormsDatabinding.getSetting(domainModelReference, eObject);
						settingsMap.add(UniqueSetting.createSetting(columnSetting));
					} catch (final DatabindingFailedException ex) {
						reportService.report(new DatabindingFailedReport(ex));
					}
				}
			} else if (manager != null) {
				// //getView for inner Eobject and get the Settings for all inner controls
				try {
					final VView view = getView(tableControl, eObject);
					final TreeIterator<EObject> eAllContents = view.eAllContents();
					while (eAllContents.hasNext()) {
						final EObject content = eAllContents.next();
						if (VControl.class.isInstance(content)) {
							final Set<UniqueSetting> settingsForInnerControl = manager
								.getAllSettingsFor(VControl.class.cast(content), eObject);
							settingsMap.addAll(settingsForInnerControl);
						}
					}
				} catch (final DatabindingFailedException ex) {
					reportService.report(new DatabindingFailedReport(ex));
				}
			}
		}
		if (bundle != null) {
			bundleContext.ungetService(serviceReference);
		}
		return settingsMap;
	}

	private VView getView(VTableControl tableControl, EObject eObject) throws DatabindingFailedException {
		VView detailView = tableControl.getDetailView();
		if (detailView == null) {
			final VElement viewModel = getViewModel(tableControl);
			final VViewModelProperties properties = ViewModelPropertiesHelper.getInhertitedPropertiesOrEmpty(viewModel);
			detailView = ViewProviderHelper.getView(eObject, properties);
		}
		return detailView;
	}

	private VElement getViewModel(VTableControl tableControl) {
		EObject container = tableControl;
		if (container.eContainer() != null) {
			container = container.eContainer();
		}
		return (VElement) container;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProvider#isApplicable(org.eclipse.emf.ecp.view.spi.model.VControl,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public double isApplicable(VControl vControl, EObject domainObject) {
		if (vControl == null) {
			reportService.report(new AbstractReport("Warning: The given VControl was null.")); //$NON-NLS-1$
			return NOT_APPLICABLE;
		}
		if (domainObject == null) {
			reportService.report(new AbstractReport("Warning: The given domain object was null.")); //$NON-NLS-1$
			return NOT_APPLICABLE;
		}

		if (VTableControl.class.isInstance(vControl)
			&& VTableDomainModelReference.class.isInstance(vControl.getDomainModelReference())) {
			return 5d;
		}

		return NOT_APPLICABLE;
	}
}
