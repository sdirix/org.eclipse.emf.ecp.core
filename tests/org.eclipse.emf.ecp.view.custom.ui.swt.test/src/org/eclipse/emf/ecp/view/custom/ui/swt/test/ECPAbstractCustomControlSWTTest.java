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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT.SWTCustomControlHelper;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.CustomFactory;
import org.eclipse.emf.ecp.view.custom.model.CustomPackage;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl.ECPCustomControlFeature;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
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
		public boolean rendered = false;
		public int lastValidationSeverity;
		public EStructuralFeature lastValidationFeature;
		public boolean disposed = false;
		public Composite textControl;
		private final boolean withControl;
		public boolean validationReseted = false;

		public ECPAbstractCustomControlSWTStub() {
			this(false);
		}

		public ECPAbstractCustomControlSWTStub(boolean withControl) {
			super(createEditableFeatures(), createReferencedFeatures());
			this.withControl = withControl;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createContentControl(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected void createContentControl(Composite composite) {
			final Label label = new Label(composite, SWT.NONE);
			label.setText(LABELTEXT);
			rendered = true;
			if (!withControl) {
				return;
			}
			for (final ECPCustomControlFeature controlFeature : editableFeaturess) {
				if (controlFeature.getTargetFeature() == CustomPackage.eINSTANCE.getCustomControl_Bundle()) {
					textControl = createControl(controlFeature, composite);
				}
			}

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleContentValidation(int,
		 *      org.eclipse.emf.ecore.EStructuralFeature)
		 */
		@Override
		protected void handleContentValidation(int severity, EStructuralFeature feature) {
			lastValidationSeverity = severity;
			lastValidationFeature = feature;

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#resetContentValidation()
		 */
		@Override
		protected void resetContentValidation() {
			validationReseted = true;

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#disposeCustomControl()
		 */
		@Override
		protected void disposeCustomControl() {
			disposed = true;

		}

		/**
		 * @param composite
		 */
		public void createValidationLabelInStub(Composite composite) {
			super.createValidationLabel(composite);

		}

		/**
		 * 
		 */
		public void stubShowError() {
			super.showError(TEST_TITEL, TEST_MESSAGE);
		}

		public SWTCustomControlHelper getStubHelper() {
			return super.getSWTHelper();

		}

		/**
		 * 
		 */
		public void stubInitValidation(Composite parent) {
			super.createValidationLabel(parent);

		}

	}

	@Before
	public void init() {
		referencedFeatures = null;
		editableFeaturess = null;
		customControl = new ECPAbstractCustomControlSWTStub();
		domainObject = CustomFactory.eINSTANCE.createCustomControl();
		customControl.init(ViewTestHelper.createECPControlContext(domainObject,
			SWTViewTestHelper.createShell()));
		testComposite = new Composite(SWTViewTestHelper.createShell(), SWT.NONE);
	}

	/**
	 * @return a test set of {@link ECPCustomControlFeature}
	 */
	public static Set<ECPCustomControlFeature> createReferencedFeatures() {
		if (referencedFeatures == null) {
			referencedFeatures = new HashSet<ECPCustomControl.ECPCustomControlFeature>();
			final EList<EStructuralFeature> eAllStructuralFeatures = CustomPackage.eINSTANCE.getCustomControl()
				.getEAllStructuralFeatures();
			for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
				referencedFeatures.add(new ECPCustomControlFeature(null, eStructuralFeature));
			}

		}
		return referencedFeatures;
	}

	/**
	 * @return a test set of {@link ECPCustomControlFeature}
	 */
	public static Set<ECPCustomControlFeature> createEditableFeatures() {
		if (editableFeaturess == null) {
			editableFeaturess = new HashSet<ECPCustomControl.ECPCustomControlFeature>();
			final EList<EStructuralFeature> eAllStructuralFeatures = CustomPackage.eINSTANCE.getCustomControl()
				.getEAllStructuralFeatures();
			for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
				editableFeaturess.add(new ECPCustomControlFeature(null, eStructuralFeature));
			}

		}
		return editableFeaturess;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createControl(org.eclipse.swt.widgets.Composite)}
	 * .
	 */
	@Test
	public void testCreateControl() {
		final Composite composite = customControl
			.createControl(new Composite(SWTViewTestHelper.createShell(), SWT.NONE));
		assertTrue(customControl.rendered);
		final Composite parentCompositeFromView = SWTCustomControlTest
			.getParentCompositeforInnerContentFromOuterComposite(composite);
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
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#showInfo(java.lang.String, java.lang.String)}
	 * .
	 */

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#createContentControl(org.eclipse.swt.widgets.Composite)}
	 * .
	 */
	@Test
	public void testCreateContentControl() {
		customControl.createContentControl(testComposite);
		assertTrue(testComposite.getChildren()[0] instanceof Label);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#handleValidation(org.eclipse.emf.common.util.Diagnostic)}
	 * .
	 */
	@Test
	public void testHandleValidation() {
		final Diagnostic validate = new Diagnostician().validate(domainObject);
		customControl.handleValidation(validate.getChildren().get(1));
		// Check Label, Check Image
		assertEquals(Diagnostic.ERROR, customControl.lastValidationSeverity);
		assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.lastValidationFeature);
		customControl.validationReseted = false;
		customControl.resetValidation();
		assertTrue(customControl.validationReseted);
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
		assertEquals(Diagnostic.ERROR, customControl.lastValidationSeverity);
		assertSame(CustomPackage.eINSTANCE.getCustomControl_Bundle(), customControl.lastValidationFeature);
		customControl.createControl(testComposite);
		customControl.handleValidation(validate.getChildren().get(1));
		final Composite textControl = customControl.textControl;
		final Control control = textControl.getChildren()[0];
		assertTrue(control instanceof Label);
		final Label label = (Label) control;
		assertNotNull(label.getImage());
		domainObject.setBundle("not empty");
		customControl.validationReseted = false;
		customControl.resetValidation();
		assertTrue(customControl.validationReseted);
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
		assertEquals(severity, customControl.lastValidationSeverity);
		assertSame(validationFeature, customControl.lastValidationFeature);
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
		validationLabel.setImage(customControl.getStubHelper().getImage(
			ECPAbstractCustomControlSWT.VALIDATION_ERROR_IMAGE));
		customControl.resetValidation();
		assertEquals(null, validationLabel.getImage());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT#setEditable(boolean)}.
	 */
	@Test
	public void testSetEditable() {
		final Composite composite = customControl.createControl(testComposite);
		customControl.setEditable(false);
		assertEquals(false, composite.getEnabled());
		// final Composite innerComposite = SWTCustomControlTest
		// .getParentCompositeforInnerContentFromOuterComposite(composite);
		// assertEquals(false, innerComposite.getEnabled());
		// final Control label = getLabelFromComposite(innerComposite);
		// assertEquals(false, label.getEnabled());

	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#init(org.eclipse.emf.ecp.edit.ECPControlContext)}
	 * .
	 */
	@Test
	public void testInit() {
		customControl.init(null);
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#getEditableFeatures()}.
	 */
	@Test
	public void testGetEditableFeatures() {
		final Set<ECPCustomControlFeature> editableFeatures = customControl.getEditableFeatures();
		editableFeatures.removeAll(createEditableFeatures());
		assertTrue(editableFeatures.isEmpty());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl#getReferencedFeatures()}.
	 */
	@Test
	public void testGetReferencedFeatures() {
		final Set<ECPCustomControlFeature> editableFeatures = customControl.getReferencedFeatures();
		editableFeatures.removeAll(createReferencedFeatures());
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
		assertFalse(customControl.disposed);
		customControl.dispose();
		assertTrue(customControl.disposed);
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
		final SWTCustomControlHelper stubHelper = customControl.getStubHelper();
		assertNotNull(stubHelper);
	}
}
