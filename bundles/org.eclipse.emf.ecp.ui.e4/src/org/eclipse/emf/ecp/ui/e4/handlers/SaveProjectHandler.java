package org.eclipse.emf.ecp.ui.e4.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;

public class SaveProjectHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Object object) {
		final ECPProject ecpProject = ECPUtil.getECPProjectManager().getProject(object);
		if (ecpProject != null) {
			ECPHandlerHelper.saveProject(ecpProject);
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Object object) {
		final ECPProject ecpProject = ECPUtil.getECPProjectManager().getProject(object);
		if (ecpProject != null) {
			return ecpProject.hasDirtyContents();
		}
		return false;
	}

}