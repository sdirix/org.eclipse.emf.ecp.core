/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.linkedView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class LinkedWithEditorPartListener implements IPartListener2 {

	private final ILinkedWithEditorView view;

	public LinkedWithEditorPartListener(ILinkedWithEditorView view) {
		this.view = view;
	}
	/**{@inheritDoc} */
	public void partActivated(IWorkbenchPartReference ref) {
		if (ref.getPart(true) instanceof IEditorPart) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}
	/**{@inheritDoc} */
	public void partBroughtToTop(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}
	/**{@inheritDoc} */
	public void partOpened(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}
	/**{@inheritDoc} */
	public void partVisible(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}

		}
	}
	/**{@inheritDoc} */
	public void partClosed(IWorkbenchPartReference ref) {
	}
	/**{@inheritDoc} */
	public void partDeactivated(IWorkbenchPartReference ref) {
	}
	/**{@inheritDoc} */
	public void partHidden(IWorkbenchPartReference ref) {
	}
	/**{@inheritDoc} */
	public void partInputChanged(IWorkbenchPartReference ref) {
	}

}
