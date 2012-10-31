/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
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

package org.eclipse.emf.ecp.ui.common;

/**
 * @author Eugen Neufeld
 */
public abstract class AbstractUICallback {

	public abstract void setCompositeUIProvider(ICompositeProvider uiProvider);

	/**
	 * return code when the callback was closed successfully
	 */
	public static final int OK = 0;

	/**
	 * return code when the callback was canceled
	 */
	public static final int CANCEL = 1;

	/**
	 * the abstract method, which return
	 * 
	 * @return {@link #OK} when successful else {@link #CANCEL}
	 */
	public abstract int open();

	public abstract void showError(String title, String message);
}
