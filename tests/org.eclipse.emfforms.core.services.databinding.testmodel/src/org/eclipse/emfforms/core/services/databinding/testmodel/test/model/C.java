/**
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lucas Koehler - initial API and implementation
 */
package org.eclipse.emfforms.core.services.databinding.testmodel.test.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>C</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.emfforms.core.services.databinding.testmodel.test.model.C#getD
 * <em>D</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage#getC()
 * @model
 * @generated
 */
public interface C extends EObject {
	/**
	 * Returns the value of the '<em><b>D</b></em>' containment reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>D</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>D</em>' containment reference.
	 * @see #setD(D)
	 * @see org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage#getC_D()
	 * @model containment="true"
	 * @generated
	 */
	D getD();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emfforms.core.services.databinding.testmodel.test.model.C#getD
	 * <em>D</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>D</em>' containment reference.
	 * @see #getD()
	 * @generated
	 */
	void setD(D value);

} // C
