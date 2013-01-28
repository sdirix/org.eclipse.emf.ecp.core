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
 * The widget implementation for editing an Integer value.
 * 
 * @author Eugen Neufeld
 */
public class IntegerWidget extends AbstractTextWidget<Integer> {

	/**
	 * Constructor for the {@link IntegerWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
	 * @param eObject the {@link EObject} of this widget
	 */
	public IntegerWidget(DataBindingContext dbc, EditingDomain editingDomain, EObject eObject) {
		super(dbc, editingDomain, eObject);
	}

	@Override
	protected Integer convertStringToModel(String s) {
		return Integer.parseInt(s);
	}

	@Override
	protected boolean validateString(String s) {
		try {
			// TODO regex?
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	@Override
	protected String convertModelToString(Integer t) {
		return Integer.toString(t);
	}

	@Override
	protected void postValidate(String text) {
		try {
			setUnvalidatedString(Integer.toString(Integer.parseInt(text)));
		} catch (NumberFormatException e) {
			setUnvalidatedString(Integer.toString(getDefaultValue()));
		}
	}

	@Override
	protected Integer getDefaultValue() {
		return 0;
	}

}
