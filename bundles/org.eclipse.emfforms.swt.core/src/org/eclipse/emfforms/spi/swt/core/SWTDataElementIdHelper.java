/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.swt.core;

import java.text.MessageFormat;

import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.swt.widgets.Control;

/**
 * Use this helper to ease setting the {@link VElement#getUuid() element} as the
 * {@link org.eclipse.swt.widgets.Widget#getData(String) SWT data}.
 *
 * @author Johannes Faltermeier
 * @since 1.9
 *
 */
public final class SWTDataElementIdHelper {

	/**
	 * Key constant for the element id.
	 */
	public static final String ELEMENT_ID_KEY = "org.eclipse.emfforms.elementId"; //$NON-NLS-1$

	private static final String ID_PATTERN = "{0}#{1}"; //$NON-NLS-1$
	private static final String CONTROL = "control"; //$NON-NLS-1$

	private SWTDataElementIdHelper() {
		// helper
	}

	/**
	 * Sets the element id with the control sub id on the given control.
	 *
	 * @param control the control to set the data on
	 * @param element the element including the id
	 */
	public static void setElementIdDataForVControl(final Control control, VControl element) {
		setElementIdDataWithSubId(control, element, CONTROL);
	}

	/**
	 * Sets the element id with the given sub id on the given control.
	 *
	 * @param control the control to set the data on
	 * @param element the element including the id
	 * @param subId the sub id
	 */
	public static void setElementIdDataWithSubId(final Control control, VElement element, String subId) {
		control.setData(ELEMENT_ID_KEY, MessageFormat.format(ID_PATTERN, element.getUuid(), subId));
	}
}
