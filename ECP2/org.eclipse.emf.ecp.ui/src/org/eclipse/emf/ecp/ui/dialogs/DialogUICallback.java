/**
 * 
 */
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.common.ICompositeProvider;

/**
 * @author Eugen Neufeld
 */
public class DialogUICallback<T extends ICompositeProvider> extends AbstractUICallback {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#setCompositeUIProvider(org.eclipse.emf.ecp.ui.common.
	 * ICompositeProvider)
	 */
	@Override
	public void setCompositeUIProvider(ICompositeProvider uiProvider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#open()
	 */
	@Override
	public int open() {
		return CANCEL;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#showError(java.lang.String, java.lang.String)
	 */
	@Override
	public void showError(String title, String message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
