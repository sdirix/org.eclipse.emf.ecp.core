/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.ui.controller.UILoginSessionController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class EmfstoreLogInHandler extends AbstractHandler
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

    new UILoginSessionController(HandlerUtil.getActiveShell(event), serverInfo).execute();

    // ((TreeView)HandlerUtil.getActivePart(event)).getRefreshAction().run();
    return null;
  }

}
