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
package org.eclipse.emf.ecp.view.spi.group.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.spi.group.model.GroupLabelAlignment;
import org.eclipse.emf.ecp.view.spi.group.model.GroupType;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;
import org.eclipse.emf.ecp.view.spi.group.model.VGroupPackage;
import org.eclipse.emf.ecp.view.spi.model.impl.VContainerImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.impl.VGroupImpl#getGroupType <em>Group Type</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.impl.VGroupImpl#getLabelAlignment <em>Label Alignment</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.impl.VGroupImpl#isCollapsed <em>Collapsed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 * @since 1.3
 */
public class VGroupImpl extends VContainerImpl implements VGroup
{
	/**
	 * The default value of the '{@link #getGroupType() <em>Group Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 * @since 1.4
	 */
	protected static final GroupType GROUP_TYPE_EDEFAULT = GroupType.NORMAL;
	/**
	 * The cached value of the '{@link #getGroupType() <em>Group Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 * @since 1.4
	 */
	protected GroupType groupType = GROUP_TYPE_EDEFAULT;
	/**
	 * The default value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected static final GroupLabelAlignment LABEL_ALIGNMENT_EDEFAULT = GroupLabelAlignment.LABEL_ALIGNED;
	/**
	 * The cached value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected GroupLabelAlignment labelAlignment = LABEL_ALIGNMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #isCollapsed() <em>Collapsed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isCollapsed()
	 * @generated
	 * @ordered
	 * @since 1.4
	 */
	protected static final boolean COLLAPSED_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isCollapsed() <em>Collapsed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isCollapsed()
	 * @generated
	 * @ordered
	 * @since 1.4
	 */
	protected boolean collapsed = COLLAPSED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected VGroupImpl()
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
		return VGroupPackage.Literals.GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.4
	 */
	@Override
	public GroupType getGroupType()
	{
		return groupType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.4
	 */
	@Override
	public void setGroupType(GroupType newGroupType)
	{
		final GroupType oldGroupType = groupType;
		groupType = newGroupType == null ? GROUP_TYPE_EDEFAULT : newGroupType;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupPackage.GROUP__GROUP_TYPE, oldGroupType,
				groupType));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.3
	 */
	@Override
	public GroupLabelAlignment getLabelAlignment()
	{
		return labelAlignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.3
	 */
	@Override
	public void setLabelAlignment(GroupLabelAlignment newLabelAlignment)
	{
		final GroupLabelAlignment oldLabelAlignment = labelAlignment;
		labelAlignment = newLabelAlignment == null ? LABEL_ALIGNMENT_EDEFAULT : newLabelAlignment;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupPackage.GROUP__LABEL_ALIGNMENT,
				oldLabelAlignment, labelAlignment));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.4
	 */
	@Override
	public boolean isCollapsed()
	{
		return collapsed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @since 1.4
	 */
	@Override
	public void setCollapsed(boolean newCollapsed)
	{
		final boolean oldCollapsed = collapsed;
		collapsed = newCollapsed;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupPackage.GROUP__COLLAPSED, oldCollapsed,
				collapsed));
		}
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
		case VGroupPackage.GROUP__GROUP_TYPE:
			return getGroupType();
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			return getLabelAlignment();
		case VGroupPackage.GROUP__COLLAPSED:
			return isCollapsed();
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
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
		case VGroupPackage.GROUP__GROUP_TYPE:
			setGroupType((GroupType) newValue);
			return;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			setLabelAlignment((GroupLabelAlignment) newValue);
			return;
		case VGroupPackage.GROUP__COLLAPSED:
			setCollapsed((Boolean) newValue);
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
		case VGroupPackage.GROUP__GROUP_TYPE:
			setGroupType(GROUP_TYPE_EDEFAULT);
			return;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			setLabelAlignment(LABEL_ALIGNMENT_EDEFAULT);
			return;
		case VGroupPackage.GROUP__COLLAPSED:
			setCollapsed(COLLAPSED_EDEFAULT);
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
		case VGroupPackage.GROUP__GROUP_TYPE:
			return groupType != GROUP_TYPE_EDEFAULT;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			return labelAlignment != LABEL_ALIGNMENT_EDEFAULT;
		case VGroupPackage.GROUP__COLLAPSED:
			return collapsed != COLLAPSED_EDEFAULT;
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
		result.append(" (groupType: "); //$NON-NLS-1$
		result.append(groupType);
		result.append(", labelAlignment: "); //$NON-NLS-1$
		result.append(labelAlignment);
		result.append(", collapsed: "); //$NON-NLS-1$
		result.append(collapsed);
		result.append(')');
		return result.toString();
	}

} // VGroupImpl
