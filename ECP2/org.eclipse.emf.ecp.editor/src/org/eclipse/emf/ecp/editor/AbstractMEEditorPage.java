/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

/**
 * An abstract class for the MEEditorPages.
 * 
 * @author Eugen Neufeld
 */
public abstract class AbstractMEEditorPage {

	/**
	 * Create a page for the editor.
	 * 
	 * @param editor the editor to add the page to
	 * @param modelElementContext the {@link EditorModelelementContext}
	 * @return the {@link FormPage}
	 */
	public abstract FormPage createPage(FormEditor editor, EditorModelelementContext modelElementContext);

}
