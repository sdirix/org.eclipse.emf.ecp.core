/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.dialogs.ModelElementCheckedTreeDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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

    ModelElementCheckedTreeDialog dialog = new ModelElementCheckedTreeDialog(HandlerUtil.getActiveShell(event),
        ePackages, new HashSet<EPackage>(), ePackages, new HashSet<EClass>());

    Set<Object> initialSelection = new HashSet<Object>();
    initialSelection.addAll(ecpProject.getFilteredPackages());
    initialSelection.addAll(ecpProject.getFilteredEClasses());

    dialog.setInitialSelections(initialSelection.toArray());

    int result = dialog.open();
    if (result == Dialog.OK)
    {
      Object[] dialogSelection = dialog.getChecked();
      Set<EPackage> filtererdPackages = new HashSet<EPackage>();
      Set<EClass> filtererdEClasses = new HashSet<EClass>();
      for (Object object : dialogSelection)
      {
        if (object instanceof EPackage)
        {
          filtererdPackages.add((EPackage)object);
        }
        else if (object instanceof EClass)
        {
          EClass eClass = (EClass)object;
          if (!filtererdPackages.contains(eClass.getEPackage()))
          {
            filtererdEClasses.add(eClass);
          }
        }
      }
      ecpProject.setFilteredPackages(filtererdPackages);
      ecpProject.setFilteredEClasses(filtererdEClasses);
    }
    return null;
  }
}
