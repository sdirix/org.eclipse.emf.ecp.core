 
package org.eclipse.emf.ecp.e4.emfstore;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.emfstore.internal.server.EMFStoreController;
import org.eclipse.emf.emfstore.internal.server.exceptions.FatalESException;
import org.osgi.service.log.LogService;

@SuppressWarnings("restriction")
public class StartLocalServerHandler {
	@Execute
	public void execute(LogService logService) {
		try {
			EMFStoreController.runAsNewThread();
		} catch (FatalESException e) {
			logService.log(LogService.LOG_ERROR, e.getMessage(), e);
		}
	}
	@CanExecute
	public boolean isEnabled(){
		return EMFStoreController.getInstance()==null;
	}
		
}