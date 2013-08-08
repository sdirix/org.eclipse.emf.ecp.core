/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.TreeEditor;

public abstract class ECPTreeViewAction implements ECPAction {

	private TreeViewer treeViewer;
	private TreeSelection treeSelection;
	private EObject root;
	private TreeEditor treeEditor;

	public ECPTreeViewAction() {
		super();

	}

	public void init(TreeViewer treeViewer,
		TreeSelection treeSelection, TreeEditor treeEditor, EObject root) {
		this.treeViewer = treeViewer;
		this.treeSelection = treeSelection;
		this.treeEditor = treeEditor;
		this.root = root;
	}

	protected TreeViewer getTreeViewer() {
		return treeViewer;
	}

	protected TreeSelection getTreeSelection() {
		return treeSelection;
	}

	protected TreeEditor getTreeEditor() {
		return treeEditor;
	}

	protected EObject getRootEObject() {
		return root;
	}

}
