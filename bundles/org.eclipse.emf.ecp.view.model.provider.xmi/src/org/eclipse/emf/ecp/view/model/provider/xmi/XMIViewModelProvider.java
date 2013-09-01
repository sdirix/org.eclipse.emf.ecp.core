/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.provider.xmi;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.view.model.View;

/**
 * Abstract class to implement a view provider reading the view model from an xmi file.
 * 
 * @author Jonas
 * 
 */
public abstract class XMIViewModelProvider implements IViewProvider {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.IViewProvider#generate(org.eclipse.emf.ecore.EObject)
	 */
	public View generate(EObject eObject) {
		final Resource resource = ViewModelFileExtensionsManager.loadResource(getXMIPath());
		final View result = EcoreUtil.copy((View) resource.getContents().get(0));
		return result;
	}

	/**
	 * 
	 * @return the URI of the xmi file containing the view model
	 */
	protected abstract URI getXMIPath();
}
