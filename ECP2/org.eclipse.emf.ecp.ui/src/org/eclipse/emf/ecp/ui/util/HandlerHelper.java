/**
 * 
 */
package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.dialogs.CheckoutDialog;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;
import org.eclipse.emf.ecp.wizards.CreateProjectWizard;
import org.eclipse.emf.ecp.wizards.FilterModelElementWizard;
import org.eclipse.emf.ecp.wizards.NewModelElementWizard;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
public class HandlerHelper
{
  public static void checkoutHandlerHelper(IStructuredSelection selection, Shell shell)
  {
    for (Object object : selection.toArray())
    {
      ECPCheckoutSource checkoutSource = (ECPCheckoutSource)object;
      CheckoutDialog dialog = new CheckoutDialog(shell, checkoutSource);
      if (dialog.open() == CheckoutDialog.OK)
      {
        String projectName = dialog.getProjectName();
        ECPProperties projectProperties = dialog.getProjectProperties();
        checkoutSource.checkout(projectName, projectProperties);
      }
    }
  }

  public static void closeHandlerHelper(IStructuredSelection selection, String currentType)
  {
    for (Object object : selection.toArray())
    {
      ECPCloseable closeable = (ECPCloseable)object;
      if ("open".equalsIgnoreCase(currentType))
      {
        closeable.open();
      }
      else if ("close".equalsIgnoreCase(currentType))
      {
        closeable.close();
      }
    }
  }

  public static void createProjectHandlerHelper(Shell shell)
  {
    CreateProjectWizard cpw = new CreateProjectWizard();
    cpw.setWindowTitle("Create new Project");
    WizardDialog wd = new WizardDialog(shell, cpw);

    int result = wd.open();
    if (result == WizardDialog.OK)
    {
    }
  }

  public static void deleteHandlerHelper(IStructuredSelection selection, Shell shell)
  {
    List<ECPDeletable> deletables = new ArrayList<ECPDeletable>();
    for (Iterator<?> it = selection.iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof ECPDeletable)
      {
        deletables.add((ECPDeletable)element);
      }
    }

    if (!deletables.isEmpty())
    {
      DeleteDialog dialog = new DeleteDialog(shell, deletables);
      if (dialog.open() == DeleteDialog.OK)
      {
        for (ECPDeletable deletable : deletables)
        {
          deletable.delete();
        }
      }
    }
  }

  public static void newModelElementHandlerHelper(IStructuredSelection selection, Shell shell)
  {
    ECPProject ecpProject = (ECPProject)selection.getFirstElement();

    NewModelElementWizard rw = new NewModelElementWizard();
    rw.setWindowTitle("Add new model element");
    rw.init(ecpProject);

    WizardDialog wd = new WizardDialog(shell, rw);

    int result = wd.open();
    if (result == WizardDialog.OK)
    {

    }
  }

  public static void filterProjectPackagesHandlerHelper(IStructuredSelection selection, Shell shell)
  {
    ECPProject ecpProject = (ECPProject)selection.getFirstElement();

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
    WizardDialog wd = new WizardDialog(shell, rw);

    int result = wd.open();
    if (result == WizardDialog.OK)
    {
    }
  }

  public static void searchModelElementHandlerHelper(IStructuredSelection selection, Shell shell,
      ECPModelContextProvider contextProvider)
  {
    ECPProject project = (ECPProject)ECPUtil.getModelContext(contextProvider, selection.getFirstElement());

    if (project == null)
    {
      MessageDialog.openInformation(shell, "Information", "You must first select the Project.");
    }
    else
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new AdapterFactoryLabelProvider(
          new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)));
      dialog.setElements(project.getElements().toArray());
      dialog.setMultipleSelection(false);
      dialog.setMessage("Enter model element name prefix or pattern (e.g. *Trun?)");
      dialog.setTitle("Search Model Element");
      if (dialog.open() == Dialog.OK)
      {
        Object[] selections = dialog.getResult();

        if (selections != null && selections.length == 1 && selections[0] instanceof EObject)
        {
          ActionHelper.openModelElement((EObject)selections[0],
              "org.eclipse.emf.ecp.ui.commands.SearchModelElementHandler", project);
        }
      }
    }
  }
}
