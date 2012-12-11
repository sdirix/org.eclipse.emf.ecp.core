package org.eclipse.emf.ecp.example.internal.leagueview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class LeagueSelectionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().showView(LeagueViewPart.ID);
		} catch (PartInitException e) {
			Activator.log(e);
		}
		return null;
	}

	

}
