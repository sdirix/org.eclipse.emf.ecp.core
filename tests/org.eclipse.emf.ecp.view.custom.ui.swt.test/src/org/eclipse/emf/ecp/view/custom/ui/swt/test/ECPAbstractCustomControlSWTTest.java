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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT.SWTCustomControlHelper;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderingHelper;
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
			super(createEditableFeatures(), createReferencedFeatures());
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
	public void testHandleValidation() {
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
		validationLabel.setImage(customControl.getStubHelper().getImage(
			ECPAbstractCustomControlSWT.VALIDATION_ERROR_IMAGE));
		customControl.resetValidation();
		assertEquals(null, validationLabel.getImage());
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
		assertFalse(customControl.isDisposed());
		customControl.dispose();
		assertTrue(customControl.isDisposed());
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
