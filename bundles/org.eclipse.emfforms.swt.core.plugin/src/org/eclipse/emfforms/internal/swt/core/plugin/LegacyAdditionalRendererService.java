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
package org.eclipse.emfforms.internal.swt.core.plugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.ECPAdditionalRendererTester;
import org.eclipse.emf.ecp.view.spi.swt.reporting.RendererInitFailedReport;
import org.eclipse.emfforms.spi.swt.core.EMFFormsAdditionalRendererService;

/**
 * Renderer service which uses the extension point derivates.
 *
 * @author Eugen Neufeld
 *
 */
public class LegacyAdditionalRendererService implements EMFFormsAdditionalRendererService<VElement> {

	private final Class<AbstractAdditionalSWTRenderer<VElement>> renderer;
	private final ECPAdditionalRendererTester tester;
	private final ReportService reportService;

	/**
	 * Default constructor.
	 *
	 * @param renderer The AbstractAdditionalSWTRenderer class
	 * @param tester The ECPAdditionalRendererTester
	 * @param reportService The ReportService
	 */
	public LegacyAdditionalRendererService(Class<AbstractAdditionalSWTRenderer<VElement>> renderer,
		ECPAdditionalRendererTester tester, ReportService reportService) {
		this.renderer = renderer;
		this.tester = tester;
		this.reportService = reportService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.core.EMFFormsAdditionalRendererService#isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement)
	 */
	@Override
	public boolean isApplicable(VElement vElement) {
		return tester.isApplicable(vElement, null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.core.EMFFormsAdditionalRendererService#getRendererInstance(VElement,
	 *      ViewModelContext)
	 */
	@Override
	public AbstractAdditionalSWTRenderer<VElement> getRendererInstance(VElement vElement,
		ViewModelContext viewModelContext) {
		final AbstractAdditionalSWTRenderer<VElement> additionalRenderer = createRenderer(vElement, viewModelContext,
			reportService, renderer);
		additionalRenderer.init();
		return additionalRenderer;
	}

	private AbstractAdditionalSWTRenderer<VElement> createRenderer(VElement vElement, ViewModelContext viewContext,
		final ReportService reportService,
		final Class<? extends AbstractAdditionalSWTRenderer<VElement>> rendererClass) {
		try {
			return rendererClass
				.getConstructor(vElement.getClass().getInterfaces()[0], ViewModelContext.class)
				.newInstance(vElement, viewContext);
		} catch (final InstantiationException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		} catch (final IllegalAccessException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		} catch (final IllegalArgumentException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		} catch (final InvocationTargetException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		} catch (final NoSuchMethodException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		} catch (final SecurityException ex) {
			reportService.report(new RendererInitFailedReport(ex));
			throw new IllegalStateException(ex);
		}

	}
}
