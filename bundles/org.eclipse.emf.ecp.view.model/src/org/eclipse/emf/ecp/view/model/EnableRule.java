/**
 */
package org.eclipse.emf.ecp.view.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enable Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.EnableRule#isDisable <em>Disable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getEnableRule()
 * @model
 * @generated
 */
public interface EnableRule extends Rule {
	/**
	 * Returns the value of the '<em><b>Disable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Disable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disable</em>' attribute.
	 * @see #setDisable(boolean)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getEnableRule_Disable()
	 * @model
	 * @generated
	 */
	boolean isDisable();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.EnableRule#isDisable <em>Disable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Disable</em>' attribute.
	 * @see #isDisable()
	 * @generated
	 */
	void setDisable(boolean value);

} // EnableRule
