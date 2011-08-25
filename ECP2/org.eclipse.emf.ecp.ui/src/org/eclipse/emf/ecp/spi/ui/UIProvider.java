/**
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.spi.ui;

import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.util.AdapterProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalRegistryElement;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eike Stepper
 */
public interface UIProvider extends InternalRegistryElement, IAdaptable, AdapterProvider
{
  public static final String TYPE = "UIProvider";

  public static final ILabelProvider EMF_LABEL_PROVIDER = new AdapterFactoryLabelProvider(
      InternalProvider.EMF_ADAPTER_FACTORY);

  public InternalProvider getProvider();

  public String getText(Object element);

  public Image getImage(Object element);

  public void fillContextMenu(IMenuManager manager, ECPModelContext context, Object[] elements);

  public Control createAddRepositoryUI(Composite parent, ECPProperties repositoryProperties, Text repositoryNameText,
      Text repositoryLabelText, Text repositoryDescriptionText);

  public Control createCheckoutUI(Composite parent, ECPCheckoutSource checkoutSource, ECPProperties projectProperties);
}
