/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import java.util.Arrays;

import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CloseableHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection ssel = (IStructuredSelection) selection;
		String currentType = event.getParameter("org.eclipse.emf.ecp.ecpclosable.type");
		Object[] selectionArray =  ssel.toArray();
		ECPCloseable[] closeable = Arrays.copyOf(selectionArray, selectionArray.length, ECPCloseable[].class);
		HandlerHelper.close(closeable, currentType);

		return null;
	}

}
