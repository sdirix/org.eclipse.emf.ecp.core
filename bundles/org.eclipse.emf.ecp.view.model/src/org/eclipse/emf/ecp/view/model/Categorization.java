/**
 */
package org.eclipse.emf.ecp.view.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Categorization</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.Categorization#getCategorizations <em>Categorizations</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getCategorization()
 * @model
 * @generated
 */
public interface Categorization extends AbstractCategorization {
	/**
	 * Returns the value of the '<em><b>Categorizations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.model.AbstractCategorization}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Categorizations</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Categorizations</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getCategorization_Categorizations()
	 * @model containment="true"
	 * @generated
	 */
	EList<AbstractCategorization> getCategorizations();

} // Categorization
