/**
 */
package org.eclipse.emf.ecp.view.separator.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.separator.model.SeparatorFactory
 * @model kind="package"
 * @generated
 */
public interface SeparatorPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/emf/ecp/view/separator/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.separator.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SeparatorPackage eINSTANCE = org.eclipse.emf.ecp.view.separator.model.impl.SeparatorPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.separator.model.impl.SeparatorImpl <em>Separator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.separator.model.impl.SeparatorImpl
	 * @see org.eclipse.emf.ecp.view.separator.model.impl.SeparatorPackageImpl#getSeparator()
	 * @generated
	 */
	int SEPARATOR = 0;

	/**
	 * The feature id for the '<em><b>Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__RULE = ViewPackage.COMPOSITE__RULE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__NAME = ViewPackage.COMPOSITE__NAME;

	/**
	 * The number of structural features of the '<em>Separator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR_FEATURE_COUNT = ViewPackage.COMPOSITE_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.separator.model.Separator <em>Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Separator</em>'.
	 * @see org.eclipse.emf.ecp.view.separator.model.Separator
	 * @generated
	 */
	EClass getSeparator();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SeparatorFactory getSeparatorFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.separator.model.impl.SeparatorImpl <em>Separator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.separator.model.impl.SeparatorImpl
		 * @see org.eclipse.emf.ecp.view.separator.model.impl.SeparatorPackageImpl#getSeparator()
		 * @generated
		 */
		EClass SEPARATOR = eINSTANCE.getSeparator();

	}

} //SeparatorPackage
