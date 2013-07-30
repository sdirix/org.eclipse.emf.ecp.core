/**
 */
package org.eclipse.emf.ecp.view.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Leaf Condition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.LeafConditionImpl#getAttribute <em>Attribute</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.LeafConditionImpl#getExpectedValue <em>Expected Value</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.LeafConditionImpl#getPathToAttribute <em>Path To Attribute</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LeafConditionImpl extends ConditionImpl implements LeafCondition {
	/**
	 * The cached value of the '{@link #getAttribute() <em>Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getAttribute()
	 * @generated
	 * @ordered
	 */
	protected EAttribute attribute;

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
	 * The cached value of the '{@link #getPathToAttribute() <em>Path To Attribute</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getPathToAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<EReference> pathToAttribute;

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
		return ViewPackage.Literals.LEAF_CONDITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAttribute() {
		if (attribute != null && attribute.eIsProxy()) {
			InternalEObject oldAttribute = (InternalEObject) attribute;
			attribute = (EAttribute) eResolveProxy(oldAttribute);
			if (attribute != oldAttribute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ViewPackage.LEAF_CONDITION__ATTRIBUTE,
						oldAttribute, attribute));
			}
		}
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute basicGetAttribute() {
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAttribute(EAttribute newAttribute) {
		EAttribute oldAttribute = attribute;
		attribute = newAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.LEAF_CONDITION__ATTRIBUTE, oldAttribute,
				attribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getExpectedValue() {
		return expectedValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExpectedValue(Object newExpectedValue) {
		Object oldExpectedValue = expectedValue;
		expectedValue = newExpectedValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.LEAF_CONDITION__EXPECTED_VALUE,
				oldExpectedValue, expectedValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EReference> getPathToAttribute() {
		if (pathToAttribute == null) {
			pathToAttribute = new EObjectResolvingEList<EReference>(EReference.class, this,
				ViewPackage.LEAF_CONDITION__PATH_TO_ATTRIBUTE);
		}
		return pathToAttribute;
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
		case ViewPackage.LEAF_CONDITION__ATTRIBUTE:
			if (resolve)
				return getAttribute();
			return basicGetAttribute();
		case ViewPackage.LEAF_CONDITION__EXPECTED_VALUE:
			return getExpectedValue();
		case ViewPackage.LEAF_CONDITION__PATH_TO_ATTRIBUTE:
			return getPathToAttribute();
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ViewPackage.LEAF_CONDITION__ATTRIBUTE:
			setAttribute((EAttribute) newValue);
			return;
		case ViewPackage.LEAF_CONDITION__EXPECTED_VALUE:
			setExpectedValue(newValue);
			return;
		case ViewPackage.LEAF_CONDITION__PATH_TO_ATTRIBUTE:
			getPathToAttribute().clear();
			getPathToAttribute().addAll((Collection<? extends EReference>) newValue);
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
		case ViewPackage.LEAF_CONDITION__ATTRIBUTE:
			setAttribute((EAttribute) null);
			return;
		case ViewPackage.LEAF_CONDITION__EXPECTED_VALUE:
			setExpectedValue(EXPECTED_VALUE_EDEFAULT);
			return;
		case ViewPackage.LEAF_CONDITION__PATH_TO_ATTRIBUTE:
			getPathToAttribute().clear();
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
		case ViewPackage.LEAF_CONDITION__ATTRIBUTE:
			return attribute != null;
		case ViewPackage.LEAF_CONDITION__EXPECTED_VALUE:
			return EXPECTED_VALUE_EDEFAULT == null ? expectedValue != null : !EXPECTED_VALUE_EDEFAULT
				.equals(expectedValue);
		case ViewPackage.LEAF_CONDITION__PATH_TO_ATTRIBUTE:
			return pathToAttribute != null && !pathToAttribute.isEmpty();
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (expectedValue: ");
		result.append(expectedValue);
		result.append(')');
		return result.toString();
	}

} // LeafConditionImpl
