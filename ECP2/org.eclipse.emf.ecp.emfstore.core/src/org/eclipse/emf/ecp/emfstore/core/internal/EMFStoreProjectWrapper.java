package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.observers.CheckoutObserver;
import org.eclipse.emf.emfstore.client.model.util.WorkspaceUtil;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PlatformUI;

public class EMFStoreProjectWrapper implements ECPCheckoutSource {

	private final InternalRepository repository;
	private final ProjectInfo projectInfo;
	private final ServerInfo serverInfo;

	public EMFStoreProjectWrapper(InternalRepository repository, ProjectInfo projectInfo, ServerInfo serverInfo) {
		this.repository = repository;
		this.projectInfo = projectInfo;
		this.serverInfo = serverInfo;
	}

	public ECPRepository getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	public ECPProvider getProvider() {
		return repository.getProvider();
	}

	public String getDefaultCheckoutName() {
		// TODO Auto-generated method stub
		return projectInfo.getName();
	}

	public ECPProject checkout(String projectName,
			ECPProperties projectProperties) {
		final ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell());
			try {
				progressDialog.open();
				progressDialog.getProgressMonitor().beginTask("Checkout project...", IProgressMonitor.UNKNOWN);
				serverInfo.getLastUsersession().logIn();
				ProjectSpace projectSpace = WorkspaceManager.getInstance().getCurrentWorkspace().checkout(serverInfo.getLastUsersession(), projectInfo);
				WorkspaceManager.getInstance().getCurrentWorkspace().save();
				WorkspaceUtil.logCheckout(projectSpace, projectSpace.getBaseVersion());
				WorkspaceManager.getObserverBus().notify(CheckoutObserver.class).checkoutDone(projectSpace);
				projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getIdentifier());
				return repository.checkout(projectName, projectProperties);
			} catch (EmfStoreException e) {
				//TODO log exception
				e.printStackTrace();
				// BEGIN SUPRESS CATCH EXCEPTION
			} catch (RuntimeException e) {
				//TODO: log exception
				e.printStackTrace();
				// END SUPRESS CATCH EXCEPTION
			} finally {
				progressDialog.getProgressMonitor().done();
				progressDialog.close();
			}
			//TODO: Handle this case
			return repository.checkout(projectName, projectProperties);
	}

}
