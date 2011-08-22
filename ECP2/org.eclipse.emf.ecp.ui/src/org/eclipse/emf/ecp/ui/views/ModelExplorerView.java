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
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ModelContentProvider;
import org.eclipse.emf.ecp.ui.model.ModelLabelProvider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eike Stepper
 */
public class ModelExplorerView extends TreeView
{
  public static final String ID = "org.eclipse.emf.ecp.ui.ModelExplorerView";

  private ModelContentProvider contentProvider = new ModelContentProvider();

  public ModelExplorerView()
  {
  }

  @Override
  protected void doCreatePartControl(Composite parent)
  {
    TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new ModelLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPProjectManager.INSTANCE);
    setViewer(viewer);
  }

  @Override
  protected void fillContextMenu(IMenuManager manager)
  {
    IStructuredSelection selection = getSelection();
    if (selection.size() == 1)
    {
      Object element = selection.getFirstElement();
      ECPModelContext context = contentProvider.getModelContext(element);
      if (context != null)
      {
        UIProvider provider = UIProviderRegistry.INSTANCE.getUIProvider(context);
        if (provider != null)
        {
          provider.fillContextMenu(context, element, manager);
        }
      }
    }

    super.fillContextMenu(manager);
  }
}
