/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.swt.treemasterdetail.util;

import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailComposite;

/**
 * This interface allows to be notified when the {@link TreeMasterDetailComposite} finished rendering a detail pane.
 *
 * @author Lucas Koehler
 *
 */
public interface DetailPanelRenderingFinishedCallback {

	/**
	 * This method is called after the renderedObject has been rendered.
	 * 
	 * @param renderedObject The rendered Object
	 */
	void renderingFinished(Object renderedObject);
}
