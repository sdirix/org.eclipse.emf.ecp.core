/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A String cell editor which displays strings.
 * 
 * @author Eugen Neufeld
 * 
 */
public class StringCellEditor extends TextCellEditor implements ECPCellEditor {

	/**
	 * Default constructor.
	 */
	public StringCellEditor() {
	}

	/**
	 * A constructor which takes only a parent.
	 * 
	 * @param parent the {@link Composite} to use as a parent.
	 */
	public StringCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * A constructor which takes the parent and the style.
	 * 
	 * @param parent the {@link Composite} to use as a parent
	 * @param style the Style to set
	 */
	public StringCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getValueProperty()
	 */
	public IValueProperty getValueProperty() {
		return CellEditorProperties.control().value(WidgetProperties.text(SWT.FocusOut));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#instantiate(org.eclipse.emf.ecore.EStructuralFeature,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	public void instantiate(EStructuralFeature feature, ViewModelContext viewModelContext) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getFormatedString(java.lang.Object)
	 */
	public String getFormatedString(Object value) {
		if (value == null) {
			return ""; //$NON-NLS-1$
		}
		return String.valueOf(value);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getColumnWidthWeight()
	 */
	public int getColumnWidthWeight() {
		return 100;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getTargetToModelStrategy()
	 */
	public UpdateValueStrategy getTargetToModelStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getModelToTargetStrategy()
	 */
	public UpdateValueStrategy getModelToTargetStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#setEditable(boolean)
	 */
	public void setEditable(boolean editable) {
		if (text != null) {
			text.setEditable(editable);
		}
	}

}
