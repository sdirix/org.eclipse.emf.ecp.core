/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.internal.preview.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * @author Alexandra Buzila
 * 
 */
public class SessionSourceProvider extends AbstractSourceProvider {
	public final static String SESSION_STATE = "org.eclipse.emf.ecp.view.model.internal.preview.e3.sessionState"; //$NON-NLS-1$
	private final static String ENABLED = "enabled"; //$NON-NLS-1$
	private final static String DISABLED = "disabled"; //$NON-NLS-1$
	boolean enabled;

	/**
	 * 
	 */
	public SessionSourceProvider() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCurrentState() {
		final Map<String, String> currentState = new HashMap<String, String>(1);
		final String state = enabled ? ENABLED : DISABLED;
		currentState.put(SESSION_STATE, state);
		return currentState;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { SESSION_STATE };
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
		{
			return; // no change
		}
		this.enabled = enabled;
		final String currentState = enabled ? ENABLED : DISABLED;
		fireSourceChanged(ISources.WORKBENCH, SESSION_STATE, currentState);
	}

}
