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
package org.eclipse.emf.ecp.ui.actions;

import org.eclipse.emf.ecp.internal.ui.Activator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Eike Stepper
 */
public abstract class AbstractAction implements IObjectActionDelegate
{
  private IWorkbenchPart targetPart;

  private IStructuredSelection selection;

  public AbstractAction()
  {
  }

  public final IWorkbenchPart getTargetPart()
  {
    return targetPart;
  }

  public final Shell getShell()
  {
    return targetPart.getSite().getShell();
  }

  public final IStructuredSelection getSelection()
  {
    return selection;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    this.targetPart = targetPart;
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    action.setEnabled(true);
    if (selection instanceof IStructuredSelection)
    {
      this.selection = (IStructuredSelection)selection;
    }
    else
    {
      this.selection = null;
    }
  }

  public final void run(IAction action)
  {
    if (selection != null)
    {
      try
      {
        run(action, selection);
      }
      catch (RuntimeException ex)
      {
        Activator.log(ex);
        throw ex;
      }
      catch (Error ex)
      {
        Activator.log(ex);
        throw ex;
      }
    }
  }

  protected abstract void run(IAction action, IStructuredSelection selection);
}
