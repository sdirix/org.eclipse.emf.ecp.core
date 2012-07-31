/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.CreateProjectWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CreateProjectHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerHelper.createProjectHandlerHelper(HandlerUtil.getActiveShell(event));
		return null;
	}
}
