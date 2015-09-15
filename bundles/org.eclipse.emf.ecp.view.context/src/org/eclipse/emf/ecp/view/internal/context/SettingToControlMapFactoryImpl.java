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
package org.eclipse.emf.ecp.view.internal.context;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecp.view.spi.context.SettingToControlMapper;
import org.eclipse.emf.ecp.view.spi.context.SettingToControlMapperFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Implementation of {@link SettingToControlMapperFactory} as an OSGI service.
 *
 * @author Lucas Koehler
 *
 */
@Component(name = "SettingToControlMapFactoryImpl")
public class SettingToControlMapFactoryImpl implements SettingToControlMapperFactory {

	private final Set<EMFFormsMappingProvider> mappingProviders = new LinkedHashSet<EMFFormsMappingProvider>();
	private ReportService reportService;

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
	 * Called by the framework to add an {@link EMFFormsMappingProvider}.
	 *
	 * @param emfFormsMappingProvider The {@link EMFFormsMappingProvider}
	 */

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	protected void addEMFFormsMappingProvider(EMFFormsMappingProvider emfFormsMappingProvider) {
		mappingProviders.add(emfFormsMappingProvider);
	}

	/**
	 * Called by the framework to remove an {@link EMFFormsMappingProvider}.
	 *
	 * @param emfFormsMappingProvider The {@link EMFFormsMappingProvider}
	 */
	protected void removeEMFFormsMappingProvider(EMFFormsMappingProvider emfFormsMappingProvider) {
		mappingProviders.remove(emfFormsMappingProvider);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.context.SettingToControlMapperFactory#createSettingToControlMapper(org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public SettingToControlMapper createSettingToControlMapper(ViewModelContext viewModelContext) {
		final SettingToControlMapper mapper = new SettingToControlMapperImpl(reportService, mappingProviders);
		mapper.instantiate(viewModelContext);
		return mapper;
	}

}
