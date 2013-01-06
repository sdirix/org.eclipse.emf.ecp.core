/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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

package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 */
public class StringWidget extends AbstractTextWidget<String> {

	private IItemPropertyDescriptor itemPropertyDescriptor;

	/**
	 * Constructor for StringWidgets.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param iItemPropertyDescriptor the {@link IItemPropertyDescriptor} to identify multiline properties
	 * @param eObject the {@link EObject} to check the multiline for
	 */
	public StringWidget(DataBindingContext dbc, EditingDomain editingDomain,
		IItemPropertyDescriptor iItemPropertyDescriptor, EObject eObject) {
		super(dbc, editingDomain, eObject);
		itemPropertyDescriptor = iItemPropertyDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	protected void createTextWidget(FormToolkit toolkit, Composite composite, int style) {
		int textStyle = style | SWT.BORDER;
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (itemPropertyDescriptor.isMultiLine(getEObject())) {
			textStyle = textStyle | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
			gridData.heightHint = 200;
		} else {
			textStyle = textStyle | SWT.SINGLE;
		}
		text = toolkit.createText(composite, new String(), textStyle);
		text.setLayoutData(gridData);
	}

	/** {@inheritDoc} */
	@Override
	protected String convertStringToModel(String s) {
		return s;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean validateString(String s) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected String convertModelToString(String t) {
		return t;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDefaultValue() {
		return "";
	}

	/** {@inheritDoc} */
	@Override
	protected void postValidate(String text) {
		// do nothing

	}

}
