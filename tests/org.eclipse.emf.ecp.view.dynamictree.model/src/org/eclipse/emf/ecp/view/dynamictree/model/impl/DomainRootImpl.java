/**
 */
package org.eclipse.emf.ecp.view.dynamictree.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainRootImpl#getIntermediate <em>Intermediate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainRootImpl extends EObjectImpl implements DomainRoot {
	/**
	 * The cached value of the '{@link #getIntermediate() <em>Intermediate</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntermediate()
	 * @generated
	 * @ordered
	 */
	protected DomainIntermediate intermediate;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DomainRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DOMAIN_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomainIntermediate getIntermediate() {
		if (intermediate != null && intermediate.eIsProxy()) {
			InternalEObject oldIntermediate = (InternalEObject)intermediate;
			intermediate = (DomainIntermediate)eResolveProxy(oldIntermediate);
			if (intermediate != oldIntermediate) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.DOMAIN_ROOT__INTERMEDIATE, oldIntermediate, intermediate));
			}
		}
		return intermediate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomainIntermediate basicGetIntermediate() {
		return intermediate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIntermediate(DomainIntermediate newIntermediate) {
		DomainIntermediate oldIntermediate = intermediate;
		intermediate = newIntermediate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOMAIN_ROOT__INTERMEDIATE, oldIntermediate, intermediate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.DOMAIN_ROOT__INTERMEDIATE:
				if (resolve) return getIntermediate();
				return basicGetIntermediate();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.DOMAIN_ROOT__INTERMEDIATE:
				setIntermediate((DomainIntermediate)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.DOMAIN_ROOT__INTERMEDIATE:
				setIntermediate((DomainIntermediate)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.DOMAIN_ROOT__INTERMEDIATE:
				return intermediate != null;
		}
		return super.eIsSet(featureID);
	}

} //DomainRootImpl
