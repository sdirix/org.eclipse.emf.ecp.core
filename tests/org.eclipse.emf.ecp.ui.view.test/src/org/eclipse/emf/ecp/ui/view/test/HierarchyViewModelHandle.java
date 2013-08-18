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
package org.eclipse.emf.ecp.ui.view.test;

import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * @author Jonas
 * 
 */
public class HierarchyViewModelHandle {

	private final Renderable root;
	private Renderable firstChild;
	private Renderable secondChild;

	private Renderable firstFirstChild;
	private Renderable firstSecondChild;
	private Renderable secondFirstChild;
	private Renderable secondSecondChild;

	/**
	 * @param root
	 */
	public HierarchyViewModelHandle(Renderable root) {
		this.root = root;
	}

	/**
	 * @return the horizontal
	 */
	public Renderable getRoot() {
		return root;
	}

	/**
	 * @param renderable
	 */
	public void addFirstChildToRoot(Renderable renderable) {
		firstChild = renderable;
		final CompositeCollection collection = (CompositeCollection) root;
		collection.getComposites().add((Composite) renderable);

	}

	/**
	 * @param renderable
	 */
	public void addSecondChildToRoot(Renderable renderable) {
		setSecondChild(renderable);
		final CompositeCollection collection = (CompositeCollection) root;
		collection.getComposites().add((Composite) renderable);

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

	/**
	 * 
	 */
	public void addFirstChildToFirstChild(Composite composite) {
		final CompositeCollection collection = (CompositeCollection) getFirstChild();
		collection.getComposites().add(composite);
		setFirstFirstChild(composite);
	}

	/**
	 * @return the firstFirstChild
	 */
	public Renderable getFirstFirstChild() {
		return firstFirstChild;
	}

	/**
	 * @param firstFirstChild the firstFirstChild to set
	 */
	public void setFirstFirstChild(Renderable firstFirstChild) {
		this.firstFirstChild = firstFirstChild;
	}

	/**
	 * @return the firstSecondChild
	 */
	public Renderable getFirstSecondChild() {
		return firstSecondChild;
	}

	/**
	 * @param firstSecondChild the firstSecondChild to set
	 */
	public void setFirstSecondChild(Renderable firstSecondChild) {
		this.firstSecondChild = firstSecondChild;
	}

	/**
	 * @return the secondFirstChild
	 */
	public Renderable getSecondFirstChild() {
		return secondFirstChild;
	}

	/**
	 * @param secondFirstChild the secondFirstChild to set
	 */
	public void setSecondFirstChild(Renderable secondFirstChild) {
		this.secondFirstChild = secondFirstChild;
	}

	/**
	 * @return the secondSecondChild
	 */
	public Renderable getSecondSecondChild() {
		return secondSecondChild;
	}

	/**
	 * @param secondSecondChild the secondSecondChild to set
	 */
	public void setSecondSecondChild(Renderable secondSecondChild) {
		this.secondSecondChild = secondSecondChild;
	}

	/**
	 * @param composite
	 */
	public void addSecondChildToFirstChild(Control composite) {
		final CompositeCollection collection = (CompositeCollection) getFirstChild();
		collection.getComposites().add(composite);
		setFirstSecondChild(composite);
	}

	/**
	 * @param composite
	 */
	public void addFirstChildToSecondChild(Control composite) {
		final CompositeCollection collection = (CompositeCollection) getSecondChild();
		collection.getComposites().add(composite);
		setSecondFirstChild(composite);

	}

	/**
	 * @param composite
	 */
	public void addSecondChildToSecondChild(Control composite) {
		final CompositeCollection collection = (CompositeCollection) getSecondChild();
		collection.getComposites().add(composite);
		setSecondSecondChild(composite);
	}

}
