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
package org.eclipse.emf.ecp.internal.editor;

import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.EditorFactory;
import org.eclipse.emf.ecp.editor.IEditorCompositeProvider;

import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The Implementation of the {@link EditorFactory}.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class EditorFactoryImpl implements EditorFactory {

	/**
	 * The instance to access from the EditorFactory.
	 */
	public static final EditorFactory INSTANCE = new EditorFactoryImpl();

	private EditorFactoryImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	public IEditorCompositeProvider getEditorComposite(EditModelElementContext modelElementContext, FormToolkit toolkit) {
		return new FormEditorComposite(modelElementContext, toolkit);
	}

}
