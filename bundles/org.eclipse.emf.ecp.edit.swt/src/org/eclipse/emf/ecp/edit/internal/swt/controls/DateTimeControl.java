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
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/**
 * This class defines a DateTimeControl which is used for displaying {@link EStructuralFeature}s which have a date
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public class DateTimeControl extends SingleControl {

	private DateTime dateWidget;

	private DateTime timeWidget;

	/**
	 * Constructor for a dateTime control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public DateTimeControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	@Override
	protected void fillControlComposite(Composite dateTimeComposite) {
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(4).applyTo(dateTimeComposite);
		createDateAndTimeWidget(dateTimeComposite);

	}

	/**
	 * This method creates the date widget, the time widget and the delete button.
	 * 
	 * @param composite the parent {@link Composite}
	 */
	private void createDateAndTimeWidget(Composite composite) {
		int numColumns = 3;
		if (isEmbedded()) {
			numColumns = 2;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(2, 0).equalWidth(false).applyTo(composite);

		dateWidget = new DateTime(composite, SWT.DATE);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dateWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_swt_dateTime_date");

		timeWidget = new DateTime(composite, SWT.TIME | SWT.SHORT);
		timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		timeWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_swt_dateTime_time");
	}

	@Override
	public void setEditable(boolean isEditable) {
		dateWidget.setEnabled(isEditable);
		timeWidget.setEnabled(isEditable);
	}

	@Override
	public void bindValue() {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
		IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
		getDataBindingContext().bindValue(target, getModelValue());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getHelpText()
	 */
	@Override
	protected String getHelpText() {
		return "This is a date-time control. The date field is DD.MM.YYYY and the time field is mm:hh";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		// TODO language
		return "No date set! Click to set date."; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset Date";
	}
}
