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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.model.VMultiFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>VMulti Feature Path Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.VMultiFeaturePathDomainModelReferenceImpl#getDomainModelEFeature <em>
 * Domain Model EFeature</em>}</li>
 * <li>
 * {@link org.eclipse.emf.ecp.view.model.impl.VMultiFeaturePathDomainModelReferenceImpl#getDomainModelEReferencePath
 * <em>Domain Model EReference Path</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VMultiFeaturePathDomainModelReferenceImpl extends EObjectImpl implements
	VMultiFeaturePathDomainModelReference
{
	/**
	 * The cached value of the '{@link #getDomainModelEFeature() <em>Domain Model EFeature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDomainModelEFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature domainModelEFeature;

	/**
	 * The cached value of the '{@link #getDomainModelEReferencePath() <em>Domain Model EReference Path</em>}' reference
	 * list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDomainModelEReferencePath()
	 * @generated
	 * @ordered
	 */
	protected EList<EReference> domainModelEReferencePath;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VMultiFeaturePathDomainModelReferenceImpl()
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
		return ViewPackage.Literals.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EStructuralFeature getDomainModelEFeature()
	{
		if (domainModelEFeature != null && domainModelEFeature.eIsProxy())
		{
			final InternalEObject oldDomainModelEFeature = (InternalEObject) domainModelEFeature;
			domainModelEFeature = (EStructuralFeature) eResolveProxy(oldDomainModelEFeature);
			if (domainModelEFeature != oldDomainModelEFeature)
			{
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
						ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE,
						oldDomainModelEFeature, domainModelEFeature));
				}
			}
		}
		return domainModelEFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EStructuralFeature basicGetDomainModelEFeature()
	{
		return domainModelEFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDomainModelEFeature(EStructuralFeature newDomainModelEFeature)
	{
		final EStructuralFeature oldDomainModelEFeature = domainModelEFeature;
		domainModelEFeature = newDomainModelEFeature;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
				ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE, oldDomainModelEFeature,
				domainModelEFeature));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EReference> getDomainModelEReferencePath()
	{
		if (domainModelEReferencePath == null)
		{
			domainModelEReferencePath = new EObjectResolvingEList<EReference>(EReference.class, this,
				ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH);
		}
		return domainModelEReferencePath;
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
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE:
			if (resolve) {
				return getDomainModelEFeature();
			}
			return basicGetDomainModelEFeature();
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH:
			return getDomainModelEReferencePath();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE:
			setDomainModelEFeature((EStructuralFeature) newValue);
			return;
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH:
			getDomainModelEReferencePath().clear();
			getDomainModelEReferencePath().addAll((Collection<? extends EReference>) newValue);
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
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE:
			setDomainModelEFeature((EStructuralFeature) null);
			return;
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH:
			getDomainModelEReferencePath().clear();
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
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE:
			return domainModelEFeature != null;
		case ViewPackage.VMULTI_FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH:
			return domainModelEReferencePath != null && !domainModelEReferencePath.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference#resolve(org.eclipse.emf.ecore.EObject)
	 */
	public boolean resolve(EObject domainModel) {
		final EStructuralFeature domainModelEFeatureValue = getDomainModelEFeature();
		if (domainModel == null || domainModelEFeatureValue == null) {
			return false;
		}
		lastResolvedEObject = domainModel;
		leftReferences = new ArrayList<EReference>(getDomainModelEReferencePath());
		for (final EReference eReference : getDomainModelEReferencePath()) {
			if (eReference.isMany()) {
				break;
			}
			if (!eReference.eContainer().equals(lastResolvedEObject.eClass())) {
				return false;
			}
			EObject child = (EObject) lastResolvedEObject.eGet(eReference);
			if (child == null) {
				child = EcoreUtil.create(eReference.getEReferenceType());
				lastResolvedEObject.eSet(eReference, child);
			}
			lastResolvedEObject = child;
			leftReferences.remove(eReference);
		}

		return true;
	}

	private List<EReference> leftReferences;
	private EObject lastResolvedEObject;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference#getIterator()
	 */
	public Iterator<Setting> getIterator() {
		final List<ReferenceCounter> referenceCounters = new ArrayList<VMultiFeaturePathDomainModelReferenceImpl.ReferenceCounter>(
			leftReferences.size());
		for (final EReference eReference : leftReferences) {
			final ReferenceCounter rc = new ReferenceCounter();
			rc.eReference = eReference;
			rc.position = 0;
			referenceCounters.add(rc);
		}
		return new Iterator<EStructuralFeature.Setting>() {

			private final List<ReferenceCounter> references = referenceCounters;
			private boolean hasNext = true;

			public void remove() {
				// TODO Auto-generated method stub

			}

			public Setting next() {
				EObject current = lastResolvedEObject;
				for (int i = 0; i < references.size(); i++) {
					final ReferenceCounter referenceCounter = references.get(i);
					final EReference eReference = referenceCounter.eReference;
					EObject child;

					if (!eReference.isMany()) {
						child = (EObject) current.eGet(eReference);
					}
					else {
						final List<EObject> children = (List<EObject>) current.eGet(eReference);
						child = children.get(referenceCounter.position);
						if (i + 1 == references.size()) {
							referenceCounter.position++;
							if (referenceCounter.position == children.size()) {
								increaseCounter(i - 1);
							}
						}
					}
					if (child == null) {
						throw new IllegalStateException("EObject in reference" + eReference.getName() + " of EObject "
							+ current.eClass().getName() + " not set!");
					}
					current = child;

				}
				return ((InternalEObject) current).eSetting(getDomainModelEFeature());
			}

			private void increaseCounter(int currentPosition) {
				if (currentPosition < 0) {
					hasNext = false;
					return;
				}
				final ReferenceCounter previousCounter = references.get(currentPosition);
				if (!previousCounter.eReference.isMany()) {
					increaseCounter(currentPosition - 1);
				}
				else {
					previousCounter.position++;
					final int numElements = calculateSizeOfElementsOnPosition(currentPosition);
					if (previousCounter.position == numElements) {
						increaseCounter(currentPosition - 1);
					}
					else {
						for (int j = currentPosition + 1; j < references.size(); j++) {
							references.get(j).position = 0;
						}
					}
				}
			}

			private int calculateSizeOfElementsOnPosition(int position) {
				EObject current = lastResolvedEObject;
				for (int j = 0; j < position; j++) {
					final ReferenceCounter referenceCounter = references.get(j);
					final EReference eReference = referenceCounter.eReference;
					EObject child;

					if (!eReference.isMany()) {
						child = (EObject) current.eGet(eReference);
					}
					else {
						final List<EObject> children = (List<EObject>) current.eGet(eReference);
						final int id = referenceCounter.position;
						child = children.get(id);
					}
					current = child;
				}
				return ((List<?>) current.eGet(references.get(position).eReference)).size();
			}

			public boolean hasNext() {
				return hasNext;
			}
		};
	}

	private class ReferenceCounter {
		EReference eReference;
		int position;
	}

} // VMultiFeaturePathDomainModelReferenceImpl
