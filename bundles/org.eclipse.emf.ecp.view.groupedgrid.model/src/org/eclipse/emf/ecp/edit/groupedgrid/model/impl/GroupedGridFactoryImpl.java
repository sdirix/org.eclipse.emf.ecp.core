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
package org.eclipse.emf.ecp.edit.groupedgrid.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecp.edit.groupedgrid.model.Group;
import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid;
import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGridFactory;
import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGridPackage;
import org.eclipse.emf.ecp.edit.groupedgrid.model.Row;
import org.eclipse.emf.ecp.edit.groupedgrid.model.Span;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class GroupedGridFactoryImpl extends EFactoryImpl implements GroupedGridFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static GroupedGridFactory init() {
		try
		{
			GroupedGridFactory theGroupedGridFactory = (GroupedGridFactory) EPackage.Registry.INSTANCE
				.getEFactory("http://org/eclipse/emf/ecp/view/groupedgrid/model");
			if (theGroupedGridFactory != null)
			{
				return theGroupedGridFactory;
			}
		} catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GroupedGridFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GroupedGridFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID())
		{
		case GroupedGridPackage.GROUPED_GRID:
			return createGroupedGrid();
		case GroupedGridPackage.GROUP:
			return createGroup();
		case GroupedGridPackage.ROW:
			return createRow();
		case GroupedGridPackage.SPAN:
			return createSpan();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GroupedGrid createGroupedGrid() {
		GroupedGridImpl groupedGrid = new GroupedGridImpl();
		return groupedGrid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Group createGroup() {
		GroupImpl group = new GroupImpl();
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Row createRow() {
		RowImpl row = new RowImpl();
		return row;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Span createSpan() {
		SpanImpl span = new SpanImpl();
		return span;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GroupedGridPackage getGroupedGridPackage() {
		return (GroupedGridPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GroupedGridPackage getPackage() {
		return GroupedGridPackage.eINSTANCE;
	}

} // GroupedGridFactoryImpl
