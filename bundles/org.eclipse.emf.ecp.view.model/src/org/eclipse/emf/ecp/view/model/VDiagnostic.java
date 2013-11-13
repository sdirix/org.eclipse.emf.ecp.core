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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>VDiagnostic</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.VDiagnostic#getDiagnostics <em>Diagnostics</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.model.VViewPackage#getDiagnostic()
 * @model
 * @generated
 */
public interface VDiagnostic extends EObject
{
	/**
	 * Returns the value of the '<em><b>Diagnostics</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagnostics</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Diagnostics</em>' attribute list.
	 * @see org.eclipse.emf.ecp.view.model.VViewPackage#getDiagnostic_Diagnostics()
	 * @model transient="true"
	 * @generated
	 */
	EList<Object> getDiagnostics();

	/**
	 * Returns the highest severity found in the diagnostics.
	 * 
	 * @return the highest severity
	 */
	int getHighestSeverity();

	/**
	 * Returns the message associated with this validation.
	 * 
	 * @return the message
	 */
	String getMessage();

} // VDiagnostic
