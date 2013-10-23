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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getLabelAlignment <em>Label Alignment</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getDomainModelReference <em>Domain Model Reference</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ControlImpl extends CompositeImpl implements Control {
	/**
	 * The default value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 */
	protected static final Alignment LABEL_ALIGNMENT_EDEFAULT = Alignment.LEFT;

	/**
	 * The cached value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 */
	protected Alignment labelAlignment = LABEL_ALIGNMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDomainModelReference() <em>Domain Model Reference</em>}' containment
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDomainModelReference()
	 * @generated
	 * @ordered
	 */
	protected VDomainModelReference domainModelReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ControlImpl() {
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
		return ViewPackage.Literals.CONTROL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VDomainModelReference getDomainModelReference()
	{
		return domainModelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDomainModelReference(VDomainModelReference newDomainModelReference,
		NotificationChain msgs)
	{
		final VDomainModelReference oldDomainModelReference = domainModelReference;
		domainModelReference = newDomainModelReference;
		if (eNotificationRequired())
		{
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE, oldDomainModelReference, newDomainModelReference);
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
	public void setDomainModelReference(VDomainModelReference newDomainModelReference)
	{
		if (newDomainModelReference != domainModelReference)
		{
			NotificationChain msgs = null;
			if (domainModelReference != null) {
				msgs = ((InternalEObject) domainModelReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE, null, msgs);
			}
			if (newDomainModelReference != null) {
				msgs = ((InternalEObject) newDomainModelReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE, null, msgs);
			}
			msgs = basicSetDomainModelReference(newDomainModelReference, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE,
				newDomainModelReference, newDomainModelReference));
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
		case ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE:
			return basicSetDomainModelReference(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Alignment getLabelAlignment()
	{
		return labelAlignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelAlignment(Alignment newLabelAlignment)
	{
		final Alignment oldLabelAlignment = labelAlignment;
		labelAlignment = newLabelAlignment == null ? LABEL_ALIGNMENT_EDEFAULT : newLabelAlignment;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.CONTROL__LABEL_ALIGNMENT,
				oldLabelAlignment, labelAlignment));
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
		switch (featureID)
		{
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			return getLabelAlignment();
		case ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE:
			return getDomainModelReference();
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
		switch (featureID)
		{
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			setLabelAlignment((Alignment) newValue);
			return;
		case ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE:
			setDomainModelReference((VDomainModelReference) newValue);
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
		switch (featureID)
		{
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			setLabelAlignment(LABEL_ALIGNMENT_EDEFAULT);
			return;
		case ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE:
			setDomainModelReference((VDomainModelReference) null);
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
		switch (featureID)
		{
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			return labelAlignment != LABEL_ALIGNMENT_EDEFAULT;
		case ViewPackage.CONTROL__DOMAIN_MODEL_REFERENCE:
			return domainModelReference != null;
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
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (labelAlignment: ");
		result.append(labelAlignment);
		result.append(')');
		return result.toString();
	}

} // ControlImpl
