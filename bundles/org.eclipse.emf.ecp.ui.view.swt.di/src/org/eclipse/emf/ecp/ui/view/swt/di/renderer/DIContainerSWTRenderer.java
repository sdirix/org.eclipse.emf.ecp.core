/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.di.renderer;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecp.ui.view.swt.di.util.SWTContextUtil;
import org.eclipse.emf.ecp.view.model.common.di.renderer.DIRendererUtil;
import org.eclipse.emf.ecp.view.model.common.di.renderer.POJORendererFactory;
import org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.swt.widgets.Composite;

/**
 * DI Renderer for {@link VContainer}.
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("restriction")
public class DIContainerSWTRenderer extends ContainerSWTRenderer<VContainer> {

	/**
	 * Named string for the children of {@link VContainer}.
	 */
	public static final String CHILDREN = "containerChildren"; //$NON-NLS-1$
	private Object pojo;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite getComposite(Composite parent) {
		final IEclipseContext childContext = DIRendererUtil.getContextForElement(getVElement(), getViewModelContext());
		SWTContextUtil.setAbstractSWTRendererObjects(childContext, getVElement(), getViewModelContext(),
			getSWTRendererFactory(), parent);
		childContext.set(CHILDREN, getChildren());
		childContext.set(VContainer.class, getVElement());
		return doGetComposite();
	}

	/**
	 * Invokes the get composite call on the pojo.
	 * 
	 * @return the composite
	 */
	protected Composite doGetComposite() {
		pojo = POJORendererFactory.getInstance().getRenderer(getVElement(), getViewModelContext());
		return (Composite) DIRendererUtil.render(pojo, getVElement(), getViewModelContext());
	}
}
