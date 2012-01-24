package org.eclipse.emf.ecp.emfstore.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class CreateProjectHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
//		CreateProjectDialog dialog = new CreateProjectDialog(HandlerUtil.getActiveShell(event),
//			null);
//		dialog.open();
//		ProjectSpace projectSpace = dialog.getProjectSpace();
//	    InternalProject project = new ECPProjectImpl(projectSpace.getProjectName(), EMFStoreProvider.INSTANCE);
//	    ECPProperties projectProperties = project.getProperties();
//	    projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getIdentifier());
//	    EMFStoreProvider.INSTANCE.handleLifecycle(project, LifecycleEvent.CREATE);
//	    project.open();
//	    ECPProjectManager.INSTANCE.addProject(project);
		return null;
	}

}
