/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Clemens Elflein - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.editor.genmodel;

import org.eclipse.emfforms.spi.editor.GenericEditor;

/**
 * The Genmodel Editor.
 *
 * @author Clemens Elflein
 */
public class GenModelEditor extends GenericEditor {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#getTitle()
	 */
	@Override
	public String getEditorTitle() {
		return "Genmodel Editor";
	}
}