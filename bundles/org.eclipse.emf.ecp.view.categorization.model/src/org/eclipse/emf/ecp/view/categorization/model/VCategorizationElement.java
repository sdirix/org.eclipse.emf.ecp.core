/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.categorization.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.view.model.VContainedElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement#getCategorizations <em>
 * Categorizations</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement#getCurrentSelection <em>Current
 * Selection</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizationElement()
 * @model
 * @generated
 */
public interface VCategorizationElement extends VContainedElement
{
	/**
	 * Returns the value of the '<em><b>Categorizations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.categorization.model.VAbstractCategorization}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Categorizations</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Categorizations</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizationElement_Categorizations()
	 * @model containment="true"
	 * @generated
	 */
	EList<VAbstractCategorization> getCategorizations();

	/**
	 * Returns the value of the '<em><b>Current Selection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Selection</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Current Selection</em>' reference.
	 * @see #setCurrentSelection(VAbstractCategorization)
	 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizationElement_CurrentSelection()
	 * @model transient="true"
	 * @generated
	 */
	VAbstractCategorization getCurrentSelection();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement#getCurrentSelection
	 * <em>Current Selection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Current Selection</em>' reference.
	 * @see #getCurrentSelection()
	 * @generated
	 */
	void setCurrentSelection(VAbstractCategorization value);

} // VCategorizationElement
