/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * 
 * Leafs may override rule evaluation behavior of their parents.
 * 
 * @author emueller
 * 
 * @param <T>
 */
public final class Leaf<T extends Renderable> extends Node<T> {

	public Leaf(T model, ECPControlContext controlContext) {
		super(model, controlContext);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
