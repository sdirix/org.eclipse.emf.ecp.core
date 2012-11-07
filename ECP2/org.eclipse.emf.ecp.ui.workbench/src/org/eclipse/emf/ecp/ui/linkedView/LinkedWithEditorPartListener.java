package org.eclipse.emf.ecp.ui.linkedView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class LinkedWithEditorPartListener implements IPartListener2 {

	private final ILinkedWithEditorView view;

	public LinkedWithEditorPartListener(ILinkedWithEditorView view) {
		this.view = view;
	}

	public void partActivated(IWorkbenchPartReference ref) {
		if (ref.getPart(true) instanceof IEditorPart) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}

	public void partBroughtToTop(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}

	public void partOpened(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}

	public void partVisible(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}

		}
	}

	public void partClosed(IWorkbenchPartReference ref) {
	}

	public void partDeactivated(IWorkbenchPartReference ref) {
	}

	public void partHidden(IWorkbenchPartReference ref) {
	}

	public void partInputChanged(IWorkbenchPartReference ref) {
	}

}
