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
package org.eclipse.emf.cdo.ecp.internal.ui.handlers;

import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.workspace.CDOWorkspace;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public class CheckinHandler extends AbstractHandler
{
  public CheckinHandler()
  {
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
    if (selection instanceof IStructuredSelection)
    {
      Object element = ((IStructuredSelection)selection).getFirstElement();
      CDOWorkspace workspace = AdapterUtil.adapt(element, CDOWorkspace.class);
      if (workspace != null)
      {
        try
        {
          workspace.checkin();
        }
        catch (CommitException ex)
        {
          throw new ExecutionException("Problem while checking in " + element, ex);
        }
      }
    }

    return null;
  }
}
