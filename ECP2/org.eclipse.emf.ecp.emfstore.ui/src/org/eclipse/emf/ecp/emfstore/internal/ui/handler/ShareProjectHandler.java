package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.wizards.ShareWizard;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.ui.controller.UIShareProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class ShareProjectHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event))
			.getFirstElement();

		ShareWizard rw = new ShareWizard();
		rw.init(project.getProvider());

		WizardDialog wd = new WizardDialog(HandlerUtil.getActiveShell(event), rw);
		int result = wd.open();
		if (result == WizardDialog.OK) {
			// TODO internal cast again
			InternalRepository repository = (InternalRepository) rw.getSelectedRepository();
			project.undispose(repository);
			ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
			// TODO Ugly
			if (projectSpace.getUsersession() == null) {
				ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
				projectSpace.setUsersession(serverInfo.getLastUsersession());
			}
			new UIShareProjectController(HandlerUtil.getActiveShell(event), projectSpace).execute();

			project.notifyObjectsChanged(new Object[] { project });
			repository.notifyObjectsChanged(new Object[] { repository });
		}
		return null;
	}
}
