package org.eclipse.emf.ecp.emfstore.handler;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.util.WorkspaceUtil;
import org.eclipse.emf.emfstore.client.ui.controller.UIUpdateProjectController;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class UpdateProjectHandler extends AbstractHandler
{

  public Object execute(ExecutionEvent event) throws ExecutionException
  {

    InternalProject project = (InternalProject)((IStructuredSelection)HandlerUtil.getCurrentSelection(event))
        .getFirstElement();
    ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
    // TODO Ugly
    if (projectSpace.getUsersession() == null)
    {
      ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
      projectSpace.setUsersession(serverInfo.getLastUsersession());
    }
    try
    {
      new UIUpdateProjectController(HandlerUtil.getActiveShell(event)).update(projectSpace);
      project.notifyObjectsChanged(new Object[] { project });
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

    return null;
  }
}
