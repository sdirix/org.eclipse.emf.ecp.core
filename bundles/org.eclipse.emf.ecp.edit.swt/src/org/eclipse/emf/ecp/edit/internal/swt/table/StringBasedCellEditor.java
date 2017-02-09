/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.emf.ecp.edit.spi.swt.table.ECPCellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Cell editor for string based cell editors that implements a custom
 * activate method that propagates changes on first key stroke.
 *
 */
public abstract class StringBasedCellEditor extends TextCellEditor implements ECPCellEditor {

	private Object initialValue;

	/**
	 * Default constructor.
	 */
	public StringBasedCellEditor() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param parent the parent {@link Composite}
	 */
	public StringBasedCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * Constructor.
	 *
	 * @param parent the parent {@link Composite}
	 * @param style SWT styling bits
	 */
	public StringBasedCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#activate(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
	 */
	@Override
	public void activate(ColumnViewerEditorActivationEvent event) {
		initialValue = text.getText();
		if (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
			&& Character.isLetterOrDigit(event.character)
			&& (getStyle() & SWT.READ_ONLY) == 0) {

			doSetValue(String.valueOf(event.character));
		}
		super.activate(event);
	}

	@Override
	protected void fireCancelEditor() {
		if (text != null && !text.isDisposed()
			&& text.getText() != null
			&& !text.getText().equals(initialValue)) {
			doSetValue(initialValue);
		}
		super.fireCancelEditor();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.TextCellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		super.doSetFocus();
		if (text.getText() != null) {
			text.setSelection(text.getText().length());
		}
	}
}
