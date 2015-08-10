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
package org.eclipse.emfforms.spi.spreadsheet.core.error.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.EMFLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorFactory;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorReport;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.Severity;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SpreadsheetImportResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reports</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.impl.SpreadsheetImportResultImpl#getErrorReports
 * <em>Error Reports</em>}</li>
 * <li>{@link org.eclipse.emfforms.spi.spreadsheet.core.error.model.impl.SpreadsheetImportResultImpl#getImportedEObjects
 * <em>Imported EObjects</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpreadsheetImportResultImpl extends MinimalEObjectImpl.Container implements SpreadsheetImportResult {
	/**
	 * The cached value of the '{@link #getErrorReports() <em>Error Reports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getErrorReports()
	 * @generated
	 * @ordered
	 */
	protected EList<ErrorReport> errorReports;
	/**
	 * The cached value of the '{@link #getImportedEObjects() <em>Imported EObjects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getImportedEObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> importedEObjects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SpreadsheetImportResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ErrorPackage.Literals.SPREADSHEET_IMPORT_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<ErrorReport> getErrorReports() {
		if (errorReports == null) {
			errorReports = new EObjectContainmentEList<ErrorReport>(ErrorReport.class, this,
				ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS);
		}
		return errorReports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<EObject> getImportedEObjects() {
		if (importedEObjects == null) {
			importedEObjects = new EObjectContainmentEList<EObject>(EObject.class, this,
				ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS);
		}
		return importedEObjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS:
			return ((InternalEList<?>) getErrorReports()).basicRemove(otherEnd, msgs);
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS:
			return ((InternalEList<?>) getImportedEObjects()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS:
			return getErrorReports();
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS:
			return getImportedEObjects();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS:
			getErrorReports().clear();
			getErrorReports().addAll((Collection<? extends ErrorReport>) newValue);
			return;
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS:
			getImportedEObjects().clear();
			getImportedEObjects().addAll((Collection<? extends EObject>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS:
			getErrorReports().clear();
			return;
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS:
			getImportedEObjects().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__ERROR_REPORTS:
			return errorReports != null && !errorReports.isEmpty();
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT__IMPORTED_EOBJECTS:
			return importedEObjects != null && !importedEObjects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	@Override
	public void reportError(Severity severity, String message) {
		reportError(severity, message, null, null);
	}

	@Override
	public void reportError(Severity severity, String message, EMFLocation emfLocation) {
		reportError(severity, message, emfLocation, null);
	}

	@Override
	public void reportError(Severity severity, String message, SheetLocation sheetLocation) {
		reportError(severity, message, null, sheetLocation);
	}

	@Override
	public void reportError(Severity severity, String message, EMFLocation emfLocation, SheetLocation sheetLocation) {
		final ErrorReport report = ErrorFactory.eINSTANCE.createErrorReport();
		report.setSeverity(severity);
		report.setMessage(message);
		report.setEmfLocation(emfLocation);
		report.setSheetLocation(sheetLocation);
		getErrorReports().add(report);
	}

} // ErrorReportsImpl
