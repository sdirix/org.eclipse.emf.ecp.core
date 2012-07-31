package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
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
    HandlerHelper.searchModelElementHandlerHelper((IStructuredSelection)HandlerUtil.getCurrentSelection(event), HandlerUtil.getActiveShell(event), contextProvider);
    return null;
  }
}
