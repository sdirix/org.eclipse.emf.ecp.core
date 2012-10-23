package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.Collection;
import java.util.Iterator;

/**
 * Context for a modelelement.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public interface EditorModelelementContext {

	/**
	 * Adds a {@link EditorModelelementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditorModelelementContextListener}
	 */
	void addModelElementContextListener(EditorModelelementContextListener modelElementContextListener);

	/**
	 * Removes a {@link EditorModelelementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditorModelelementContextListener}
	 */
	void removeModelElementContextListener(EditorModelelementContextListener modelElementContextListener);

	/**
	 * Returns all {@link EObject} in the context, which are of a certain type.
	 * 
	 * @param clazz
	 *            the type
	 * @param association
	 * @return a {@link Collection} of {@link EObject} Iterator
	 */
	Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association);

	/**
	 * Returns the editing domain.
	 * 
	 * @return the editing domain
	 */
	EditingDomain getEditingDomain();

	/**
	 * Returns the {@link EditorMetamodelContext}.
	 * 
	 * @return the {@link EditorMetamodelContext}.
	 */
	EditorMetamodelContext getMetaModelElementContext();

	/**
	 * Called if the context is not used anymore. Use for cleanup.
	 */
	void dispose();

	boolean contains(EObject modelElement);

	/**
	 * @param eReference
	 *            the reference to get the link elements for
	 * @return {@link Iterator} over all available {@link EObject}
	 */
	Iterator<EObject> getLinkElements(EReference eReference);

	void openEditor(EObject o, String source);

	/**
	 * @param newMEInstance
	 */
	void addModelElement(EObject newMEInstance);

}
