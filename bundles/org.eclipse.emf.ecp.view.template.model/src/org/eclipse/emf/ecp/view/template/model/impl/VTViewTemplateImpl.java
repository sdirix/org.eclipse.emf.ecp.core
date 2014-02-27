/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.template.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecp.view.template.model.VTControlValidationTemplate;
import org.eclipse.emf.ecp.view.template.model.VTTemplatePackage;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplate;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>View Template</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTViewTemplateImpl#getControlValidationConfiguration <em>
 * Control Validation Configuration</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VTViewTemplateImpl extends MinimalEObjectImpl.Container implements VTViewTemplate
{
	/**
	 * The cached value of the '{@link #getControlValidationConfiguration() <em>Control Validation Configuration</em>}'
	 * containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getControlValidationConfiguration()
	 * @generated
	 * @ordered
	 */
	protected VTControlValidationTemplate controlValidationConfiguration;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VTViewTemplateImpl()
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
		return VTTemplatePackage.Literals.VIEW_TEMPLATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VTControlValidationTemplate getControlValidationConfiguration()
	{
		return controlValidationConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetControlValidationConfiguration(
		VTControlValidationTemplate newControlValidationConfiguration, NotificationChain msgs)
	{
		VTControlValidationTemplate oldControlValidationConfiguration = controlValidationConfiguration;
		controlValidationConfiguration = newControlValidationConfiguration;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION, oldControlValidationConfiguration,
				newControlValidationConfiguration);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setControlValidationConfiguration(VTControlValidationTemplate newControlValidationConfiguration)
	{
		if (newControlValidationConfiguration != controlValidationConfiguration)
		{
			NotificationChain msgs = null;
			if (controlValidationConfiguration != null)
				msgs = ((InternalEObject) controlValidationConfiguration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION, null, msgs);
			if (newControlValidationConfiguration != null)
				msgs = ((InternalEObject) newControlValidationConfiguration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION, null, msgs);
			msgs = basicSetControlValidationConfiguration(newControlValidationConfiguration, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION, newControlValidationConfiguration,
				newControlValidationConfiguration));
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
		case VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION:
			return basicSetControlValidationConfiguration(null, msgs);
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
		case VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION:
			return getControlValidationConfiguration();
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
		case VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION:
			setControlValidationConfiguration((VTControlValidationTemplate) newValue);
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
		case VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION:
			setControlValidationConfiguration((VTControlValidationTemplate) null);
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
		case VTTemplatePackage.VIEW_TEMPLATE__CONTROL_VALIDATION_CONFIGURATION:
			return controlValidationConfiguration != null;
		}
		return super.eIsSet(featureID);
	}

} // VTViewTemplateImpl
