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
package org.eclipse.emf.ecp.editor.controls;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.edit.EMFEditObservables;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * This is the abstract implementation of a single control.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class AbstractSingleControl extends AbstractControl implements IValidatableControl {

	private Label labelWidgetImage;// Label for diagnostic image
	private ECPWidget widget;
	private ControlDecoration controlDecoration;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#isMulti()
	 */
	@Override
	protected boolean isMulti() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#createControl(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected Control createControl(Composite parent, final int style) {
		final Composite composite = getToolkit().createComposite(parent, style);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(2 + getNumberOfAddtionalElements()).spacing(2, 0)
			.applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

		labelWidgetImage = getToolkit().createLabel(composite, "    ");
		labelWidgetImage.setBackground(parent.getBackground());

		// FIXME
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(labelWidgetImage);

		widget = getWidget();
		Control control = widget.createWidget(getToolkit(), composite, style);
		widget.setEditable(isEditable());

		controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
		controlDecoration.setDescriptionText("Invalid input");
		controlDecoration.setShowHover(true);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();

		IObservableValue model = EMFEditObservables.observeValue(getContext().getEditingDomain(), getContext()
			.getModelElement(), getStructuralFeature());
		widget.bindValue(model, controlDecoration);

		createControlActions(composite);

		return composite;
	}

	/**
	 * The number of additional elements being added after the widget.
	 * For example buttons for clearing the value.
	 * 
	 * @return the number of elements that will be added
	 */
	protected abstract int getNumberOfAddtionalElements();

	/**
	 * Creates and adds actions to show in the control after the widget.
	 * 
	 * @param composite the {@link Composite} to add the actions to
	 * 
	 */
	protected abstract void createControlActions(Composite composite);

	/**
	 * Creates the widget to use in this control.
	 * 
	 * @return the {@link ECPWidget} to use
	 */
	protected abstract ECPWidget getWidget();

	@Override
	public void dispose() {
		labelWidgetImage.dispose();
		controlDecoration.dispose();
		widget.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 **/
	public void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			Image image = org.eclipse.emf.ecp.internal.editor.Activator
				.getImageDescriptor("icons/validation_error.png").createImage();
			labelWidgetImage.setImage(image);
			labelWidgetImage.setToolTipText(diagnostic.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 **/
	public void resetValidation() {
		if (labelWidgetImage == null || labelWidgetImage.isDisposed()) {
			return;
		}
		labelWidgetImage.setImage(null);
		labelWidgetImage.setToolTipText("    ");

	}
}
