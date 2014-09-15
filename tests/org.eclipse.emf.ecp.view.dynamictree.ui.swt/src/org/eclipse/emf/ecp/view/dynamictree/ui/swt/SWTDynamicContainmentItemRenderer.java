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

import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;

/**
 * SWT renderer for {@link DynamicContainmentItem}s.
 * 
 * @author emueller
 */
@SuppressWarnings("restriction")
public class SWTDynamicContainmentItemRenderer extends ContainerSWTRenderer<DynamicContainmentItem> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getCustomVariant()
	 */
	@Override
	protected String getCustomVariant() {
		// TODO Auto-generated method stub
		return "test";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getChildren()
	 */

	@Override
	protected Collection<VContainedElement> getChildren() {
		return Collections.singleton(getVElement().getComposite());
	}

}
