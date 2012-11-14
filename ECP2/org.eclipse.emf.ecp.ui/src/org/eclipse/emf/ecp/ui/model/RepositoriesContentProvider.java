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
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.observer.IECPRepositoriesChangedObserver;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public class RepositoriesContentProvider extends ECPContentProvider<ECPRepositoryManager> implements
// ECPRepositoryManager.Listener
	IECPRepositoriesChangedObserver {
	private final ECPProvider allowedProvider;

	public RepositoriesContentProvider() {
		allowedProvider = null;
	}

	public RepositoriesContentProvider(ECPProvider allowedProvider) {
		this.allowedProvider = allowedProvider;
	}

	public void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception {
		refreshViewer();
	}

	public void objectsChanged(ECPRepository repository, Object[] objects) throws Exception {
		// do always a full refresh
		refreshViewer(true, objects);
	}

	@Override
	protected void connectInput(ECPRepositoryManager input) {
		super.connectInput(input);
		input.addObserver(this);
	}

	@Override
	protected void disconnectInput(ECPRepositoryManager input) {
		input.removeObserver(this);
		super.disconnectInput(input);
	}

	@Override
	protected boolean isSlow(Object parent) {
		InternalProvider provider = (InternalProvider) ECPProviderRegistry.INSTANCE.getProvider(parent);
		if (provider != null) {
			return provider.isSlow(parent);
		}

		return super.isSlow(parent);
	}

	@Override
	protected void fillChildren(Object parent, InternalChildrenList childrenList) {
		if (parent == ECPRepositoryManager.INSTANCE) {
			if (allowedProvider == null) {
				childrenList.addChildren(ECPRepositoryManager.INSTANCE.getRepositories());
			} else {
				for (ECPRepository ecpRepository : ECPRepositoryManager.INSTANCE.getRepositories()) {
					if (allowedProvider.equals(ecpRepository.getProvider())) {
						childrenList.addChild(ecpRepository);
					}
				}
			}
		} else {
			super.fillChildren(parent, childrenList);
		}
	}
}
