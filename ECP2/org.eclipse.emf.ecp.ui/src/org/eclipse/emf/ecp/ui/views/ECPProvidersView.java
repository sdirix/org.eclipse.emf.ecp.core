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

import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ProvidersContentProvider;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @author Eike Stepper
 */
public class ECPProvidersView extends TreeView
{
  public static final String ID = "org.eclipse.emf.ecp.ui.ECPProvidersView";

  public ECPProvidersView()
  {
  }

  @Override
  protected void configureViewer(TreeViewer viewer)
  {
    viewer.setContentProvider(new ProvidersContentProvider());
    viewer.setLabelProvider(new ProvidersLabelProvider());
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPProviderRegistry.INSTANCE);
  }
}
