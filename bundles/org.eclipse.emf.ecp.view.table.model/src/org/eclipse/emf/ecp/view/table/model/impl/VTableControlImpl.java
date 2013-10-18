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
package org.eclipse.emf.ecp.view.table.model.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.impl.ControlImpl;
import org.eclipse.emf.ecp.view.table.model.VTableColumn;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
import org.eclipse.emf.ecp.view.table.model.VTablePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.table.model.impl.VTableControlImpl#getColumns <em>Columns</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.table.model.impl.VTableControlImpl#isAddRemoveDisabled <em>Add Remove Disabled
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VTableControlImpl extends ControlImpl implements VTableControl
{
	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<VTableColumn> columns;

	/**
	 * The default value of the '{@link #isAddRemoveDisabled() <em>Add Remove Disabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isAddRemoveDisabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ADD_REMOVE_DISABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAddRemoveDisabled() <em>Add Remove Disabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isAddRemoveDisabled()
	 * @generated
	 * @ordered
	 */
	protected boolean addRemoveDisabled = ADD_REMOVE_DISABLED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VTableControlImpl()
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
		return VTablePackage.Literals.TABLE_CONTROL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<VTableColumn> getColumns()
	{
		if (columns == null)
		{
			columns = new EObjectContainmentEList<VTableColumn>(VTableColumn.class, this,
				VTablePackage.TABLE_CONTROL__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAddRemoveDisabled()
	{
		return addRemoveDisabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAddRemoveDisabled(boolean newAddRemoveDisabled)
	{
		boolean oldAddRemoveDisabled = addRemoveDisabled;
		addRemoveDisabled = newAddRemoveDisabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED,
				oldAddRemoveDisabled, addRemoveDisabled));
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
		case VTablePackage.TABLE_CONTROL__COLUMNS:
			return ((InternalEList<?>) getColumns()).basicRemove(otherEnd, msgs);
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
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
		case VTablePackage.TABLE_CONTROL__COLUMNS:
			return getColumns();
		case VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
			return isAddRemoveDisabled();
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
		case VTablePackage.TABLE_CONTROL__COLUMNS:
			getColumns().clear();
			getColumns().addAll((Collection<? extends VTableColumn>) newValue);
			return;
		case VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
			setAddRemoveDisabled((Boolean) newValue);
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
		case VTablePackage.TABLE_CONTROL__COLUMNS:
			getColumns().clear();
			return;
		case VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
			setAddRemoveDisabled(ADD_REMOVE_DISABLED_EDEFAULT);
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
		case VTablePackage.TABLE_CONTROL__COLUMNS:
			return columns != null && !columns.isEmpty();
		case VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
			return addRemoveDisabled != ADD_REMOVE_DISABLED_EDEFAULT;
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
	public String toString()
	{
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (addRemoveDisabled: ");
		result.append(addRemoveDisabled);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.model.AbstractControl#getDomainModelReferences()
	 */
	@Override
	public Set<VDomainModelReference> getDomainModelReferences() {
		final Set<VDomainModelReference> result = new LinkedHashSet<VDomainModelReference>();
		result.add(getDomainModelReference());
		final Iterator<Setting> settings = getDomainModelReference().getIterator();
		if (settings.hasNext()) {
			final Setting setting = settings.next();
			if (setting != null) {
				@SuppressWarnings("unchecked")
				final List<? extends EObject> objects =
					(List<? extends EObject>) setting.getEObject().eGet(
						setting.getEStructuralFeature());

				for (final EObject object : objects) {
					for (final VTableColumn tc : getColumns()) {
						final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
							.createVFeaturePathDomainModelReference();
						modelReference.setDomainModelEFeature(tc.getAttribute());

						final boolean resolve = modelReference.resolve(object);
						if (!resolve) {
							// TODO: log
						}

						result.add(modelReference);
					}
				}
			}
		}

		return result;
	}

} // VTableControlImpl
