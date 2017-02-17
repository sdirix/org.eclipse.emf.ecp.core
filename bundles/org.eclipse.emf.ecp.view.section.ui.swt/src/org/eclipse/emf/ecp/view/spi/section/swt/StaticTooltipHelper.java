/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jonas Helming - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.section.swt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.view.spi.model.VAttachment;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.emfforms.spi.view.annotation.model.VAnnotation;
import org.eclipse.swt.widgets.Control;

/**
 * Helper to add static tooltips to controls, which are defined in view models.
 *
 * @author Jonas Helming
 *
 */
public final class StaticTooltipHelper {
	private static final String TOOLTIP_ANNOTATION = "tooltip"; //$NON-NLS-1$

	/**
	 * Default Constructor.
	 */
	private StaticTooltipHelper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Adds a static tooltip to a control. The text of the tooltip is derived from a annotation of the {@link VElement}
	 * using the key "tooltip"
	 *
	 * @param vElement the {@link VElement} to derive the tooltip from
	 * @param control the {@link Control} to add the tooltip to.
	 */
	public static void addToolStaticTipFromAnnotation(VElement vElement, Control control) {
		final VAnnotation toolTipAnnotation = getToolTipAnnotation(vElement);
		if (toolTipAnnotation == null) {
			return;
		}
		if (toolTipAnnotation.getValue() == null) {
			return;
		}
		if (toolTipAnnotation.getValue().isEmpty()) {
			return;
		}
		control.setToolTipText(toolTipAnnotation.getValue());
	}

	/**
	 * Retrieve the tool tip annotation from a {@link VElement} of null if there is none.
	 *
	 * @param vElement the vElement to retrieve the tooltip annotation from.
	 * @return the annotation defining the tooltip
	 */
	public static VAnnotation getToolTipAnnotation(VElement vElement) {
		final EList<VAttachment> attachments = vElement.getAttachments();
		if (attachments == null) {
			return null;
		}
		for (final VAttachment vAttachment : attachments) {
			if (VAnnotation.class.isAssignableFrom(vAttachment.getClass())) {
				final VAnnotation annotation = (VAnnotation) vAttachment;
				if (annotation.getKey() != null && annotation.getKey().equals(TOOLTIP_ANNOTATION)) {
					return annotation;
				}
			}
		}
		return null;
	}

}
