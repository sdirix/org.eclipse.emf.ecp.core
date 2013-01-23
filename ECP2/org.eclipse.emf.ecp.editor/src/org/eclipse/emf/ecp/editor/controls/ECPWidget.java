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

package org.eclipse.emf.ecp.editor.controls;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This abstract class desribes an ECPWidget. This class is used in controls.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPWidget {

	/**
	 * Create a new Widget.
	 * 
	 * @param toolkit the {@link FormToolkit} to use
	 * @param composite the parent {@link Composite}
	 * @param style the style for the created widget
	 * @return the created {@link Control}
	 */
	public abstract Control createWidget(final FormToolkit toolkit, final Composite composite, final int style);

	/**
	 * Sets the state of the widget to be either editable or not.
	 * 
	 * @param isEditable whether to set the widget editable
	 */
	public abstract void setEditable(boolean isEditable);

	/**
	 * This returns the created control.
	 * 
	 * @return the control created by this widget
	 */
	public abstract Control getControl();

	/**
	 * Called in order to dispose content of the widget.
	 */
	public abstract void dispose();

	/**
	 * Called to set the databinding for this control.
	 * 
	 * @param modelValue the modelValue to use
	 * @param controlDecoration the {@link ControlDecoration}
	 */
	public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);
}
