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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Categorizable Element</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.VCategorizableElement#getChildren <em>Children</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.categorization.model.VCategorizableElement#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizableElement()
 * @model abstract="true"
 * @generated
 */
// TODO API
@SuppressWarnings("restriction")
public interface VCategorizableElement extends VElement
{

	/**
	 * Returns the value of the '<em><b>Children</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.view.categorization.model.VCategorizableElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Children</em>' reference list.
	 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizableElement_Children()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<VCategorizableElement> getChildren();

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.categorization.model.VCategorizationPackage#getCategorizableElement_Label()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getLabel();

	List<ECPAction> getECPActions();

	void setECPActions(List<ECPAction> actions);
} // VCategorizableElement
