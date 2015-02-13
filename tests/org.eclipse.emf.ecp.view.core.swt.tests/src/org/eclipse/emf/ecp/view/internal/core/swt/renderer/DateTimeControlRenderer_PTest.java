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

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestFactory;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.emf.ecp.view.test.common.swt.spi.DatabindingClassRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(DatabindingClassRunner.class)
public class DateTimeControlRenderer_PTest extends AbstractControl_PTest {

	@Before
	public void before() {
		final SWTRendererFactory factory = mock(SWTRendererFactory.class);
		setup();
		renderer = new DateTimeControlSWTRenderer(vControl, context, factory);
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

	private void assertControl(Control render) {
		assertTrue(Composite.class.isInstance(render));
		final Composite top = Composite.class.cast(render);
		assertEquals(2, top.getChildren().length);
		assertTrue(Composite.class.isInstance(top.getChildren()[0]));
		assertTrue(Button.class.isInstance(top.getChildren()[1]));
		final Composite stack = Composite.class.cast(top.getChildren()[0]);
		assertTrue(StackLayout.class.isInstance(stack.getLayout()));
		assertEquals(2, stack.getChildren().length);
		assertTrue(Composite.class.isInstance(stack.getChildren()[0]));
		assertTrue(Label.class.isInstance(stack.getChildren()[1]));
		final Composite dateTime = Composite.class.cast(stack.getChildren()[0]);
		assertEquals(3, dateTime.getChildren().length);
		assertTrue(DateTime.class.isInstance(dateTime.getChildren()[0]));
		assertTrue(DateTime.class.isInstance(dateTime.getChildren()[1]));
		assertTrue(Button.class.isInstance(dateTime.getChildren()[2]));
	}

	@Override
	protected void mockControl() {
		final SimpleTestObject eObject = TestFactory.eINSTANCE.createSimpleTestObject();
		super.mockControl(eObject, TestPackage.eINSTANCE.getSimpleTestObject_Date());
	}

	@Test
	public void testDatabindingServiceUsageInitialBinding() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final Date initialValue = new Date();
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, Date.class);

		final DateTime[] widgets = setUpDatabindingTest(mockedObservable);
		final DateTime dateWidget = widgets[0];
		final DateTime timeWidget = widgets[1];

		assertTrue(isDateEqualToDateTimes(initialValue, dateWidget, timeWidget));

	}

	@Test
	public void testDatabindingServiceUsageChangeObservable() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final Date initialValue = new Date();
		final Date changedValue = new Date(System.currentTimeMillis() * 2);
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, Date.class);

		final DateTime[] widgets = setUpDatabindingTest(mockedObservable);
		final DateTime dateWidget = widgets[0];
		final DateTime timeWidget = widgets[1];

		mockedObservable.setValue(changedValue);

		assertTrue(isDateEqualToDateTimes(changedValue, dateWidget, timeWidget));

	}

	@Test
	public void testDatabindingServiceUsageChangeControl() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final Date initialValue = new Date();
		final Date changedValue = new Date(System.currentTimeMillis() * 2);
		final WritableValue mockedObservable = new WritableValue(realm, initialValue, Date.class);

		final DateTime[] widgets = setUpDatabindingTest(mockedObservable);
		final DateTime dateWidget = widgets[0];
		final DateTime timeWidget = widgets[1];

		final Calendar cal = Calendar.getInstance();
		cal.setTime(changedValue);
		dateWidget.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
		dateWidget.notifyListeners(SWT.Selection, new Event());
		timeWidget.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		timeWidget.notifyListeners(SWT.Selection, new Event());

		// mockedObservable.getValue() cannot be compared directly to changedValue because the widgets can't save
		// milliseconds. From this it follows that mockedObservable.getValue() and changedValue might not be equal
		// even though the binding is working.
		assertTrue(
			"Date: " + mockedObservable.getValue() + "\nDateWidget: " + dateWidget.getDay() + "-"
				+ dateWidget.getMonth() + "-" + dateWidget.getYear() + "\nTimeWidget: " + timeWidget.getHours() + ":"
				+ timeWidget.getMinutes() + ":" + timeWidget.getSeconds(),
			isDateEqualToDateTimes((Date) mockedObservable.getValue(), dateWidget, timeWidget));
	}

	/**
	 * Universal set up stuff for the data binding test cases.
	 *
	 * @param mockedObservable
	 * @return A DateTime array. DateTime[0] contains the date widget and DateTime[1] the time widget.
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	private DateTime[] setUpDatabindingTest(final WritableValue mockedObservable) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		Mockito.reset(databindingService);
		when(databindingService.getObservableValue(any(VDomainModelReference.class), any(EObject.class))).thenReturn(
			mockedObservable);

		final Control renderControl = renderControl(new SWTGridCell(0, 2, renderer));

		final Composite composite = (Composite) renderControl;
		final Composite stackComposite = (Composite) composite.getChildren()[0];
		final Composite dateTimeComposite = (Composite) stackComposite.getChildren()[0];
		final DateTime dateWidget = (DateTime) dateTimeComposite.getChildren()[0];
		final DateTime timeWidget = (DateTime) dateTimeComposite.getChildren()[1];
		final DateTime[] widgets = new DateTime[] { dateWidget, timeWidget };

		return widgets;
	}

	private boolean isDateEqualToDateTimes(Date date, DateTime dateWidget, DateTime timeWidget) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int dateYear = cal.get(Calendar.YEAR);
		final int dateMonth = cal.get(Calendar.MONTH);
		final int dateDay = cal.get(Calendar.DAY_OF_MONTH);
		final int dateHour = cal.get(Calendar.HOUR_OF_DAY);
		final int dateMinute = cal.get(Calendar.MINUTE);
		final int dateSeconds = cal.get(Calendar.SECOND);

		final int widgetYear = dateWidget.getYear();
		final int widgetMonth = dateWidget.getMonth();
		final int widgetDay = dateWidget.getDay();
		final int widgetHour = timeWidget.getHours();
		final int widgetMinute = timeWidget.getMinutes();
		final int widgetSeconds = timeWidget.getSeconds();

		if (dateYear == widgetYear && dateMonth == widgetMonth && dateDay == widgetDay
			&& dateHour == widgetHour && dateMinute == widgetMinute && dateSeconds == widgetSeconds) {
			return true;
		}
		return false;
	}
}
