/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.rule.model.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.spi.rule.model.NotApplicableForEvaluationException;
import org.eclipse.emf.ecp.view.spi.rule.model.RulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Leaf Condition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.rule.model.impl.LeafConditionImpl#getExpectedValue <em>Expected Value</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.rule.model.impl.LeafConditionImpl#getDomainModelReference <em>Domain Model
 * Reference</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 * @since 1.2
 */
public class LeafConditionImpl extends ConditionImpl implements LeafCondition {
	/**
	 * The default value of the '{@link #getExpectedValue() <em>Expected Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getExpectedValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object EXPECTED_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpectedValue() <em>Expected Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getExpectedValue()
	 * @generated
	 * @ordered
	 */
	protected Object expectedValue = EXPECTED_VALUE_EDEFAULT;

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
	protected LeafConditionImpl() {
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
		return RulePackage.Literals.LEAF_CONDITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getExpectedValue() {
		return expectedValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setExpectedValue(Object newExpectedValue) {
		final Object oldExpectedValue = expectedValue;
		expectedValue = newExpectedValue;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RulePackage.LEAF_CONDITION__EXPECTED_VALUE,
				oldExpectedValue, expectedValue));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
				RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE, oldDomainModelReference, newDomainModelReference);
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
	public void setDomainModelReference(VDomainModelReference newDomainModelReference)
	{
		if (newDomainModelReference != domainModelReference)
		{
			NotificationChain msgs = null;
			if (domainModelReference != null) {
				msgs = ((InternalEObject) domainModelReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE, null, msgs);
			}
			if (newDomainModelReference != null) {
				msgs = ((InternalEObject) newDomainModelReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE, null, msgs);
			}
			msgs = basicSetDomainModelReference(newDomainModelReference, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE,
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
		case RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE:
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID)
		{
		case RulePackage.LEAF_CONDITION__EXPECTED_VALUE:
			return getExpectedValue();
		case RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE:
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
		case RulePackage.LEAF_CONDITION__EXPECTED_VALUE:
			setExpectedValue(newValue);
			return;
		case RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE:
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
		case RulePackage.LEAF_CONDITION__EXPECTED_VALUE:
			setExpectedValue(EXPECTED_VALUE_EDEFAULT);
			return;
		case RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE:
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
		case RulePackage.LEAF_CONDITION__EXPECTED_VALUE:
			return EXPECTED_VALUE_EDEFAULT == null ? expectedValue != null : !EXPECTED_VALUE_EDEFAULT
				.equals(expectedValue);
		case RulePackage.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE:
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
		result.append(" (expectedValue: "); //$NON-NLS-1$
		result.append(expectedValue);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.rule.model.Condition#evaluate()
	 */
	@Override
	public boolean evaluate() {
		if (!getDomainModelReference().getEStructuralFeatureIterator().hasNext()) {
			return true;
		}
		final Iterator<Setting> settingIterator = getDomainModelReference().getIterator();
		boolean result = false;
		final Object expectedValue = getExpectedValue();
		while (settingIterator.hasNext()) {
			try {
				result |= doEvaluate(settingIterator.next(), expectedValue, false, null);
			} catch (final NotApplicableForEvaluationException e) {
				continue;
			}
		}
		return result;
	}

	private static boolean doEvaluate(Setting setting, Object expectedValue, boolean useNewValue, Object newValue)
		throws NotApplicableForEvaluationException {

		final EObject parent = setting.getEObject();
		final EStructuralFeature feature = setting.getEStructuralFeature();
		final EClass attributeClass = feature.getEContainingClass();
		if (!attributeClass.isInstance(parent)) {
			throw new NotApplicableForEvaluationException();
		}
		Object value;
		if (!useNewValue) {
			value = parent.eGet(feature);
		} else {
			value = newValue;
		}
		if (!feature.isMany()) {
			if (expectedValue == null) {
				return value == null;
			}
			if (EcorePackage.eINSTANCE.getEEnum().isInstance(feature.getEType())) {
				return expectedValue.equals(Enumerator.class.cast(value).getLiteral());
			}
			return expectedValue.equals(value);
		}

		// EMF API
		@SuppressWarnings("unchecked")
		final List<Object> objects = (List<Object>) value;
		return objects.contains(expectedValue);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.rule.model.Condition#evaluateChangedValues(java.util.Map)
	 */
	@Override
	public boolean evaluateChangedValues(Map<Setting, Object> possibleNewValues) {
		boolean result = false;
		final Object expectedValue = getExpectedValue();

		for (final Setting setting : possibleNewValues.keySet()) {
			try {
				result |= doEvaluate(setting, expectedValue, true, possibleNewValues.get(setting));
			} catch (final NotApplicableForEvaluationException e) {
				continue;
			}
		}

		return result;
	}

} // LeafConditionImpl
