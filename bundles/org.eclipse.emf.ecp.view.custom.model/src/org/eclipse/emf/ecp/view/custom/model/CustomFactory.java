/**
 */
package org.eclipse.emf.ecp.view.custom.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.custom.model.CustomPackage
 * @generated
 */
public interface CustomFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CustomFactory eINSTANCE = org.eclipse.emf.ecp.view.custom.model.impl.CustomFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>VPredefined Domain Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>VPredefined Domain Model Reference</em>'.
	 * @generated
	 */
	VPredefinedDomainModelReference createVPredefinedDomainModelReference();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CustomPackage getCustomPackage();

} //CustomFactory
