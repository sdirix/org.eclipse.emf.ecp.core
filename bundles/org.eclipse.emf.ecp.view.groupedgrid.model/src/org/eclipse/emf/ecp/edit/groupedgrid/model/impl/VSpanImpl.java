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
package org.eclipse.emf.ecp.edit.groupedgrid.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.edit.groupedgrid.model.VGroupedGridPackage;
import org.eclipse.emf.ecp.edit.groupedgrid.model.VSpan;
import org.eclipse.emf.ecp.view.model.impl.VAttachmentImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Span</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.edit.groupedgrid.model.impl.VSpanImpl#getHorizontalSpan <em>Horizontal Span</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VSpanImpl extends VAttachmentImpl implements VSpan
{
	/**
	 * The default value of the '{@link #getHorizontalSpan() <em>Horizontal Span</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getHorizontalSpan()
	 * @generated
	 * @ordered
	 */
	protected static final int HORIZONTAL_SPAN_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHorizontalSpan() <em>Horizontal Span</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getHorizontalSpan()
	 * @generated
	 * @ordered
	 */
	protected int horizontalSpan = HORIZONTAL_SPAN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VSpanImpl()
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
		return VGroupedGridPackage.Literals.SPAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getHorizontalSpan()
	{
		return horizontalSpan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHorizontalSpan(int newHorizontalSpan)
	{
		int oldHorizontalSpan = horizontalSpan;
		horizontalSpan = newHorizontalSpan;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupedGridPackage.SPAN__HORIZONTAL_SPAN,
				oldHorizontalSpan, horizontalSpan));
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
		case VGroupedGridPackage.SPAN__HORIZONTAL_SPAN:
			return getHorizontalSpan();
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
		case VGroupedGridPackage.SPAN__HORIZONTAL_SPAN:
			setHorizontalSpan((Integer) newValue);
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
		case VGroupedGridPackage.SPAN__HORIZONTAL_SPAN:
			setHorizontalSpan(HORIZONTAL_SPAN_EDEFAULT);
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
		case VGroupedGridPackage.SPAN__HORIZONTAL_SPAN:
			return horizontalSpan != HORIZONTAL_SPAN_EDEFAULT;
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
		result.append(" (horizontalSpan: ");
		result.append(horizontalSpan);
		result.append(')');
		return result.toString();
	}

} // VSpanImpl
