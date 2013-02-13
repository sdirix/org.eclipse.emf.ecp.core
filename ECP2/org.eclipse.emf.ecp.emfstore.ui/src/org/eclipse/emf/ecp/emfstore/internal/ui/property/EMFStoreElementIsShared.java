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
package org.eclipse.emf.ecp.emfstore.internal.ui.property;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;

import org.eclipse.core.expressions.PropertyTester;

/**
 * This tests whether an element is shared.
 * 
 * @author Tobias Verhoeven
 */
public class EMFStoreElementIsShared extends PropertyTester {

	/** {@inheritDoc} */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		InternalProject project = (InternalProject) ECPProjectManager.INSTANCE.getProject(receiver);
		if (project != null) {
			ProjectSpace ps = EMFStoreProvider.INSTANCE.getProjectSpace(project);
			if (ps != null) {
				return Boolean.valueOf(ps.isShared()).equals(expectedValue);
			}
		}
		return false;
	}
}
