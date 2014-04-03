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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecp.view.spi.model.impl.VControlImpl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumn;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTablePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.table.model.impl.VTableControlImpl#getColumns <em>Columns</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.table.model.impl.VTableControlImpl#isAddRemoveDisabled <em>Add Remove
 * Disabled</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.table.model.impl.VTableControlImpl#isEnableDetailEditingDialog <em>Enable
 * Detail Editing Dialog</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VTableControlImpl extends VControlImpl implements VTableControl
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
	 * The default value of the '{@link #isEnableDetailEditingDialog() <em>Enable Detail Editing Dialog</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isEnableDetailEditingDialog()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLE_DETAIL_EDITING_DIALOG_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnableDetailEditingDialog() <em>Enable Detail Editing Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isEnableDetailEditingDialog()
	 * @generated
	 * @ordered
	 */
	protected boolean enableDetailEditingDialog = ENABLE_DETAIL_EDITING_DIALOG_EDEFAULT;

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
	@Override
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
	@Override
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
	@Override
	public void setAddRemoveDisabled(boolean newAddRemoveDisabled)
	{
		final boolean oldAddRemoveDisabled = addRemoveDisabled;
		addRemoveDisabled = newAddRemoveDisabled;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VTablePackage.TABLE_CONTROL__ADD_REMOVE_DISABLED,
				oldAddRemoveDisabled, addRemoveDisabled));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isEnableDetailEditingDialog()
	{
		return enableDetailEditingDialog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setEnableDetailEditingDialog(boolean newEnableDetailEditingDialog)
	{
		final boolean oldEnableDetailEditingDialog = enableDetailEditingDialog;
		enableDetailEditingDialog = newEnableDetailEditingDialog;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTablePackage.TABLE_CONTROL__ENABLE_DETAIL_EDITING_DIALOG, oldEnableDetailEditingDialog,
				enableDetailEditingDialog));
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
		case VTablePackage.TABLE_CONTROL__ENABLE_DETAIL_EDITING_DIALOG:
			return isEnableDetailEditingDialog();
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
		case VTablePackage.TABLE_CONTROL__ENABLE_DETAIL_EDITING_DIALOG:
			setEnableDetailEditingDialog((Boolean) newValue);
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
		case VTablePackage.TABLE_CONTROL__ENABLE_DETAIL_EDITING_DIALOG:
			setEnableDetailEditingDialog(ENABLE_DETAIL_EDITING_DIALOG_EDEFAULT);
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
		case VTablePackage.TABLE_CONTROL__ENABLE_DETAIL_EDITING_DIALOG:
			return enableDetailEditingDialog != ENABLE_DETAIL_EDITING_DIALOG_EDEFAULT;
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
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (addRemoveDisabled: "); //$NON-NLS-1$
		result.append(addRemoveDisabled);
		result.append(", enableDetailEditingDialog: "); //$NON-NLS-1$
		result.append(enableDetailEditingDialog);
		result.append(')');
		return result.toString();
	}

	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.emf.ecp.view.model.AbstractControl#getDomainModelReferences()
	// */
	// @Override
	// public EList<VDomainModelReference> getDomainModelReferences() {
	// final EList<VDomainModelReference> result = super.getDomainModelReferences();
	// if (result.isEmpty()) {
	// return result;
	// }
	// final Iterator<Setting> settings = result.get(0).getIterator();
	// if (settings.hasNext()) {
	// final Setting setting = settings.next();
	// if (setting != null) {
	// @SuppressWarnings("unchecked")
	// final List<? extends EObject> objects =
	// (List<? extends EObject>) setting.getEObject().eGet(
	// setting.getEStructuralFeature());
	//
	// for (final EObject object : objects) {
	// for (final VTableColumn tc : getColumns()) {
	// final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
	// .createVFeaturePathDomainModelReference();
	// modelReference.setDomainModelEFeature(tc.getAttribute());
	//
	// final boolean resolve = modelReference.resolve(object);
	// if (!resolve) {
	// // TODO: log
	// }
	//
	// result.add(modelReference);
	// }
	// }
	// }
	// }
	//
	// return result;
	// }

} // VTableControlImpl
