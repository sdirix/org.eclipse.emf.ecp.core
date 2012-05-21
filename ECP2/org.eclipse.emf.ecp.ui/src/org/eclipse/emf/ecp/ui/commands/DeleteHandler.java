package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.ui.dialogs.CheckoutDialog;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteHandler extends AbstractHandler
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;

    List<ECPDeletable> deletables = new ArrayList<ECPDeletable>();
    for (Iterator<?> it = ssel.iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof ECPDeletable)
      {
        deletables.add((ECPDeletable)element);
      }
    }

    if (!deletables.isEmpty())
    {
      DeleteDialog dialog = new DeleteDialog(HandlerUtil.getActiveShell(event), deletables);
      if (dialog.open() == CheckoutDialog.OK)
      {
        for (ECPDeletable deletable : deletables)
        {
          deletable.delete();
        }
      }
    }
    return null;
  }
}
