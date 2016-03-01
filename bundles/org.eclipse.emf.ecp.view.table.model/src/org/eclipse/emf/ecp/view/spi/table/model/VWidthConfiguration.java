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
package org.eclipse.emf.ecp.view.spi.table.model;

import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Width Configuration</b></em>'.
 *
 * @since 1.9
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration#getColumnDomainReference
 *        <em>Column Domain Reference</em>}</li>
 *        <li>{@link org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration#getWeight <em>Weight</em>}</li>
 *        </ul>
 *
 * @see org.eclipse.emf.ecp.view.spi.table.model.VTablePackage#getWidthConfiguration()
 * @model
 * @generated
 */
public interface VWidthConfiguration extends VTableColumnConfiguration {
	/**
	 * Returns the value of the '<em><b>Column Domain Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Domain Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Column Domain Reference</em>' reference.
	 * @see #setColumnDomainReference(VDomainModelReference)
	 * @see org.eclipse.emf.ecp.view.spi.table.model.VTablePackage#getWidthConfiguration_ColumnDomainReference()
	 * @model required="true"
	 * @generated
	 */
	VDomainModelReference getColumnDomainReference();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration#getColumnDomainReference
	 * <em>Column Domain Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Column Domain Reference</em>' reference.
	 * @see #getColumnDomainReference()
	 * @generated
	 */
	void setColumnDomainReference(VDomainModelReference value);

	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * The default value is <code>"100"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(int)
	 * @see org.eclipse.emf.ecp.view.spi.table.model.VTablePackage#getWidthConfiguration_Weight()
	 * @model default="100" required="true"
	 * @generated
	 */
	int getWeight();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration#getWeight
	 * <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(int value);

} // VWidthConfiguration
