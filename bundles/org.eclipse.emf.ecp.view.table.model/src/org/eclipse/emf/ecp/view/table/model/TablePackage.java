/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.table.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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
 * @see org.eclipse.emf.ecp.view.table.model.TableFactory
 * @model kind="package"
 * @generated
 */
public interface TablePackage extends EPackage {
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
	String eNS_URI = "http://org/eclipse/emf/ecp/view/table/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.table.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	TablePackage eINSTANCE = org.eclipse.emf.ecp.view.table.model.impl.TablePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.table.model.impl.TableControlImpl <em>Control</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.table.model.impl.TableControlImpl
	 * @see org.eclipse.emf.ecp.view.table.model.impl.TablePackageImpl#getTableControl()
	 * @generated
	 */
	int TABLE_CONTROL = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__VISIBLE = ViewPackage.CONTROL__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__ENABLED = ViewPackage.CONTROL__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__READONLY = ViewPackage.CONTROL__READONLY;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__ATTACHMENTS = ViewPackage.CONTROL__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__NAME = ViewPackage.CONTROL__NAME;

	/**
	 * The feature id for the '<em><b>Target Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__TARGET_FEATURES = ViewPackage.CONTROL__TARGET_FEATURES;

	/**
	 * The feature id for the '<em><b>Target Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__TARGET_FEATURE = ViewPackage.CONTROL__TARGET_FEATURE;

	/**
	 * The feature id for the '<em><b>Hint</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__HINT = ViewPackage.CONTROL__HINT;

	/**
	 * The feature id for the '<em><b>Mandatory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__MANDATORY = ViewPackage.CONTROL__MANDATORY;

	/**
	 * The feature id for the '<em><b>Path To Feature</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__PATH_TO_FEATURE = ViewPackage.CONTROL__PATH_TO_FEATURE;

	/**
	 * The feature id for the '<em><b>Label Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__LABEL_ALIGNMENT = ViewPackage.CONTROL__LABEL_ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__COLUMNS = ViewPackage.CONTROL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Add Remove Disabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL__ADD_REMOVE_DISABLED = ViewPackage.CONTROL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Control</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_CONTROL_FEATURE_COUNT = ViewPackage.CONTROL_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.table.model.impl.TableColumnImpl <em>Column</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.table.model.impl.TableColumnImpl
	 * @see org.eclipse.emf.ecp.view.table.model.impl.TablePackageImpl#getTableColumn()
	 * @generated
	 */
	int TABLE_COLUMN = 1;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_COLUMN__ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_COLUMN__READ_ONLY = 1;

	/**
	 * The number of structural features of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TABLE_COLUMN_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.table.model.TableControl <em>Control</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Control</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableControl
	 * @generated
	 */
	EClass getTableControl();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.table.model.TableControl#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Columns</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableControl#getColumns()
	 * @see #getTableControl()
	 * @generated
	 */
	EReference getTableControl_Columns();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.ecp.view.table.model.TableControl#isAddRemoveDisabled <em>Add Remove Disabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Add Remove Disabled</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableControl#isAddRemoveDisabled()
	 * @see #getTableControl()
	 * @generated
	 */
	EAttribute getTableControl_AddRemoveDisabled();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.table.model.TableColumn <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Column</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableColumn
	 * @generated
	 */
	EClass getTableColumn();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.table.model.TableColumn#getAttribute
	 * <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableColumn#getAttribute()
	 * @see #getTableColumn()
	 * @generated
	 */
	EReference getTableColumn_Attribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.table.model.TableColumn#isReadOnly
	 * <em>Read Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Read Only</em>'.
	 * @see org.eclipse.emf.ecp.view.table.model.TableColumn#isReadOnly()
	 * @see #getTableColumn()
	 * @generated
	 */
	EAttribute getTableColumn_ReadOnly();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TableFactory getTableFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.table.model.impl.TableControlImpl
		 * <em>Control</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.table.model.impl.TableControlImpl
		 * @see org.eclipse.emf.ecp.view.table.model.impl.TablePackageImpl#getTableControl()
		 * @generated
		 */
		EClass TABLE_CONTROL = eINSTANCE.getTableControl();

		/**
		 * The meta object literal for the '<em><b>Columns</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TABLE_CONTROL__COLUMNS = eINSTANCE.getTableControl_Columns();

		/**
		 * The meta object literal for the '<em><b>Add Remove Disabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TABLE_CONTROL__ADD_REMOVE_DISABLED = eINSTANCE.getTableControl_AddRemoveDisabled();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.table.model.impl.TableColumnImpl
		 * <em>Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.table.model.impl.TableColumnImpl
		 * @see org.eclipse.emf.ecp.view.table.model.impl.TablePackageImpl#getTableColumn()
		 * @generated
		 */
		EClass TABLE_COLUMN = eINSTANCE.getTableColumn();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TABLE_COLUMN__ATTRIBUTE = eINSTANCE.getTableColumn_Attribute();

		/**
		 * The meta object literal for the '<em><b>Read Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TABLE_COLUMN__READ_ONLY = eINSTANCE.getTableColumn_ReadOnly();

	}

} // TablePackage
