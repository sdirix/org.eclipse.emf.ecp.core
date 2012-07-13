package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.ecp.ui.views.TreeView;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class SearchModelElementHandler extends AbstractHandler
{

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ECPModelContextProvider contextProvider = (ECPModelContextProvider)((TreeView)HandlerUtil.getActivePart(event))
        .getViewer().getContentProvider();
    ECPProject project = (ECPProject)ECPUtil.getModelContext(contextProvider,
        ((IStructuredSelection)HandlerUtil.getCurrentSelection(event)).getFirstElement());

    if (project == null)
    {
      MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information",
          "You must first select the Project.");
    }
    else
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(HandlerUtil.getActiveShell(event),
          new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
              ComposedAdapterFactory.Descriptor.Registry.INSTANCE)));
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
    return null;
  }
}
