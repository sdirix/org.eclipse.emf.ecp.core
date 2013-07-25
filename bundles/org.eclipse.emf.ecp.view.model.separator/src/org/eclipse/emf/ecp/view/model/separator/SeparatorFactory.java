/**
 */
package org.eclipse.emf.ecp.view.model.separator;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.ecp.view.model.separator.SeparatorPackage
 * @generated
 */
public interface SeparatorFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	SeparatorFactory eINSTANCE = org.eclipse.emf.ecp.view.model.separator.impl.SeparatorFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Separator</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Separator</em>'.
	 * @generated
	 */
	Separator createSeparator();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	SeparatorPackage getSeparatorPackage();

} // SeparatorFactory
