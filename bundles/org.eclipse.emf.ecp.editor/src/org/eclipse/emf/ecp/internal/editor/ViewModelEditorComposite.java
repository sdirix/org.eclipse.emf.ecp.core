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
import org.eclipse.emf.ecp.editor.IEditorCompositeProvider;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.internal.ECPSWTViewRendererImpl;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * 
 * @author Eugen Neufeld
 */
public class ViewModelEditorComposite implements IEditorCompositeProvider {

	private final ECPControlContext modelElementContext;
	private ECPSWTView swtView;
	private final View view;

	/**
	 * Default Constructor.
	 * 
	 * @param modelElementContext the {@link ECPControlContext}
	 */
	public ViewModelEditorComposite(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;
		view = ViewProviderHelper.getView(modelElementContext.getModelElement());
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#createUI(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createUI(Composite parent) {
		try {
			swtView = ECPSWTViewRendererImpl.render(parent, modelElementContext, view);
			return (Composite) swtView.getSWTControl();
		} catch (final ECPRendererException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#dispose()
	 */
	public void dispose() {
		swtView.dispose();

	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#updateLiveValidation()
	 */
	public void updateLiveValidation() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#focus()
	 */
	public void focus() {
		swtView.getSWTControl().setFocus();
	}

}
