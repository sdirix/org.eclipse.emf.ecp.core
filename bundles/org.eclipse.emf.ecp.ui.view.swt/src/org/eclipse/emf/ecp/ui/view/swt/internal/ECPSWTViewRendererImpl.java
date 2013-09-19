/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jonas
 * 
 */
public class ECPSWTViewRendererImpl implements ECPSWTViewRenderer {

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ECPRendererException
	 * 
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer#render(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public ECPSWTView render(Composite parent, EObject domainObject) throws ECPRendererException {
		return render(parent, domainObject, getView(domainObject));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer#render(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecp.view.model.View)
	 */
	public ECPSWTView render(Composite parent, EObject domainObject, View viewModel) throws ECPRendererException {
		final ECPControlContext modelElementContext = createDefaultContext(domainObject, viewModel);
		return render(parent, modelElementContext, viewModel);
	}

	/**
	 * @param domainObject
	 * @return
	 */
	private ECPControlContext createDefaultContext(EObject domainObject, View view) {
		return new DefaultControlContext(domainObject, view);
	}

	/**
	 * Renders a view with a model element context.
	 * For internal use only.
	 * 
	 * @param parent the parent swt composite
	 * @param modelElementContext the model element context
	 * @return a {@link ECPSWTView}
	 * @throws ECPRendererException if there is an exception during rendering
	 */
	public static ECPSWTView render(Composite parent, ECPControlContext modelElementContext)
		throws ECPRendererException {
		// final View view = getView(modelElementContext.getModelElement());
		final View view = (View) modelElementContext.getViewContext().getViewModel();
		return render(parent, modelElementContext, view);
	}

	private static View getView(EObject domainObject) {
		final View view = ViewProviderHelper.getView(domainObject);
		return view;
	}

	private static ECPSWTView render(Composite parent, ECPControlContext modelElementContext, final View view)
		throws ECPRendererException {
		final ModelRenderer<?> renderer = ModelRenderer.INSTANCE.getRenderer();

		Node<?> node = null;
		RendererContext<?> rendererContext;
		node = NodeBuilders.INSTANCE.build(view, modelElementContext);
		final ViewModelContext viewContext = modelElementContext.getViewContext();
		viewContext.registerViewChangeListener(node);
		rendererContext = renderer.render(node, parent);
		final Composite composite = (Composite) rendererContext.getControl();
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);
		final ECPSWTView swtView = new ECPSWTViewImpl(composite, rendererContext, viewContext);
		return swtView;
	}

}
