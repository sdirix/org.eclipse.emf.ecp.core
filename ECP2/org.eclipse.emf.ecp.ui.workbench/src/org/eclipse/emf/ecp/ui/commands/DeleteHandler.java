package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection ssel = (IStructuredSelection) selection;

		HandlerHelper.deleteHandlerHelper(ssel, HandlerUtil.getActiveShell(event));
		return null;
	}
}
