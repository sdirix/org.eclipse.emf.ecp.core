package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.ecore.EObject;

/**
 * Listens to the changes of a context. notify ui provider to close
 * 
 * @author helming
 */
public interface EditorModelelementContextListener {

	/**
	 * Called if a model element is deleted. Is only called for the root node if
	 * a tree of model elements is deleted.
	 */
	public abstract void onModelElementDeleted(EObject deleted);

	/**
	 * Call if the context gets deleted.
	 */
	public abstract void onContextDeleted();

}
