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
package org.eclipse.emf.ecp.view.label.model;

import org.eclipse.emf.ecp.view.model.Composite;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Label</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.label.model.Label#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.label.model.LabelPackage#getLabel()
 * @model
 * @generated
 */
public interface Label extends Composite
{
	/**
	 * Returns the value of the '<em><b>Style</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.ecp.view.label.model.LabelStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.label.model.LabelStyle
	 * @see #setStyle(LabelStyle)
	 * @see org.eclipse.emf.ecp.view.label.model.LabelPackage#getLabel_Style()
	 * @model
	 * @generated
	 */
	LabelStyle getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.label.model.Label#getStyle <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.label.model.LabelStyle
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(LabelStyle value);

} // Label
