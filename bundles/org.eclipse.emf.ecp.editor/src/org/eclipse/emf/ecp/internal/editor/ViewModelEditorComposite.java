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

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.editor.IEditorCompositeProvider;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * 
 * @author Eugen Neufeld
 */
public class ViewModelEditorComposite implements IEditorCompositeProvider {

	private final ECPControlContext modelElementContext;
	private RendererContext<?> rendererContext;

	/**
	 * Default Constructor.
	 * 
	 * @param modelElementContext the {@link ECPControlContext}
	 */
	public ViewModelEditorComposite(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#createUI(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createUI(Composite parent) {
		final ModelRenderer<?> renderer = ModelRenderer.INSTANCE.getRenderer();

		Node<?> node = null;
		try {
			node = NodeBuilders.INSTANCE
				.build(modelElementContext.getViewContext().getViewModel(), modelElementContext);
			modelElementContext.getViewContext().registerViewChangeListener(node);
			rendererContext = renderer.render(node, parent);

		} catch (final NoRendererFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (final NoPropertyDescriptorFoundExeption ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		// TODO: remove cast via type parameterization
		final Composite tabContent = (Composite) rendererContext.getControl();
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tabContent.setLayoutData(gridData);
		// TODO: strange API
		rendererContext.addListener(node);
		rendererContext.triggerValidation();
		return tabContent;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#dispose()
	 */
	public void dispose() {
		rendererContext.dispose();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#updateLiveValidation()
	 */
	public void updateLiveValidation() {
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.editor.IEditorCompositeProvider#focus()
	 */
	public void focus() {
		((Composite) rendererContext.getControl()).setFocus();
	}
}
