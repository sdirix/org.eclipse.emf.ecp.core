package org.eclipse.emf.ecp.ide.editor.view.control;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.view.editor.controls.ControlRootEClassControl;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ViewEditorIDEViewRootControl extends ControlRootEClassControl{

	public ViewEditorIDEViewRootControl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecp.view.editor.controls.ControlRootEClassControl#getInput()
	 */
	@Override
	protected Object getInput() {
		return selectECore();
	}
	
	private Object selectECore(){
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(Display.getDefault()
			.getActiveShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.setValidator(new ISelectionStatusValidator() {

			public IStatus validate(Object[] selection) {
				if (selection.length == 1) {
					if (selection[0] instanceof File) {
						File file = (File) selection[0];
						if (file.getType() == File.FILE) {
							return new Status(IStatus.OK, "org.eclipse.emf.ecp.ide.editor.view.control", IStatus.OK, null, null);
						}
					}
				}
				return new Status(IStatus.ERROR, "org.eclipse.emf.ecp.ide.editor.view.control", IStatus.ERROR, "Please Select a File",
					null);
			}
		});
		dialog.setTitle("Select XMI");

		if (dialog.open() == Dialog.OK) {
			if (dialog.getFirstResult() instanceof File) {
				File file = (File) dialog.getFirstResult();
				ResourceSet resourceSet = new ResourceSetImpl();
				Resource resource = resourceSet.createResource(URI.createPlatformResourceURI(file.getFullPath()
					.toString(), true));
				try {
					resource.load(null);
					return resource.getContents().get(0);
				} catch (IOException ex) {
					MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error", "Error parsing XMI-File!");
				}
			}
		}
		return null;
	}

}
