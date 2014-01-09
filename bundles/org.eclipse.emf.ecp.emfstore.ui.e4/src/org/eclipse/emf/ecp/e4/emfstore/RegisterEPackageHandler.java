package org.eclipse.emf.ecp.e4.emfstore;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecp.emfstore.internal.ui.handler.RegisterEPackageHelper;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.swt.widgets.Shell;

public class RegisterEPackageHandler {
	@Execute
	public void execute(Shell shell,
		@Named(IServiceConstants.ACTIVE_SELECTION) @Optional List<InternalRepository> ecpRepositories) {
		RegisterEPackageHelper.registerEPackage(ecpRepositories.get(0), shell);
	}

	@CanExecute
	public boolean canExecute(
		@Named(IServiceConstants.ACTIVE_SELECTION) @Optional List<InternalRepository> ecpRepositories) {
		return ecpRepositories.size() == 1;
	}
}