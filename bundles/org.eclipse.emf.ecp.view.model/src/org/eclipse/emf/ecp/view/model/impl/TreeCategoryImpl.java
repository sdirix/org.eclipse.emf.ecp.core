/**
 */
package org.eclipse.emf.ecp.view.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tree Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.model.impl.TreeCategoryImpl#getChildComposite <em>Child Composite</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.impl.TreeCategoryImpl#getTargetFeature <em>Target Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.model.impl.TreeCategoryImpl#getPathToFeature <em>Path To Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TreeCategoryImpl extends AbstractCategorizationImpl implements TreeCategory {
	/**
	 * The cached value of the '{@link #getChildComposite() <em>Child Composite</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildComposite()
	 * @generated
	 * @ordered
	 */
	protected Composite childComposite;

	/**
	 * The cached value of the '{@link #getTargetFeature() <em>Target Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature targetFeature;

	/**
	 * The cached value of the '{@link #getPathToFeature() <em>Path To Feature</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPathToFeature()
	 * @generated
	 * @ordered
	 */
	protected EList<EReference> pathToFeature;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeCategoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ViewPackage.Literals.TREE_CATEGORY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Composite getChildComposite() {
		return childComposite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChildComposite(Composite newChildComposite, NotificationChain msgs) {
		Composite oldChildComposite = childComposite;
		childComposite = newChildComposite;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE, oldChildComposite, newChildComposite);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildComposite(Composite newChildComposite) {
		if (newChildComposite != childComposite) {
			NotificationChain msgs = null;
			if (childComposite != null)
				msgs = ((InternalEObject)childComposite).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE, null, msgs);
			if (newChildComposite != null)
				msgs = ((InternalEObject)newChildComposite).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE, null, msgs);
			msgs = basicSetChildComposite(newChildComposite, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE, newChildComposite, newChildComposite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature getTargetFeature() {
		if (targetFeature != null && targetFeature.eIsProxy()) {
			InternalEObject oldTargetFeature = (InternalEObject)targetFeature;
			targetFeature = (EStructuralFeature)eResolveProxy(oldTargetFeature);
			if (targetFeature != oldTargetFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ViewPackage.TREE_CATEGORY__TARGET_FEATURE, oldTargetFeature, targetFeature));
			}
		}
		return targetFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetTargetFeature() {
		return targetFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetFeature(EStructuralFeature newTargetFeature) {
		EStructuralFeature oldTargetFeature = targetFeature;
		targetFeature = newTargetFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.TREE_CATEGORY__TARGET_FEATURE, oldTargetFeature, targetFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EReference> getPathToFeature() {
		if (pathToFeature == null) {
			pathToFeature = new EObjectResolvingEList<EReference>(EReference.class, this, ViewPackage.TREE_CATEGORY__PATH_TO_FEATURE);
		}
		return pathToFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				return basicSetChildComposite(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				return getChildComposite();
			case ViewPackage.TREE_CATEGORY__TARGET_FEATURE:
				if (resolve) return getTargetFeature();
				return basicGetTargetFeature();
			case ViewPackage.TREE_CATEGORY__PATH_TO_FEATURE:
				return getPathToFeature();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				setChildComposite((Composite)newValue);
				return;
			case ViewPackage.TREE_CATEGORY__TARGET_FEATURE:
				setTargetFeature((EStructuralFeature)newValue);
				return;
			case ViewPackage.TREE_CATEGORY__PATH_TO_FEATURE:
				getPathToFeature().clear();
				getPathToFeature().addAll((Collection<? extends EReference>)newValue);
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
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				setChildComposite((Composite)null);
				return;
			case ViewPackage.TREE_CATEGORY__TARGET_FEATURE:
				setTargetFeature((EStructuralFeature)null);
				return;
			case ViewPackage.TREE_CATEGORY__PATH_TO_FEATURE:
				getPathToFeature().clear();
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
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				return childComposite != null;
			case ViewPackage.TREE_CATEGORY__TARGET_FEATURE:
				return targetFeature != null;
			case ViewPackage.TREE_CATEGORY__PATH_TO_FEATURE:
				return pathToFeature != null && !pathToFeature.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TreeCategoryImpl
