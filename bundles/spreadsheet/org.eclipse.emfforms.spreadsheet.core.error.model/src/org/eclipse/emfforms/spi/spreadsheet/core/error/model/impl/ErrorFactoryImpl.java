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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.DMRLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.EMFLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorFactory;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorPackage;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorReport;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SettingLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.Severity;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SheetLocation;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SpreadsheetImportResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ErrorFactoryImpl extends EFactoryImpl implements ErrorFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static ErrorFactory init() {
		try {
			final ErrorFactory theErrorFactory = (ErrorFactory) EPackage.Registry.INSTANCE
				.getEFactory(ErrorPackage.eNS_URI);
			if (theErrorFactory != null) {
				return theErrorFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ErrorFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ErrorFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ErrorPackage.SPREADSHEET_IMPORT_RESULT:
			return createSpreadsheetImportResult();
		case ErrorPackage.ERROR_REPORT:
			return createErrorReport();
		case ErrorPackage.SHEET_LOCATION:
			return createSheetLocation();
		case ErrorPackage.EMF_LOCATION:
			return createEMFLocation();
		case ErrorPackage.SETTING_LOCATION:
			return createSettingLocation();
		case ErrorPackage.DMR_LOCATION:
			return createDMRLocation();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case ErrorPackage.SEVERITY:
			return createSeverityFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case ErrorPackage.SEVERITY:
			return convertSeverityToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SpreadsheetImportResult createSpreadsheetImportResult() {
		final SpreadsheetImportResultImpl spreadsheetImportResult = new SpreadsheetImportResultImpl();
		return spreadsheetImportResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ErrorReport createErrorReport() {
		final ErrorReportImpl errorReport = new ErrorReportImpl();
		return errorReport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMFLocation createEMFLocation() {
		final EMFLocationImpl emfLocation = new EMFLocationImpl();
		return emfLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SettingLocation createSettingLocation() {
		final SettingLocationImpl settingLocation = new SettingLocationImpl();
		return settingLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DMRLocation createDMRLocation() {
		final DMRLocationImpl dmrLocation = new DMRLocationImpl();
		return dmrLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SheetLocation createSheetLocation() {
		final SheetLocationImpl sheetLocation = new SheetLocationImpl();
		return sheetLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Severity createSeverityFromString(EDataType eDataType, String initialValue) {
		final Severity result = Severity.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
				"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertSeverityToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ErrorPackage getErrorPackage() {
		return (ErrorPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ErrorPackage getPackage() {
		return ErrorPackage.eINSTANCE;
	}

	@Override
	public EMFLocation createEMFLocation(EObject root, SettingLocation settingLocation, DMRLocation dmrLocation) {
		final EMFLocation location = createEMFLocation();
		location.setRoot(root);
		location.setSettingLocation(settingLocation);
		location.setDmrLocation(dmrLocation);
		return location;
	}

	@Override
	public EMFLocation createEMFLocation(EObject root, DMRLocation dmrLocation) {
		return createEMFLocation(root, null, dmrLocation);
	}

	@Override
	public EMFLocation createEMFLocation(EObject root, SettingLocation settingLocation) {
		return createEMFLocation(root, settingLocation, null);
	}

	@Override
	public SettingLocation createSettingLocation(EObject eObject, EStructuralFeature feature) {
		final SettingLocation location = createSettingLocation();
		location.setEObject(eObject);
		location.setFeature(feature);
		return location;
	}

	@Override
	public DMRLocation createDMRLocation(VDomainModelReference dmr) {
		final DMRLocation location = createDMRLocation();
		location.setDomainModelReference(dmr);
		return location;
	}

	@Override
	public SheetLocation createSheetLocation(String sheet, int column, int row) {
		final SheetLocation location = createSheetLocation();
		location.setSheet(sheet);
		location.setColumn(column);
		location.setRow(row);
		return location;
	}

} // ErrorFactoryImpl
