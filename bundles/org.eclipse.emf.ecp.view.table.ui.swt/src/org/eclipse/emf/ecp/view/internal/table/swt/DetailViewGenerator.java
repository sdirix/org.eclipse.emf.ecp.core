/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.table.swt;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;

/**
 * Helper class for generating a {@link VView} based on the columns of {@link VTableControl}.
 *
 * @author jfaltermeier
 *
 */
public final class DetailViewGenerator {

	private DetailViewGenerator() {
	}

	/**
	 * Generates a {@link VView} based on the columns of the given {@link VTableControl}.
	 *
	 * @param tableControl the control
	 * @return the generated view
	 */
	public static VView generateView(VTableControl tableControl) {
		final VView vView = VViewFactory.eINSTANCE.createView();
		for (final VDomainModelReference column : VTableDomainModelReference.class.cast(
			tableControl.getDomainModelReference()).getColumnDomainModelReferences()) {
			final VControl vControl = VViewFactory.eINSTANCE.createControl();
			vControl.setDomainModelReference(EcoreUtil.copy(column));
			boolean controlReadOnly = tableControl.isReadonly() || !tableControl.isEnabled();
			controlReadOnly = TableConfigurationHelper.isReadOnly(tableControl, column);
			vControl.setReadonly(controlReadOnly);
			vView.getChildren().add(vControl);
		}
		return vView;
	}
}
