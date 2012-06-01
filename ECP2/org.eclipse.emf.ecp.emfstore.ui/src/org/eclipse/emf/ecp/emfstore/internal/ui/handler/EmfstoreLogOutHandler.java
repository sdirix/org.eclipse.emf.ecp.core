/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.ui.views.TreeView;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.util.WorkspaceUtil;
import org.eclipse.emf.emfstore.client.ui.controller.UISessionController;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class EmfstoreLogOutHandler extends AbstractHandler
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    final ECPRepository ecpRepository = (ECPRepository)((IStructuredSelection)HandlerUtil.getCurrentSelection(event))
        .getFirstElement();
    final ServerInfo serverInfo = EMFStoreProvider.getServerInfo((InternalRepository)ecpRepository);
    try
    {
      new UISessionController(HandlerUtil.getActiveShell(event)).logout(serverInfo.getLastUsersession());
    }
    catch (EmfStoreException ex)
    {
      String title = "Error";
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ex.getMessage());
      title = ex.getClass().getName();
      MessageDialog.openError(HandlerUtil.getActiveShell(event), title, stringBuilder.toString());
      WorkspaceUtil.handleException("An unexpected error in a EMFStore plugin occured.", ex);
    }
    ((TreeView)HandlerUtil.getActivePart(event)).getRefreshAction().run();
    return null;
  }

}
