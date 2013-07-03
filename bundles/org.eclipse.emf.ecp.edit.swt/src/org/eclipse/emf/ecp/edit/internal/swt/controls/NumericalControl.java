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
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;

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
		return "org_eclipse_emf_ecp_control_numerical";
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
		final IObservableValue value = SWTObservables.observeText(getText(), SWT.FocusOut);
		final NumericalTargetToModelUpdateStrategy targetToModelStrategy = new NumericalTargetToModelUpdateStrategy();
		NumericalModelToTargetUpdateStrategy modelToTargetStrategy = new NumericalModelToTargetUpdateStrategy();
		final Binding binding = getDataBindingContext().bindValue(value, getModelValue(), targetToModelStrategy,
			modelToTargetStrategy);

		if (isEmbedded()) {
			// focus out is not fired if control is embedded;
			// use value change listener to get same behavior for control
			value.addValueChangeListener(new IValueChangeListener() {
				public void handleValueChange(ValueChangeEvent event) {
					Object newValue = event.diff.getNewValue();
					DecimalFormat format = NumericalHelper.setupFormat(getModelElementContext().getLocale(),
						getInstanceClass());
					try {
						Number number = format.parse((String) newValue);
						value.setValue(format.format(number));
						binding.updateTargetToModel();
					} catch (ParseException ex) {
						targetToModelStrategy.revertToOldValue(value);
					}
				}
			});
		}

		return binding;
	}

	private Class<?> getInstanceClass() {
		return getStructuralFeature().getEType().getInstanceClass();
	}

	private String getFormatText() {

		if (NumericalHelper.isInteger(getInstanceClass())) {
			return "The format is '#'.";
		} else if (NumericalHelper.isDouble(getInstanceClass())) {
			return "The format is '#.#'.";
		}

		return "";
	}

	// TODO: Remarks EM:
	// NumericalModelToTargetUpdateStrategy and NumericalTargetToModelUpdateStrategy
	// both use the same format, when converting values form the model to the target
	// and vice versa in case the value is auto-corrected by the conversion and needs
	// to be displayed again. A helper class encapsulating this common format would be nice.
	private class NumericalModelToTargetUpdateStrategy extends ModelToTargetUpdateStrategy {

		@Override
		public Object convertValue(Object value) {
			final DecimalFormat format = NumericalHelper.setupFormat(getModelElementContext().getLocale(),
				getInstanceClass());
			return format.format(value);
		}
	}

	private class NumericalTargetToModelUpdateStrategy extends TargetToModelUpdateStrategy {

		private DecimalFormat format;

		NumericalTargetToModelUpdateStrategy() {
			super();
			format = NumericalHelper.setupFormat(getModelElementContext().getLocale(), getInstanceClass());

		}

		@Override
		protected Object convertValue(final Object value) {

			try {
				Number number = null;
				if (value == null) {
					number = NumericalHelper.getDefaultValue(getInstanceClass());
				} else {
					ParsePosition pp = new ParsePosition(0);
					number = format.parse((String) value, pp);
					if (pp.getErrorIndex() != -1 || pp.getIndex() != ((String) value).length()) {
						return revertToOldValue(value);
					}
					if (NumericalHelper.isInteger(getInstanceClass())) {
						boolean maxValue = false;
						Class<?> instanceClass = getInstanceClass();
						String formatedValue = "";
						try {
							if (Integer.class.isAssignableFrom(instanceClass)
								|| Integer.class.getField("TYPE").get(null).equals(instanceClass)) {
								if (Integer.MAX_VALUE == number.intValue()) {
									maxValue = true;
									formatedValue = format.format(Integer.MAX_VALUE);
								}
							} else if (Long.class.isAssignableFrom(instanceClass)
								|| Long.class.getField("TYPE").get(null).equals(instanceClass)) {
								if (Long.MAX_VALUE == number.longValue()) {
									maxValue = true;
									formatedValue = format.format(Long.MAX_VALUE);
								}
							}
						} catch (IllegalArgumentException ex) {
							Activator.logException(ex);
						} catch (SecurityException ex) {
							Activator.logException(ex);
						} catch (IllegalAccessException ex) {
							Activator.logException(ex);
						} catch (NoSuchFieldException ex) {
							Activator.logException(ex);
						}

						if (maxValue) {
							getText().setText(formatedValue);
							return NumericalHelper.numberToInstanceClass(number, getInstanceClass());
						}
					}
				}
				String formatedNumber = "";
				if (number != null) {
					formatedNumber = format.format(number);
				}
				// if (number.toString().contains("E")
				// || ((String) value).matches("0*" + formatedNumber + "\\"
				// + format.getDecimalFormatSymbols().getDecimalSeparator() + "?0*")) {
				//
				// }
				// return revertToOldValue(value);
				getText().setText(formatedNumber);
				if (formatedNumber.length() == 0) {
					return null;
				}
				return NumericalHelper.numberToInstanceClass(format.parse(formatedNumber), getInstanceClass());
			} catch (ParseException ex) {
				return revertToOldValue(value);
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
				getText().setText(format.format(result));
			}

			if (getStructuralFeature().isUnsettable() && result == null) {
				showUnsetLabel();
				return SetCommand.UNSET_VALUE;
			}
			return result;
		}
	}

}
