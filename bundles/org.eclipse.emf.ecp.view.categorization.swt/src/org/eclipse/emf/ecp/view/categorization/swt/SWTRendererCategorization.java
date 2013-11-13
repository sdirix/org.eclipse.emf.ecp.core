/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.categorization.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderer;
import org.eclipse.emf.ecp.view.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.categorization.model.VCategory;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * The Class SWTRendererGroup.
 */
public class SWTRendererCategorization implements CustomSWTRenderer {

	/**
	 * Instantiates a new SWT renderer group.
	 */
	public SWTRendererCategorization() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends VElement>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends VElement>, SWTRenderer<?>> map = new HashMap<Class<? extends VElement>, SWTRenderer<?>>();
		map.put(VCategorization.class, SWTCategorizationRenderer.INSTANCE);
		map.put(VCategory.class, SWTCategoryRenderer.INSTANCE);
		map.put(VCategorizationElement.class, SWTCategorizationElementRenderer.INSTANCE);
		return map;
	}

}
