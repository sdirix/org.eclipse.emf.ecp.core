package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.TreeEditor;

public abstract class ECPTreeViewAction implements ECPAction{

	private  TreeViewer treeViewer;
	private  TreeSelection treeSelection;
	private  EObject root;
    private  TreeEditor treeEditor;

	public ECPTreeViewAction() {
		super();
		
	}
	public void init(TreeViewer treeViewer,
            TreeSelection treeSelection,TreeEditor treeEditor, EObject root){
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
