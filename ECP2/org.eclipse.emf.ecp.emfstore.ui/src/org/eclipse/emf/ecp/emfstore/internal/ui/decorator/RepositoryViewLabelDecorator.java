/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.IUsersession;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.Usersession;
import org.eclipse.emf.emfstore.internal.client.model.observers.LoginObserver;
import org.eclipse.emf.emfstore.internal.client.model.observers.LogoutObserver;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;

/**
 * Decorates the label of a {@link ServerInfo} object according to its login state.
 * 
 * @author Eugen Neufeld
 * @see ILightweightLabelDecorator
 */
public class RepositoryViewLabelDecorator extends LabelProvider implements ILightweightLabelDecorator, LoginObserver,
	LogoutObserver {

	/**
	 * {@inheritDoc}
	 */
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof ECPRepository) {
			InternalRepository repository = (InternalRepository) element;
			ECPProvider provider = repository.getProvider();

			if (provider != null && EMFStoreProvider.NAME.equalsIgnoreCase(provider.getName())) {
				ServerInfo server = EMFStoreProvider.INSTANCE.getServerInfo(repository);
				if (server.getLastUsersession() != null && server.getLastUsersession().isLoggedIn()) {
					decoration.addOverlay(Activator.getImageDescriptor("icons/bullet_green.png"),
						IDecoration.BOTTOM_RIGHT);
				} else {
					decoration.addOverlay(Activator.getImageDescriptor("icons/bullet_delete.png"),
						IDecoration.BOTTOM_RIGHT);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.client.model.observers.LoginObserver#loginCompleted(org.eclipse.emf.emfstore.client.model.Usersession)
	 */
	public void loginCompleted(IUsersession session) {
		update((Usersession) session);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.client.model.observers.LogoutObserver#logoutCompleted(org.eclipse.emf.emfstore.client.model.Usersession)
	 */
	public void logoutCompleted(IUsersession session) {
		update((Usersession) session);
	}

	private void update(final Usersession usersession) {

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				fireLabelProviderChanged(new LabelProviderChangedEvent(RepositoryViewLabelDecorator.this,
					EMFStoreProvider.INSTANCE.getRepository(usersession.getServerInfo())));
			}
		});
	}
}
