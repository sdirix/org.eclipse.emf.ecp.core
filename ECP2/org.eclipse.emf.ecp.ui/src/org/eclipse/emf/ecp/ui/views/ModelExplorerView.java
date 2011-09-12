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
package org.eclipse.emf.ecp.ui.views;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ModelContentProvider;
import org.eclipse.emf.ecp.ui.model.ModelLabelProvider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @author Eike Stepper
 */
public class ModelExplorerView extends TreeView
{
  public static final String ID = "org.eclipse.emf.ecp.ui.ModelExplorerView";

  private ModelContentProvider contentProvider = new ModelContentProvider();

  public ModelExplorerView()
  {
    super(ID);
  }

  @Override
  protected void configureViewer(TreeViewer viewer)
  {
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new ModelLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPProjectManager.INSTANCE);
  }

  @Override
  protected void fillContextMenu(IMenuManager manager)
  {
    Object[] elements = getSelection().toArray();

    ECPModelContext context = ECPUtil.getModelContext(contentProvider, elements);
    if (context != null)
    {
      UIProvider provider = UIProviderRegistry.INSTANCE.getUIProvider(context);
      if (provider != null)
      {
        provider.fillContextMenu(manager, context, elements);
      }
    }

    super.fillContextMenu(manager);
  }
}
