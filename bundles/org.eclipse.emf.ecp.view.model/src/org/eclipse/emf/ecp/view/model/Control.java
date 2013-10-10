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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getLabelAlignment <em>Label Alignment</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getDomainModelReferences <em>Domain Model References</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getControlId <em>Control Id</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl()
 * @model
 * @generated
 */
public interface Control extends Composite {
	/**
	 * Returns the value of the '<em><b>Control Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Control Id</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Control Id</em>' attribute.
	 * @see #setControlId(String)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_ControlId()
	 * @model
	 * @generated
	 */
	String getControlId();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Control#getControlId <em>Control Id</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Control Id</em>' attribute.
	 * @see #getControlId()
	 * @generated
	 */
	void setControlId(String value);

	/**
	 * Returns the value of the '<em><b>Label Alignment</b></em>' attribute.
	 * The default value is <code>"Left"</code>.
	 * The literals are from the enumeration {@link org.eclipse.emf.ecp.view.model.Alignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Alignment</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @see #setLabelAlignment(Alignment)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_LabelAlignment()
	 * @model default="Left"
	 * @generated
	 */
	Alignment getLabelAlignment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Control#getLabelAlignment <em>Label Alignment</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @see #getLabelAlignment()
	 * @generated
	 */
	void setLabelAlignment(Alignment value);

	/**
	 * Returns the value of the '<em><b>Domain Model References</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.model.VDomainModelReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domain Model References</em>' containment reference list isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Domain Model References</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_DomainModelReferences()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<VDomainModelReference> getDomainModelReferences();

} // Control
