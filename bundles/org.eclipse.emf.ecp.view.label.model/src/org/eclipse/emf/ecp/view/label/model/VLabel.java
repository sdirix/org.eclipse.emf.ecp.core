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

import org.eclipse.emf.ecp.view.spi.model.VContainedElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Label</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.label.model.VLabel#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.label.model.VLabelPackage#getLabel()
 * @model
 * @generated
 */
public interface VLabel extends VContainedElement
{
	/**
	 * Returns the value of the '<em><b>Style</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.ecp.view.label.model.VLabelStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.label.model.VLabelStyle
	 * @see #setStyle(VLabelStyle)
	 * @see org.eclipse.emf.ecp.view.label.model.VLabelPackage#getLabel_Style()
	 * @model
	 * @generated
	 */
	VLabelStyle getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.label.model.VLabel#getStyle <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.label.model.VLabelStyle
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(VLabelStyle value);

} // VLabel
