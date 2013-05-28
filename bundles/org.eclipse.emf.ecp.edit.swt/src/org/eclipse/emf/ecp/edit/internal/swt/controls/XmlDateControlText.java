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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a XMLDateControl. It is used to display values of type {@link XMLGregorianCalendar}. This control only
 * displays a date widget.
 * 
 * @author Eugen Neufeld
 * 
 */
public class XmlDateControlText extends AbstractTextControl {
	/**
	 * This is the default constructor.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} of the binding
	 * @param modelElementContext the {@link ECPControlContext}
	 * @param embedded whether this control is used embedded (e.g in another control)
	 */
	public XmlDateControlText(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_control_xmldate";
	}

	@Override
	protected String getUnsetLabelText() {
		// TODO language
		return "No date set! Click to set date."; //$NON-NLS-1$
	}

	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset date";
	}

	@Override
	protected void customizeText(Text text) {
		super.customizeText(text);
		text.setMessage(((SimpleDateFormat) setupFormat()).toPattern());
	}

	@Override
	protected void fillControlComposite(Composite composite) {
		super.fillControlComposite(composite);
		((GridLayout) composite.getLayout()).numColumns = 2;
		final Button button = new Button(composite, SWT.PUSH);
		button.setText("Datum wählen");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final Shell dialog = new Shell(getText().getShell(), SWT.NONE);
				dialog.setLayout(new GridLayout(1, false));

				dialog.setLocation(button.getParent().toDisplay(button.getLocation().x,
					button.getLocation().y + button.getSize().y));

				final DateTime calendar = new DateTime(dialog, SWT.CALENDAR | SWT.BORDER);
				XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) getModelValue().getValue();
				Calendar cal = Calendar.getInstance(getModelElementContext().getLocale());
				if (gregorianCalendar != null) {
					cal.setTime(gregorianCalendar.toGregorianCalendar().getTime());
				}
				calendar.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

				IObservableValue dateObserver = SWTObservables.observeSelection(calendar);
				final Binding binding = getDataBindingContext().bindValue(dateObserver, getModelValue(),
					new DateTargetToModelUpdateStrategy(), new DateModelToTargetUpdateStrategy());
				binding.updateModelToTarget();
				calendar.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						binding.dispose();
						dialog.close();
					}
				});
				// dialog.setFocus();
				calendar.addFocusListener(new FocusListener() {

					public void focusLost(FocusEvent event) {
						binding.dispose();
						dialog.close();
					}

					public void focusGained(FocusEvent event) {
						// TODO Auto-generated method stub

					}
				});
				dialog.pack();
				dialog.open();
			}

		});
	}

	@Override
	public Binding bindValue() {
		// TODO: FocusOut doesn't seem to fire in case the same invalid text is
		// entered twice
		IObservableValue value = SWTObservables.observeText(getText(), SWT.FocusOut);
		final Binding binding = getDataBindingContext().bindValue(value, getModelValue(),
			new DateTargetToModelUpdateStrategy(), new DateModelToTargetUpdateStrategy());
		return binding;
	}

	private class DateModelToTargetUpdateStrategy extends ModelToTargetUpdateStrategy {

		@Override
		public Object convertValue(Object value) {
			// final DecimalFormat format = (DecimalFormat) DecimalFormat
			// .getInstance(getModelElementContext().getLocale());
			// format.setGroupingUsed(false);
			// format.setParseIntegerOnly(isInteger());
			final DateFormat format = setupFormat();
			XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) value;
			if (gregorianCalendar == null) {
				return null;
			}
			Date date = gregorianCalendar.toGregorianCalendar().getTime();
			return format.format(date);
		}
	}

	private class DateTargetToModelUpdateStrategy extends TargetToModelUpdateStrategy {

		private DateFormat format;

		DateTargetToModelUpdateStrategy() {
			super();
			format = setupFormat();

		}

		@Override
		protected Object convertValue(final Object value) {
			try {
				Date date = null;
				if (String.class.isInstance(value)) {
					date = format.parse((String) value);
				} else if (Date.class.isInstance(value)) {
					date = (Date) value;
				} else if (value == null) {
					return value;
				}
				String formatedDate = format.format(date);
				getText().setText(formatedDate);

				Calendar targetCal = Calendar.getInstance();
				targetCal.setTime(date);
				XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
				cal.setYear(targetCal.get(Calendar.YEAR));
				cal.setMonth(targetCal.get(Calendar.MONTH) + 1);
				cal.setDay(targetCal.get(Calendar.DAY_OF_MONTH));

				cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

				return cal;
			} catch (DatatypeConfigurationException ex) {
				// Activator.logException(ex);
			} catch (ParseException ex) {
				return revertToOldValue(value);
			}
			return null;
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
				XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) result;
				Date date = gregorianCalendar.toGregorianCalendar().getTime();
				getText().setText(format.format(date));
			}

			if (getStructuralFeature().isUnsettable() && result == null) {
				showUnsetLabel();
				return SetCommand.UNSET_VALUE;
			}
			return result;
		}
	}

	private DateFormat setupFormat() {
		return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, getModelElementContext().getLocale());
	}
}
