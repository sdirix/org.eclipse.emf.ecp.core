/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 */
package org.eclipse.emf.ecp.view.mapping.test.example;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.emf.ecp.view.mapping.test.example.ExampleFactory
 * @model kind="package"
 * @generated
 */
public interface ExamplePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNAME = "example"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/ecp/example/index/model"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.example.index.model"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	ExamplePackage eINSTANCE = org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.RootImpl <em>Root</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.RootImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getRoot()
	 * @generated
	 */
	int ROOT = 0;

	/**
	 * The feature id for the '<em><b>Intermediate</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ROOT__INTERMEDIATE = 0;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateImpl
	 * <em>Intermediate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getIntermediate()
	 * @generated
	 */
	int INTERMEDIATE = 1;

	/**
	 * The feature id for the '<em><b>Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE__CONTAINER = 0;

	/**
	 * The number of structural features of the '<em>Intermediate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Intermediate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.ContainerImpl
	 * <em>Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ContainerImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getContainer()
	 * @generated
	 */
	int CONTAINER = 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CONTAINER__CHILDREN = 0;

	/**
	 * The number of structural features of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CONTAINER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.AbstractChildImpl
	 * <em>Abstract Child</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.AbstractChildImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getAbstractChild()
	 * @generated
	 */
	int ABSTRACT_CHILD = 3;

	/**
	 * The number of structural features of the '<em>Abstract Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CHILD_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Abstract Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CHILD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.ChildImpl <em>Child</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ChildImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getChild()
	 * @generated
	 */
	int CHILD = 4;

	/**
	 * The feature id for the '<em><b>Intermediate Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CHILD__INTERMEDIATE_TARGET = ABSTRACT_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CHILD_FEATURE_COUNT = ABSTRACT_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int CHILD_OPERATION_COUNT = ABSTRACT_CHILD_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateTargetImpl
	 * <em>Intermediate Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateTargetImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getIntermediateTarget()
	 * @generated
	 */
	int INTERMEDIATE_TARGET = 5;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE_TARGET__TARGET = 0;

	/**
	 * The number of structural features of the '<em>Intermediate Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE_TARGET_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Intermediate Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int INTERMEDIATE_TARGET_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.TargetImpl <em>Target</em>}
	 * ' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.TargetImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getTarget()
	 * @generated
	 */
	int TARGET = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TARGET__NAME = 0;

	/**
	 * The number of structural features of the '<em>Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TARGET_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TARGET_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.EClassToAdditionMapImpl
	 * <em>EClass To Addition Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.EClassToAdditionMapImpl
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getEClassToAdditionMap()
	 * @generated
	 */
	int ECLASS_TO_ADDITION_MAP = 7;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ECLASS_TO_ADDITION_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ECLASS_TO_ADDITION_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EClass To Addition Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ECLASS_TO_ADDITION_MAP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EClass To Addition Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ECLASS_TO_ADDITION_MAP_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.Root <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Root</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Root
	 * @generated
	 */
	EClass getRoot();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.mapping.test.example.Root#getIntermediate <em>Intermediate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Intermediate</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Root#getIntermediate()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_Intermediate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.Intermediate
	 * <em>Intermediate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Intermediate</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Intermediate
	 * @generated
	 */
	EClass getIntermediate();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.mapping.test.example.Intermediate#getContainer <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Container</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Intermediate#getContainer()
	 * @see #getIntermediate()
	 * @generated
	 */
	EReference getIntermediate_Container();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.Container
	 * <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Container</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Container
	 * @generated
	 */
	EClass getContainer();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.emf.ecp.view.mapping.test.example.Container#getChildren
	 * <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Children</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Container#getChildren()
	 * @see #getContainer()
	 * @generated
	 */
	EReference getContainer_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.AbstractChild
	 * <em>Abstract Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Abstract Child</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.AbstractChild
	 * @generated
	 */
	EClass getAbstractChild();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.Child <em>Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Child</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Child
	 * @generated
	 */
	EClass getChild();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.mapping.test.example.Child#getIntermediateTarget <em>Intermediate Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Intermediate Target</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Child#getIntermediateTarget()
	 * @see #getChild()
	 * @generated
	 */
	EReference getChild_IntermediateTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.IntermediateTarget
	 * <em>Intermediate Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Intermediate Target</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.IntermediateTarget
	 * @generated
	 */
	EClass getIntermediateTarget();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.mapping.test.example.IntermediateTarget#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.IntermediateTarget#getTarget()
	 * @see #getIntermediateTarget()
	 * @generated
	 */
	EReference getIntermediateTarget_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.mapping.test.example.Target <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Target</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Target
	 * @generated
	 */
	EClass getTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.mapping.test.example.Target#getName
	 * <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.ecp.view.mapping.test.example.Target#getName()
	 * @see #getTarget()
	 * @generated
	 */
	EAttribute getTarget_Name();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EClass To Addition Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EClass To Addition Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.ecore.EClass"
	 *        valueType="org.eclipse.emf.ecp.view.mapping.test.example.AbstractChild"
	 * @generated
	 */
	EClass getEClassToAdditionMap();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEClassToAdditionMap()
	 * @generated
	 */
	EReference getEClassToAdditionMap_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEClassToAdditionMap()
	 * @generated
	 */
	EReference getEClassToAdditionMap_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ExampleFactory getExampleFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.RootImpl
		 * <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.RootImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getRoot()
		 * @generated
		 */
		EClass ROOT = eINSTANCE.getRoot();

		/**
		 * The meta object literal for the '<em><b>Intermediate</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ROOT__INTERMEDIATE = eINSTANCE.getRoot_Intermediate();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateImpl
		 * <em>Intermediate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getIntermediate()
		 * @generated
		 */
		EClass INTERMEDIATE = eINSTANCE.getIntermediate();

		/**
		 * The meta object literal for the '<em><b>Container</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference INTERMEDIATE__CONTAINER = eINSTANCE.getIntermediate_Container();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.ContainerImpl
		 * <em>Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ContainerImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getContainer()
		 * @generated
		 */
		EClass CONTAINER = eINSTANCE.getContainer();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference CONTAINER__CHILDREN = eINSTANCE.getContainer_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.AbstractChildImpl
		 * <em>Abstract Child</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.AbstractChildImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getAbstractChild()
		 * @generated
		 */
		EClass ABSTRACT_CHILD = eINSTANCE.getAbstractChild();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.ChildImpl
		 * <em>Child</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ChildImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getChild()
		 * @generated
		 */
		EClass CHILD = eINSTANCE.getChild();

		/**
		 * The meta object literal for the '<em><b>Intermediate Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference CHILD__INTERMEDIATE_TARGET = eINSTANCE.getChild_IntermediateTarget();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateTargetImpl
		 * <em>Intermediate Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.IntermediateTargetImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getIntermediateTarget()
		 * @generated
		 */
		EClass INTERMEDIATE_TARGET = eINSTANCE.getIntermediateTarget();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference INTERMEDIATE_TARGET__TARGET = eINSTANCE.getIntermediateTarget_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.mapping.test.example.impl.TargetImpl
		 * <em>Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.TargetImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getTarget()
		 * @generated
		 */
		EClass TARGET = eINSTANCE.getTarget();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute TARGET__NAME = eINSTANCE.getTarget_Name();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.ecp.view.mapping.test.example.impl.EClassToAdditionMapImpl
		 * <em>EClass To Addition Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.EClassToAdditionMapImpl
		 * @see org.eclipse.emf.ecp.view.mapping.test.example.impl.ExamplePackageImpl#getEClassToAdditionMap()
		 * @generated
		 */
		EClass ECLASS_TO_ADDITION_MAP = eINSTANCE.getEClassToAdditionMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ECLASS_TO_ADDITION_MAP__KEY = eINSTANCE.getEClassToAdditionMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ECLASS_TO_ADDITION_MAP__VALUE = eINSTANCE.getEClassToAdditionMap_Value();

	}

} // ExamplePackage
