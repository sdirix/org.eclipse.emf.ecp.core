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

import org.eclipse.emf.ecp.editor.controls.ECPWidget;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * Abstract super class for AttributeWidgets providing a typical constructor and getter methods for the set values.
 * 
 * @author Eugen Neufeld
 */
public abstract class ECPAttributeWidget extends ECPWidget {
	private final DataBindingContext dbc;
	private final EditingDomain editingDomain;

	/**
	 * Constructor for the {@link ECPAttributeWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
	 */
	public ECPAttributeWidget(DataBindingContext dbc, EditingDomain editingDomain) {
		this.dbc = dbc;
		this.editingDomain = editingDomain;
	}

	/**
	 * Returns the {@link DataBindingContext} set in the constructor.
	 * 
	 * @return the {@link DataBindingContext}
	 */
	protected DataBindingContext getDataBindingContext() {
		return dbc;
	}

	/**
	 * Returns the {@link EditingDomain} set in the constructor.
	 * 
	 * @return the {@link EditingDomain}
	 */
	protected EditingDomain getEditingDomain() {
		return editingDomain;
	}
}
