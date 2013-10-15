/**
 */
package org.eclipse.emf.ecp.view.custom.model;

import org.eclipse.emf.ecore.EAttribute;
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
 * @see org.eclipse.emf.ecp.view.custom.model.CustomFactory
 * @model kind="package"
 * @generated
 */
public interface CustomPackage extends EPackage {
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
	String eNS_URI = "http://org/eclipse/emf/ecp/view/custom/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.custom.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CustomPackage eINSTANCE = org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl <em>VPredefined Domain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl#getVPredefinedDomainModelReference()
	 * @generated
	 */
	int VPREDEFINED_DOMAIN_MODEL_REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Control Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID = ViewPackage.VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>VPredefined Domain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VPREDEFINED_DOMAIN_MODEL_REFERENCE_FEATURE_COUNT = ViewPackage.VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference <em>VPredefined Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>VPredefined Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference
	 * @generated
	 */
	EClass getVPredefinedDomainModelReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference#getControlId <em>Control Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Control Id</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference#getControlId()
	 * @see #getVPredefinedDomainModelReference()
	 * @generated
	 */
	EAttribute getVPredefinedDomainModelReference_ControlId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CustomFactory getCustomFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl <em>VPredefined Domain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl#getVPredefinedDomainModelReference()
		 * @generated
		 */
		EClass VPREDEFINED_DOMAIN_MODEL_REFERENCE = eINSTANCE.getVPredefinedDomainModelReference();

		/**
		 * The meta object literal for the '<em><b>Control Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID = eINSTANCE.getVPredefinedDomainModelReference_ControlId();

	}

} //CustomPackage
