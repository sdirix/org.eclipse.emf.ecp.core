/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.wizards.FilterModelElementWizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
public class ProjectFilterdPackagesHandler extends AbstractHandler
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;
    ECPProject ecpProject = (ECPProject)ssel.getFirstElement();

    Set<EPackage> ePackages = new HashSet<EPackage>();

    for (Object object : Registry.INSTANCE.values())
    {
      if (object instanceof EPackage)
      {
        EPackage ePackage = (EPackage)object;
        ePackages.add(ePackage);
      }

    }

    FilterModelElementWizard rw = new FilterModelElementWizard();
    rw.setWindowTitle("Filter Modelelements");
    rw.init(ePackages, new HashSet<EPackage>(), ePackages, new HashSet<EClass>(), ecpProject);
    WizardDialog wd = new WizardDialog(HandlerUtil.getActiveShell(event), rw);

    int result = wd.open();
    if (result == WizardDialog.OK)
    {
    }
    return null;
  }
}
