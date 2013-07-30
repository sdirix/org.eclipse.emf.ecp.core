/**
 */
package org.eclipse.emf.ecp.view.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.TableControlImpl#getColumns <em>Columns</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.impl.TableControlImpl#isAddRemoveDisabled <em>Add Remove Disabled</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TableControlImpl extends ControlImpl implements TableControl {
	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<TableColumn> columns;

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
	protected TableControlImpl() {
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
		return ViewPackage.Literals.TABLE_CONTROL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<TableColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectContainmentEList<TableColumn>(TableColumn.class, this,
				ViewPackage.TABLE_CONTROL__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAddRemoveDisabled() {
		return addRemoveDisabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAddRemoveDisabled(boolean newAddRemoveDisabled) {
		boolean oldAddRemoveDisabled = addRemoveDisabled;
		addRemoveDisabled = newAddRemoveDisabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ViewPackage.TABLE_CONTROL__ADD_REMOVE_DISABLED,
				oldAddRemoveDisabled, addRemoveDisabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ViewPackage.TABLE_CONTROL__COLUMNS:
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ViewPackage.TABLE_CONTROL__COLUMNS:
			return getColumns();
		case ViewPackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ViewPackage.TABLE_CONTROL__COLUMNS:
			getColumns().clear();
			getColumns().addAll((Collection<? extends TableColumn>) newValue);
			return;
		case ViewPackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
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
	public void eUnset(int featureID) {
		switch (featureID) {
		case ViewPackage.TABLE_CONTROL__COLUMNS:
			getColumns().clear();
			return;
		case ViewPackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ViewPackage.TABLE_CONTROL__COLUMNS:
			return columns != null && !columns.isEmpty();
		case ViewPackage.TABLE_CONTROL__ADD_REMOVE_DISABLED:
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
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (addRemoveDisabled: ");
		result.append(addRemoveDisabled);
		result.append(')');
		return result.toString();
	}

} // TableControlImpl
