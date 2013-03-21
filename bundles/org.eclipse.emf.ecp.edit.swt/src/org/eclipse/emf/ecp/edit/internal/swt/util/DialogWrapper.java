/**
 * 
 */
package org.eclipse.emf.ecp.edit.internal.swt.util;


import org.eclipse.jface.dialogs.Dialog;

/**
 * @author Eugen Neufeld
 * 
 */
public interface DialogWrapper {

	void openDialog(final Dialog dialog, final ECPDialogExecutor callBack);
}
