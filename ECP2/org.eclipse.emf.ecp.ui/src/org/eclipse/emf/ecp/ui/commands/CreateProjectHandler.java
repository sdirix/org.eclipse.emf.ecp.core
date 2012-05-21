/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.ui.dialogs.CreateProjectDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CreateProjectHandler extends AbstractHandler
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    CreateProjectDialog dialog = new CreateProjectDialog(HandlerUtil.getActiveShell(event));
    if (dialog.open() == Dialog.OK)
    {
      ECPProvider selectedProvider = dialog.getProvider();
      if (selectedProvider == null)
      {
        return null;
      }
      ECPProperties projectProperties = ECPUtil.createProperties();

      String projectName = dialog.getProjectName();
      ECPProject project = ECPProjectManager.INSTANCE.createProject(selectedProvider, projectName, projectProperties);

      ((InternalProvider)selectedProvider).handleLifecycle(project, LifecycleEvent.CREATE);
      project.open();
    }
    return null;
  }
}
