/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * This class defines a SingleControl which is used for displaying {@link EStructuralFeature}s which have maximum 1
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class SingleControl extends SWTControl {

	private static final String VALIDATION_ERROR_ICON = "icons/validation_error.png";//$NON-NLS-1$

	// private static final Color VALIDATION_ERROR_BACKGROUND_COLOR=new Color(Display.getDefault(), 255, 140, 0);

	/**
	 * Constructor for a single control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public SingleControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			Image image = Activator.getImage(SingleControl.VALIDATION_ERROR_ICON);
			validationLabel.setImage(image);
			validationLabel.setToolTipText(diagnostic.getMessage());
			updateValidationColor(validationLabel.getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		} else {
			resetValidation();
		}
	}

	/**
	 * Allows controls to supply a second visual effect for controls on validation. The color to set is provided as the
	 * parameter.
	 * 
	 * @param color the color to set, null if the default background color should be set
	 */
	protected void updateValidationColor(Color color) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetValidation() {
		if (validationLabel == null || validationLabel.isDisposed()) {
			return;
		}
		validationLabel.setImage(null);
		validationLabel.setToolTipText("");
		updateValidationColor(null);
	}

	@Override
	public void dispose() {
		validationLabel.dispose();
	}
}
