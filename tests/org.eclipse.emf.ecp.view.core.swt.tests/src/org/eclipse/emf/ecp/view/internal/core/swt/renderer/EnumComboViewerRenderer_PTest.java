/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestEnum;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestFactory;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.emf.ecp.view.test.common.swt.spi.DatabindingClassRunner;
import org.eclipse.emf.emfforms.spi.core.services.labelprovider.EMFFormsLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Plugin test for {@link EnumComboViewerSWTRenderer}.
 *
 * @author Lucas Koehler
 *
 */
@RunWith(DatabindingClassRunner.class)
public class EnumComboViewerRenderer_PTest extends AbstractControl_PTest {

	@Before
	public void before() {
		final SWTRendererFactory factory = mock(SWTRendererFactory.class);
		setup();
		renderer = new EnumComboViewerSWTRenderer(vControl, context, factory);
		renderer.init();
	}

	@After
	public void testTearDown() {
		dispose();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.AbstractControl_PTest#mockControl()
	 */
	@Override
	protected void mockControl() {
		final SimpleTestObject eObject = TestFactory.eINSTANCE.createSimpleTestObject();
		super.mockControl(eObject, TestPackage.eINSTANCE.getSimpleTestObject_MyEnum());
	}

	@Test
	public void testDatabindingServiceUsageInitialBinding() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final TestEnum initialValue = TestEnum.B;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, TestEnum.class);

		final Combo combo = setUpDatabindingTest(mockedObservable);
		assertEquals(initialValue.getName(), combo.getText());

	}

	@Test
	public void testDatabindingServiceUsageChangeObservable() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final TestEnum initialValue = TestEnum.B;
		final TestEnum changedValue = TestEnum.C;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, TestEnum.class);

		final Combo combo = setUpDatabindingTest(mockedObservable);
		mockedObservable.setValue(changedValue);
		assertEquals(changedValue.getName(), combo.getText());

	}

	@Test
	public void testDatabindingServiceUsageChangeControl() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final TestEnum initialValue = TestEnum.B;
		final TestEnum changedValue = TestEnum.C;
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, TestEnum.class);

		final Combo combo = setUpDatabindingTest(mockedObservable);
		combo.setText(changedValue.getName());
		combo.notifyListeners(SWT.Selection, new Event());

		assertEquals(changedValue.getName(), ((TestEnum) mockedObservable.getValue()).getName());

	}

	/**
	 * Universal set up stuff for the data binding test cases.
	 *
	 * @param mockedObservable
	 * @return
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	private Combo setUpDatabindingTest(final WritableValue mockedObservable) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		Mockito.reset(databindingService);
		when(databindingService.getObservableValue(any(VDomainModelReference.class), any(EObject.class))).thenReturn(
			mockedObservable);

		final Control renderControl = renderControl(new SWTGridCell(0, 2, renderer));

		final Combo combo = (Combo) renderControl;
		return combo;
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
}
