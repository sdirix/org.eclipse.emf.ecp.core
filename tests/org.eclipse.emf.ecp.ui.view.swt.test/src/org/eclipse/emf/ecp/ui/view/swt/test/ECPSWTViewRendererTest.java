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
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class ECPSWTViewRendererTest {

	private static EObject domainObject;
	private static Shell shell;
	private static View view;

	@Before
	public void init() {
		// setup model
		domainObject = createDomainObject();
		shell = SWTViewTestHelper.createShell();
		view = ViewFactory.eINSTANCE.createView();
	}

	@Test
	public void testEmptyView() throws ECPRendererException {

		final ECPSWTView swtView = ECPSWTViewRenderer.INSTANCE.render(shell, domainObject, view);

		final Control control = swtView.getSWTControl();
		checkFirstLevel(control);
	}

	private void checkFirstLevel(final Control control) {
		assertEquals("The Composite for the View has not been rendered", 1,
			shell.getChildren().length);
		assertTrue("View was not rendered as Composite",
			shell.getChildren()[0] instanceof Composite);
		assertEquals("Returned control and rendered control are not the same",
			control, shell.getChildren()[0]);
		assertTrue(control instanceof Composite);
	}

	private static Player createDomainObject() {
		final Player player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Test");
		return player;
	}

	@Test
	public void testViewWithControls() throws ECPRendererException {
		final org.eclipse.emf.ecp.view.model.Control control = ViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getPlayer_Name());
		control.getDomainModelReferences().add(domainModelReference);
		view.getChildren().add(control);

		final ECPSWTView swtView = ECPSWTViewRenderer.INSTANCE.render(shell, domainObject, view);

		final Control swtControl = swtView.getSWTControl();
		checkFirstLevel(swtControl);
		final Composite composite = (Composite) swtControl;
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControlWithLabel(composite));
	}
}
