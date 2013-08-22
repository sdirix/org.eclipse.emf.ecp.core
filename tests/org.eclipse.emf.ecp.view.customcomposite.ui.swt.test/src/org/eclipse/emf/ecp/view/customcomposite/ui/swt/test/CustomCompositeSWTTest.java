/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.customcomposite.ui.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eugen Neufeld
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class CustomCompositeSWTTest {
	private Shell shell;
	private EObject domainElement;

	@Before
	public void init() {
		shell = SWTViewTestHelper.createShell();

		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.getESuperTypes().add(EcorePackage.eINSTANCE.getEClass());
		domainElement = eClass;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCustomCompositeWithoutComposite() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final View view = ViewFactory.eINSTANCE.createView();

		final CustomComposite customComposite = ViewFactory.eINSTANCE.createCustomComposite();
		view.getChildren().add(customComposite);

		final Control render = SWTViewTestHelper.render(customComposite, domainElement, shell);
		assertNull(render);
	}

	@Test
	public void testCustomCompositeWithComposite() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final View view = ViewFactory.eINSTANCE.createView();

		final CustomComposite customComposite = ViewFactory.eINSTANCE.createCustomComposite();
		customComposite.setBundle("org.eclipse.emf.ecp.view.customcomposite.ui.swt.test");
		customComposite.setClassName("org.eclipse.emf.ecp.view.customcomposite.ui.swt.test.TestCustomComposite");
		view.getChildren().add(customComposite);

		final Control render = SWTViewTestHelper.render(customComposite, domainElement, shell);
		assertNotNull(render);
		assertTrue(Composite.class.isInstance(render));
		final Composite composite = (Composite) render;
		final Control control = composite.getChildren()[0];
		assertTrue(Label.class.isInstance(control));
		final Label label = (Label) control;
		assertEquals("Datum", label.getText());
	}

}
