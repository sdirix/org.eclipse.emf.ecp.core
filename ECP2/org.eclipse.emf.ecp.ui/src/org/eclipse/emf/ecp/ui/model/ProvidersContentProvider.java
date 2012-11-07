/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.model;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.observer.IECPProvidersChangedObserver;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

/**
 * @author Eike Stepper
 */
public class ProvidersContentProvider extends TreeContentProvider<ECPProviderRegistry> implements
// ECPProviderRegistry.Listener
	IECPProvidersChangedObserver {
	private final boolean excludesProvidersThatCannotAddRepositories;

	public ProvidersContentProvider() {
		this(false);
	}

	public ProvidersContentProvider(boolean excludesProvidersThatCannotAddRepositories) {
		this.excludesProvidersThatCannotAddRepositories = excludesProvidersThatCannotAddRepositories;
	}

	public final boolean excludesProvidersThatCannotAddRepositories() {
		return excludesProvidersThatCannotAddRepositories;
	}

	@Override
	protected void fillChildren(Object parent, InternalChildrenList childrenList) {
		if (parent == ECPProviderRegistry.INSTANCE) {
			ECPProvider[] providers = ECPProviderRegistry.INSTANCE.getProviders();
			if (!excludesProvidersThatCannotAddRepositories) {
				childrenList.addChildren(providers);
			} else {
				for (ECPProvider provider : providers) {
					if (provider.canAddRepositories()) {
						childrenList.addChild(provider);
					}
				}
			}
		}
	}

	public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception {
		refreshViewer();
	}

	@Override
	protected void connectInput(ECPProviderRegistry input) {
		super.connectInput(input);
		input.addObserver(this);
	}

	@Override
	protected void disconnectInput(ECPProviderRegistry input) {
		input.removeObserver(this);
		super.disconnectInput(input);
	}
}
