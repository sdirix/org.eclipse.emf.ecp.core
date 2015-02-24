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
 * Lucas Koehler - databinding tests
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.util.Locale;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.edit.internal.swt.controls.NumericalHelper;
import org.eclipse.emf.ecp.edit.spi.ViewLocaleService;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.emf.ecp.view.test.common.swt.spi.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.spi.SWTTestUtil;
import org.eclipse.emf.emfforms.spi.core.services.labelprovider.EMFFormsLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@SuppressWarnings("restriction")
@RunWith(DatabindingClassRunner.class)
public class NumberControlRenderer_PTest extends AbstractControl_PTest {

	@Before
	public void before() {
		final SWTRendererFactory factory = mock(SWTRendererFactory.class);
		setup();
		renderer = new NumberControlSWTRenderer(vControl, context, factory);
		renderer.init();
	}

	@After
	public void testTearDown() {
		dispose();
	}

	@Test
	public void renderControlLabelAlignmentNone()
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		final Control render = renderControl(new SWTGridCell(0, 1, renderer));
		assertControl(render);
	}

	@Test
	public void renderControlLabelAlignmentLeft()
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		final Control render = renderControl(new SWTGridCell(0, 2, renderer));

		assertControl(render);
	}

	/**
	 * Tests whether the {@link EMFFormsLabelProvider} is used to get the labels of the control.
	 *
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	@Test
	public void testLabelServiceUsage() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		labelServiceUsage();
	}

	private void assertControl(Control render) {
		assertTrue(Text.class.isInstance(render));
		assertEquals(SWT.RIGHT, Text.class.cast(render).getStyle()
			& SWT.RIGHT);
		assertEquals(SWT.RIGHT, Text.class.cast(render).getStyle()
			& SWT.RIGHT);

		assertEquals("org_eclipse_emf_ecp_control_numerical", Text.class.cast(render).getData(CUSTOM_VARIANT));
	}

	@Override
	protected void mockControl() {
		final EStructuralFeature eObject = EcoreFactory.eINSTANCE.createEAttribute();
		final EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getETypedElement_LowerBound();
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabindingServiceUsageInitialBinding() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final int initialValue = 13;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, Integer.class);
		final Text text = setUpDatabindingTest(mockedObservable);

		final DecimalFormat format = getDecimalFormat(Integer.class);

		assertEquals(format.format(initialValue), text.getText());

	}

	@Test
	public void testDatabindingServiceUsageChangeObservable() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final int initialValue = 13;
		final int changedValue = 42;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, Integer.class);

		final Text text = setUpDatabindingTest(mockedObservable);
		mockedObservable.setValue(changedValue);

		final DecimalFormat format = getDecimalFormat(Integer.class);

		assertEquals(format.format(changedValue), text.getText());

	}

	@Test
	public void testDatabindingServiceUsageChangeControl() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final int initialValue = 13;
		final int changedValue = 42;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, String.class);

		final Text text = setUpDatabindingTest(mockedObservable);

		final DecimalFormat format = getDecimalFormat(Integer.class);
		SWTTestUtil.typeAndFocusOut(text, format.format(changedValue));

		assertEquals(changedValue, mockedObservable.getValue());

	}

	/**
	 * Universal set up stuff for the data binding test cases.
	 *
	 * @param mockedObservable
	 * @return
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	private Text setUpDatabindingTest(final WritableValue mockedObservable) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		Mockito.reset(databindingService);
		when(databindingService.getObservableValue(any(VDomainModelReference.class), any(EObject.class))).thenReturn(
			mockedObservable);

		final Control renderControl = renderControl(new SWTGridCell(0, 2, renderer));
		final Text text = (Text) renderControl;
		return text;
	}

	private DecimalFormat getDecimalFormat(Class<?> instanceClass) {
		return NumericalHelper.setupFormat(getLocale(context), instanceClass);
	}

	private Locale getLocale(ViewModelContext viewModelContext) {
		final ViewLocaleService service = viewModelContext.getService(ViewLocaleService.class);
		if (service == null) {
			return Locale.getDefault();
		}
		return service.getLocale();
	}

}
