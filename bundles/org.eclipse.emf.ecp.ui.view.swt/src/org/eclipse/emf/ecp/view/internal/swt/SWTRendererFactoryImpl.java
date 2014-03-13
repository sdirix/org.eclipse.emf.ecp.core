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
package org.eclipse.emf.ecp.view.internal.swt;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.ECPAdditionalRendererTester;
import org.eclipse.emf.ecp.view.spi.swt.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.osgi.framework.Bundle;

/**
 * @author Eugen
 * 
 */
public final class SWTRendererFactoryImpl implements SWTRendererFactory {

	private static final String TEST_DYNAMIC = "dynamicTest";//$NON-NLS-1$
	private static final String TEST_STATIC = "staticTest";//$NON-NLS-1$
	private static final String TESTER_PRIORITY = "priority";//$NON-NLS-1$
	private static final String TESTER_VELEMENT = "element"; //$NON-NLS-1$
	private static final String RENDERER_TESTER = "testClass"; //$NON-NLS-1$

	private static final String RENDER_EXTENSION = "org.eclipse.emf.ecp.ui.view.swt.renderers"; //$NON-NLS-1$
	private static final String ADDITIONAL_RENDER_EXTENSION = "org.eclipse.emf.ecp.ui.view.swt.additionalRenderers"; //$NON-NLS-1$
	// private final Map<Class<? extends VElement>, AbstractSWTRenderer<VElement>> rendererMapping = new
	// LinkedHashMap<Class<? extends VElement>, AbstractSWTRenderer<VElement>>();

	private final Set<ECPRendererDescription> rendererDescriptors = new LinkedHashSet<ECPRendererDescription>();
	private final Set<ECPAdditionalRendererDescription> additionalRendererDescriptors = new LinkedHashSet<ECPAdditionalRendererDescription>();

	/**
	 * Default constructor for the renderer factory.
	 */
	public SWTRendererFactoryImpl() {
		readRenderer();
		readAdditionalRenderer();
	}

	private void readRenderer() {
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

					rendererDescriptors.add(new ECPRendererDescription(renderer, tester));
				} catch (final CoreException ex) {
					ex.printStackTrace();
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
				} catch (final InvalidRegistryObjectException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void readAdditionalRenderer() {
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
					// final Set<ECPAdditionalRendererTester> tester = new LinkedHashSet<ECPAdditionalRendererTester>();
					// final IConfigurationElement testerExtension = configurationElement.getChildren(TEST_DYNAMIC)[0];
					// only dynamic tester allowed
					// tester
					// .add((ECPAdditionalRendererTester) testerExtension.createExecutableExtension(RENDERER_TESTER));

					additionalRendererDescriptors.add(new ECPAdditionalRendererDescription(renderer, tester));
				} catch (final CoreException ex) {
					ex.printStackTrace();
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
				} catch (final InvalidRegistryObjectException e) {
					e.printStackTrace();
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

	// /**
	// * Searches for a fitting renderer and then renders the passed {@link VElement}.
	// *
	// * @param parent the {@link Composite} to render on
	// * @param viewContext the {@link ViewModelContext} to use
	// * @param vElement the {@link VElement} to render
	// * @return the list for {@link RenderingResultRow} which can be empty if no fitting render could be found
	// * @throws NoPropertyDescriptorFoundExeption is thrown when the binded domain object does not have a property
	// * descriptor
	// * @throws NoRendererFoundException is thrown when no renderer could be found
	// */
	// public List<RenderingResultRow<Control>> render(Composite parent,
	// VElement vElement, ViewModelContext viewContext) throws NoRendererFoundException,
	// NoPropertyDescriptorFoundExeption {
	//
	// final AbstractSWTRenderer<VElement> renderer = getRenderer(vElement, viewContext);
	// if (renderer == null) {
	// throw new NoRendererFoundException(vElement);
	// }
	// return renderer.render(parent, vElement, viewContext);
	// }
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory#getRenderer(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	public AbstractSWTRenderer<VElement> getRenderer(VElement vElement, ViewModelContext viewContext) {
		int highestPriority = -1;
		AbstractSWTRenderer<VElement> bestCandidate = null;
		for (final ECPRendererDescription description : rendererDescriptors) {

			int currentPriority = -1;

			for (final ECPRendererTester tester : description.getTester()) {
				final int testerPriority = tester.isApplicable(vElement, viewContext);
				if (testerPriority > currentPriority) {
					currentPriority = testerPriority;
				}

			}

			if (currentPriority > highestPriority) {
				highestPriority = currentPriority;
				try {
					bestCandidate = description.getRenderer().newInstance();
				} catch (final InstantiationException ex) {
					Activator.log(ex);
					return null;
				} catch (final IllegalAccessException ex) {
					Activator.log(ex);
					return null;
				}
			}
		}
		bestCandidate.init(vElement, viewContext);
		return bestCandidate;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory#getAdditionalRenderer(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	public Collection<AbstractAdditionalSWTRenderer<VElement>> getAdditionalRenderer(VElement vElement,
		ViewModelContext viewModelContext) {
		final Set<AbstractAdditionalSWTRenderer<VElement>> renderers = new LinkedHashSet<AbstractAdditionalSWTRenderer<VElement>>();
		for (final ECPAdditionalRendererDescription description : additionalRendererDescriptors) {
			final ECPAdditionalRendererTester tester = description.getTester();
			if (tester.isApplicable(vElement, viewModelContext)) {
				try {
					final AbstractAdditionalSWTRenderer<VElement> renderer = description.getRenderer()
						.newInstance();
					renderer.init(vElement, viewModelContext);
					renderers.add(renderer);
					continue;
				} catch (final InstantiationException ex) {
					Activator.log(ex);
					continue;
				} catch (final IllegalAccessException ex) {
					Activator.log(ex);
					continue;
				}
			}
		}
		return renderers;
	}
}
