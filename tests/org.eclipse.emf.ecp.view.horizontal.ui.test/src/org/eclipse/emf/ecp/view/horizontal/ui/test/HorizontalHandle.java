/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.horizontal.ui.test;

import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * @author Jonas
 * 
 */
public class HorizontalHandle {

	private final Renderable horizontal;
	private Renderable firstChild;
	private Renderable secondChild;

	/**
	 * @param horizontal
	 */
	public HorizontalHandle(Renderable horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * @return the horizontal
	 */
	public Renderable getHorizontal() {
		return horizontal;
	}

	/**
	 * @param renderable
	 */
	public void addFirstChildToRoot(Renderable renderable) {
		firstChild = renderable;
		final ColumnComposite columnComposite = (ColumnComposite) horizontal;
		columnComposite.getComposites().add((Composite) renderable);

	}

	/**
	 * @param renderable
	 */
	public void addSecondChildToRoot(Renderable renderable) {
		setSecondChild(renderable);
		final ColumnComposite columnComposite = (ColumnComposite) horizontal;
		columnComposite.getComposites().add((Composite) renderable);

	}

	/**
	 * @return the first child
	 */
	public Renderable getFirstChild() {
		return firstChild;
	}

	/**
	 * @return the secondChild
	 */
	public Renderable getSecondChild() {
		return secondChild;
	}

	/**
	 * @param secondChild the secondChild to set
	 */
	public void setSecondChild(Renderable secondChild) {
		this.secondChild = secondChild;
	}

}
