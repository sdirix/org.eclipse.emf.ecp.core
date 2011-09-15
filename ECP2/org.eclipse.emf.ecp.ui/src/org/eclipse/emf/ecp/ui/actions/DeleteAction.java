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

import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.ui.dialogs.CheckoutDialog;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class DeleteAction extends AbstractAction
{
  public DeleteAction()
  {
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    super.selectionChanged(action, selection);
    for (ECPDeletable deletable : getDeletables())
    {
      if (!deletable.canDelete())
      {
        action.setEnabled(false);
        return;
      }
    }

    action.setEnabled(true);
  }

  @Override
  protected void run(IAction action, IStructuredSelection selection)
  {
    List<ECPDeletable> deletables = getDeletables();
    if (!deletables.isEmpty())
    {
      DeleteDialog dialog = new DeleteDialog(getShell(), deletables);
      if (dialog.open() == CheckoutDialog.OK)
      {
        for (ECPDeletable deletable : deletables)
        {
          deletable.delete();
        }
      }
    }
  }

  protected List<ECPDeletable> getDeletables()
  {
    List<ECPDeletable> deletables = new ArrayList<ECPDeletable>();
    for (Iterator<?> it = getSelection().iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof ECPDeletable)
      {
        deletables.add((ECPDeletable)element);
      }
    }

    return deletables;
  }
}
