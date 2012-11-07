package org.eclipse.emf.ecp.ui.linkedView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;

public interface ILinkedWithEditorView {

	void editorActivated(IEditorPart activatedEditor);
	IViewSite getViewSite();
}
