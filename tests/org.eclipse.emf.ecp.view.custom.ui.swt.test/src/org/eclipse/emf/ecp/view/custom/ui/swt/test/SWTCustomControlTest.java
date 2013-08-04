/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.view.custom.ui.swt.test;

import static org.junit.Assert.assertSame;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.CustomFactory;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class SWTCustomControlTest {

	/**
	 * @author Jonas
	 * 
	 */
	public class TestHandel {

		private final View view;
		private final CustomControl customControl;

		/**
		 * @param view
		 * @param customControl
		 */
		public TestHandel(View view, CustomControl customControl) {
			this.view = view;
			this.customControl = customControl;
		}

	}

	private static final String Bundle_ID = "org.eclipse.emf.ecp.view.custom.ui.swt.test";

	/**
	 * 
	 */

	@Test
	public void testCustomControlinView() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final TestHandel testHandel = createCustomControlInView();
		final Shell shell = SWTViewTestHelper.createShell();
		final Composite composite = (Composite) SWTViewTestHelper.render(testHandel.view, shell);
		assertSame(getParentCompositeFromView(composite), CustomControlStub.parent);
	}

	/**
	 * @return
	 */
	private TestHandel createCustomControlInView() {
		final View view = ViewFactory.eINSTANCE.createView();
		final CustomControl customControl = createCustomControl();
		view.getChildren().add(customControl);
		customControl.setBundle(Bundle_ID);
		customControl.setClassName("org.eclipse.emf.ecp.view.custom.ui.swt.test.CustomControlStub");
		final TestHandel testHandel = new TestHandel(view, customControl);
		return testHandel;
	}

	/**
	 * @param composite
	 * @return the parent composite for the custom control
	 */
	private static Composite getParentCompositeFromView(Composite composite) {
		Composite child = (Composite) composite.getChildren()[0];
		child = getParentCompositeforInnerContentFromOuterComposite(child);
		return child;
	}

	/**
	 * @param child
	 * @return the inner composite from the outer one
	 */
	// TODO move to SWTTestHelper?
	public static Composite getParentCompositeforInnerContentFromOuterComposite(Composite child) {
		return (Composite) child.getChildren()[0];
	}

	@Test
	public void testCustomControlinViewWithoutClass() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final View view = ViewFactory.eINSTANCE.createView();
		final CustomControl customControl = createCustomControl();
		view.getChildren().add(customControl);
		customControl.setBundle(Bundle_ID);
		customControl.setClassName("org.eclipse.emf.ecp.view.customcomposite.ui.swt.test.NoExisting");
		// setup ui
		final Shell shell = SWTViewTestHelper.createShell();
		SWTViewTestHelper.render(view, shell);
		// TODO: What to expect
	}

	/**
	 * @return
	 */
	private CustomControl createCustomControl() {
		// TODO Auto-generated method stub
		return CustomFactory.eINSTANCE.createCustomControl();
	}
}
