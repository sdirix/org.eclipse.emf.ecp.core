/**
 * 
 */
package org.eclipse.emf.ecp.edit.internal.swt.util;


import org.eclipse.jface.dialogs.Dialog;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPDialogExecutor {

	private Dialog dialog;

	/**
	 * @param dialog
	 */
	public ECPDialogExecutor(Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @param codeResult
	 */
	public abstract void handleResult(int codeResult);

	public void execute() {
		DialogOpener.openDialog(dialog, this);
	}
}
