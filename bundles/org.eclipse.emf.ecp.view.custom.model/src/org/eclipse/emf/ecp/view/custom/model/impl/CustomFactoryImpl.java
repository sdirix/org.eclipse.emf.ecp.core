/**
 */
package org.eclipse.emf.ecp.view.custom.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecp.view.custom.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CustomFactoryImpl extends EFactoryImpl implements CustomFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CustomFactory init() {
		try {
			CustomFactory theCustomFactory = (CustomFactory)EPackage.Registry.INSTANCE.getEFactory("http://org/eclipse/emf/ecp/view/custom/model"); 
			if (theCustomFactory != null) {
				return theCustomFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CustomFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CustomFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case CustomPackage.VPREDEFINED_DOMAIN_MODEL_REFERENCE: return createVPredefinedDomainModelReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VPredefinedDomainModelReference createVPredefinedDomainModelReference() {
		VPredefinedDomainModelReferenceImpl vPredefinedDomainModelReference = new VPredefinedDomainModelReferenceImpl();
		return vPredefinedDomainModelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CustomPackage getCustomPackage() {
		return (CustomPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CustomPackage getPackage() {
		return CustomPackage.eINSTANCE;
	}

} //CustomFactoryImpl
