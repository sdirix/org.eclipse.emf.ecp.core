/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Common interface for renderers.
 * 
 * @author emueller
 * 
 * @param <C>
 *            the actual type of the control being rendered
 * @param <R>
 *            a subtype of {@link VContainedElement} specifying the view model type to be rendered
 * @param <C>
 */
public interface ControlRenderer<R extends VElement, C> {

	/**
	 * Renders the given {@link Node}
	 * 
	 * @param model
	 *            the semantic view model representation of the control to be rendered
	 * @param controlContext
	 *            the control context containing the {@link EObject} to be rendered
	 * @param adapterFactoryItemDelegator
	 *            an {@link AdapterFactoryItemDelegator} instance that may be used during rendering
	 * @param initData
	 *            arbitrary data needed by the renderer to initialize itself, e.g. for SWT,
	 *            you must pass in the parent {@link org.eclipse.swt.widgets.Composite}
	 * @return
	 */
	// TODO: JAVADOC
	List<RenderingResultRow<C>> render(Node<R> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption;

}
