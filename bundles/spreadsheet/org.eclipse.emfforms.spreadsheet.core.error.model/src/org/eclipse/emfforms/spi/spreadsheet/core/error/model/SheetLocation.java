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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Excel Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getSheet <em>Sheet</em>}</li>
 * <li>{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getColumn <em>Column</em>}</li>
 * <li>{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getRow <em>Row</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage#getSheetLocation()
 * @model
 * @generated
 */
public interface SheetLocation extends EObject {
	/**
	 * Returns the value of the '<em><b>Sheet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sheet</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Sheet</em>' attribute.
	 * @see #setSheet(String)
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage#getSheetLocation_Sheet()
	 * @model required="true"
	 * @generated
	 */
	String getSheet();

	/**
	 * Sets the value of the '{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getSheet
	 * <em>Sheet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Sheet</em>' attribute.
	 * @see #getSheet()
	 * @generated
	 */
	void setSheet(String value);

	/**
	 * Returns the value of the '<em><b>Column</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Column</em>' attribute.
	 * @see #setColumn(int)
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage#getSheetLocation_Column()
	 * @model required="true"
	 * @generated
	 */
	int getColumn();

	/**
	 * Sets the value of the '{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getColumn
	 * <em>Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Column</em>' attribute.
	 * @see #getColumn()
	 * @generated
	 */
	void setColumn(int value);

	/**
	 * Returns the value of the '<em><b>Row</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Row</em>' attribute.
	 * @see #setRow(int)
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage#getSheetLocation_Row()
	 * @model required="true"
	 * @generated
	 */
	int getRow();

	/**
	 * Sets the value of the '{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation#getRow
	 * <em>Row</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Row</em>' attribute.
	 * @see #getRow()
	 * @generated
	 */
	void setRow(int value);

} // ExcelLocation
