/**
 */
package org.eclipse.emf.ecp.view.dynamictree.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Intermediate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainIntermediateImpl#getTestElementContainer <em>Test Element Container</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainIntermediateImpl extends EObjectImpl implements DomainIntermediate {
	/**
	 * The cached value of the '{@link #getTestElementContainer() <em>Test Element Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestElementContainer()
	 * @generated
	 * @ordered
	 */
	protected TestElementContainer testElementContainer;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DomainIntermediateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DOMAIN_INTERMEDIATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestElementContainer getTestElementContainer() {
		if (testElementContainer != null && testElementContainer.eIsProxy()) {
			InternalEObject oldTestElementContainer = (InternalEObject)testElementContainer;
			testElementContainer = (TestElementContainer)eResolveProxy(oldTestElementContainer);
			if (testElementContainer != oldTestElementContainer) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER, oldTestElementContainer, testElementContainer));
			}
		}
		return testElementContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestElementContainer basicGetTestElementContainer() {
		return testElementContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTestElementContainer(TestElementContainer newTestElementContainer) {
		TestElementContainer oldTestElementContainer = testElementContainer;
		testElementContainer = newTestElementContainer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER, oldTestElementContainer, testElementContainer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER:
				if (resolve) return getTestElementContainer();
				return basicGetTestElementContainer();
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
			case ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER:
				setTestElementContainer((TestElementContainer)newValue);
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
			case ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER:
				setTestElementContainer((TestElementContainer)null);
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
			case ModelPackage.DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER:
				return testElementContainer != null;
		}
		return super.eIsSet(featureID);
	}

} //DomainIntermediateImpl
