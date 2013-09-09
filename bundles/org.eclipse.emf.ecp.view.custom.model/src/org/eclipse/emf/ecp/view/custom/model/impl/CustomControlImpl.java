/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.model.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.CustomPackage;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl.ECPCustomControlFeature;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlInitException;
import org.eclipse.emf.ecp.view.model.impl.AbstractControlImpl;
import org.osgi.framework.Bundle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl#getBundle <em>Bundle</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.custom.model.impl.CustomControlImpl#getClassName <em>Class Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class CustomControlImpl extends AbstractControlImpl implements CustomControl {
	/**
	 * The default value of the '{@link #getBundle() <em>Bundle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getBundle()
	 * @generated
	 * @ordered
	 */
	protected static final String BUNDLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBundle() <em>Bundle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getBundle()
	 * @generated
	 * @ordered
	 */
	protected String bundle = BUNDLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getClassName()
	 * @generated
	 * @ordered
	 */
	protected static final String CLASS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getClassName()
	 * @generated
	 * @ordered
	 */
	protected String className = CLASS_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CustomControlImpl() {
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
		return CustomPackage.Literals.CUSTOM_CONTROL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getBundle() {
		return bundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setBundle(String newBundle) {
		final String oldBundle = bundle;
		bundle = newBundle;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, CustomPackage.CUSTOM_CONTROL__BUNDLE, oldBundle,
				bundle));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setClassName(String newClassName) {
		final String oldClassName = className;
		className = newClassName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, CustomPackage.CUSTOM_CONTROL__CLASS_NAME,
				oldClassName, className));
		}
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
		case CustomPackage.CUSTOM_CONTROL__BUNDLE:
			return getBundle();
		case CustomPackage.CUSTOM_CONTROL__CLASS_NAME:
			return getClassName();
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case CustomPackage.CUSTOM_CONTROL__BUNDLE:
			setBundle((String) newValue);
			return;
		case CustomPackage.CUSTOM_CONTROL__CLASS_NAME:
			setClassName((String) newValue);
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
		case CustomPackage.CUSTOM_CONTROL__BUNDLE:
			setBundle(BUNDLE_EDEFAULT);
			return;
		case CustomPackage.CUSTOM_CONTROL__CLASS_NAME:
			setClassName(CLASS_NAME_EDEFAULT);
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
		case CustomPackage.CUSTOM_CONTROL__BUNDLE:
			return BUNDLE_EDEFAULT == null ? bundle != null : !BUNDLE_EDEFAULT.equals(bundle);
		case CustomPackage.CUSTOM_CONTROL__CLASS_NAME:
			return CLASS_NAME_EDEFAULT == null ? className != null : !CLASS_NAME_EDEFAULT.equals(className);
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
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (bundle: ");
		result.append(bundle);
		result.append(", className: ");
		result.append(className);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return all editable and referenced features of the associated {@link ECPCustomControl}.
	 * 
	 * @generated NOT
	 */
	@Override
	public EList<EStructuralFeature> getTargetFeatures() {
		final EList<EStructuralFeature> result = new BasicEList<EStructuralFeature>();
		if (getBundle() == null || getClassName() == null) {
			return result;
		}
		try {
			final ECPCustomControl categoryComposite = getECPCustomControl();
			for (final ECPCustomControlFeature editFeature : categoryComposite.getEditableFeatures()) {
				result.add(editFeature.getTargetFeature());
			}
			for (final ECPCustomControlFeature refFeature : categoryComposite.getReferencedFeatures()) {
				result.add(refFeature.getTargetFeature());
			}
		} catch (final ECPCustomControlInitException ex) {
			// TODO activate?
			// Activator.logException(e);
		}

		return result;
	}

	private Class<?> getClass(String pluginID, String className)
		throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(pluginID);
		if (bundle == null) {
			throw new ClassNotFoundException(className
				+ " cannot be loaded because because bundle " + pluginID
				+ " cannot be resolved");
		}
		return bundle.loadClass(className);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.custom.model.CustomControl#getECPCustomControl()
	 */
	public ECPCustomControl getECPCustomControl() throws ECPCustomControlInitException {
		try {
			final Class<?> clazz = getClass(getBundle(), getClassName());
			final Constructor<?> constructor = clazz.getConstructor();
			final Object obj = constructor.newInstance();
			return (ECPCustomControl) obj;
		} catch (final ClassNotFoundException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final NoSuchMethodException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final SecurityException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final InstantiationException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final IllegalAccessException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final IllegalArgumentException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		} catch (final InvocationTargetException ex) {
			throw new ECPCustomControlInitException("Error during initialisation of ECPCustomControl.", ex);
		}
	}

} // CustomControlImpl
