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
 ******************************************************************************/
package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.internal.editor.EditorFactoryImpl;

import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Factory class to create the FormEditorComposite.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface EditorFactory {
	/**
	 * The instance of the factory.
	 */
	EditorFactory INSTANCE = EditorFactoryImpl.INSTANCE;

	/**
	 * Instantiate the editorComposite.
	 * 
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param toolkit the {@link FormToolkit} to use
	 * @return the {@link IEditorCompositeProvider}
	 */
	IEditorCompositeProvider getEditorComposite(EditModelElementContext modelElementContext, FormToolkit toolkit);
}
