/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.table.nebula.grid;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.nebula.grid.GridControlDetailPanelRenderer;
import org.eclipse.emfforms.spi.swt.core.AbstractSWTRenderer;
import org.eclipse.emfforms.spi.swt.core.di.EMFFormsDIRendererService;
import org.osgi.service.component.annotations.Component;

/**
 * GridControlDetailPanelSWTRendererService which provides the GridControlDetailPanelRenderer.
 *
 * @author Mat Hansen <mhansen@eclipsesource.com>
 *
 */
@Component
public class GridControlDetailPanelSWTRendererService implements EMFFormsDIRendererService<VTableControl> {

	@Override
	public double isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VTableControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		if (DetailEditing.WITH_PANEL == VTableControl.class.cast(vElement).getDetailEditing()) {
			return 12;
		}
		return NOT_APPLICABLE;
	}

	@Override
	public Class<? extends AbstractSWTRenderer<VTableControl>> getRendererClass() {
		return GridControlDetailPanelRenderer.class;
	}

}
