/**
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 */
package org.eclipse.emf.ecp.view.template.selector.hierarchy.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 *
 * @since 1.17
 *        <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.template.selector.hierarchy.model.VTHierarchyPackage
 * @generated
 */
public interface VTHierarchyFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	VTHierarchyFactory eINSTANCE = org.eclipse.emf.ecp.view.template.selector.hierarchy.model.impl.VTHierarchyFactoryImpl
		.init();

	/**
	 * Returns a new object of class '<em>Selector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Selector</em>'.
	 * @generated
	 */
	VTHierarchySelector createHierarchySelector();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	VTHierarchyPackage getHierarchyPackage();

} // VTHierarchyFactory