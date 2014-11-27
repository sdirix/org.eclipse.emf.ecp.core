package org.eclipse.emf.ecp.view.template.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.template.model.VTStyleSelector;
import org.eclipse.emf.ecp.view.template.selector.domainmodelreference.model.VTDomainModelReferenceSelector;
import org.eclipse.emf.ecp.view.template.selector.domainmodelreference.model.VTDomainmodelreferenceFactory;
import org.junit.Before;
import org.junit.Test;

public class DomainModelReferenceSelector_Test {

	private VTDomainModelReferenceSelector domainModelReferenceSelector;

	@Before
	public void setup() {
		domainModelReferenceSelector = VTDomainmodelreferenceFactory.eINSTANCE
				.createDomainModelReferenceSelector();

	}

	@Test
	public void testNotAControl() {
		double specificity = domainModelReferenceSelector.isApplicable(
				mock(VElement.class), mock(ViewModelContext.class));
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}

	@Test
	public void testControlWithoutDomainModelReference() {
		VControl vControl = mock(VControl.class);

		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, mock(ViewModelContext.class));
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}

	@Test
	public void testSelectorDomainModelReferenceNotResolvable() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(mock(EObject.class));
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}
	
	@Test
	public void testControlDomainModelReferenceShort() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		VDomainModelReference controlDomainModelReference=mock(VDomainModelReference.class);
		when(vControl.getDomainModelReference()).thenReturn(controlDomainModelReference);
		when(controlDomainModelReference.getIterator()).thenReturn(new HashSet<Setting>().iterator());
		
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(EcoreFactory.eINSTANCE.createEClass());
		
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}
	
	@Test
	public void testControlDomainModelReferenceLong() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		VDomainModelReference controlDomainModelReference=mock(VDomainModelReference.class);
		when(vControl.getDomainModelReference()).thenReturn(controlDomainModelReference);
		Set<Setting> settings=new LinkedHashSet<Setting>();
		EClass eClass=EcoreFactory.eINSTANCE.createEClass();
		settings.add(InternalEObject.class.cast(eClass).eSetting(EcorePackage.eINSTANCE
						.getENamedElement_Name()));
		settings.add(InternalEObject.class.cast(eClass).eSetting(EcorePackage.eINSTANCE
				.getEClass_Abstract()));
		when(controlDomainModelReference.getIterator()).thenReturn(settings.iterator());
		
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(eClass);
		
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}
	
	@Test
	public void testControlDomainModelReferenceCorrect() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		VDomainModelReference controlDomainModelReference=mock(VDomainModelReference.class);
		when(vControl.getDomainModelReference()).thenReturn(controlDomainModelReference);
		Set<Setting> settings=new LinkedHashSet<Setting>();
		EClass eClass=EcoreFactory.eINSTANCE.createEClass();
		settings.add(InternalEObject.class.cast(eClass).eSetting(EcorePackage.eINSTANCE
						.getENamedElement_Name()));
		
		when(controlDomainModelReference.getIterator()).thenReturn(settings.iterator());
		
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(eClass);
		
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(10d, specificity,
				0d);
	}
	
	@Test
	public void testControlDomainModelReferenceDifferentEObjects() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		VDomainModelReference controlDomainModelReference=mock(VDomainModelReference.class);
		when(vControl.getDomainModelReference()).thenReturn(controlDomainModelReference);
		Set<Setting> settings=new LinkedHashSet<Setting>();
		
		settings.add(InternalEObject.class.cast(EcoreFactory.eINSTANCE.createEClass()).eSetting(EcorePackage.eINSTANCE
						.getENamedElement_Name()));
		
		when(controlDomainModelReference.getIterator()).thenReturn(settings.iterator());
		
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(EcoreFactory.eINSTANCE.createEClass());
		
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}
	
	@Test
	public void testControlDomainModelReferenceDifferentEFeatures() {
		VFeaturePathDomainModelReference selectorDomainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		selectorDomainModelReference
				.setDomainModelEFeature(EcorePackage.eINSTANCE
						.getENamedElement_Name());
		domainModelReferenceSelector
				.setDomainModelReference(selectorDomainModelReference);

		VControl vControl = mock(VControl.class);
		VDomainModelReference controlDomainModelReference=mock(VDomainModelReference.class);
		when(vControl.getDomainModelReference()).thenReturn(controlDomainModelReference);
		Set<Setting> settings=new LinkedHashSet<Setting>();
		
		settings.add(InternalEObject.class.cast(EcoreFactory.eINSTANCE.createEClass()).eSetting(EcorePackage.eINSTANCE
						.getEClass_Abstract()));
		
		when(controlDomainModelReference.getIterator()).thenReturn(settings.iterator());
		
		ViewModelContext viewModelContext = mock(ViewModelContext.class);
		when(viewModelContext.getDomainModel()).thenReturn(EcoreFactory.eINSTANCE.createEClass());
		
		double specificity = domainModelReferenceSelector.isApplicable(
				vControl, viewModelContext);
		assertEquals(VTStyleSelector.NOT_APPLICABLE.doubleValue(), specificity,
				0d);
	}
}
