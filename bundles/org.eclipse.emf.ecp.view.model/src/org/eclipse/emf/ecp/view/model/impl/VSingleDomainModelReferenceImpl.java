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
package org.eclipse.emf.ecp.view.model.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecp.view.model.VSingleDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>VSingle Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.VSingleDomainModelReferenceImpl#getDomainModel <em>Domain Model</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.VSingleDomainModelReferenceImpl#getModelFeature <em>Model Feature
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class VSingleDomainModelReferenceImpl extends EObjectImpl implements VSingleDomainModelReference
{
	/**
	 * The cached value of the '{@link #getDomainModel() <em>Domain Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDomainModel()
	 * @generated
	 * @ordered
	 */
	protected EObject domainModel;

	/**
	 * The cached value of the '{@link #getModelFeature() <em>Model Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getModelFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature modelFeature;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VSingleDomainModelReferenceImpl()
	{
		super();
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
		return ViewPackage.Literals.VSINGLE_DOMAIN_MODEL_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getDomainModel()
	{
		if (domainModel != null && domainModel.eIsProxy())
		{
			InternalEObject oldDomainModel = (InternalEObject) domainModel;
			domainModel = eResolveProxy(oldDomainModel);
			if (domainModel != oldDomainModel)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
						ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL, oldDomainModel, domainModel));
			}
		}
		return domainModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetDomainModel()
	{
		return domainModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDomainModel(EObject newDomainModel)
	{
		EObject oldDomainModel = domainModel;
		domainModel = newDomainModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL, oldDomainModel, domainModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EStructuralFeature getModelFeature()
	{
		if (modelFeature != null && modelFeature.eIsProxy())
		{
			InternalEObject oldModelFeature = (InternalEObject) modelFeature;
			modelFeature = (EStructuralFeature) eResolveProxy(oldModelFeature);
			if (modelFeature != oldModelFeature)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
						ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE, oldModelFeature, modelFeature));
			}
		}
		return modelFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EStructuralFeature basicGetModelFeature()
	{
		return modelFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setModelFeature(EStructuralFeature newModelFeature)
	{
		EStructuralFeature oldModelFeature = modelFeature;
		modelFeature = newModelFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE, oldModelFeature, modelFeature));
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
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL:
			if (resolve)
				return getDomainModel();
			return basicGetDomainModel();
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE:
			if (resolve)
				return getModelFeature();
			return basicGetModelFeature();
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
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL:
			setDomainModel((EObject) newValue);
			return;
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE:
			setModelFeature((EStructuralFeature) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL:
			setDomainModel((EObject) null);
			return;
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE:
			setModelFeature((EStructuralFeature) null);
			return;
		}
		super.eUnset(featureID);
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
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL:
			return domainModel != null;
		case ViewPackage.VSINGLE_DOMAIN_MODEL_REFERENCE__MODEL_FEATURE:
			return modelFeature != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference#getIterator()
	 * @generated NOT
	 */
	public Iterator<Setting> getIterator() {
		if (getDomainModel() == null || getModelFeature() == null) {
			final List<Setting> list = Collections.emptyList();
			return list.iterator();
		}
		final List<Setting> singletonList = Collections.singletonList(((InternalEObject) getDomainModel())
			.eSetting(getModelFeature()));
		return singletonList.iterator();
	}

} // VSingleDomainModelReferenceImpl
