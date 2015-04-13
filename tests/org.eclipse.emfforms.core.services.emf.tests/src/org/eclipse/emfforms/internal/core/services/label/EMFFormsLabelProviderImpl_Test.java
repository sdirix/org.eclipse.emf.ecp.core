/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.label;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.MessageFormat;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.emfspecificservice.EMFSpecificService;
import org.eclipse.emfforms.spi.core.services.label.NoLabelFoundException;
import org.eclipse.emfforms.spi.core.services.locale.EMFFormsLocaleProvider;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsLabelProviderImpl_Test {

	/**
	 * Helper Interface for mocking.
	 *
	 * @author Eugen Neufeld
	 *
	 */
	public interface TestObservableValue extends IObservableValue, IObserving {
	}

	private EMFFormsLabelProviderImpl labelProvider;
	private EMFFormsDatabinding databinding;
	private DefaultRealm realm;

	@Before
	public void setup() {
		labelProvider = new EMFFormsLabelProviderImpl();
		databinding = mock(EMFFormsDatabinding.class);
		labelProvider.setEMFFormsDatabinding(databinding);
		realm = new DefaultRealm();
	}

	@After
	public void tearDown() {
		realm.dispose();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDisplayNameVDomainModelReferenceNull() throws NoLabelFoundException {
		labelProvider.getDisplayName(null);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NoLabelFoundException.class)
	public void testGetDisplayNameVDomainModelReferenceThrowDatabindingException() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		when(databinding.getValueProperty(domainModelReference)).thenThrow(DatabindingFailedException.class);

		final ReportService reportService = mock(ReportService.class);
		labelProvider.setReportService(reportService);
		labelProvider.getDisplayName(domainModelReference);
		verify(reportService).report(any(DatabindingFailedReport.class));
	}

	@Test
	public void testGetDisplayNameVDomainModelReference() throws NoLabelFoundException, DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getEContainingClass()).thenReturn(EcorePackage.eINSTANCE.getEObject());
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final EMFSpecificService emfSpecificService = mock(EMFSpecificService.class);
		final IItemPropertySource propertySource = mock(IItemPropertySource.class);
		when(emfSpecificService.getIItemPropertySource(any(EObject.class))).thenReturn(propertySource);
		labelProvider.setEMFSpecificService(emfSpecificService);

		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		labelProvider.setEMFFormsLocalizationService(localizationService);

		final String key = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String value = "My Value"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), key)).thenReturn(value);

		final IObservableValue displayName = labelProvider.getDisplayName(domainModelReference);
		assertEquals(value, displayName.getValue());
	}

	@Test
	public void testGetDisplayNameVDomainModelReferenceFallbackAbstract() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getName()).thenReturn("myValue"); //$NON-NLS-1$
		final EClass eClass = mock(EClass.class);
		when(eClass.isAbstract()).thenReturn(true);
		when(eStructuralFeature.getEContainingClass()).thenReturn(eClass);
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final IObservableValue displayName = labelProvider.getDisplayName(domainModelReference);
		assertEquals("My Value", displayName.getValue()); //$NON-NLS-1$
	}

	@Test
	public void testGetDisplayNameVDomainModelReferenceFallbackInterface() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getName()).thenReturn("myValue"); //$NON-NLS-1$
		final EClass eClass = mock(EClass.class);
		when(eClass.isInterface()).thenReturn(true);
		when(eStructuralFeature.getEContainingClass()).thenReturn(eClass);
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final IObservableValue displayName = labelProvider.getDisplayName(domainModelReference);
		assertEquals("My Value", displayName.getValue()); //$NON-NLS-1$
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDisplayNameVDomainModelReferenceNullEObject() throws NoLabelFoundException {
		labelProvider.getDisplayName(null, mock(EObject.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDisplayNameVDomainModelReferenceEObjectNull() throws NoLabelFoundException {
		labelProvider.getDisplayName(mock(VDomainModelReference.class), null);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NoLabelFoundException.class)
	public void testGetDisplayNameVDomainModelReferenceEObjectThrowDatabindingException() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final EObject eObject = mock(EObject.class);
		when(databinding.getObservableValue(domainModelReference, eObject)).thenThrow(DatabindingFailedException.class);

		final ReportService reportService = mock(ReportService.class);
		labelProvider.setReportService(reportService);
		labelProvider.getDisplayName(domainModelReference, eObject);
		verify(reportService).report(any(DatabindingFailedReport.class));
	}

	@Test
	public void testGetDisplayNameVDomainModelReferenceEObject() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final EObject eObject = EcoreFactory.eINSTANCE.createEObject();
		final TestObservableValue observableValue = mock(TestObservableValue.class);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(observableValue.getValueType()).thenReturn(eStructuralFeature);
		when(observableValue.getObserved()).thenReturn(eObject);

		when(databinding.getObservableValue(domainModelReference, eObject)).thenReturn(observableValue);

		final EMFSpecificService emfSpecificService = mock(EMFSpecificService.class);
		final IItemPropertySource propertySource = mock(IItemPropertySource.class);
		when(emfSpecificService.getIItemPropertySource(any(EObject.class))).thenReturn(propertySource);
		labelProvider.setEMFSpecificService(emfSpecificService);

		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		labelProvider.setEMFFormsLocalizationService(localizationService);

		final String key = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String value = "My Value"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), key)).thenReturn(value);

		final IObservableValue displayName = labelProvider.getDisplayName(domainModelReference, eObject);
		assertEquals(value, displayName.getValue());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDescriptionVDomainModelReferenceNull() throws NoLabelFoundException {
		labelProvider.getDescription(null);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NoLabelFoundException.class)
	public void testGetDescriptionVDomainModelReferenceThrowDatabindingException() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		when(databinding.getValueProperty(domainModelReference)).thenThrow(DatabindingFailedException.class);

		final ReportService reportService = mock(ReportService.class);
		labelProvider.setReportService(reportService);
		labelProvider.getDescription(domainModelReference);
		verify(reportService).report(any(DatabindingFailedReport.class));
	}

	@Test
	public void testGetDescriptionVDomainModelReference() throws NoLabelFoundException, DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);

		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getEContainingClass()).thenReturn(EcorePackage.eINSTANCE.getEObject());
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final EMFSpecificService emfSpecificService = mock(EMFSpecificService.class);
		final IItemPropertySource propertySource = mock(IItemPropertySource.class);
		when(emfSpecificService.getIItemPropertySource(any(EObject.class))).thenReturn(propertySource);
		labelProvider.setEMFSpecificService(emfSpecificService);

		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		labelProvider.setEMFFormsLocalizationService(localizationService);

		final String key = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String featureText = "My Feature"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), key)).thenReturn(featureText);
		final String descriptionPlaceHolder = "My Description {1} {2}"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), "_UI_PropertyDescriptor_description")) //$NON-NLS-1$
			.thenReturn(descriptionPlaceHolder);
		final String eObjectText = "My EObject"; //$NON-NLS-1$
		when(
			localizationService.getString(propertySource.getClass(),
				String.format("_UI_%1$s_type", EcorePackage.eINSTANCE.getEObject().getName()))).thenReturn(eObjectText); //$NON-NLS-1$

		final IObservableValue description = labelProvider.getDescription(domainModelReference);
		assertEquals(MessageFormat.format(descriptionPlaceHolder, featureText, eObjectText), description.getValue());
	}

	@Test
	public void testGetDescriptionVDomainModelReferenceFallbackAbstract() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getName()).thenReturn("myValue"); //$NON-NLS-1$
		final EClass eClass = mock(EClass.class);
		when(eClass.isAbstract()).thenReturn(true);
		when(eStructuralFeature.getEContainingClass()).thenReturn(eClass);
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final IObservableValue displayName = labelProvider.getDescription(domainModelReference);
		assertEquals("My Value", displayName.getValue()); //$NON-NLS-1$
	}

	@Test
	public void testGetDescriptionVDomainModelReferenceFallbackInterface() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final IValueProperty valueProperty = mock(IValueProperty.class);
		when(databinding.getValueProperty(domainModelReference)).thenReturn(valueProperty);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(eStructuralFeature.getName()).thenReturn("myValue"); //$NON-NLS-1$
		final EClass eClass = mock(EClass.class);
		when(eClass.isInterface()).thenReturn(true);
		when(eStructuralFeature.getEContainingClass()).thenReturn(eClass);
		when(valueProperty.getValueType()).thenReturn(eStructuralFeature);

		final IObservableValue displayName = labelProvider.getDescription(domainModelReference);
		assertEquals("My Value", displayName.getValue()); //$NON-NLS-1$
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDescriptionVDomainModelReferenceNullEObject() throws NoLabelFoundException {
		labelProvider.getDescription(null, mock(EObject.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDescriptionVDomainModelReferenceEObjectNull() throws NoLabelFoundException {
		labelProvider.getDescription(mock(VDomainModelReference.class), null);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NoLabelFoundException.class)
	public void testGetDescriptionVDomainModelReferenceEObjectThrowDatabindingException() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final EObject eObject = mock(EObject.class);
		when(databinding.getObservableValue(domainModelReference, eObject)).thenThrow(DatabindingFailedException.class);

		final ReportService reportService = mock(ReportService.class);
		labelProvider.setReportService(reportService);
		labelProvider.getDescription(domainModelReference, eObject);
		verify(reportService).report(any(DatabindingFailedReport.class));
	}

	@Test
	public void testGetDescriptionVDomainModelReferenceEObject() throws NoLabelFoundException,
		DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final EObject eObject = EcoreFactory.eINSTANCE.createEObject();
		final TestObservableValue observableValue = mock(TestObservableValue.class);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(observableValue.getValueType()).thenReturn(eStructuralFeature);
		when(observableValue.getObserved()).thenReturn(eObject);

		when(databinding.getObservableValue(domainModelReference, eObject)).thenReturn(observableValue);

		final EMFSpecificService emfSpecificService = mock(EMFSpecificService.class);
		final IItemPropertySource propertySource = mock(IItemPropertySource.class);
		when(emfSpecificService.getIItemPropertySource(any(EObject.class))).thenReturn(propertySource);
		labelProvider.setEMFSpecificService(emfSpecificService);

		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		labelProvider.setEMFFormsLocalizationService(localizationService);

		final String key = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String featureText = "My Feature"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), key)).thenReturn(featureText);
		final String descriptionPlaceHolder = "My Description {1} {2}"; //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), "_UI_PropertyDescriptor_description")) //$NON-NLS-1$
			.thenReturn(descriptionPlaceHolder);
		final String eObjectText = "My EObject"; //$NON-NLS-1$
		when(
			localizationService.getString(propertySource.getClass(),
				String.format("_UI_%1$s_type", EcorePackage.eINSTANCE.getEObject().getName()))).thenReturn(eObjectText); //$NON-NLS-1$

		final IObservableValue description = labelProvider.getDescription(domainModelReference, eObject);
		assertEquals(MessageFormat.format(descriptionPlaceHolder, featureText, eObjectText), description.getValue());
	}

	@Test
	public void testNotifyLocaleChange() throws NoLabelFoundException, DatabindingFailedException {
		final VDomainModelReference domainModelReference = mock(VDomainModelReference.class);
		final EObject eObject = EcoreFactory.eINSTANCE.createEObject();
		final TestObservableValue observableValue = mock(TestObservableValue.class);
		final EStructuralFeature eStructuralFeature = mock(EStructuralFeature.class);
		when(observableValue.getValueType()).thenReturn(eStructuralFeature);
		when(observableValue.getObserved()).thenReturn(eObject);

		when(databinding.getObservableValue(domainModelReference, eObject)).thenReturn(observableValue);

		final EMFSpecificService emfSpecificService = mock(EMFSpecificService.class);
		final IItemPropertySource propertySource = mock(IItemPropertySource.class);
		when(emfSpecificService.getIItemPropertySource(any(EObject.class))).thenReturn(propertySource);
		labelProvider.setEMFSpecificService(emfSpecificService);

		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		labelProvider.setEMFFormsLocalizationService(localizationService);

		final String keyDisplayName = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String valueDisplayName = "My Value"; //$NON-NLS-1$
		final String valueDisplayNameNew = "My Value New"; //$NON-NLS-1$

		final String keyDescription = String.format(
			"_UI_%1$s_%2$s_feature", EcorePackage.eINSTANCE.getEObject().getName(), eStructuralFeature.getName()); //$NON-NLS-1$
		final String featureText = "My Feature"; //$NON-NLS-1$
		final String descriptionPlaceHolder = "My Description {1} {2}"; //$NON-NLS-1$
		final String eObjectText = "My EObject"; //$NON-NLS-1$
		final String descriptionPlaceHolderNew = "My Description {1} {2} New"; //$NON-NLS-1$
		final String valueDescription = MessageFormat.format(descriptionPlaceHolder, featureText, eObjectText);
		final String valueDescriptionNew = MessageFormat.format(descriptionPlaceHolderNew, featureText, eObjectText);

		when(localizationService.getString(propertySource.getClass(), keyDescription)).thenReturn(featureText);
		when(localizationService.getString(propertySource.getClass(), "_UI_PropertyDescriptor_description")) //$NON-NLS-1$
			.thenReturn(descriptionPlaceHolder, descriptionPlaceHolderNew);
		when(
			localizationService.getString(propertySource.getClass(),
				String.format("_UI_%1$s_type", EcorePackage.eINSTANCE.getEObject().getName()))).thenReturn(eObjectText); //$NON-NLS-1$
		when(localizationService.getString(propertySource.getClass(), keyDisplayName)).thenReturn(valueDisplayName,
			valueDisplayNameNew);

		final IObservableValue labelObservableValue = labelProvider.getDisplayName(domainModelReference, eObject);
		final IObservableValue descriptionObservableValue = labelProvider.getDescription(domainModelReference, eObject);
		assertEquals(valueDisplayName, labelObservableValue.getValue());
		assertEquals(valueDescription, descriptionObservableValue.getValue());

		labelProvider.notifyLocaleChange();
		assertEquals(valueDisplayNameNew, labelObservableValue.getValue());
		assertEquals(valueDescriptionNew, descriptionObservableValue.getValue());
	}

	@Test
	public void testSetEMFFormsLocaleProvider() {
		final EMFFormsLocaleProvider localeProvider = mock(EMFFormsLocaleProvider.class);
		labelProvider.setEMFFormsLocaleProvider(localeProvider);
		verify(localeProvider).addEMFFormsLocaleChangeListener(labelProvider);
	}
}
