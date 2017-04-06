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
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enablement Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.table.model.VEnablementConfiguration#getColumnDomainModelReference <em>Column
 * Domain Model Reference</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.ecp.view.spi.table.model.VTablePackage#getEnablementConfiguration()
 * @model
 * @generated
 * @since 1.13
 */
public interface VEnablementConfiguration extends VElement, VTableColumnConfiguration {
	/**
	 * Returns the value of the '<em><b>Column Domain Model Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Domain Model Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Column Domain Model Reference</em>' reference.
	 * @see #setColumnDomainModelReference(VDomainModelReference)
	 * @see org.eclipse.emf.ecp.view.spi.table.model.VTablePackage#getEnablementConfiguration_ColumnDomainModelReference()
	 * @model required="true"
	 * @generated
	 */
	VDomainModelReference getColumnDomainModelReference();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.emf.ecp.view.spi.table.model.VEnablementConfiguration#getColumnDomainModelReference
	 * <em>Column Domain Model Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Column Domain Model Reference</em>' reference.
	 * @see #getColumnDomainModelReference()
	 * @generated
	 */
	void setColumnDomainModelReference(VDomainModelReference value);

} // VEnablementConfiguration
