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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecp.view.template.model.VTControlValidationTemplate;
import org.eclipse.emf.ecp.view.template.model.VTTemplatePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control Validation Template</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getOkColorHEX <em>Ok Color
 * HEX</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getOkImageURL <em>Ok Image
 * URL</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getInfoColorHEX <em>Info
 * Color HEX</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getInfoImageURL <em>Info
 * Image URL</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getWarningColorHEX <em>
 * Warning Color HEX</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getWarningImageURL <em>
 * Warning Image URL</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getErrorColorHEX <em>Error
 * Color HEX</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getErrorImageURL <em>Error
 * Image URL</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getCancelColorHEX <em>Cancel
 * Color HEX</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.template.model.impl.VTControlValidationTemplateImpl#getCancelImageURL <em>Cancel
 * Image URL</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VTControlValidationTemplateImpl extends MinimalEObjectImpl.Container implements
	VTControlValidationTemplate
{
	/**
	 * The default value of the '{@link #getOkColorHEX() <em>Ok Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOkColorHEX()
	 * @generated
	 * @ordered
	 */
	protected static final String OK_COLOR_HEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOkColorHEX() <em>Ok Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOkColorHEX()
	 * @generated
	 * @ordered
	 */
	protected String okColorHEX = OK_COLOR_HEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getOkImageURL() <em>Ok Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOkImageURL()
	 * @generated
	 * @ordered
	 */
	protected static final String OK_IMAGE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOkImageURL() <em>Ok Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOkImageURL()
	 * @generated
	 * @ordered
	 */
	protected String okImageURL = OK_IMAGE_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getInfoColorHEX() <em>Info Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getInfoColorHEX()
	 * @generated
	 * @ordered
	 */
	protected static final String INFO_COLOR_HEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInfoColorHEX() <em>Info Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getInfoColorHEX()
	 * @generated
	 * @ordered
	 */
	protected String infoColorHEX = INFO_COLOR_HEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getInfoImageURL() <em>Info Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getInfoImageURL()
	 * @generated
	 * @ordered
	 */
	protected static final String INFO_IMAGE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInfoImageURL() <em>Info Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getInfoImageURL()
	 * @generated
	 * @ordered
	 */
	protected String infoImageURL = INFO_IMAGE_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getWarningColorHEX() <em>Warning Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWarningColorHEX()
	 * @generated
	 * @ordered
	 */
	protected static final String WARNING_COLOR_HEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWarningColorHEX() <em>Warning Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWarningColorHEX()
	 * @generated
	 * @ordered
	 */
	protected String warningColorHEX = WARNING_COLOR_HEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getWarningImageURL() <em>Warning Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWarningImageURL()
	 * @generated
	 * @ordered
	 */
	protected static final String WARNING_IMAGE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWarningImageURL() <em>Warning Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWarningImageURL()
	 * @generated
	 * @ordered
	 */
	protected String warningImageURL = WARNING_IMAGE_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getErrorColorHEX() <em>Error Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getErrorColorHEX()
	 * @generated
	 * @ordered
	 */
	protected static final String ERROR_COLOR_HEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getErrorColorHEX() <em>Error Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getErrorColorHEX()
	 * @generated
	 * @ordered
	 */
	protected String errorColorHEX = ERROR_COLOR_HEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getErrorImageURL() <em>Error Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getErrorImageURL()
	 * @generated
	 * @ordered
	 */
	protected static final String ERROR_IMAGE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getErrorImageURL() <em>Error Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getErrorImageURL()
	 * @generated
	 * @ordered
	 */
	protected String errorImageURL = ERROR_IMAGE_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getCancelColorHEX() <em>Cancel Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCancelColorHEX()
	 * @generated
	 * @ordered
	 */
	protected static final String CANCEL_COLOR_HEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCancelColorHEX() <em>Cancel Color HEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCancelColorHEX()
	 * @generated
	 * @ordered
	 */
	protected String cancelColorHEX = CANCEL_COLOR_HEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getCancelImageURL() <em>Cancel Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCancelImageURL()
	 * @generated
	 * @ordered
	 */
	protected static final String CANCEL_IMAGE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCancelImageURL() <em>Cancel Image URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCancelImageURL()
	 * @generated
	 * @ordered
	 */
	protected String cancelImageURL = CANCEL_IMAGE_URL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VTControlValidationTemplateImpl()
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
		return VTTemplatePackage.Literals.CONTROL_VALIDATION_TEMPLATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getOkColorHEX()
	{
		return okColorHEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOkColorHEX(String newOkColorHEX)
	{
		String oldOkColorHEX = okColorHEX;
		okColorHEX = newOkColorHEX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_COLOR_HEX, oldOkColorHEX, okColorHEX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getOkImageURL()
	{
		return okImageURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOkImageURL(String newOkImageURL)
	{
		String oldOkImageURL = okImageURL;
		okImageURL = newOkImageURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_IMAGE_URL, oldOkImageURL, okImageURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getInfoColorHEX()
	{
		return infoColorHEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInfoColorHEX(String newInfoColorHEX)
	{
		String oldInfoColorHEX = infoColorHEX;
		infoColorHEX = newInfoColorHEX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_COLOR_HEX, oldInfoColorHEX, infoColorHEX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getInfoImageURL()
	{
		return infoImageURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInfoImageURL(String newInfoImageURL)
	{
		String oldInfoImageURL = infoImageURL;
		infoImageURL = newInfoImageURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_IMAGE_URL, oldInfoImageURL, infoImageURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getWarningColorHEX()
	{
		return warningColorHEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWarningColorHEX(String newWarningColorHEX)
	{
		String oldWarningColorHEX = warningColorHEX;
		warningColorHEX = newWarningColorHEX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_COLOR_HEX, oldWarningColorHEX, warningColorHEX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getWarningImageURL()
	{
		return warningImageURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWarningImageURL(String newWarningImageURL)
	{
		String oldWarningImageURL = warningImageURL;
		warningImageURL = newWarningImageURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_IMAGE_URL, oldWarningImageURL, warningImageURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getErrorColorHEX()
	{
		return errorColorHEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setErrorColorHEX(String newErrorColorHEX)
	{
		String oldErrorColorHEX = errorColorHEX;
		errorColorHEX = newErrorColorHEX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_COLOR_HEX, oldErrorColorHEX, errorColorHEX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getErrorImageURL()
	{
		return errorImageURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setErrorImageURL(String newErrorImageURL)
	{
		String oldErrorImageURL = errorImageURL;
		errorImageURL = newErrorImageURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_IMAGE_URL, oldErrorImageURL, errorImageURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getCancelColorHEX()
	{
		return cancelColorHEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCancelColorHEX(String newCancelColorHEX)
	{
		String oldCancelColorHEX = cancelColorHEX;
		cancelColorHEX = newCancelColorHEX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_COLOR_HEX, oldCancelColorHEX, cancelColorHEX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getCancelImageURL()
	{
		return cancelImageURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCancelImageURL(String newCancelImageURL)
	{
		String oldCancelImageURL = cancelImageURL;
		cancelImageURL = newCancelImageURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
				VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_IMAGE_URL, oldCancelImageURL, cancelImageURL));
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
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_COLOR_HEX:
			return getOkColorHEX();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_IMAGE_URL:
			return getOkImageURL();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_COLOR_HEX:
			return getInfoColorHEX();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_IMAGE_URL:
			return getInfoImageURL();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_COLOR_HEX:
			return getWarningColorHEX();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_IMAGE_URL:
			return getWarningImageURL();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_COLOR_HEX:
			return getErrorColorHEX();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_IMAGE_URL:
			return getErrorImageURL();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_COLOR_HEX:
			return getCancelColorHEX();
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_IMAGE_URL:
			return getCancelImageURL();
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
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_COLOR_HEX:
			setOkColorHEX((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_IMAGE_URL:
			setOkImageURL((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_COLOR_HEX:
			setInfoColorHEX((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_IMAGE_URL:
			setInfoImageURL((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_COLOR_HEX:
			setWarningColorHEX((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_IMAGE_URL:
			setWarningImageURL((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_COLOR_HEX:
			setErrorColorHEX((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_IMAGE_URL:
			setErrorImageURL((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_COLOR_HEX:
			setCancelColorHEX((String) newValue);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_IMAGE_URL:
			setCancelImageURL((String) newValue);
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
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_COLOR_HEX:
			setOkColorHEX(OK_COLOR_HEX_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_IMAGE_URL:
			setOkImageURL(OK_IMAGE_URL_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_COLOR_HEX:
			setInfoColorHEX(INFO_COLOR_HEX_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_IMAGE_URL:
			setInfoImageURL(INFO_IMAGE_URL_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_COLOR_HEX:
			setWarningColorHEX(WARNING_COLOR_HEX_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_IMAGE_URL:
			setWarningImageURL(WARNING_IMAGE_URL_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_COLOR_HEX:
			setErrorColorHEX(ERROR_COLOR_HEX_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_IMAGE_URL:
			setErrorImageURL(ERROR_IMAGE_URL_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_COLOR_HEX:
			setCancelColorHEX(CANCEL_COLOR_HEX_EDEFAULT);
			return;
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_IMAGE_URL:
			setCancelImageURL(CANCEL_IMAGE_URL_EDEFAULT);
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
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_COLOR_HEX:
			return OK_COLOR_HEX_EDEFAULT == null ? okColorHEX != null : !OK_COLOR_HEX_EDEFAULT.equals(okColorHEX);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__OK_IMAGE_URL:
			return OK_IMAGE_URL_EDEFAULT == null ? okImageURL != null : !OK_IMAGE_URL_EDEFAULT.equals(okImageURL);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_COLOR_HEX:
			return INFO_COLOR_HEX_EDEFAULT == null ? infoColorHEX != null : !INFO_COLOR_HEX_EDEFAULT
				.equals(infoColorHEX);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__INFO_IMAGE_URL:
			return INFO_IMAGE_URL_EDEFAULT == null ? infoImageURL != null : !INFO_IMAGE_URL_EDEFAULT
				.equals(infoImageURL);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_COLOR_HEX:
			return WARNING_COLOR_HEX_EDEFAULT == null ? warningColorHEX != null : !WARNING_COLOR_HEX_EDEFAULT
				.equals(warningColorHEX);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__WARNING_IMAGE_URL:
			return WARNING_IMAGE_URL_EDEFAULT == null ? warningImageURL != null : !WARNING_IMAGE_URL_EDEFAULT
				.equals(warningImageURL);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_COLOR_HEX:
			return ERROR_COLOR_HEX_EDEFAULT == null ? errorColorHEX != null : !ERROR_COLOR_HEX_EDEFAULT
				.equals(errorColorHEX);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__ERROR_IMAGE_URL:
			return ERROR_IMAGE_URL_EDEFAULT == null ? errorImageURL != null : !ERROR_IMAGE_URL_EDEFAULT
				.equals(errorImageURL);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_COLOR_HEX:
			return CANCEL_COLOR_HEX_EDEFAULT == null ? cancelColorHEX != null : !CANCEL_COLOR_HEX_EDEFAULT
				.equals(cancelColorHEX);
		case VTTemplatePackage.CONTROL_VALIDATION_TEMPLATE__CANCEL_IMAGE_URL:
			return CANCEL_IMAGE_URL_EDEFAULT == null ? cancelImageURL != null : !CANCEL_IMAGE_URL_EDEFAULT
				.equals(cancelImageURL);
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
		result.append(" (okColorHEX: ");
		result.append(okColorHEX);
		result.append(", okImageURL: ");
		result.append(okImageURL);
		result.append(", infoColorHEX: ");
		result.append(infoColorHEX);
		result.append(", infoImageURL: ");
		result.append(infoImageURL);
		result.append(", warningColorHEX: ");
		result.append(warningColorHEX);
		result.append(", warningImageURL: ");
		result.append(warningImageURL);
		result.append(", errorColorHEX: ");
		result.append(errorColorHEX);
		result.append(", errorImageURL: ");
		result.append(errorImageURL);
		result.append(", cancelColorHEX: ");
		result.append(cancelColorHEX);
		result.append(", cancelImageURL: ");
		result.append(cancelImageURL);
		result.append(')');
		return result.toString();
	}

} // VTControlValidationTemplateImpl
