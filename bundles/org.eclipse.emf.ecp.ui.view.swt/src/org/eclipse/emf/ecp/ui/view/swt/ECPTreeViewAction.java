package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.TreeEditor;

public abstract class ECPTreeViewAction {

	private final TreeViewer treeViewer;
	private final TreeSelection treeSelection;
	private final EObject root;
    private final TreeEditor treeEditor;

	public ECPTreeViewAction(TreeViewer treeViewer,
			TreeSelection treeSelection,TreeEditor treeEditor, EObject root) {
		super();
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
	
	public abstract void run();
	
}
