/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Max Hohenegger (Bug 377561)
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget;

import org.eclipse.core.databinding.DataBindingContext;
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
 * A control used for primitive types that may be represented by a textual representation.
 * 
 * @author helming
 * @author emueller
 * @author Eugen Neufeld
 */
public abstract class MEAttributeControl extends AbstractMEControl implements IValidatableControl {

	private Label labelWidgetImage; // Label for diagnostic image

	private ControlDecoration controlDecoration;

	private ECPAttributeWidget widget;

	@Override
	protected Class<EAttribute> getEStructuralFeatureType() {
		return EAttribute.class;
	}

	@Override
	protected boolean isMulti() {
		return false;
	}

	@Override
	protected Control createControl(Composite parent, int style) {
		Composite composite = getToolkit().createComposite(parent, style);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(2, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

		labelWidgetImage = getToolkit().createLabel(composite, "    ");
		labelWidgetImage.setBackground(parent.getBackground());
		// FIXME
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(labelWidgetImage);

		widget = getAttributeWidget(getContext().getDataBindingContext());
		Control control = widget.createWidget(getToolkit(), composite, style);
		widget.setEditable(isEditable());

		controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
		controlDecoration.setDescriptionText("Invalid input");
		controlDecoration.setShowHover(true);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();

		IObservableValue model = EMFEditObservables.observeValue(getContext().getEditingDomain(), getModelElement(),
			getStructuralFeature());
		widget.bindValue(model, controlDecoration);
		return composite;
	}

	/**
	 * @return
	 */
	protected abstract ECPAttributeWidget getAttributeWidget(DataBindingContext dbc);

	/**
	 * {@inheritDoc}
	 **/
	public void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			Image image = org.eclipse.emf.ecp.internal.editor.Activator.getImageDescriptor("icons/validation_error.png")
				.createImage();
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

	@Override
	public void dispose() {
		super.dispose();
		labelWidgetImage.dispose();
		widget.dispose();
		controlDecoration.dispose();
	}

}
