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

import org.eclipse.emf.ecore.EClass;
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
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.impl.VCategorizableElementImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class VCategorizableElementImpl extends VElementImpl implements VCategorizableElement
{
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;
	private final DerivedAttributeAdapter childrenAdapter;
	private final DerivedAttributeAdapter labelAdapter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VCategorizableElementImpl()
	{
		super();
		labelAdapter = new DerivedAttributeAdapter(this, VCategorizationPackage.Literals.CATEGORIZABLE_ELEMENT__LABEL);
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__CHILDREN:
			return getChildren();
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__LABEL:
			return getLabel();
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
		case VCategorizationPackage.CATEGORIZABLE_ELEMENT__LABEL:
			return LABEL_EDEFAULT == null ? getLabel() != null : !LABEL_EDEFAULT.equals(getLabel());
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
