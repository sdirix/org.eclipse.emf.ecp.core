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
package org.eclipse.emf.ecp.view.horizontal.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecp.view.horizontal.model.HorizontalFactory;
import org.eclipse.emf.ecp.view.horizontal.model.HorizontalPackage;
import org.eclipse.emf.ecp.view.horizontal.model.VHorizontalLayout;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class HorizontalFactoryImpl extends EFactoryImpl implements HorizontalFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static HorizontalFactory init()
	{
		try
		{
			HorizontalFactory theHorizontalFactory = (HorizontalFactory) EPackage.Registry.INSTANCE
				.getEFactory(HorizontalPackage.eNS_URI);
			if (theHorizontalFactory != null)
			{
				return theHorizontalFactory;
			}
		} catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new HorizontalFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public HorizontalFactoryImpl()
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
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
		case HorizontalPackage.VHORIZONTAL_LAYOUT:
			return createVHorizontalLayout();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VHorizontalLayout createVHorizontalLayout()
	{
		VHorizontalLayoutImpl vHorizontalLayout = new VHorizontalLayoutImpl();
		return vHorizontalLayout;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public HorizontalPackage getHorizontalPackage()
	{
		return (HorizontalPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static HorizontalPackage getPackage()
	{
		return HorizontalPackage.eINSTANCE;
	}

} // HorizontalFactoryImpl
