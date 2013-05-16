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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import java.text.Format;

/**
 * This abstract class is used as a common superclass for all widgets that use a {@link Text} widget.
 * 
 * @author Eugen Neufeld
 */
public abstract class AbstractTextControl extends SingleControl {

	/**
	 * The {@link Text} holding the value.
	 */
	private Text text;
	private boolean doVerify;
	private ControlDecoration controlDecoration;

	/**
	 * Constructor for a control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public AbstractTextControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	@Override
	protected void updateValidationColor(Color color) {
		text.setBackground(color);
	}

	@Override
	protected void fillControlComposite(Composite composite) {
		doVerify = false;
		createTextWidget(composite);
		addControlDecoration(composite.getParent());
	}

	private void addControlDecoration(Composite composite) {
		controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP, composite);
		controlDecoration.hide();
		// TODO language
		controlDecoration.setDescriptionText("Invalid input");//$NON-NLS-1$
		controlDecoration.setShowHover(true);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
	}

	private void createTextWidget(Composite composite) {
		text = new Text(composite, getTextWidgetStyle());
		text.setLayoutData(getTextWidgetLayoutData());
		if (getStructuralFeature().isUnsettable()) {
			text.setMessage("<unset>");
		}
		text.setData(CUSTOM_VARIANT, getTextVariantID());
		customizeText(text);
	}

	/**
	 * This method allows to set custom values to the text field, e.g. a tooltip or a validation.
	 * 
	 * @param text the text widget to customize
	 */
	protected void customizeText(Text text) {

	}

	/**
	 * The VariantId to use e.g. for RAP
	 * 
	 * @return the String identifying this control
	 */
	protected abstract String getTextVariantID();

	/**
	 * The LayoutData for the created {@link Text} widget. Can be changed by the concrete classes.
	 * 
	 * @return the {@link GridData} to apply
	 */
	protected GridData getTextWidgetLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, true, true);
	}

	/**
	 * The style to apply to the text widget. This can be changed by the concrete classes.
	 * 
	 * @return the style to apply
	 */
	protected int getTextWidgetStyle() {
		return SWT.SINGLE | SWT.BORDER;
	}

	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { text };
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean isEditable) {
		text.setEditable(isEditable);
	}

	@Override
	public Binding bindValue() {
		IObservableValue value = SWTObservables.observeText(text, SWT.FocusOut);
		Binding binding = getDataBindingContext().bindValue(value, getModelValue(), new TargetToModelUpdateStrategy(),
			new ModelToTargetUpdateStrategy());
		return binding;
	}

	/**
	 * Sets the content of the SWT text control to the given string without calling {@link #validateString(String)}.
	 * 
	 * @param string
	 *            the content of the SWT Text control
	 */
	protected void setUnvalidatedString(String string) {
		boolean oldDoVerify = doVerify;
		doVerify = false;
		text.setText(string);
		doVerify = oldDoVerify;
	}

	@Override
	public void dispose() {
		controlDecoration.dispose();
		text.dispose();
		super.dispose();
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * An {@link EMFUpdateConvertValueStrategy} that encapsulates the converting
	 * of the actual value. Use this class to provide a specific context
	 * for the conversion of the value, but likewise enable it clients to modify
	 * the conversion behavior.
	 * 
	 * @author emueller
	 * 
	 */
	class EMFUpdateConvertValueStrategy extends EMFUpdateValueStrategy {

		/**
		 * Constructor.
		 */
		public EMFUpdateConvertValueStrategy() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.UpdateValueStrategy#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object value) {
			return convertValue(value);
		}

		protected Object convertValue(Object value) {
			return super.convert(value);
		}
	}

	protected class ModelToTargetUpdateStrategy extends EMFUpdateConvertValueStrategy {

		@Override
		public Object convert(Object value) {
			controlDecoration.hide();
			updateValidationColor(null);
			return convertValue(value);
		}

	}

	protected class TargetToModelUpdateStrategy extends EMFUpdateConvertValueStrategy {

		TargetToModelUpdateStrategy() {
			super();
		}

		@Override
		public IStatus validateAfterGet(Object value) {
			IStatus status = super.validateAfterGet(value);
			if (status.getSeverity() == IStatus.ERROR) {
				controlDecoration.show();
				controlDecoration.setDescriptionText(status.getMessage());
			} else {
				controlDecoration.hide();
				controlDecoration.setDescriptionText(null);
			}
			return status;
		}

		/**
		 * Returns the formatter specific for ecp
		 * By default this is <code>null</code>. In case clients override this,
		 * the update strategy will
		 * 
		 * @return
		 */
		protected Format getLocaleFormat() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object convert(Object value) {
			try {
				controlDecoration.hide();
				updateValidationColor(null);
				if (getStructuralFeature().isUnsettable() && value != null && String.class.isInstance(value)
					&& ((String) value).equals("")) {
					return null;
				}

				Format format = getLocaleFormat();
				Object convertedValue = convertValue(value);

				if (format != null && !convertedValue.equals(value)) {
					getText().setText(format.format(convertedValue));
				}

				return convertedValue;

			} catch (IllegalArgumentException e) {
				controlDecoration.show();
				updateValidationColor(getText().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
				controlDecoration.setDescriptionText("Invalid input " + e.getLocalizedMessage());
				throw e;
			}
		}

	}
}
