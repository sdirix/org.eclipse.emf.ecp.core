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
package org.eclipse.emf.ecp.ui.actions;

import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.model.TreeContentProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Eike Stepper
 */
public class RefreshViewerAction extends Action
{
  private final Viewer viewer;

  public RefreshViewerAction(Viewer viewer)
  {
    super("Refresh", Activator.getImageDescriptor("icons/refresh.gif"));
    setToolTipText("Refresh viewer content");
    this.viewer = viewer;
  }

  @Override
  public void run()
  {
    try
    {
      if (viewer instanceof StructuredViewer)
      {
        StructuredViewer structuredViewer = (StructuredViewer)viewer;
        IContentProvider contentProvider = structuredViewer.getContentProvider();
        if (contentProvider instanceof TreeContentProvider)
        {
          TreeContentProvider<?> treeContentProvider = (TreeContentProvider<?>)contentProvider;
          treeContentProvider.refreshViewer();
          return;
        }
      }

      viewer.refresh();
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }
}
