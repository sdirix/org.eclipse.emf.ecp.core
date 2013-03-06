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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This class defines a SingleControl which is used for displaying {@link EStructuralFeature}s which have maximum 1
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class SingleControl extends SWTControl {

	private static final String VALIDATION_ERROR_ICON = "icons/validation_error.png";//$NON-NLS-1$
	private Label validationLabel;

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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.edit.controls.AbstractControl#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		int numColumns = 2;
		if (isEmbedded()) {
			numColumns--;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(10, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(composite);
		if (!isEmbedded()) {
			validationLabel = new Label(composite, SWT.NONE);
			// set the size of the label to the size of the image
			GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);
		}

		Composite innerComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(innerComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(innerComposite);
		fillInnerComposite(innerComposite);
		setEditable(isEditable());
		bindValue();
		return composite;
	}

	/**
	 * This method must be overridden by concrete classes. Here the widget displaying the data is added to the
	 * composite.
	 * 
	 * @param composite the {@link Composite} to add the widget to
	 */
	protected abstract void fillInnerComposite(Composite composite);

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
