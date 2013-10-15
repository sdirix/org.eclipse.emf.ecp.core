/**
 */
package org.eclipse.emf.ecp.view.custom.model.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.view.custom.model.CustomPackage;
import org.eclipse.emf.ecp.view.custom.model.ECPPredefinedReferences;
import org.eclipse.emf.ecp.view.custom.model.VPredefinedDomainModelReference;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>VPredefined Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.custom.model.impl.VPredefinedDomainModelReferenceImpl#getControlId <em>Control Id
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VPredefinedDomainModelReferenceImpl extends EObjectImpl implements VPredefinedDomainModelReference {
	/**
	 * The default value of the '{@link #getControlId() <em>Control Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getControlId()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTROL_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getControlId() <em>Control Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getControlId()
	 * @generated
	 * @ordered
	 */
	protected String controlId = CONTROL_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VPredefinedDomainModelReferenceImpl() {
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
		return CustomPackage.Literals.VPREDEFINED_DOMAIN_MODEL_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getControlId() {
		return controlId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setControlId(String newControlId) {
		final String oldControlId = controlId;
		controlId = newControlId;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
				CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID, oldControlId, controlId));
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
		case CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID:
			return getControlId();
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
		case CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID:
			setControlId((String) newValue);
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
		case CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID:
			setControlId(CONTROL_ID_EDEFAULT);
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
		case CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE__CONTROL_ID:
			return CONTROL_ID_EDEFAULT == null ? controlId != null : !CONTROL_ID_EDEFAULT.equals(controlId);
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
		result.append(" (controlId: ");
		result.append(controlId);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference#resolve(org.eclipse.emf.ecore.EObject)
	 */
	public boolean resolve(EObject eObject) {
		final ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();
		// load control
		final ECPPredefinedReferences customControl = controlFactory.createControl(getControlId());
		Activator.getDefault().ungetECPControlFactory();
		if (customControl == null) {
			return false;
		}
		// read stuff from control
		controlReferences = customControl.getNeededDomainModelReferences();
		// resolve references from control
		boolean result = true;
		for (final VDomainModelReference domainModelReference : controlReferences) {
			result &= domainModelReference.resolve(eObject);
		}
		return result;
	}

	private Set<VDomainModelReference> controlReferences = Collections.emptySet();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference#getIterator()
	 */
	public Iterator<Setting> getIterator() {
		return new Iterator<Setting>() {
			private Iterator<Setting> currentSubIterator;
			private final Iterator<VDomainModelReference> referencesIterator = controlReferences.iterator();

			public boolean hasNext() {
				return referencesIterator.hasNext() || currentSubIterator != null && currentSubIterator.hasNext();
			}

			public Setting next() {
				if (currentSubIterator == null) {
					currentSubIterator = referencesIterator.next().getIterator();
				}
				final Setting result = currentSubIterator.next();
				if (!currentSubIterator.hasNext()) {
					currentSubIterator = null;
				}
				return result;
			}

			public void remove() {
				// TODO Auto-generated method stub

			}
		};
	}

} // VPredefinedDomainModelReferenceImpl
