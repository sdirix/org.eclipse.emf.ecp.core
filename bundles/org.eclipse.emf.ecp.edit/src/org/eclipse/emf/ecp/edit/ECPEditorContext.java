package org.eclipse.emf.ecp.edit;

import org.eclipse.emf.ecore.EObject;

public interface ECPEditorContext {

	/**
	 * Checks whether the current {@link ECPControlContext} has unsaved changes.
	 * 
	 * @return true if there are unsaved changes
	 */
	boolean isDirty();

	/**
	 * Triggers the save of the changes.
	 */
	void save();
	/**
	 * Called if the context is not used anymore. Use for cleanup.
	 */
	void dispose();
	/**
	 * Adds a {@link EditModelElementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditModelElementContextListener}
	 */
	void addModelElementContextListener(EditModelElementContextListener modelElementContextListener);

	/**
	 * Removes a {@link EditModelElementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditModelElementContextListener}
	 */
	void removeModelElementContextListener(EditModelElementContextListener modelElementContextListener);
	
	/**
	 * Returns the {@link EObject} of this {@link ECPControlContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();
	
	ECPControlContext getECPControlContext();
}
