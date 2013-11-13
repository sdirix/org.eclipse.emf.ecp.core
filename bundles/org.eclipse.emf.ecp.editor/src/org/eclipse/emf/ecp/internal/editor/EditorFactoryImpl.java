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

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.editor.EditorFactory;
import org.eclipse.emf.ecp.editor.IEditorCompositeProvider;

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
	public IEditorCompositeProvider getEditorComposite(ECPControlContext modelElementContext) {
		// return new FormEditorComposite(modelElementContext);
		return new ViewModelEditorComposite(modelElementContext);
	}

}
