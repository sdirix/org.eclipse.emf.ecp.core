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
package org.eclipse.emf.ecp.edit.groupedgrid.model;

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
 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGridFactory
 * @model kind="package"
 * @generated
 */
public interface GroupedGridPackage extends EPackage {
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
	String eNS_URI = "http://org/eclipse/emf/ecp/view/groupedgrid/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.groupedgrid.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	GroupedGridPackage eINSTANCE = org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridImpl
	 * <em>Grouped Grid</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridImpl
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getGroupedGrid()
	 * @generated
	 */
	int GROUPED_GRID = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__VISIBLE = ViewPackage.COMPOSITE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__ENABLED = ViewPackage.COMPOSITE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__READONLY = ViewPackage.COMPOSITE__READONLY;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__ATTACHMENTS = ViewPackage.COMPOSITE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__NAME = ViewPackage.COMPOSITE__NAME;

	/**
	 * The feature id for the '<em><b>Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID__GROUPS = ViewPackage.COMPOSITE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Grouped Grid</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUPED_GRID_FEATURE_COUNT = ViewPackage.COMPOSITE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupImpl <em>Group</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupImpl
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 1;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUP__ROWS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUP__NAME = 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.RowImpl <em>Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.RowImpl
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getRow()
	 * @generated
	 */
	int ROW = 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ROW__CHILDREN = 0;

	/**
	 * The number of structural features of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ROW_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.SpanImpl <em>Span</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.SpanImpl
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getSpan()
	 * @generated
	 */
	int SPAN = 3;

	/**
	 * The feature id for the '<em><b>Horizontal Span</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SPAN__HORIZONTAL_SPAN = ViewPackage.ATTACHMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Span</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SPAN_FEATURE_COUNT = ViewPackage.ATTACHMENT_FEATURE_COUNT + 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid
	 * <em>Grouped Grid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Grouped Grid</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid
	 * @generated
	 */
	EClass getGroupedGrid();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Groups</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid#getGroups()
	 * @see #getGroupedGrid()
	 * @generated
	 */
	EReference getGroupedGrid_Groups();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.edit.groupedgrid.model.Group#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Group#getRows()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_Rows();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.Group#getName
	 * <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Group#getName()
	 * @see #getGroup()
	 * @generated
	 */
	EAttribute getGroup_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.Row <em>Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Row</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Row
	 * @generated
	 */
	EClass getRow();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.edit.groupedgrid.model.Row#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Row#getChildren()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.Span <em>Span</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Span</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Span
	 * @generated
	 */
	EClass getSpan();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.ecp.edit.groupedgrid.model.Span#getHorizontalSpan <em>Horizontal Span</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Horizontal Span</em>'.
	 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.Span#getHorizontalSpan()
	 * @see #getSpan()
	 * @generated
	 */
	EAttribute getSpan_HorizontalSpan();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GroupedGridFactory getGroupedGridFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridImpl
		 * <em>Grouped Grid</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridImpl
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getGroupedGrid()
		 * @generated
		 */
		EClass GROUPED_GRID = eINSTANCE.getGroupedGrid();

		/**
		 * The meta object literal for the '<em><b>Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GROUPED_GRID__GROUPS = eINSTANCE.getGroupedGrid_Groups();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupImpl
		 * <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupImpl
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getGroup()
		 * @generated
		 */
		EClass GROUP = eINSTANCE.getGroup();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GROUP__ROWS = eINSTANCE.getGroup_Rows();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GROUP__NAME = eINSTANCE.getGroup_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.RowImpl <em>Row</em>}
		 * ' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.RowImpl
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getRow()
		 * @generated
		 */
		EClass ROW = eINSTANCE.getRow();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ROW__CHILDREN = eINSTANCE.getRow_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.SpanImpl
		 * <em>Span</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.SpanImpl
		 * @see org.eclipse.emf.ecp.edit.groupedgrid.model.impl.GroupedGridPackageImpl#getSpan()
		 * @generated
		 */
		EClass SPAN = eINSTANCE.getSpan();

		/**
		 * The meta object literal for the '<em><b>Horizontal Span</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SPAN__HORIZONTAL_SPAN = eINSTANCE.getSpan_HorizontalSpan();

	}

} // GroupedGridPackage
