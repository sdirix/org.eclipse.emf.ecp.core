/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
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
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.custom.model.VCustomFactory
 * @model kind="package"
 * @generated
 */
public interface VCustomPackage extends EPackage {
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
	VCustomPackage eINSTANCE = org.eclipse.emf.ecp.view.custom.model.impl.VCustomPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl <em>Predefined Domain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.VCustomPackageImpl#getPredefinedDomainModelReference()
	 * @generated
	 */
	int PREDEFINED_DOMAIN_MODEL_REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Control Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID = ViewPackage.VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Predefined Domain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PREDEFINED_DOMAIN_MODEL_REFERENCE_FEATURE_COUNT = ViewPackage.VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference <em>Predefined Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Predefined Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference
	 * @generated
	 */
	EClass getPredefinedDomainModelReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference#getControlId <em>Control Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Control Id</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference#getControlId()
	 * @see #getPredefinedDomainModelReference()
	 * @generated
	 */
	EAttribute getPredefinedDomainModelReference_ControlId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	VCustomFactory getCustomFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl <em>Predefined Domain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.VCustomPackageImpl#getPredefinedDomainModelReference()
		 * @generated
		 */
		EClass PREDEFINED_DOMAIN_MODEL_REFERENCE = eINSTANCE.getPredefinedDomainModelReference();

		/**
		 * The meta object literal for the '<em><b>Control Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID = eINSTANCE.getPredefinedDomainModelReference_ControlId();

	}

} // VCustomPackage
