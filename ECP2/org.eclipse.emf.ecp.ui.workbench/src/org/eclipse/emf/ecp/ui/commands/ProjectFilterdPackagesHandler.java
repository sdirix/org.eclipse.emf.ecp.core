/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.CheckedModelElementHelper;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.FilterModelElementWizard;
import org.eclipse.emf.ecp.wizards.WizardUICallback;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class ProjectFilterdPackagesHandler extends AbstractHandler
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  /**
   * {@inheritDoc}
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;
    
    HandlerHelper.filterProjectPackages((ECPProject)ssel.getFirstElement(),new WizardUICallback<CheckedModelElementHelper>(HandlerUtil.getActiveShell(event), new FilterModelElementWizard()));
    
    
    return null;
  }
}
