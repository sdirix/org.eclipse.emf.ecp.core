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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.Fan;
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
		private final Control customControl;

		/**
		 * @param view
		 * @param customControl
		 */
		public TestHandel(View view, Control customControl) {
			this.view = view;
			this.customControl = customControl;
		}

		/**
		 * @return the view
		 */
		public View getView() {
			return view;
		}

		/**
		 * @return the customControl
		 */
		public Control getCustomControl() {
			return customControl;
		}

	}

	private static final String BUNDLE_ID = "org.eclipse.emf.ecp.view.custom.ui.swt.test";

	/**
	 * 
	 */

	@Test
	public void testCustomControlinView() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Renderable controlInView = createCustomControlInView();
		final Shell shell = SWTViewTestHelper.createShell();
		final Composite composite = (Composite) SWTViewTestHelper.render(controlInView, shell);
		assertSame(composite, CustomControlStub.getParent());
	}

	/**
	 * @return
	 */
	private Renderable createCustomControlInView() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Control customControl = createCustomControl();
		view.getChildren().add(customControl);
		// customControl.setBundle(BUNDLE_ID);
		// TODO check id
		customControl.setControlId("org.eclipse.emf.ecp.view.custom.ui.swt.test.CustomControlStub");
		return view;
	}

	@Test
	public void testCustomControlinViewWithoutClass() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final View view = ViewFactory.eINSTANCE.createView();
		final Control customControl = createCustomControl();
		view.getChildren().add(customControl);
		// customControl.setBundle(BUNDLE_ID);
		// TODO check id
		customControl.setControlId("org.eclipse.emf.ecp.view.customcomposite.ui.swt.test.NoExisting");
		// setup ui
		final Shell shell = SWTViewTestHelper.createShell();
		SWTViewTestHelper.render(view, shell);
		// TODO: What to expect
	}

	@Test
	public void testCustomControlInit() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Control customControl = createCustomControl();
		view.getChildren().add(customControl);
		// customControl.setBundle(BUNDLE_ID);
		// TODO check id
		customControl.setControlId("org.eclipse.emf.ecp.view.custom.ui.swt.test.CustomControlStub2");

		final Fan domainModel = BowlingFactory.eINSTANCE.createFan();
		new ViewModelContextImpl(view, domainModel);

		assertNotNull(domainModel.getFavouriteMerchandise());
	}

	/**
	 * @return
	 */
	private Control createCustomControl() {
		return ViewFactory.eINSTANCE.createControl();
	}
}
