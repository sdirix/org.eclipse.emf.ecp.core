/********************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 ********************************************************************************/
package org.eclipse.emf.ecp.internal.ui;

import org.eclipse.net4j.util.AdapterUtil;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPContainer;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPProviderAware;
import org.eclipse.emf.ecp.core.util.observer.ECPObserver;
import org.eclipse.emf.ecp.internal.core.util.ElementRegistry;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.ui.CompositeStateObserver;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import java.util.Collection;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class UIProviderRegistryImpl extends ElementRegistry<UIProvider, ECPObserver> implements
	UIProviderRegistry {
	/**
	 * This is the Instance used by the {@link UIProviderRegistry} for providing its instance.
	 */
	public static final UIProviderRegistryImpl INSTANCE = new UIProviderRegistryImpl();

	private final UIProviderParser extensionParser = new UIProviderParser();

	private UIProviderRegistryImpl() {
	}

	/** {@inheritDoc} */
	public UIProvider getUIProvider(Object adaptable) {
		if (adaptable instanceof ECPProviderAware) {
			ECPProvider provider = ((ECPProviderAware) adaptable).getProvider();
			if (provider != null) {
				return getUIProvider(provider);
			}
		}

		return AdapterUtil.adapt(adaptable, UIProvider.class);
	}

	private synchronized UIProvider getUIProvider(ECPProvider provider) {
		UIProvider uiProvider = (UIProvider) ((InternalProvider) provider).getUIProvider();
		if (uiProvider != null) {
			return uiProvider;
		}

		for (UIProvider ui : getUIProviders()) {
			if (ui.getProvider().equals(provider)) {
				uiProvider = ui;
				break;
			}
		}

		if (uiProvider == null) {
			uiProvider = new DefaultUIProvider(provider.getName() + ".default");
		}

		((InternalProvider) provider).setUIProvider(uiProvider);
		return uiProvider;
	}

	/** {@inheritDoc} */
	public UIProvider getUIProvider(String name) {
		return getElement(name);
	}

	/** {@inheritDoc} */
	public Collection<UIProvider> getUIProviders() {
		return getElements();
	}

	/** {@inheritDoc} */
	public boolean hasUIProviders() {
		return hasElements();
	}

	// @Override
	// protected UIProvider[] createElementArray(int size) {
	// return new UIProvider[size];
	// }

	@Override
	protected void notifyObservers(Collection<UIProvider> oldElements, Collection<UIProvider> newElements)
		throws Exception {
	}

	@Override
	protected void doActivate() throws Exception {
		super.doActivate();
		extensionParser.activate();
	}

	@Override
	protected void doDeactivate() throws Exception {
		extensionParser.deactivate();
		super.doDeactivate();
	}

	/**
	 * @author Eike Stepper
	 */
	private final class UIProviderParser extends ExtensionParser<UIProvider> {
		private static final String EXTENSION_POINT_NAME = "uiProviders";

		public UIProviderParser() {
			super(UIProviderRegistryImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
		}

		@Override
		protected UIProvider createElement(String name, IConfigurationElement configurationElement) {
			UIProviderDescriptor descriptor = new UIProviderDescriptor(name, configurationElement);
			descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
			descriptor.setDescription(configurationElement.getAttribute("description"));
			return descriptor;
		}
	}

	/**
	 * @author Eike Stepper
	 */
	private final class UIProviderDescriptor extends ExtensionDescriptor<UIProvider> implements UIProvider {
		public UIProviderDescriptor(String name, IConfigurationElement configurationElement) {
			super(UIProviderRegistryImpl.this, name, TYPE, configurationElement);
		}

		public InternalProvider getProvider() {
			return getResolvedElement().getProvider();
		}

		public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
			return getResolvedElement().getAdapter(adaptable, adapterType);
		}

		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType) {
			return getResolvedElement().getAdapter(adapterType);
		}

		public String getText(Object element) {
			return getResolvedElement().getText(element);
		}

		public Image getImage(Object element) {
			return getResolvedElement().getImage(element);
		}

		public void fillContextMenu(IMenuManager manager, ECPContainer context, Object[] elements) {
			getResolvedElement().fillContextMenu(manager, context, elements);
		}

		public Control createAddRepositoryUI(Composite parent, ECPProperties repositoryProperties,
			Text repositoryNameText, Text repositoryLabelText, Text repositoryDescriptionText) {
			return getResolvedElement().createAddRepositoryUI(parent, repositoryProperties, repositoryNameText,
				repositoryLabelText, repositoryDescriptionText);
		}

		public Control createCheckoutUI(Composite parent, ECPCheckoutSource checkoutSource,
			ECPProperties projectProperties) {
			return getResolvedElement().createCheckoutUI(parent, checkoutSource, projectProperties);
		}

		public Control createNewProjectUI(Composite parent, CompositeStateObserver observer,
			ECPProperties projectProperties) {
			return getResolvedElement().createNewProjectUI(parent, observer, projectProperties);
		}
	}
}
