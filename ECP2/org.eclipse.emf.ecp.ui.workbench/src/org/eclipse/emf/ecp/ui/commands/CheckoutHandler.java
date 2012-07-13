/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.ui.dialogs.CheckoutDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CheckoutHandler extends AbstractHandler
{
  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;
    for (Object object : ssel.toArray())
    {
      ECPCheckoutSource checkoutSource = (ECPCheckoutSource)object;
      CheckoutDialog dialog = new CheckoutDialog(HandlerUtil.getActiveShell(event), checkoutSource);
      if (dialog.open() == CheckoutDialog.OK)
      {
        String projectName = dialog.getProjectName();
        ECPProperties projectProperties = dialog.getProjectProperties();
        checkoutSource.checkout(projectName, projectProperties);
      }
    }
    return null;
  }

}
