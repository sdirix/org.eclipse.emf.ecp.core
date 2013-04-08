package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddRepositoryHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ECPHandlerHelper.createRepository(HandlerUtil.getActiveShell(event));
		return null;
	}

}
