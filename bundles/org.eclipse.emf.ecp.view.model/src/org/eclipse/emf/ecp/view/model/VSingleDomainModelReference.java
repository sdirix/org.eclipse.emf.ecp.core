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
package org.eclipse.emf.ecp.view.model;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>VSingle Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.VSingleDomainModelReference#getDomainModel <em>Domain Model</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.VSingleDomainModelReference#getModelFeature <em>Model Feature</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getVSingleDomainModelReference()
 * @model abstract="true"
 * @generated
 */
public interface VSingleDomainModelReference extends VDomainModelReference
{
	/**
	 * Returns the value of the '<em><b>Domain Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domain Model</em>' reference isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Domain Model</em>' reference.
	 * @see #setDomainModel(EObject)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getVSingleDomainModelReference_DomainModel()
	 * @model transient="true"
	 * @generated
	 */
	EObject getDomainModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.VSingleDomainModelReference#getDomainModel
	 * <em>Domain Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Domain Model</em>' reference.
	 * @see #getDomainModel()
	 * @generated
	 */
	void setDomainModel(EObject value);

	/**
	 * Returns the value of the '<em><b>Model Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Feature</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Model Feature</em>' reference.
	 * @see #setModelFeature(EStructuralFeature)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getVSingleDomainModelReference_ModelFeature()
	 * @model transient="true"
	 * @generated
	 */
	EStructuralFeature getModelFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.VSingleDomainModelReference#getModelFeature
	 * <em>Model Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Model Feature</em>' reference.
	 * @see #getModelFeature()
	 * @generated
	 */
	void setModelFeature(EStructuralFeature value);

} // VSingleDomainModelReference
