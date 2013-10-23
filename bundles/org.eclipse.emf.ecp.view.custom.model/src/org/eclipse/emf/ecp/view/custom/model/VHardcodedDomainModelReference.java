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
package org.eclipse.emf.ecp.view.custom.model;

import org.eclipse.emf.ecp.view.model.VDomainModelReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>VPredefined Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.custom.model.VHardcodedDomainModelReference#getControlId <em>Control Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.custom.model.VCustomPackage#getHardcodedDomainModelReference()
 * @model
 * @generated
 */
public interface VHardcodedDomainModelReference extends VDomainModelReference {
	/**
	 * Returns the value of the '<em><b>Control Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Control Id</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Control Id</em>' attribute.
	 * @see #setControlId(String)
	 * @see org.eclipse.emf.ecp.view.custom.model.VCustomPackage#getHardcodedDomainModelReference_ControlId()
	 * @model required="true"
	 * @generated
	 */
	String getControlId();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.custom.model.VHardcodedDomainModelReference#getControlId <em>Control Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Control Id</em>' attribute.
	 * @see #getControlId()
	 * @generated
	 */
	void setControlId(String value);

} // VHardcodedDomainModelReference
