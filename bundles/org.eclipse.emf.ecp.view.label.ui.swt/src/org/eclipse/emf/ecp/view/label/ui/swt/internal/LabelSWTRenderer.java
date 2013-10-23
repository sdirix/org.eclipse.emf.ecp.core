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
package org.eclipse.emf.ecp.view.label.ui.swt.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderer;
import org.eclipse.emf.ecp.view.label.model.VLabel;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * Label SWT Renderer Class for providing an own renderer for the Label element.
 * 
 * @author Eugen Neufeld
 * 
 */
public class LabelSWTRenderer implements CustomSWTRenderer {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends VElement>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, SWTRenderer<?>> renderers;
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, SWTRenderer<?>>();
		renderers.put(VLabel.class, new SWTLabelRenderer());
		return renderers;
	}

}
