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
package org.eclipse.emf.ecp.view.spi.table.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.impl.VElementImpl;
import org.eclipse.emf.ecp.view.spi.table.model.VEnablementConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VSingleColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTablePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enablement Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.table.model.impl.VEnablementConfigurationImpl#getColumnDomainModelReference
 * <em>Column Domain Model Reference</em>}</li>
 * </ul>
 *
 * @generated
 * @since 1.13
 */
public class VEnablementConfigurationImpl extends VElementImpl implements VEnablementConfiguration {
	/**
	 * The cached value of the '{@link #getColumnDomainModelReference() <em>Column Domain Model Reference</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getColumnDomainModelReference()
	 * @generated
	 * @ordered
	 */
	protected VDomainModelReference columnDomainModelReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected VEnablementConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VTablePackage.Literals.ENABLEMENT_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public VDomainModelReference getColumnDomainModelReference() {
		if (columnDomainModelReference != null && columnDomainModelReference.eIsProxy()) {
			final InternalEObject oldColumnDomainModelReference = (InternalEObject) columnDomainModelReference;
			columnDomainModelReference = (VDomainModelReference) eResolveProxy(oldColumnDomainModelReference);
			if (columnDomainModelReference != oldColumnDomainModelReference) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
						VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE,
						oldColumnDomainModelReference, columnDomainModelReference));
				}
			}
		}
		return columnDomainModelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public VDomainModelReference basicGetColumnDomainModelReference() {
		return columnDomainModelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setColumnDomainModelReference(VDomainModelReference newColumnDomainModelReference) {
		final VDomainModelReference oldColumnDomainModelReference = columnDomainModelReference;
		columnDomainModelReference = newColumnDomainModelReference;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE, oldColumnDomainModelReference,
				columnDomainModelReference));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
			if (resolve) {
				return getColumnDomainModelReference();
			}
			return basicGetColumnDomainModelReference();
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
			setColumnDomainModelReference((VDomainModelReference) newValue);
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
	public void eUnset(int featureID) {
		switch (featureID) {
		case VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
			setColumnDomainModelReference((VDomainModelReference) null);
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
			return columnDomainModelReference != null;
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
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == VTableColumnConfiguration.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == VSingleColumnConfiguration.class) {
			switch (derivedFeatureID) {
			case VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
				return VTablePackage.SINGLE_COLUMN_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == VTableColumnConfiguration.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == VSingleColumnConfiguration.class) {
			switch (baseFeatureID) {
			case VTablePackage.SINGLE_COLUMN_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE:
				return VTablePackage.ENABLEMENT_CONFIGURATION__COLUMN_DOMAIN_MODEL_REFERENCE;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} // VEnablementConfigurationImpl