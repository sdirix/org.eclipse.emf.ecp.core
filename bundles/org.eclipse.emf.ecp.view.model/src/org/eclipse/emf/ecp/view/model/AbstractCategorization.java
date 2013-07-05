/**
 */
package org.eclipse.emf.ecp.view.model;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Categorization</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.AbstractCategorization#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.AbstractCategorization#getActions <em>Actions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getAbstractCategorization()
 * @model abstract="true"
 * @generated
 */
public interface AbstractCategorization extends Renderable {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getAbstractCategorization_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.AbstractCategorization#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Actions</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.model.Action}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actions</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getAbstractCategorization_Actions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Action> getActions();

} // AbstractCategorization
