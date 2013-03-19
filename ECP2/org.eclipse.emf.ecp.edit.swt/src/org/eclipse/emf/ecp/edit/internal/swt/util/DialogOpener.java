/**
 * 
 */
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.ecp.edit.internal.swt.Activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;

/**
 * @author Eugen Neufeld
 * 
 */
public class DialogOpener {

	public static void openDialog(Dialog dialog, ECPDialogExecutor callBack) {
		DialogWrapper wrapper = null;
		IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.edit.swt.dialogWrapper");
		for (IConfigurationElement e : controls) {
			try {
				wrapper = (DialogWrapper) e.createExecutableExtension("class");
				break;
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}
		if (wrapper == null) {
			callBack.handleResult(dialog.open());
		}
		wrapper.openDialog(dialog, callBack);
	}
}
