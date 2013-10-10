/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.ui.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT.SWTCustomControlHelper;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderingHelper;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.CustomFactory;
import org.eclipse.emf.ecp.view.custom.model.CustomPackage;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlChangeListener;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class ECPAbstractCustomControlSWTTest {

	private static final String LABELTEXT = "Some Text";
	private static Set<ECPCustomControlFeature> allFeatures;
	private static Set<ECPCustomControlFeature> referencedFeatures;
	private static Set<ECPCustomControlFeature> editableFeaturess;
	private ECPAbstractCustomControlSWTStub customControl;
	private Composite testComposite;
	private CustomControl domainObject;

	/**
	 * @author Jonas
	 * 
	 */
	public class ECPAbstractCustomControlSWTStub extends ECPAbstractCustomControlSWT {

		private static final String TEST_MESSAGE = "TestMessage";
		private static final String TEST_TITEL = "TestTitel";
		private boolean rendered;
		private int lastValidationSeverity;
		private EStructuralFeature lastValidationFeature;
		private boolean disposed;
		private Composite textControl;
		private final boolean withControl;
		private boolean validationReseted;

		public ECPAbstractCustomControlSWTStub() {
			this(false);
		}

		public ECPAbstractCustomControlSWTStub(boolean withControl) {
			super(createFeatures());
			this.withControl = withControl;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createControls(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected List<RenderingResultRow<Control>> createControls(Composite composite) {
			final List<RenderingResultRow<Control>> result = new ArrayList<RenderingResultRow<Control>>();

			final Label label = new Label(composite, SWT.NONE);
			label.setText(LABELTEXT);
			setRendered(true);

			if (!withControl) {
				result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
					.createRenderingResultRow(label));
				return result;
			}
			for (final ECPCustomControlFeature controlFeature : editableFeaturess) {
				if (controlFeature.getTargetFeature() == CustomPackage.eINSTANCE.getCustomControl_Bundle()) {
					setTextControl(createControl(controlFeature, composite));
					result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
						.createRenderingResultRow(label, getTextControl()));
				}
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleContentValidation(int,
		 *      org.eclipse.emf.ecore.EStructuralFeature)
		 */
		@Override
		protected void handleContentValidation(int severity, EStructuralFeature feature) {
			setLastValidationSeverity(severity);
			setLastValidationFeature(feature);

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#resetContentValidation()
		 */
		@Override
		protected void resetContentValidation() {
			setValidationReseted(true);

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#disposeCustomControl()
		 */
		@Override
		protected void disposeCustomControl() {
			setDisposed(true);

		}

		/**
		 * @param composite
		 */
		public void createValidationLabelInStub(Composite composite) {
			super.createValidationLabel(composite);

		}

		public void stubShowError() {
			super.showError(TEST_TITEL, TEST_MESSAGE);
		}

		public void stubShowInfo() {
			super.showInfo(TEST_TITEL, TEST_MESSAGE);
		}

		public SWTCustomControlHelper getStubSWTHelper() {
			return super.getSWTHelper();

		}

		public CustomControlHelper getStubHelper() {
			return super.getHelper();

		}

		/**
		 * 
		 */
		public void stubInitValidation(Composite parent) {
			super.createValidationLabel(parent);

		}

		/**
		 * @return the rendered
		 */
		public boolean isRendered() {
			return rendered;
		}

		/**
		 * @param rendered the rendered to set
		 */
		public void setRendered(boolean rendered) {
			this.rendered = rendered;
		}

		/**
		 * @return the lastValidationSeverity
		 */
		public int getLastValidationSeverity() {
			return lastValidationSeverity;
		}

		/**
		 * @param lastValidationSeverity the lastValidationSeverity to set
		 */
		public void setLastValidationSeverity(int lastValidationSeverity) {
			this.lastValidationSeverity = lastValidationSeverity;
		}

		/**
		 * @return the lastValidationFeature
		 */
		public EStructuralFeature getLastValidationFeature() {
			return lastValidationFeature;
		}

		/**
		 * @param lastValidationFeature the lastValidationFeature to set
		 */
		public void setLastValidationFeature(EStructuralFeature lastValidationFeature) {
			this.lastValidationFeature = lastValidationFeature;
		}

		/**
		 * @return the disposed
		 */
		public boolean isDisposed() {
			return disposed;
		}

		/**
		 * @param disposed the disposed to set
		 */
		public void setDisposed(boolean disposed) {
			this.disposed = disposed;
		}

		/**
		 * @return the textControl
		 */
		public Composite getTextControl() {
			return textControl;
		}

		/**
		 * @param textControl the textControl to set
		 */
		public void setTextControl(Composite textControl) {
			this.textControl = textControl;
		}

		/**
		 * @return the validationReseted
		 */
		public boolean isValidationReseted() {
			return validationReseted;
		}

		/**
		 * @param validationReseted the validationReseted to set
		 */
		public void setValidationReseted(boolean validationReseted) {
			this.validationReseted = validationReseted;
		}

	}

	@Before
	public void init() {
		referencedFeatures = null;
		editableFeaturess = null;
		allFeatures = null;
		customControl = new ECPAbstractCustomControlSWTStub();
		domainObject = CustomFactory.eINSTANCE.createCustomControl();
		customControl.init(ViewTestHelper.createECPControlContext(domainObject,
			SWTViewTestHelper.createShell()));
		testComposite = new Composite(SWTViewTestHelper.createShell(), SWT.NONE);
	}

	public static Set<ECPCustomControlFeature> createFeatures() {

		allFeatures = new LinkedHashSet<ECPCustomControlFeature>();
		allFeatures.addAll(createEditableFeatures());
		allFeatures.addAll(createReferencedFeatures());

		return allFeatures;
	}

	/**
	 * @return a test set of {@link ECPCustomControlFeature}
	 */
	public static Set<ECPCustomControlFeature> createReferencedFeatures() {
		if (referencedFeatures == null) {
			referencedFeatures = new LinkedHashSet<ECPCustomControlFeature>();
			final EList<EStructuralFeature> eAllStructuralFeatures = CustomPackage.eINSTANCE.getCustomControl()
				.getEAllStructuralFeatures();
			for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
				referencedFeatures.add(new ECPCustomControlFeature(null, eStructuralFeature, false));
			}

		}
		return referencedFeatures;
	}

	/**
	 * @return a test set of {@link ECPCustomControlFeature}
	 */
	public static Set<ECPCustomControlFeature> createEditableFeatures() {
		if (editableFeaturess == null) {
			editableFeaturess = new LinkedHashSet<ECPCustomControlFeature>();
			final EList<EStructuralFeature> eAllStructuralFeatures = CustomPackage.eINSTANCE.getCustomControl()
				.getEAllStructuralFeatures();
			for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
				editableFeaturess.add(new ECPCustomControlFeature(null, eStructuralFeature, true));
			}

		}
		return editableFeaturess;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createControls(org.eclipse.swt.widgets.Composite)}
	 * .
	 */
	@Test
	public void testCreateControl() {
		// final Composite composite = customControl
		// .createControls(new Composite(SWTViewTestHelper.createShell(), SWT.NONE));

		final Composite composite = new Composite(SWTViewTestHelper.createShell(), SWT.NONE);
		customControl.createControls(composite);

		assertTrue(customControl.isRendered());
		final Composite parentCompositeFromView = composite;
		final Control control = getLabelFromComposite(parentCompositeFromView);
		assertTrue("Control is not the one rendered by the custom control", control instanceof Label);
		final Label label = (Label) control;
		assertEquals(LABELTEXT, label.getText());
	}

	private Control getLabelFromComposite(final Composite parentCompositeFromView) {
		return parentCompositeFromView.getChildren()[0];
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createValidationLabel(org.eclipse.swt.widgets.Composite)}
	 * .
	 */
	@Test
	public void testCreateValidationLabel() {
		customControl.createValidationLabelInStub(testComposite);
		final Control control = testComposite.getChildren()[0];
		assertTrue(control instanceof Label);
		assertNull(control.getLayoutData());

	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#showInfo(java.lang.String, java.lang.String)}
	 * .
	 */

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createControls(org.eclipse.swt.widgets.Composite)}
	 * .
	 */
	@Test
	public void testCreateContentControl() {
		customControl.createControls(testComposite);
		assertTrue(testComposite.getChildren()[0] instanceof Label);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleValidation(org.eclipse.emf.common.util.Diagnostic)}
	 * .
	 */
	@Test
	public void testHandleValidationWithoutLabel() {
		final Diagnostic validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate.getChildren().get(1));
		// Check Label, Check Image
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
		assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.getLastValidationFeature());
		customControl.setValidationReseted(false);
		customControl.resetValidation();
		assertTrue(customControl.isValidationReseted());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleValidation(org.eclipse.emf.common.util.Diagnostic)}
	 * .
	 */
	@Test
	public void testHandleValidationWithLabel() {
		customControl.createValidationLabelInStub(testComposite);
		final Diagnostic validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate.getChildren().get(1));
		// Check Label, Check Image
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
		assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.getLastValidationFeature());
		customControl.setValidationReseted(false);
		customControl.resetValidation();
		assertTrue(customControl.isValidationReseted());
	}

	@Test
	public void testHandleValidationWithLabelOriginalDiagniostic() {
		customControl.createValidationLabelInStub(testComposite);
		final Diagnostic validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate);
		// Check Label, Check Image
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
		// FIXME should a fitting sub diagnostic have been used?
		// assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.getLastValidationFeature());
		customControl.setValidationReseted(false);
		customControl.resetValidation();
		assertTrue(customControl.isValidationReseted());
	}

	@Test
	public void testHandleValidationNotErrorWarning() {
		// FIXME accept diagnostics without eObject?
		final Diagnostic validate = new BasicDiagnostic(Diagnostic.OK, null, 0, "All right!", null);
		customControl.handleValidation(validate);
		// FIXME correct assumption?
		assertTrue(customControl.isValidationReseted());
	}

	@Test
	public void testHandleValidationWarningNoData() {
		// FIXME accept diagnostics without eObject?
		final Diagnostic validate = new BasicDiagnostic(Diagnostic.WARNING, null, 0, "Warning!", null);
		customControl.handleValidation(validate);
		assertEquals(Diagnostic.WARNING, customControl.getLastValidationSeverity());
	}

	@Test
	public void testHandleValidationErrorOnlyEObject() {
		final Diagnostic validate = new BasicDiagnostic(Diagnostic.ERROR, null, 0, "Error!",
			new Object[] { domainObject });
		customControl.handleValidation(validate);
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
	}

	@Test
	public void testHandleValidationErrorTwoEObject() {
		final Diagnostic validate = new BasicDiagnostic(Diagnostic.ERROR, null, 0, "Error!",
			new Object[] { domainObject, domainObject });
		customControl.handleValidation(validate);
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleValidation(org.eclipse.emf.common.util.Diagnostic)}
	 * .
	 */
	@Test
	public void testHandleValidationOfControl() {
		customControl = new ECPAbstractCustomControlSWTStub(true);
		domainObject = CustomFactory.eINSTANCE.createCustomControl();
		customControl.init(ViewTestHelper.createECPControlContext(domainObject,
			SWTViewTestHelper.createShell()));
		Diagnostic validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate.getChildren().get(1));
		// Check Label, Check Image
		assertEquals(Diagnostic.ERROR, customControl.getLastValidationSeverity());
		assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.getLastValidationFeature());
		customControl.createControls(testComposite);
		customControl.handleValidation(validate.getChildren().get(1));
		final Composite textControl = customControl.getTextControl();
		final Control control = textControl.getChildren()[0];
		assertTrue(control instanceof Label);
		final Label label = (Label) control;
		assertNotNull(label.getImage());
		domainObject.setBundle("not empty");
		customControl.setValidationReseted(false);
		customControl.resetValidation();
		assertTrue(customControl.isValidationReseted());
		validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate.getChildren().get(1));
		assertNull(label.getImage());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleContentValidation(int, org.eclipse.emf.ecore.EStructuralFeature)}
	 * .
	 */
	@Test
	public void testHandleContentValidation() {
		final EAttribute validationFeature = CustomPackage.eINSTANCE.getCustomControl_Bundle();
		final int severity = 0;
		customControl.handleContentValidation(severity, validationFeature);
		assertEquals(severity, customControl.getLastValidationSeverity());
		assertSame(validationFeature, customControl.getLastValidationFeature());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#resetValidation()}.
	 */
	@Test
	public void testResetValidation() {
		customControl.resetValidation();
		final Composite composite = new Composite(SWTViewTestHelper.createShell(), SWT.NONE);
		customControl.createValidationLabelInStub(composite);
		final Label validationLabel = (Label) composite.getChildren()[0];
		validationLabel.setImage(customControl.getStubSWTHelper().getImage(
			ECPAbstractCustomControlSWT.VALIDATION_ERROR_IMAGE));
		customControl.resetValidation();
		assertEquals(false, validationLabel.isVisible());
	}

	@Test
	public void testInit() {
		customControl.init(ViewTestHelper.createECPControlContext(domainObject,
			SWTViewTestHelper.createShell()));
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#getECPCustomControlFeatures()}
	 * .
	 */
	@Test
	public void testGetEditableFeatures() {
		final Set<ECPCustomControlFeature> editableFeatures = customControl.getECPCustomControlFeatures();
		editableFeatures.removeAll(createFeatures());
		assertTrue(editableFeatures.isEmpty());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#showLabel()}.
	 */
	@Test
	public void testShowLabel() {
		assertFalse(customControl.showLabel());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#dispose()}.
	 */
	@Test
	public void testDispose() {
		customControl.createValidationLabelInStub(testComposite);
		final Label validationLabel = (Label) testComposite.getChildren()[0];

		assertFalse(validationLabel.isDisposed());
		assertFalse(customControl.isDisposed());
		customControl.dispose();
		assertTrue(customControl.isDisposed());
		assertTrue(validationLabel.isDisposed());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#disposeCustomControl()}.
	 */
	@Test
	public void testDisposeCustomControl() {
		customControl.disposeCustomControl();
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#getHelper()}.
	 */
	@Test
	public void testGetHelper() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNotNull(stubHelper);
	}

	@Test
	public void testGetAddImage() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNotNull("Add image not loaded.", stubHelper.getImage(ECPAbstractCustomControlSWT.ADD_IMAGE));
	}

	@Test
	public void testGetDeleteImage() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNotNull("Delete image not loaded.", stubHelper.getImage(ECPAbstractCustomControlSWT.DELETE_IMAGE));
	}

	@Test
	public void testGetHelpImage() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNotNull("Help image not loaded.", stubHelper.getImage(ECPAbstractCustomControlSWT.HELP_IMAGE));

	}

	@Test
	public void testGetValidationErrorImage() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNotNull("ValidationError image not loaded.",
			stubHelper.getImage(ECPAbstractCustomControlSWT.VALIDATION_ERROR_IMAGE));
	}

	@Test
	public void testGetDefaultImage() {
		final SWTCustomControlHelper stubHelper = customControl.getStubSWTHelper();
		assertNull("Found undefined image.",
			stubHelper.getImage(-1));
	}

	private ECPCustomControlFeature getFeature(Set<ECPCustomControlFeature> features,
		EStructuralFeature structuralFeature, boolean isEditable) {
		final Iterator<ECPCustomControlFeature> iterator = features.iterator();

		while (iterator.hasNext()) {
			final ECPCustomControlFeature feature = iterator.next();
			if (feature.getTargetFeature() == structuralFeature && feature.isEditable() == isEditable) {
				return feature;
			}
		}

		throw new NoSuchElementException();
	}

	@Test
	public void testCustomControlGetHelp() {
		final ECPCustomControlFeature feature = editableFeaturess.iterator().next();
		final String help = customControl.getStubHelper().getHelp(feature);
		final ComposedAdapterFactory caf = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator afid = new AdapterFactoryItemDelegator(caf);
		final IItemPropertyDescriptor propertyDescriptor = afid.getPropertyDescriptor(feature.getRelevantEObject(),
			feature.getTargetFeature());
		final String description = propertyDescriptor.getDescription(null);
		assertEquals(description, help);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCustomControlGetHelpWrongFeature() {
		final ECPCustomControlFeature feature = new ECPCustomControlFeature(null,
			EcorePackage.eINSTANCE.getENamedElement_Name(), false);
		customControl.getStubHelper().getHelp(feature);
		fail("No Exception thrown");
	}

	@Test
	public void testCustomControlGetLabel() {
		final ECPCustomControlFeature feature = editableFeaturess.iterator().next();
		final String label = customControl.getStubHelper().getLabel(feature);
		final ComposedAdapterFactory caf = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator afid = new AdapterFactoryItemDelegator(caf);
		final IItemPropertyDescriptor propertyDescriptor = afid.getPropertyDescriptor(feature.getRelevantEObject(),
			feature.getTargetFeature());
		final String displayName = propertyDescriptor.getDisplayName(null);
		assertEquals(displayName, label);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCustomControlGetLabelWrongFeature() {
		final ECPCustomControlFeature feature = new ECPCustomControlFeature(null,
			EcorePackage.eINSTANCE.getENamedElement_Name(), false);
		customControl.getStubHelper().getLabel(feature);
		fail("No Exception thrown");
	}

	/**
	 * Test set value on {@link ECPCustomControlFeature}.
	 */
	@Test
	public void testCustomControlFeatureSet() {
		final ECPCustomControlFeature bundleFeature = getFeature(customControl.getECPCustomControlFeatures(),
			CustomPackage.eINSTANCE.getCustomControl_Bundle(), true);
		bundleFeature.setValue("test");
		assertEquals(domainObject.eGet(CustomPackage.eINSTANCE.getCustomControl_Bundle()), "test");
	}

	/**
	 * Test set value on {@link ECPCustomControlFeature} that is not editable.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testCustomControlFeatureSetNotEditable() {
		final ECPCustomControlFeature bundleFeature = getFeature(customControl.getECPCustomControlFeatures(),
			CustomPackage.eINSTANCE.getCustomControl_Bundle(), false);
		bundleFeature.setValue("test");
	}

	/**
	 * Test get value on {@link ECPCustomControlFeature}.
	 */
	@Test
	public void testCustomControlFeatureGet() {
		final ECPCustomControlFeature bundleFeature = getFeature(customControl.getECPCustomControlFeatures(),
			CustomPackage.eINSTANCE.getCustomControl_Bundle(), true);
		assertEquals(domainObject.eGet(CustomPackage.eINSTANCE.getCustomControl_Bundle()), bundleFeature.getValue());
	}

	/**
	 * Test set listener on {@link ECPCustomControlFeature}.
	 */
	@Test
	public void testCustomControlFeatureListener() {
		final ECPCustomControlFeature bundleFeature = getFeature(customControl.getECPCustomControlFeatures(),
			CustomPackage.eINSTANCE.getCustomControl_Bundle(), true);
		final List<Integer> result = new ArrayList<Integer>();
		bundleFeature.registerChangeListener(new ECPCustomControlChangeListener() {
			public void notifyChanged() {
				result.add(1);
			}
		});

		bundleFeature.setValue("test");
		assertEquals("Listener should have been called once", 1, result.size());
	}

	@Test
	public void testBindTargetToModel() {
		final ECPCustomControlFeature bundleFeature = getFeature(customControl.getECPCustomControlFeatures(),
			CustomPackage.eINSTANCE.getCustomControl_Bundle(), true);
		customControl.createControls(testComposite);
		final Label label = (Label) testComposite.getChildren()[0];
		final IObservableValue obsValue = SWTObservables.observeText(label);
		bundleFeature.bindTargetToModel(obsValue, null, null);
		bundleFeature.setValue("testtesttest");
		assertEquals("testtesttest", label.getText());
	}

	@Test
	public void testReadonlyCustomControl() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		domainObject = CustomFactory.eINSTANCE.createCustomControl();
		domainObject.setReadonly(true);
		domainObject.setBundle("org.eclipse.emf.ecp.view.custom.ui.swt.test");
		domainObject
			.setClassName("org.eclipse.emf.ecp.view.custom.ui.swt.test.CustomControlStub");
		final Control control = SWTViewTestHelper.render(domainObject, SWTViewTestHelper.createShell());
		assertFalse(control.getEnabled());
	}
}
