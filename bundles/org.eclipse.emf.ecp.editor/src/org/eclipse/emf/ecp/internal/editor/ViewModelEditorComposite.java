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
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ViewModelEditorComposite implements IEditorCompositeProvider {

	private ECPControlContext modelElementContext;
	private RendererContext rendererContext;

	/**
	 * Default Constructor.
	 * 
	 * @param modelElementContext the {@link ECPControlContext}
	 */
	public ViewModelEditorComposite(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;
	}

	public Composite createUI(Composite parent) {
		View view = getView();
		ModelRenderer renderer = ModelRenderer.INSTANCE.getRenderer(new Object[] { parent });
		rendererContext = renderer.render(view, modelElementContext);
		// TODO: remove cast via type parameterization
		Composite tabContent = (Composite) rendererContext.getNode().getRenderedResult();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tabContent.setLayoutData(gridData);
		// TODO: strange api
		rendererContext.addListener(rendererContext.getNode());
		rendererContext.triggerValidation();
		return tabContent;
	}

	public void dispose() {
		rendererContext.dispose();
	}

	public void updateLiveValidation() {
		// TODO Auto-generated method stub

	}

	public void focus() {
		((Composite) rendererContext.getNode().getRenderedResult()).setFocus();
	}

	/**
	 * @return
	 */
	private View getView() {
		// IViewProvider viewProvider = new ViewProvider(eClass);
		// IViewProvider viewProvider = new PlayerViewProvider();
		// View view = viewProvider.generate(modelElementContext.getModelElement());
		// return view;
		int highestPrio = IViewProvider.NOT_APPLICABLE;
		IViewProvider selectedProvider = null;
		for (IViewProvider viewProvider : ViewProviderHelper.getViewProviders()) {
			int prio = viewProvider.canRender(modelElementContext.getModelElement());
			if (prio > highestPrio) {
				highestPrio = prio;
				selectedProvider = viewProvider;
			}
		}
		if (selectedProvider != null) {
			return selectedProvider.generate(modelElementContext.getModelElement());
		}
		return null;

	}
}
