/*
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

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.ui.actions.AddRepositoryAction;
import org.eclipse.emf.ecp.ui.model.RepositoriesContentProvider;
import org.eclipse.emf.ecp.ui.model.RepositoriesLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @author Eike Stepper
 */
public class ModelRepositoriesView extends TreeView implements ECPProviderRegistry.Listener
{
  public static final String ID = "org.eclipse.emf.ecp.ui.ModelRepositoriesView";

  private RepositoriesContentProvider contentProvider = new RepositoriesContentProvider();

  private Action addRepositoryAction;

  public ModelRepositoriesView()
  {
    super(ID);
  }

  @Override
  public void dispose()
  {
    ECPProviderRegistry.INSTANCE.removeListener(this);
    super.dispose();
  }

  public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception
  {
    setEnablements();
  }

  protected void setEnablements()
  {
    addRepositoryAction.setEnabled(canAddRepositories());
  }

  protected boolean canAddRepositories()
  {
    for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders())
    {
      if (provider.canAddRepositories())
      {
        return true;
      }
    }

    return false;
  }

  @Override
  protected void configureViewer(TreeViewer viewer)
  {
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new RepositoriesLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPRepositoryManager.INSTANCE);

    addRepositoryAction = new AddRepositoryAction(getSite().getShell());
    setEnablements();
    ECPProviderRegistry.INSTANCE.addListener(this);
  }

  @Override
  protected void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(addRepositoryAction);
    super.fillLocalToolBar(manager);
  }
}
