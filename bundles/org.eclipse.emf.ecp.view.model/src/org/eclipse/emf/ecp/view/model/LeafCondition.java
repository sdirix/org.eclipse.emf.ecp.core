/**
 */
package org.eclipse.emf.ecp.view.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Leaf Condition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.LeafCondition#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.LeafCondition#getExpectedValue <em>Expected Value</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.LeafCondition#getPathToAttribute <em>Path To Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getLeafCondition()
 * @model
 * @generated
 */
public interface LeafCondition extends Condition {
	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' reference.
	 * @see #setAttribute(EAttribute)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getLeafCondition_Attribute()
	 * @model required="true"
	 * @generated
	 */
	EAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.LeafCondition#getAttribute <em>Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' reference.
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(EAttribute value);

	/**
	 * Returns the value of the '<em><b>Expected Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expected Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expected Value</em>' attribute.
	 * @see #setExpectedValue(Object)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getLeafCondition_ExpectedValue()
	 * @model required="true"
	 * @generated
	 */
	Object getExpectedValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.LeafCondition#getExpectedValue <em>Expected Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expected Value</em>' attribute.
	 * @see #getExpectedValue()
	 * @generated
	 */
	void setExpectedValue(Object value);

	/**
	 * Returns the value of the '<em><b>Path To Attribute</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path To Attribute</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path To Attribute</em>' reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getLeafCondition_PathToAttribute()
	 * @model
	 * @generated
	 */
	EList<EReference> getPathToAttribute();

} // LeafCondition
