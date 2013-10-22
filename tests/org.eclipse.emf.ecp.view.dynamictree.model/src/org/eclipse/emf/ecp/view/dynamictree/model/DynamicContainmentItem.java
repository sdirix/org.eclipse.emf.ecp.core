/**
 */
package org.eclipse.emf.ecp.view.dynamictree.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dynamic Containment Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getDomainModel <em>Domain Model</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getItems <em>Items</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getComposite <em>Composite</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage#getDynamicContainmentItem()
 * @model
 * @generated
 */
public interface DynamicContainmentItem extends Renderable {
	/**
	 * Returns the value of the '<em><b>Domain Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domain Model</em>' reference isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Domain Model</em>' reference.
	 * @see #setDomainModel(EObject)
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage#getDynamicContainmentItem_DomainModel()
	 * @model transient="true"
	 * @generated
	 */
	EObject getDomainModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getDomainModel <em>Domain Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Domain Model</em>' reference.
	 * @see #getDomainModel()
	 * @generated
	 */
	void setDomainModel(EObject value);

	/**
	 * Returns the value of the '<em><b>Items</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage#getDynamicContainmentItem_Items()
	 * @model containment="true" transient="true"
	 * @generated
	 */
	EList<DynamicContainmentItem> getItems();

	/**
	 * Returns the value of the '<em><b>Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Composite</em>' containment reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Composite</em>' containment reference.
	 * @see #setComposite(Composite)
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage#getDynamicContainmentItem_Composite()
	 * @model containment="true"
	 * @generated
	 */
	Composite getComposite();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getComposite <em>Composite</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Composite</em>' containment reference.
	 * @see #getComposite()
	 * @generated
	 */
	void setComposite(Composite value);

} // DynamicContainmentItem
