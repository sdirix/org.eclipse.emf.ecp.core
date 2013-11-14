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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecp.view.model.VView)
	 */
	public ECPSWTView render(Composite parent, EObject domainObject, VView viewModel) throws ECPRendererException {
		final ECPControlContext modelElementContext = createDefaultContext(domainObject, viewModel);
		return render(parent, modelElementContext, viewModel);
	}

	/**
	 * @param domainObject
	 * @return
	 */
	private ECPControlContext createDefaultContext(EObject domainObject, VView view) {
		return new DefaultControlContext(domainObject, view);
	}

	private static VView getView(EObject domainObject) {
		final VView view = ViewProviderHelper.getView(domainObject);
		return view;
	}

	public static ECPSWTView render(Composite parent, ECPControlContext modelElementContext, final VView view)
		throws ECPRendererException {
		final ModelRenderer<Control> renderer = ModelRenderer.INSTANCE.getRenderer();

		final ViewModelContext viewContext = new ViewModelContextImpl(view, modelElementContext.getModelElement());

		final List<RenderingResultRow<Control>> render = renderer.render(view, parent);

		// a view returns always a composite and always only one row with one control
		final Composite composite = (Composite) render.get(0).getControls().iterator().next();

		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);

		final ECPSWTView swtView = new ECPSWTViewImpl(composite, viewContext);
		return swtView;
	}

}
