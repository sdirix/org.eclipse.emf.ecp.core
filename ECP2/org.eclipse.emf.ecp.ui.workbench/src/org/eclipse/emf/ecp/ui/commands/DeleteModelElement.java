package org.eclipse.emf.ecp.ui.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.ui.views.TreeView;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteModelElement extends AbstractHandler {

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);

		// TODO replace code with util call
		ECPModelContextProvider contextProvider = (ECPModelContextProvider) ((TreeView) HandlerUtil
			.getActivePart(event)).getViewer().getContentProvider();

		InternalProject project = (InternalProject) contextProvider.getModelContext(selection.getFirstElement());
		
		HandlerHelper.deleteModelElement(project, (List<EObject>)selection.toList());
		return null;
	}

}
