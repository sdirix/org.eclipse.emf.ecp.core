/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.controls.ControlMessages;
import org.eclipse.emf.ecp.view.spi.core.swt.SWTControlRenderer;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

/**
 * A control which can handle {@link Date}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTDateTimeControlRenderer extends SWTControlRenderer {
	/**
	 * Default constructor.
	 */
	public SWTDateTimeControlRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	SWTDateTimeControlRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	@Override
	protected Binding[] createBindings(Control control, Setting setting) {
		final IObservableValue dateObserver = SWTObservables.observeSelection(((Composite) control).getChildren()[0]);
		final IObservableValue timeObserver = SWTObservables.observeSelection(((Composite) control).getChildren()[1]);
		final IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
		final Binding binding = getDataBindingContext().bindValue(target, getModelValue(setting));
		return new Binding[] { binding };
	}

	@Override
	protected Control createControl(Composite parent, Setting setting) {
		final Composite dateTimeComposite = new Composite(parent, SWT.NONE);
		dateTimeComposite.setBackground(parent.getBackground());
		GridLayoutFactory.fillDefaults().numColumns(3).spacing(2, 0).equalWidth(false)
			.applyTo(dateTimeComposite);

		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(dateTimeComposite);

		final DateTime dateWidget = new DateTime(dateTimeComposite, SWT.DATE | SWT.BORDER);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dateWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_dateTime_date"); //$NON-NLS-1$

		final DateTime timeWidget = new DateTime(dateTimeComposite, SWT.TIME | SWT.SHORT | SWT.BORDER);
		timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		timeWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_dateTime_time"); //$NON-NLS-1$

		return dateTimeComposite;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTControlRenderer#setValidationColor(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Color)
	 */
	@Override
	protected void setValidationColor(Control control, Color validationColor) {
		((Composite) control).getChildren()[0].setBackground(validationColor);
		((Composite) control).getChildren()[1].setBackground(validationColor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#getUnsetText()
	 */
	@Override
	protected String getUnsetText() {
		return ControlMessages.DateTimeControl_NoDateSetClickToSetDate;
	}

}
