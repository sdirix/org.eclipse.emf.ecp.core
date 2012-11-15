package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.ui.controller.UICommitProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CommitProjectHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event))
			.getFirstElement();
		ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
		// TODO Ugly
		if (projectSpace.getUsersession() == null) {
			ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
			projectSpace.setUsersession(serverInfo.getLastUsersession());
		}
		new UICommitProjectController(HandlerUtil.getActiveShell(event), projectSpace).execute();
		// is structural because of possible merge
		project.notifyObjectsChanged(new Object[] { project }, true);
		return null;
	}
}
