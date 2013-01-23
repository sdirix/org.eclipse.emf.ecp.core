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

package org.eclipse.emf.ecp.internal.editor.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * The widget implementation for editing a Double value.
 * 
 * @author Eugen Neufeld
 */
public class DoubleWidget extends AbstractTextWidget<Double> {
	/**
	 * Constructor for the {@link DoubleWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
	 * @param eObject the {@link EObject} for this widget
	 */
	public DoubleWidget(DataBindingContext dbc, EditingDomain editingDomain, EObject eObject) {
		super(dbc, editingDomain, eObject);
	}

	@Override
	protected Double convertStringToModel(String s) {
		return Double.parseDouble(s);
	}

	@Override
	protected boolean validateString(String s) {
		/*
		 * Do not perform any validation here, since a double can be represented with characters which include 'E', 'f'
		 * or
		 * 'd'. Furthermore if values become to be, 'Infinity' is also a valid value.
		 */
		return true;
	}

	@Override
	protected String convertModelToString(Double t) {
		return Double.toString(t);
	}

	@Override
	protected void postValidate(String text) {
		try {
			setUnvalidatedString(Double.toString(Double.parseDouble(text)));
		} catch (NumberFormatException e) {
			setUnvalidatedString(Double.toString(getDefaultValue()));
		}
	}

	@Override
	protected Double getDefaultValue() {
		return 0.0;
	}
}
