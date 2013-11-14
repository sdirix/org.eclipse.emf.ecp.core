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

import org.eclipse.emf.ecp.view.model.VElement;

public interface ModelRenderer<C> {

	ModelRendererFactory INSTANCE = new ModelRendererFactoryImpl();

	<R extends VElement> List<RenderingResultRow<C>> render(R vElement, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;

}
