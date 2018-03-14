/**
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.table.ui.nebula.grid.test.model.audit;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.emf.ecp.view.table.ui.nebula.grid.test.model.audit.AuditPackage
 * @generated
 */
public interface AuditFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	AuditFactory eINSTANCE = org.eclipse.emf.ecp.view.table.ui.nebula.grid.test.model.audit.impl.AuditFactoryImpl
		.init();

	/**
	 * Returns a new object of class '<em>Organization</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Organization</em>'.
	 * @generated
	 */
	Organization createOrganization();

	/**
	 * Returns a new object of class '<em>Bot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Bot</em>'.
	 * @generated
	 */
	Bot createBot();

	/**
	 * Returns a new object of class '<em>Guest User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Guest User</em>'.
	 * @generated
	 */
	GuestUser createGuestUser();

	/**
	 * Returns a new object of class '<em>Registered User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Registered User</em>'.
	 * @generated
	 */
	RegisteredUser createRegisteredUser();

	/**
	 * Returns a new object of class '<em>Admin User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Admin User</em>'.
	 * @generated
	 */
	AdminUser createAdminUser();

	/**
	 * Returns a new object of class '<em>Privileged Bot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Privileged Bot</em>'.
	 * @generated
	 */
	PrivilegedBot createPrivilegedBot();

	/**
	 * Returns a new object of class '<em>Member Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Member Group</em>'.
	 * @generated
	 */
	MemberGroup createMemberGroup();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	AuditPackage getAuditPackage();

} // AuditFactory