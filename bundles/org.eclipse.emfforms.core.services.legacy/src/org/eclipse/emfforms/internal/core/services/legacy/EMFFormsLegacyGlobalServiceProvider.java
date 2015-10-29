/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.legacy;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.ecp.view.spi.context.GlobalViewModelService;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicePolicy;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceScope;

/**
 * An {@link EMFFormsScopedServiceProvider} for {@link GlobalViewModelService}.
 *
 * @author Eugen Neufeld
 *
 */
public class EMFFormsLegacyGlobalServiceProvider implements EMFFormsScopedServiceProvider {

	private final GlobalViewModelService globalViewModelService;
	private final ReportService reportService;

	/**
	 * Default constructor used to create an {@link EMFFormsLegacyGlobalServiceProvider}.
	 * 
	 * @param globalViewModelService The GlobalViewModelService to wrap
	 * @param reportService The {@link ReportService} to use for logging
	 */
	public EMFFormsLegacyGlobalServiceProvider(GlobalViewModelService globalViewModelService,
		ReportService reportService) {
		this.globalViewModelService = globalViewModelService;
		this.reportService = reportService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getPolicy()
	 */
	@Override
	public EMFFormsScopedServicePolicy getPolicy() {
		return EMFFormsScopedServicePolicy.IMMEDIATE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getScope()
	 */
	@Override
	public EMFFormsScopedServiceScope getScope() {
		return EMFFormsScopedServiceScope.GLOBAL;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getPriority()
	 */
	@Override
	public double getPriority() {
		return globalViewModelService.getPriority();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getType()
	 */
	@Override
	public Class<?> getType() {
		return globalViewModelService.getClass();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#provideService()
	 */
	@Override
	public Object provideService() {
		try {
			return globalViewModelService.getClass().getConstructor().newInstance();
		} catch (final InstantiationException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final IllegalAccessException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final IllegalArgumentException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final InvocationTargetException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final NoSuchMethodException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final SecurityException ex) {
			reportService.report(new AbstractReport(ex));
		}
		return null;
	}

}
