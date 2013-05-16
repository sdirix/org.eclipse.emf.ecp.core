/********************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 ********************************************************************************/
package org.eclipse.emf.ecp.internal.ui.model;

import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.observer.ECPPropertiesObserver;
import org.eclipse.emf.ecp.internal.core.util.Properties;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * @author Eike Stepper
 */
public class PropertiesContentProvider extends StructuredContentProvider<Properties> implements ECPPropertiesObserver {
	/**
	 * Constructor.
	 */
	public PropertiesContentProvider() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getElements(Object inputElement) {
		return getInput().getElements().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertiesChanged(ECPProperties properties, Collection<Entry<String, String>> oldProperties,
		Collection<Entry<String, String>> newProperties) {
		if (properties.equals(getInput())) {
			refreshViewer();
		}
	}

	@Override
	protected void connectInput(Properties input) {
		super.connectInput(input);
		input.addObserver(this);
	}

	@Override
	protected void disconnectInput(Properties input) {
		input.removeObserver(this);
		super.disconnectInput(input);
	}
}
