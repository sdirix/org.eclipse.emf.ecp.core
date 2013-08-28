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
package org.eclipse.emf.ecp.view.vertical.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalLayout;
import org.eclipse.emf.ecp.view.vertical.model.VerticalFactory;
import org.eclipse.emf.ecp.view.vertical.model.VerticalPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class VerticalPackageImpl extends EPackageImpl implements VerticalPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass vVerticalLayoutEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
	 * EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
	 * performs initialization of the package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.ecp.view.vertical.model.VerticalPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private VerticalPackageImpl()
	{
		super(eNS_URI, VerticalFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link VerticalPackage#eINSTANCE} when that field is accessed. Clients should
	 * not invoke it directly. Instead, they should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static VerticalPackage init()
	{
		if (isInited)
			return (VerticalPackage) EPackage.Registry.INSTANCE.getEPackage(VerticalPackage.eNS_URI);

		// Obtain or create and register package
		VerticalPackageImpl theVerticalPackage = (VerticalPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof VerticalPackageImpl ? EPackage.Registry.INSTANCE
			.get(eNS_URI)
			: new VerticalPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ViewPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theVerticalPackage.createPackageContents();

		// Initialize created meta-data
		theVerticalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theVerticalPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(VerticalPackage.eNS_URI, theVerticalPackage);
		return theVerticalPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getVVerticalLayout()
	{
		return vVerticalLayoutEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VerticalFactory getVerticalFactory()
	{
		return (VerticalFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents()
	{
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		vVerticalLayoutEClass = createEClass(VVERTICAL_LAYOUT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents()
	{
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ViewPackage theViewPackage = (ViewPackage) EPackage.Registry.INSTANCE.getEPackage(ViewPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		vVerticalLayoutEClass.getESuperTypes().add(theViewPackage.getCompositeCollection());

		// Initialize classes and features; add operations and parameters
		initEClass(vVerticalLayoutEClass, VVerticalLayout.class, "VVerticalLayout", !IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} // VerticalPackageImpl
