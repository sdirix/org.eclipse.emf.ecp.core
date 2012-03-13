package org.eclipse.emf.ecp.emfstore.handler;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.ui.controller.UIProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateProjectHandler extends AbstractHandler
{

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    UIProjectController uiProjectController = new UIProjectController(HandlerUtil.getActiveShell(event));
    ProjectSpace projectSpace = uiProjectController.createLocalProject();

    ECPProperties projectProperties = ECPUtil.createProperties();
    projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getIdentifier());

    ECPProject project = ECPProjectManager.INSTANCE.createProject(EMFStoreProvider.INSTANCE,
        projectSpace.getProjectName(), projectProperties);

    EMFStoreProvider.INSTANCE.handleLifecycle(project, LifecycleEvent.CREATE);
    project.open();
    return null;
  }
}
