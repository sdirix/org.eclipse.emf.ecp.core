/**
 */
package org.eclipse.emf.ecp.view.rule.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.rule.model.RulePackage
 * @generated
 */
public interface RuleFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuleFactory eINSTANCE = org.eclipse.emf.ecp.view.rule.model.impl.RuleFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Leaf Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Leaf Condition</em>'.
	 * @generated
	 */
	LeafCondition createLeafCondition();

	/**
	 * Returns a new object of class '<em>Or Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Or Condition</em>'.
	 * @generated
	 */
	OrCondition createOrCondition();

	/**
	 * Returns a new object of class '<em>And Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>And Condition</em>'.
	 * @generated
	 */
	AndCondition createAndCondition();

	/**
	 * Returns a new object of class '<em>Show Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Show Rule</em>'.
	 * @generated
	 */
	ShowRule createShowRule();

	/**
	 * Returns a new object of class '<em>Enable Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enable Rule</em>'.
	 * @generated
	 */
	EnableRule createEnableRule();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RulePackage getRulePackage();

} //RuleFactory
