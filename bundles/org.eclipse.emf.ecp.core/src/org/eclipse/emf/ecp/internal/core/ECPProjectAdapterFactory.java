/*******************************************************************************
 * Copyright (c) 2013 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import org.eclipse.core.runtime.IAdapterFactory;

/**
 * An {@link IAdapterFactory} that can adapt to {@link ECPProject}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ECPProjectAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (ECPProject.class.isAssignableFrom(adapterType)) {
			return ECPUtil.getECPProjectManager().getProject(adaptableObject);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return new Class[] { ECPProject.class };
	}

}
