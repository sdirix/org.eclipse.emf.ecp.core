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

package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.composites.ICompositeProvider;

import org.eclipse.jface.wizard.Wizard;

/**
 * An abstract class for all Wizard with only one page that use an {@link ICompositeProvider} to create this page.
 * 
 * @param <T> the {@link ICompositeProvider} to use during build
 * 
 * @author Eugen Neufeld
 */
public abstract class ECPWizard<T extends ICompositeProvider> extends Wizard {
	private T uiProvider;

	/**
	 * Sets the {@link ICompositeProvider} for this wizard.
	 * 
	 * @param compositeProvider the {@link ICompositeProvider} to set
	 */
	@SuppressWarnings("unchecked")
	public void setCompositeProvider(ICompositeProvider compositeProvider) {
		this.uiProvider = (T) compositeProvider;
	}

	/**
	 * Returns the set {@link ICompositeProvider}.
	 * 
	 * @return the {@link ICompositeProvider} that was set
	 */

	protected T getCompositeProvider() {
		return uiProvider;
	}
}
