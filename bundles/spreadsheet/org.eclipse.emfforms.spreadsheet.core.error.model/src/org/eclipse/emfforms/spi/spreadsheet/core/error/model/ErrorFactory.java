/**
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 */
package org.eclipse.emfforms.spi.spreadsheet.core.error.model;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage
 * @generated
 */
public interface ErrorFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	ErrorFactory eINSTANCE = org.eclipse.emfforms.spi.spreadsheet.core.error.model.impl.ErrorFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Spreadsheet Import Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Spreadsheet Import Result</em>'.
	 * @generated
	 */
	SpreadsheetImportResult createSpreadsheetImportResult();

	/**
	 * Returns a new object of class '<em>Report</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Report</em>'.
	 * @generated
	 */
	ErrorReport createErrorReport();

	/**
	 * Returns a new object of class '<em>EMF Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>EMF Location</em>'.
	 * @generated
	 */
	EMFLocation createEMFLocation();

	EMFLocation createEMFLocation(EObject root, SettingLocation settingLocation, DMRLocation dmrLocation);

	EMFLocation createEMFLocation(EObject root, DMRLocation dmrLocation);

	EMFLocation createEMFLocation(EObject root, SettingLocation settingLocation);

	/**
	 * Returns a new object of class '<em>Setting Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Setting Location</em>'.
	 * @generated
	 */
	SettingLocation createSettingLocation();

	SettingLocation createSettingLocation(EObject eObject, EStructuralFeature feature);

	/**
	 * Returns a new object of class '<em>DMR Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>DMR Location</em>'.
	 * @generated
	 */
	DMRLocation createDMRLocation();

	DMRLocation createDMRLocation(VDomainModelReference dmr);

	/**
	 * Returns a new object of class '<em>Sheet Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Sheet Location</em>'.
	 * @generated
	 */
	SheetLocation createSheetLocation();

	/**
	 * Creates a new object which identifies the affected cell in the spreadsheet.
	 *
	 * @param sheet the sheet id
	 * @param column the column id
	 * @param row the row id
	 * @return the {@link SheetLocation location}
	 */
	SheetLocation createSheetLocation(String sheet, int column, int row);

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	ErrorPackage getErrorPackage();

} // ErrorFactory
