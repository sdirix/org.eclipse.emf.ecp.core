/**
 */
package org.eclipse.emf.ecp.view.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.Category#getComposite <em>Composite</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getCategory()
 * @model
 * @generated
 */
public interface Category extends AbstractCategorization {
	/**
	 * Returns the value of the '<em><b>Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Composite</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Composite</em>' containment reference.
	 * @see #setComposite(Composite)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getCategory_Composite()
	 * @model containment="true"
	 * @generated
	 */
	Composite getComposite();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Category#getComposite <em>Composite</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Composite</em>' containment reference.
	 * @see #getComposite()
	 * @generated
	 */
	void setComposite(Composite value);

} // Category
