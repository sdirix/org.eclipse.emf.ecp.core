 
package org.eclipse.emf.ecp.e4.emfstore;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UILoginSessionController;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class LogInHandler {
	@Execute
	public void execute(Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION)@Optional ECPRepository ecpRepository) {
		
		final ESServer serverInfo = ((EMFStoreProvider)ECPUtil.getResolvedElement(ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME))).getServerInfo((InternalRepository) ecpRepository);
		new UILoginSessionController(shell, serverInfo).execute();

		// ((TreeView)HandlerUtil.getActivePart(event)).getRefreshAction().run();
		((InternalRepository) ecpRepository).notifyObjectsChanged((Collection)Collections.singleton(ecpRepository ));
	}
		
}