/**
 */
package org.eclipse.emf.ecp.view.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tree Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.TreeCategory#getChildComposite <em>Child Composite</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.TreeCategory#getTargetFeature <em>Target Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.TreeCategory#getPathToFeature <em>Path To Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getTreeCategory()
 * @model
 * @generated
 */
public interface TreeCategory extends AbstractCategorization {
	/**
	 * Returns the value of the '<em><b>Child Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Composite</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Composite</em>' containment reference.
	 * @see #setChildComposite(Composite)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getTreeCategory_ChildComposite()
	 * @model containment="true"
	 * @generated
	 */
	Composite getChildComposite();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.TreeCategory#getChildComposite <em>Child Composite</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Composite</em>' containment reference.
	 * @see #getChildComposite()
	 * @generated
	 */
	void setChildComposite(Composite value);

	/**
	 * Returns the value of the '<em><b>Target Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Feature</em>' reference.
	 * @see #setTargetFeature(EStructuralFeature)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getTreeCategory_TargetFeature()
	 * @model required="true"
	 * @generated
	 */
	EStructuralFeature getTargetFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.TreeCategory#getTargetFeature <em>Target Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Feature</em>' reference.
	 * @see #getTargetFeature()
	 * @generated
	 */
	void setTargetFeature(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Path To Feature</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path To Feature</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path To Feature</em>' reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getTreeCategory_PathToFeature()
	 * @model
	 * @generated
	 */
	EList<EReference> getPathToFeature();

} // TreeCategory
