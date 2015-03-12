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

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.model.common.ECPStaticRendererTester;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.ECPAdditionalRendererTester;
import org.eclipse.emf.ecp.view.spi.swt.reporting.ECPRendererDescriptionInitFailedReport;
import org.eclipse.emfforms.spi.swt.core.EMFFormsAdditionalRendererService;
import org.eclipse.emfforms.spi.swt.core.EMFFormsRendererService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Component to parse the extension points and populate the new EMFFormsRendererFactory.
 *
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings("rawtypes")
public class LegacyRendererContributionParser {
	private static final String TEST_DYNAMIC = "dynamicTest";//$NON-NLS-1$
	private static final String TEST_STATIC = "staticTest";//$NON-NLS-1$
	private static final String TESTER_PRIORITY = "priority";//$NON-NLS-1$
	private static final String TESTER_VELEMENT = "element"; //$NON-NLS-1$
	private static final String RENDERER_TESTER = "testClass"; //$NON-NLS-1$

	private static final String RENDER_EXTENSION = "org.eclipse.emf.ecp.ui.view.swt.renderers"; //$NON-NLS-1$
	private static final String ADDITIONAL_RENDER_EXTENSION = "org.eclipse.emf.ecp.ui.view.swt.additionalRenderers"; //$NON-NLS-1$

	private ReportService reportService;

	private final Set<ServiceRegistration<EMFFormsRendererService>> rendererServiceRegistrations = new LinkedHashSet<ServiceRegistration<EMFFormsRendererService>>();
	private final Set<ServiceRegistration<EMFFormsAdditionalRendererService>> additionalRendererServiceRegistrations = new LinkedHashSet<ServiceRegistration<EMFFormsAdditionalRendererService>>();

	/**
	 * Called by the initializer to set the {@link ReportService}.
	 *
	 * @param reportService The ReportService to set
	 */
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Called by the initializer to unset the {@link ReportService}.
	 *
	 * @param reportService The ReportService to unset
	 */
	protected void unsetReportService(ReportService reportService) {
		this.reportService = null;
	}

	/**
	 * Activate method of OSGI Component.
	 *
	 * @param bundleContext The {@link BundleContext} to use
	 */
	protected void activate(BundleContext bundleContext) {
		readRenderer(bundleContext);
		readAdditionalRenderer(bundleContext);
	}

	/**
	 * Deactivate method of OSGI Component.
	 */
	protected void deactivate() {
		for (final ServiceRegistration<EMFFormsRendererService> registration : rendererServiceRegistrations) {
			registration.unregister();
		}
		for (final ServiceRegistration<EMFFormsAdditionalRendererService> registration : additionalRendererServiceRegistrations) {
			registration.unregister();
		}
	}

	private void readRenderer(BundleContext bundleContext) {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
			.getExtensionPoint(RENDER_EXTENSION);
		for (final IExtension extension : extensionPoint.getExtensions()) {

			for (final IConfigurationElement configurationElement : extension
				.getConfigurationElements()) {
				try {
					final Class<AbstractSWTRenderer<VElement>> renderer = loadClass(configurationElement
						.getContributor().getName(), configurationElement
						.getAttribute("renderer")); //$NON-NLS-1$

					final Set<ECPRendererTester> tester = new LinkedHashSet<ECPRendererTester>();
					for (final IConfigurationElement testerExtension : configurationElement.getChildren()) {
						if (TEST_DYNAMIC.equals(testerExtension.getName())) {
							tester.add((ECPRendererTester) testerExtension.createExecutableExtension(RENDERER_TESTER));
						}
						else if (TEST_STATIC.equals(testerExtension.getName())) {

							final int priority = Integer.parseInt(testerExtension.getAttribute(TESTER_PRIORITY));

							final String vElement = testerExtension.getAttribute(TESTER_VELEMENT);
							final Class<? extends VElement> supportedEObject = loadClass(testerExtension
								.getContributor()
								.getName(), vElement);

							tester.add(new ECPStaticRendererTester(priority,
								supportedEObject));
						}
					}

					final LegacyRendererService rendererService = new LegacyRendererService(renderer, tester,
						reportService);
					final ServiceRegistration<EMFFormsRendererService> serviceRegistration = bundleContext
						.registerService(EMFFormsRendererService.class, rendererService, null);
					rendererServiceRegistrations.add(serviceRegistration);
				} catch (final CoreException ex) {
					report(new ECPRendererDescriptionInitFailedReport(ex));
				} catch (final ClassNotFoundException ex) {
					report(new ECPRendererDescriptionInitFailedReport(ex));
				} catch (final InvalidRegistryObjectException ex) {
					report(new ECPRendererDescriptionInitFailedReport(ex));
				}
			}
		}
	}

	private void readAdditionalRenderer(BundleContext bundleContext) {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
			.getExtensionPoint(ADDITIONAL_RENDER_EXTENSION);

		for (final IExtension extension : extensionPoint.getExtensions()) {

			for (final IConfigurationElement configurationElement : extension
				.getConfigurationElements()) {
				try {
					final Class<AbstractAdditionalSWTRenderer<VElement>> renderer = loadClass(configurationElement
						.getContributor().getName(), configurationElement
						.getAttribute("renderer")); //$NON-NLS-1$
					final ECPAdditionalRendererTester tester = (ECPAdditionalRendererTester) configurationElement
						.createExecutableExtension("tester"); //$NON-NLS-1$

					final LegacyAdditionalRendererService rendererService = new LegacyAdditionalRendererService(
						renderer, tester, reportService);
					final ServiceRegistration<EMFFormsAdditionalRendererService> serviceRegistration = bundleContext
						.registerService(EMFFormsAdditionalRendererService.class, rendererService, null);
					additionalRendererServiceRegistrations.add(serviceRegistration);
				} catch (final CoreException ex) {
					report(new ECPRendererDescriptionInitFailedReport(ex));
				} catch (final ClassNotFoundException e) {
					report(new ECPRendererDescriptionInitFailedReport(e));
				} catch (final InvalidRegistryObjectException e) {
					report(new ECPRendererDescriptionInitFailedReport(e));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz)
		throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + bundleName);
		}
		return (Class<T>) bundle.loadClass(clazz);
	}

	private void report(AbstractReport reportEntity) {
		reportService.report(reportEntity);
	}
}
