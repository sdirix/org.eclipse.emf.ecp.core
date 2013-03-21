package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddRepositoryHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerHelper.createRepository(HandlerUtil.getActiveShell(event));
		return null;
	}

}
