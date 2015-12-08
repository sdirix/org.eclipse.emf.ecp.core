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

import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicePolicy;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceScope;

/**
 * An {@link EMFFormsScopedServiceProvider} for {@link ViewModelService}.
 *
 * @param <T> The actual type of the {@link ViewModelService}
 * @author Eugen Neufeld
 *
 */
public class EMFFormsLegacyLocalServiceProvider<T extends ViewModelService>
	implements EMFFormsScopedServiceProvider<T> {

	private final Class<T> type;
	private final double priority;
	private final ReportService reportService;

	/**
	 * Default constructor used to create an {@link EMFFormsLegacyLocalServiceProvider}.
	 *
	 * @param type The type of the service to wrap
	 * @param priority The priority of the wrapped service
	 * @param reportService The {@link ReportService} to use for logging
	 */
	public EMFFormsLegacyLocalServiceProvider(Class<T> type, double priority, ReportService reportService) {
		this.type = type;
		this.priority = priority;
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
		return EMFFormsScopedServiceScope.LOCAL;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getPriority()
	 */
	@Override
	public double getPriority() {
		return priority;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#getType()
	 */
	@Override
	public Class<T> getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider#provideService()
	 */
	@Override
	public T provideService() {
		try {
			return type.newInstance();
		} catch (final InstantiationException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final IllegalAccessException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final IllegalArgumentException ex) {
			reportService.report(new AbstractReport(ex));
		} catch (final SecurityException ex) {
			reportService.report(new AbstractReport(ex));
		}
		return null;
	}

}
