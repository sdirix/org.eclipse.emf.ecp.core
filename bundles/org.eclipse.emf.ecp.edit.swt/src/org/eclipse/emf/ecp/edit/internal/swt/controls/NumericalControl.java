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
 * Edgar Mueller - refactored control to respect locale settings
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * This class is used as a common class for all number controls.
 * 
 * @author Eugen Neufeld
 * @author emueller
 */
public class NumericalControl extends AbstractTextControl {

	/**
	 * Constructor for a String control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public NumericalControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	@Override
	protected int getTextWidgetStyle() {
		return super.getTextWidgetStyle() | SWT.RIGHT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.AbstractTextControl#getTextVariantID()
	 */
	@Override
	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_control_swt_number";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		// TODO language
		return "No number set! Click to set number."; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset number";
	}

	@Override
	protected void customizeText(Text text) {
		super.customizeText(text);
		text.setMessage(getFormatText());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.AbstractTextControl#bindValue()
	 */
	@Override
	public Binding bindValue() {
		// TODO: FocusOut doesn't seem to fire in case the same invalid text is entered twice
		IObservableValue value = SWTObservables.observeText(getText(), SWT.FocusOut);
		final Binding binding = getDataBindingContext().bindValue(value, getModelValue(),
			new NumericalTargetToModelUpdateStrategy(), new NumericalModelToTargetUpdateStrategy());
		return binding;
	}

	private Class<?> getInstanceClass() {
		return getStructuralFeature().getEType().getInstanceClass();
	}

	private String getFormatText() {

		if (isInteger()) {
			return "The format is '#'.";
		} else if (isDouble()) {
			return "The format is '#.#'.";
		}

		return "";
	}

	private boolean isDouble() {
		Class<?> instanceClass = getInstanceClass();
		if (instanceClass.isPrimitive()) {
			try {
				return Double.class.getField("TYPE").get(null).equals(instanceClass);
			} catch (IllegalArgumentException ex) {
				Activator.logException(ex);
			} catch (SecurityException ex) {
				Activator.logException(ex);
			} catch (IllegalAccessException ex) {
				Activator.logException(ex);
			} catch (NoSuchFieldException ex) {
				Activator.logException(ex);
			}
		} else if (BigDecimal.class.isAssignableFrom(instanceClass)) {
			return true;
		} else if (Double.class.isAssignableFrom(instanceClass)) {
			return true;
		}

		return false;
	}

	private boolean isInteger() {
		Class<?> instanceClass = getInstanceClass();
		if (instanceClass.isPrimitive()) {
			try {
				return Integer.class.getField("TYPE").get(null).equals(instanceClass);
			} catch (IllegalArgumentException ex) {
				Activator.logException(ex);
			} catch (SecurityException ex) {
				Activator.logException(ex);
			} catch (IllegalAccessException ex) {
				Activator.logException(ex);
			} catch (NoSuchFieldException ex) {
				Activator.logException(ex);
			}
		} else if (BigInteger.class.isAssignableFrom(instanceClass)) {
			return true;
		} else if (Integer.class.isAssignableFrom(instanceClass)) {
			return true;
		}

		return false;
	}

	// TODO: Remarks EM:
	// NumericalModelToTargetUpdateStrategy and NumericalTargetToModelUpdateStrategy
	// both use the same format, when converting values form the model to the target
	// and vice versa in case the value is auto-corrected by the conversion and needs
	// to be displayed again. A helper class encapsulating this common format would be nice.
	private class NumericalModelToTargetUpdateStrategy extends ModelToTargetUpdateStrategy {

		@Override
		public Object convertValue(Object value) {
			final NumberFormat format = NumberFormat.getInstance(getModelElementContext().getLocale());
			format.setGroupingUsed(false);
			IConverter converter = new Converter(getInstanceClass(), String.class) {
				public Object convert(Object toObject) {
					return format.format(toObject);
				}
			};
			return converter.convert(value);
		}
	}

	private class NumericalTargetToModelUpdateStrategy extends TargetToModelUpdateStrategy {

		private NumberFormat format;

		NumericalTargetToModelUpdateStrategy() {
			super();
			format = NumberFormat.getInstance();
			format.setGroupingUsed(false);
		}

		@Override
		public Format getLocaleFormat() {
			NumberFormat localeFormat = NumberFormat.getInstance(getModelElementContext().getLocale());
			localeFormat.setGroupingUsed(false);
			return localeFormat;
		}

		@Override
		protected Object convertValue(final Object value) {

			if (isDouble()) {
				IConverter toDouble = new Converter(String.class, getInstanceClass()) {

					public Object convert(Object fromObject) {
						try {
							return new Double(format.parse((String) fromObject).doubleValue());
						} catch (ParseException ex) {
							return revertToOldValue(value);
						}
					}
				};

				return toDouble.convert(value);
			}

			// must be integer
			IConverter toInteger = new Converter(String.class, getInstanceClass()) {
				public Object convert(Object fromObject) {
					try {
						return new Integer(format.parse((String) fromObject).intValue());
					} catch (ParseException ex) {
						return revertToOldValue(value);
					}
				}
			};

			return toInteger.convert(value);
		}
	}

	private Object revertToOldValue(final Object value) {

		if (getStructuralFeature().getDefaultValue() == null && (value == null || value.equals(""))) {
			return null;
		}

		Object result = getModelValue().getValue();

		MessageDialog messageDialog = new MessageDialog(getText().getShell(), "Invalid Number", null,
			"The Number you have entered is invalid. The value will be unset.", MessageDialog.ERROR,
			new String[] { JFaceResources.getString(IDialogLabelKeys.OK_LABEL_KEY) }, 0);

		new ECPDialogExecutor(messageDialog) {
			@Override
			public void handleResult(int codeResult) {

			}
		}.execute();

		if (result == null) {
			getText().setText("");
		} else {
			getText().setText(result.toString());
		}

		if (getStructuralFeature().isUnsettable() && result == null) {
			showUnsetLabel();
			return SetCommand.UNSET_VALUE;
		}
		return result;
	}
}
