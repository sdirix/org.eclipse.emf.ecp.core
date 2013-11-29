/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.categorization.model.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.categorization.model.DerivedAttributeAdapter;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizableElement;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage;
import org.eclipse.emf.ecp.view.model.impl.VElementImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Categorizable Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.impl.VCategorizableElementImpl#getChildren <em>Children
 * </em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.impl.VCategorizableElementImpl#getLabelObject <em>Label
 * Object</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class VCategorizableElementImpl extends VElementImpl implements VCategorizableElement
{
	private final DerivedAttributeAdapter childrenAdapter;
	private final DerivedAttributeAdapter labelAdapter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected VCategorizableElementImpl()
	{
		super();
		labelAdapter = new DerivedAttributeAdapter(this,
			VCategorizationPackage.Literals.CATEGORIZABLE_ELEMENT__LABEL_OBJECT);
		childrenAdapter = new DerivedAttributeAdapter(this,
			VCategorizationPackage.Literals.CATEGORIZABLE_ELEMENT__CHILDREN);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return VCategorizationPackage.Literals.CATEGORIZABLE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<VCategorizableElement> getChildren()
	{
		// TODO: implement this method to return the 'Children' reference list
		// Ensure that you remove @generated or mark it @generated NOT
		// The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and
		// org.eclipse.emf.ecore.EStructuralFeature.Setting
		// so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getLabelObject()
	{
		// TODO: implement this method to return the 'Label Object' reference
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__CHILDREN:
			return getChildren();
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__LABEL_OBJECT:
			return getLabelObject();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__CHILDREN:
			return !getChildren().isEmpty();
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__LABEL_OBJECT:
			return getLabelObject() != null;
		}
		return super.eIsSet(featureID);
	}

	protected void addNavigatedDependencyToLabelAdapter(EStructuralFeature navigationFeature,
		EStructuralFeature dependantFeature) {
		labelAdapter.addNavigatedDependency(navigationFeature, dependantFeature);
	}

	protected void addNavigatedDependencyToChildrenAdapter(EStructuralFeature navigationFeature,
		EStructuralFeature dependantFeature) {
		childrenAdapter.addNavigatedDependency(navigationFeature, dependantFeature);
	}

	protected void addLocalDependencyToLabelAdapter(EStructuralFeature localFeature) {
		labelAdapter.addLocalDependency(localFeature);
	}

	protected void addLocalDependencyToChildrenAdapter(EStructuralFeature localFeature) {
		childrenAdapter.addLocalDependency(localFeature);
	}

} // VCategorizableElementImpl
