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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getTargetFeature <em>Target Feature</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getHint <em>Hint</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#isMandatory <em>Mandatory</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getPathToFeature <em>Path To Feature</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl#getLabelAlignment <em>Label Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ControlImpl extends AbstractControlImpl implements Control {
	/**
	 * The cached value of the '{@link #getTargetFeature() <em>Target Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getTargetFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature targetFeature;

	/**
	 * The cached value of the '{@link #getHint() <em>Hint</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getHint()
	 * @generated
	 * @ordered
	 */
	protected EList<String> hint;

	/**
	 * The default value of the '{@link #isMandatory() <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isMandatory()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MANDATORY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMandatory() <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isMandatory()
	 * @generated
	 * @ordered
	 */
	protected boolean mandatory = MANDATORY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPathToFeature() <em>Path To Feature</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getPathToFeature()
	 * @generated
	 * @ordered
	 */
	protected EList<EReference> pathToFeature;

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
	public EStructuralFeature getTargetFeature() {
		if (targetFeature != null && targetFeature.eIsProxy())
		{
			InternalEObject oldTargetFeature = (InternalEObject) targetFeature;
			targetFeature = (EStructuralFeature) eResolveProxy(oldTargetFeature);
			if (targetFeature != oldTargetFeature)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ViewPackage.CONTROL__TARGET_FEATURE,
						oldTargetFeature, targetFeature));
			}
		}
		return targetFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EStructuralFeature basicGetTargetFeature() {
		return targetFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTargetFeature(EStructuralFeature newTargetFeature) {
		EStructuralFeature oldTargetFeature = targetFeature;
		targetFeature = newTargetFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.CONTROL__TARGET_FEATURE,
				oldTargetFeature, targetFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<String> getHint() {
		if (hint == null)
		{
			hint = new EDataTypeUniqueEList<String>(String.class, this, ViewPackage.CONTROL__HINT);
		}
		return hint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMandatory(boolean newMandatory) {
		boolean oldMandatory = mandatory;
		mandatory = newMandatory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.CONTROL__MANDATORY, oldMandatory,
				mandatory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EReference> getPathToFeature() {
		if (pathToFeature == null)
		{
			pathToFeature = new EObjectResolvingEList<EReference>(EReference.class, this,
				ViewPackage.CONTROL__PATH_TO_FEATURE);
		}
		return pathToFeature;
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
		Alignment oldLabelAlignment = labelAlignment;
		labelAlignment = newLabelAlignment == null ? LABEL_ALIGNMENT_EDEFAULT : newLabelAlignment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.CONTROL__LABEL_ALIGNMENT,
				oldLabelAlignment, labelAlignment));
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
		case ViewPackage.CONTROL__TARGET_FEATURE:
			if (resolve)
				return getTargetFeature();
			return basicGetTargetFeature();
		case ViewPackage.CONTROL__HINT:
			return getHint();
		case ViewPackage.CONTROL__MANDATORY:
			return isMandatory();
		case ViewPackage.CONTROL__PATH_TO_FEATURE:
			return getPathToFeature();
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			return getLabelAlignment();
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
		switch (featureID)
		{
		case ViewPackage.CONTROL__TARGET_FEATURE:
			setTargetFeature((EStructuralFeature) newValue);
			return;
		case ViewPackage.CONTROL__HINT:
			getHint().clear();
			getHint().addAll((Collection<? extends String>) newValue);
			return;
		case ViewPackage.CONTROL__MANDATORY:
			setMandatory((Boolean) newValue);
			return;
		case ViewPackage.CONTROL__PATH_TO_FEATURE:
			getPathToFeature().clear();
			getPathToFeature().addAll((Collection<? extends EReference>) newValue);
			return;
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			setLabelAlignment((Alignment) newValue);
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
		case ViewPackage.CONTROL__TARGET_FEATURE:
			setTargetFeature((EStructuralFeature) null);
			return;
		case ViewPackage.CONTROL__HINT:
			getHint().clear();
			return;
		case ViewPackage.CONTROL__MANDATORY:
			setMandatory(MANDATORY_EDEFAULT);
			return;
		case ViewPackage.CONTROL__PATH_TO_FEATURE:
			getPathToFeature().clear();
			return;
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			setLabelAlignment(LABEL_ALIGNMENT_EDEFAULT);
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
		case ViewPackage.CONTROL__TARGET_FEATURE:
			return targetFeature != null;
		case ViewPackage.CONTROL__HINT:
			return hint != null && !hint.isEmpty();
		case ViewPackage.CONTROL__MANDATORY:
			return mandatory != MANDATORY_EDEFAULT;
		case ViewPackage.CONTROL__PATH_TO_FEATURE:
			return pathToFeature != null && !pathToFeature.isEmpty();
		case ViewPackage.CONTROL__LABEL_ALIGNMENT:
			return labelAlignment != LABEL_ALIGNMENT_EDEFAULT;
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
		result.append(" (hint: ");
		result.append(hint);
		result.append(", mandatory: ");
		result.append(mandatory);
		result.append(", labelAlignment: ");
		result.append(labelAlignment);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public EList<EStructuralFeature> getTargetFeatures() {
		final EList<EStructuralFeature> result = new BasicEList<EStructuralFeature>();
		if (getTargetFeature() != null) {
			result.add(getTargetFeature());
		}
		return result;
	}

} // ControlImpl
