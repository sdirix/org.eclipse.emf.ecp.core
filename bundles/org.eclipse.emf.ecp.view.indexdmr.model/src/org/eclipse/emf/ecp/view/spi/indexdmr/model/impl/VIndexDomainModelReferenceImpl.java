/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.spi.indexdmr.model.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexDomainModelReference;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexdmrPackage;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.impl.VFeaturePathDomainModelReferenceImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Index Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.indexdmr.model.impl.VIndexDomainModelReferenceImpl#getTargetDMR <em>Target
 * DMR</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.indexdmr.model.impl.VIndexDomainModelReferenceImpl#getIndex <em>Index</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VIndexDomainModelReferenceImpl extends VFeaturePathDomainModelReferenceImpl implements
	VIndexDomainModelReference
{
	/**
	 * The cached value of the '{@link #getTargetDMR() <em>Target DMR</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getTargetDMR()
	 * @generated
	 * @ordered
	 */
	protected VDomainModelReference targetDMR;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VIndexDomainModelReferenceImpl()
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
		return VIndexdmrPackage.Literals.INDEX_DOMAIN_MODEL_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VDomainModelReference getTargetDMR()
	{
		return targetDMR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetTargetDMR(VDomainModelReference newTargetDMR, NotificationChain msgs)
	{
		final VDomainModelReference oldTargetDMR = targetDMR;
		targetDMR = newTargetDMR;
		if (eNotificationRequired())
		{
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR, oldTargetDMR, newTargetDMR);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTargetDMR(VDomainModelReference newTargetDMR)
	{
		if (newTargetDMR != targetDMR)
		{
			NotificationChain msgs = null;
			if (targetDMR != null) {
				msgs = ((InternalEObject) targetDMR).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR, null, msgs);
			}
			if (newTargetDMR != null) {
				msgs = ((InternalEObject) newTargetDMR).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR, null, msgs);
			}
			msgs = basicSetTargetDMR(newTargetDMR, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
				VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR, newTargetDMR, newTargetDMR));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getIndex()
	{
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setIndex(int newIndex)
	{
		final int oldIndex = index;
		index = newIndex;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__INDEX,
				oldIndex, index));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR:
			return basicSetTargetDMR(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR:
			return getTargetDMR();
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__INDEX:
			return getIndex();
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
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR:
			setTargetDMR((VDomainModelReference) newValue);
			return;
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__INDEX:
			setIndex((Integer) newValue);
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
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR:
			setTargetDMR((VDomainModelReference) null);
			return;
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__INDEX:
			setIndex(INDEX_EDEFAULT);
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
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__TARGET_DMR:
			return targetDMR != null;
		case VIndexdmrPackage.INDEX_DOMAIN_MODEL_REFERENCE__INDEX:
			return index != INDEX_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (index: "); //$NON-NLS-1$
		result.append(index);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.model.impl.VFeaturePathDomainModelReferenceImpl#init(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean init(EObject eObject) {
		if (getTargetDMR() == null) {
			return false;
		}
		if (getIndex() < 0) {
			return false;
		}
		final boolean init = super.init(eObject);
		final List<EObject> list = (List<EObject>) lastResolvedEObject.eGet(getDomainModelEFeature());
		if (list.size() <= getIndex() && getIndex() != 0) {
			return false;
		}
		EObject foundChild = null;
		if (list.size() == 0) {
			final EReference eReference = (EReference) getDomainModelEFeature();
			foundChild = EcoreUtil.create(eReference.getEReferenceType());
			list.add(foundChild);
		}
		else {
			foundChild = list.get(getIndex());
		}

		final boolean valueDMR = getTargetDMR().init(foundChild);
		return init && valueDMR;
	}

	@Override
	public Iterator<Setting> getIterator() {
		if (getTargetDMR() == null) {
			final Set<Setting> settings = Collections.emptySet();
			return settings.iterator();
		}
		return getTargetDMR().getIterator();
	}

	@Override
	public Iterator<EStructuralFeature> getEStructuralFeatureIterator() {
		if (getTargetDMR() == null) {
			final Set<EStructuralFeature> features = Collections.emptySet();
			return features.iterator();
		}
		return getTargetDMR().getEStructuralFeatureIterator();
	}

} // VIndexDomainModelReferenceImpl
