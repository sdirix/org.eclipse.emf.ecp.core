package org.eclipse.emf.ecp.emfstore.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.internal.core.ECPProjectImpl;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.ui.views.emfstorebrowser.views.CreateProjectDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateProjectHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		CreateProjectDialog dialog = new CreateProjectDialog(HandlerUtil.getActiveShell(event),
			null);
		dialog.open();
		ProjectSpace projectSpace = dialog.getProjectSpace();
	    InternalProject project = new ECPProjectImpl(projectSpace.getProjectName(), EMFStoreProvider.INSTANCE);
	    ECPProperties projectProperties = project.getProperties();
	    projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getIdentifier());
	    EMFStoreProvider.INSTANCE.handleLifecycle(project, LifecycleEvent.CREATE);
	    project.open();
	    ECPProjectManager.INSTANCE.addProject(project);
		return null;
	}

}
