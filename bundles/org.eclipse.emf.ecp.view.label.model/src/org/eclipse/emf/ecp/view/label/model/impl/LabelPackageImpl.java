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
package org.eclipse.emf.ecp.view.label.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecp.view.label.model.Label;
import org.eclipse.emf.ecp.view.label.model.LabelFactory;
import org.eclipse.emf.ecp.view.label.model.LabelPackage;
import org.eclipse.emf.ecp.view.label.model.LabelStyle;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class LabelPackageImpl extends EPackageImpl implements LabelPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass labelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum labelStyleEEnum = null;

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
	 * @see org.eclipse.emf.ecp.view.label.model.LabelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LabelPackageImpl()
	{
		super(eNS_URI, LabelFactory.eINSTANCE);
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
	 * This method is used to initialize {@link LabelPackage#eINSTANCE} when that field is accessed. Clients should not
	 * invoke it directly. Instead, they should simply access that field to obtain the package. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LabelPackage init()
	{
		if (isInited)
			return (LabelPackage) EPackage.Registry.INSTANCE.getEPackage(LabelPackage.eNS_URI);

		// Obtain or create and register package
		LabelPackageImpl theLabelPackage = (LabelPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LabelPackageImpl ? EPackage.Registry.INSTANCE
			.get(eNS_URI) : new LabelPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ViewPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theLabelPackage.createPackageContents();

		// Initialize created meta-data
		theLabelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theLabelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LabelPackage.eNS_URI, theLabelPackage);
		return theLabelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLabel()
	{
		return labelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLabel_Style()
	{
		return (EAttribute) labelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getLabelStyle()
	{
		return labelStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LabelFactory getLabelFactory()
	{
		return (LabelFactory) getEFactoryInstance();
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
		labelEClass = createEClass(LABEL);
		createEAttribute(labelEClass, LABEL__STYLE);

		// Create enums
		labelStyleEEnum = createEEnum(LABEL_STYLE);
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
		labelEClass.getESuperTypes().add(theViewPackage.getComposite());

		// Initialize classes and features; add operations and parameters
		initEClass(labelEClass, Label.class, "Label", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLabel_Style(), this.getLabelStyle(), "style", null, 0, 1, Label.class, !IS_TRANSIENT,
			!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(labelStyleEEnum, LabelStyle.class, "LabelStyle");
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H0);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H1);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H2);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H3);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H4);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H5);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H6);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H7);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H8);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.H9);
		addEEnumLiteral(labelStyleEEnum, LabelStyle.SEPARATOR);

		// Create resource
		createResource(eNS_URI);
	}

} // LabelPackageImpl
