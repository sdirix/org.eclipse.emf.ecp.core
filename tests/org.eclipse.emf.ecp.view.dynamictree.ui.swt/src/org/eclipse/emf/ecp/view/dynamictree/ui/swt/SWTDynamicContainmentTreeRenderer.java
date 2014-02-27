/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 */
package org.eclipse.emf.ecp.view.dynamictree.ui.swt;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;

/**
 * SWT renderer for {@link DynamicContainmentTree}s.
 * 
 * @author emueller
 */
public class SWTDynamicContainmentTreeRenderer extends SWTContainerRenderer<DynamicContainmentTree> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getCustomVariant()
	 */
	@Override
	protected String getCustomVariant() {
		// TODO Auto-generated method stub
		return "testTree";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getChildren()
	 */
	@Override
	protected Collection<VContainedElement> getChildren() {
		// TODO Auto-generated method stub
		return Collections.singleton(getVElement().getComposite());
	}

}
