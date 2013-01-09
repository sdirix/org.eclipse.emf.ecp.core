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

package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecp.ui.composites.ICompositeProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eugen Neufeld
 */
public abstract class AbstractUICallback {
	/**
	 * return code when the callback was closed successfully.
	 */
	public static final int OK = 0;

	/**
	 * return code when the callback was canceled.
	 */
	public static final int CANCEL = 1;

	private final Shell shell;

	private ICompositeProvider compositeProvider;

	private String title;
	private String description;
	private String settingSectionName;

	/**
	 * The constructor.
	 * 
	 * @param shell the {@link Shell} to use
	 */
	public AbstractUICallback(Shell shell) {
		this.shell = shell;
	}

	/**
	 * This sets the {@link ICompositeProvider} for this callback.
	 * 
	 * @param compositeProvider the {@link ICompositeProvider} to set
	 */
	public void setCompositeUIProvider(ICompositeProvider compositeProvider) {
		this.compositeProvider = compositeProvider;
	}

	/**
	 * Display an ErrorMessage.
	 * 
	 * @param title the title of the error dialog
	 * @param message the error message
	 */
	public void showError(String title, String message) {
		MessageDialog.openError(shell, title, message);
	}

	/**
	 * The shell to use for creation of the UI Callback.
	 * 
	 * @return the Shell
	 */
	protected Shell getShell() {
		return shell;
	}

	/**
	 * The {@link ICompositeProvider} to use when creating the UI Callback.
	 * 
	 * @return the {@link ICompositeProvider} to use
	 */
	protected ICompositeProvider getCompositeProvider() {
		return compositeProvider;
	}

	/**
	 * Returns the title to display.
	 * 
	 * @return the text to display as the title
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * Returns the description to display.
	 * 
	 * @return the text to display as the description
	 */
	protected String getDescription() {
		return description;
	}

	/**
	 * Returns the SettingSectionName to use.
	 * 
	 * @return the SettingSectionName to use for saving settings
	 */
	protected String getSettingSectionName() {
		return settingSectionName;
	}

	/**
	 * Sets the title to display in the callback.
	 * 
	 * @param title the text to display as the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the description to display in the callback.
	 * 
	 * @param description the text to display as the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the settingSectionName to use to save data.
	 * 
	 * @param settingSectionName the section name where to save the settings
	 */
	public void setSettingSectionName(String settingSectionName) {
		this.settingSectionName = settingSectionName;
	}

	/**
	 * This opens the callback. The result indicates which button was pressed to close the callback.
	 * 
	 * @return {@link #OK} when successful else {@link #CANCEL}
	 */
	public abstract int open();

	/**
	 * This triggers the dispose of the resources allocated by this callback.
	 */
	public abstract void dispose();
}
