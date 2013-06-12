/**
 */
package org.eclipse.emf.ecp.view.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Show Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.ShowRule#isHide <em>Hide</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getShowRule()
 * @model
 * @generated
 */
public interface ShowRule extends Rule {
	/**
	 * Returns the value of the '<em><b>Hide</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hide</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hide</em>' attribute.
	 * @see #setHide(boolean)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getShowRule_Hide()
	 * @model
	 * @generated
	 */
	boolean isHide();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.ShowRule#isHide <em>Hide</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hide</em>' attribute.
	 * @see #isHide()
	 * @generated
	 */
	void setHide(boolean value);

} // ShowRule
