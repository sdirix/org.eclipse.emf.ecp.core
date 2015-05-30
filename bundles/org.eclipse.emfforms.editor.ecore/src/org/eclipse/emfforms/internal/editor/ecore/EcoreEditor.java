/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * cleme_000 - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.editor.ecore;

import org.eclipse.emfforms.spi.editor.GenericEditor;
import org.eclipse.emfforms.spi.editor.InitializeChildCallback;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailComposite;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailSWTFactory;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class extends the GenericEditor to provide customized features for Ecore files.
 */
public class EcoreEditor extends GenericEditor {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.editor.GenericEditor#getCreateElementCallback()
	 */
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new InitializeChildCallback();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.editor.GenericEditor#getEditorTitle()
	 */
	@Override
	protected String getEditorTitle() {
		return "Ecore Model Editor";
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.editor.GenericEditor#createTreeMasterDetail(org.eclipse.swt.widgets.Composite,
	 *      java.lang.Object, org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback)
	 */
	@Override
	protected TreeMasterDetailComposite createTreeMasterDetail(Composite composite, Object editorInput,
		CreateElementCallback createElementCallback) {
		return TreeMasterDetailSWTFactory.createTreeMasterDetail(composite, SWT.NONE, editorInput,
			new EcoreEditorTMDCustomization(createElementCallback));
	}
}
