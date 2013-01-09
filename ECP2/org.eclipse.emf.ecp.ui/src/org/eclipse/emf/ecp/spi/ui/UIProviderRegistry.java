/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.spi.ui;

import org.eclipse.emf.ecp.core.util.ECPModelContext;

import org.eclipse.swt.graphics.Image;

/**
 * @author Eike Stepper
 */
public interface UIProviderRegistry {
	/**
	 * This is the Instance to use for the {@link UIProviderRegistry}.
	 */
	UIProviderRegistry INSTANCE = org.eclipse.emf.ecp.internal.ui.UIProviderRegistryImpl.INSTANCE;

	public UIProvider getUIProvider(Object adaptable);

	public UIProvider getUIProvider(String name);

	public UIProvider[] getUIProviders();

	public boolean hasUIProviders();

	public String getText(ECPModelContext context, Object adaptable);

	public Image getImage(ECPModelContext context, Object adaptable);
}
