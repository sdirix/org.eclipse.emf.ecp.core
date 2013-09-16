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
 * 
 * @see org.eclipse.emf.ecp.view.custom.model.CustomFactory
 * @model kind="package"
 * @generated
 */
public interface CustomPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/emf/ecp/view/custom/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.custom.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	CustomPackage eINSTANCE = org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl <em>Control</em>}
	 * ' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl
	 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl#getCustomControl()
	 * @generated
	 */
	int CUSTOM_CONTROL = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__VISIBLE = ViewPackage.ABSTRACT_CONTROL__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__ENABLED = ViewPackage.ABSTRACT_CONTROL__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__READONLY = ViewPackage.ABSTRACT_CONTROL__READONLY;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__ATTACHMENTS = ViewPackage.ABSTRACT_CONTROL__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__NAME = ViewPackage.ABSTRACT_CONTROL__NAME;

	/**
	 * The feature id for the '<em><b>Target Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__TARGET_FEATURES = ViewPackage.ABSTRACT_CONTROL__TARGET_FEATURES;

	/**
	 * The feature id for the '<em><b>Bundle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__BUNDLE = ViewPackage.ABSTRACT_CONTROL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL__CLASS_NAME = ViewPackage.ABSTRACT_CONTROL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Control</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_CONTROL_FEATURE_COUNT = ViewPackage.ABSTRACT_CONTROL_FEATURE_COUNT + 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.custom.model.CustomControl <em>Control</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Control</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.CustomControl
	 * @generated
	 */
	EClass getCustomControl();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.custom.model.CustomControl#getBundle
	 * <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bundle</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.CustomControl#getBundle()
	 * @see #getCustomControl()
	 * @generated
	 */
	EAttribute getCustomControl_Bundle();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.ecp.view.custom.model.CustomControl#getClassName <em>Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Class Name</em>'.
	 * @see org.eclipse.emf.ecp.view.custom.model.CustomControl#getClassName()
	 * @see #getCustomControl()
	 * @generated
	 */
	EAttribute getCustomControl_ClassName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CustomFactory getCustomFactory();

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
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl
		 * <em>Control</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl
		 * @see org.eclipse.emf.ecp.view.custom.model.impl.CustomPackageImpl#getCustomControl()
		 * @generated
		 */
		EClass CUSTOM_CONTROL = eINSTANCE.getCustomControl();

		/**
		 * The meta object literal for the '<em><b>Bundle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CUSTOM_CONTROL__BUNDLE = eINSTANCE.getCustomControl_Bundle();

		/**
		 * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CUSTOM_CONTROL__CLASS_NAME = eINSTANCE.getCustomControl_ClassName();

	}

} // CustomPackage
