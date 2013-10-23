/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Muenchen - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.swt.widgets.Composite;

public interface CompositeFactory extends ValidationListener {

	// CompositeFactory INSTANCE=CompositeFactoryImpl.INSTANCE;

	Composite getComposite(Composite composite,
		org.eclipse.emf.ecp.view.model.VContainedElement modelComposite,
		ECPControlContext context);

	void dispose();
	// void updateLiveValidation(EObject validationEObject);
}
