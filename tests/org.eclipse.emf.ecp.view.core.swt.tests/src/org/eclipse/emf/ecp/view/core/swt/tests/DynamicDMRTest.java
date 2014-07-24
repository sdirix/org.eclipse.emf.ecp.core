/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.core.swt.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Fan;
import org.eclipse.emf.emfstore.bowling.Merchandise;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class DynamicDMRTest {

	private static final String EMPTY = "";
	private static final String NAME_INIT = "name";
	private static final String NAME_OTHER = "other";

	private Shell shell;
	private VView view;
	private Fan domain;
	private Control control;

	@Before
	public void before() {
		shell = SWTViewTestHelper.createShell();
		view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(BowlingPackage.eINSTANCE.getFan());
		domain = BowlingFactory.eINSTANCE.createFan();
	}

	@Ignore
	@Test
	public void testInitMissingContainmentElement() {
		// setup
		addFavMerchNameControl();
		// act
		render();
		// assert
		assertText(EMPTY, true);
	}

	@Ignore
	@Test
	public void testInitMissingReferencedElement() {
		// setup
		addFavPlayerNameControl();
		// act
		render();
		// assert
		assertText(EMPTY, true);
	}

	@Ignore
	@Test
	public void testRemoveContainmentElement() {
		// setup
		addFavMerchNameControl();
		changeDomain(merchandise(NAME_INIT), null);
		render();
		assertText(NAME_INIT, true);
		// act
		changeDomain(null, null);
		// assert
		assertText(EMPTY, false);
	}

	@Ignore
	@Test
	public void testRemoveReferencedElement() {
		// setup
		addFavPlayerNameControl();
		changeDomain(null, player(NAME_INIT));
		render();
		assertText(NAME_INIT, true);
		// act
		changeDomain(null, null);
		// assert
		assertText(EMPTY, false);
	}

	@Ignore
	@Test
	public void testAddMissingContainmentElement() {
		// setup
		addFavMerchNameControl();
		render();
		assertText(EMPTY, true);
		// act
		changeDomain(merchandise(NAME_INIT), null);
		// assert
		assertText(NAME_INIT, true);
	}

	@Ignore
	@Test
	public void testAddMissingContainmentElement2Times() {
		// setup
		addFavMerchNameControl();
		render();
		assertText(EMPTY, true);
		// act
		changeDomain(merchandise(NAME_INIT), null);
		// assert
		assertText(NAME_INIT, true);

		changeDomain(merchandise(NAME_INIT + "2"), null);
		// assert
		assertText(NAME_INIT + "2", true);
	}

	@Ignore
	@Test
	public void testAddRemovedContainmentElement() {
		// setup
		addFavMerchNameControl();
		changeDomain(merchandise(NAME_INIT), null);
		render();
		assertText(NAME_INIT, true);
		// act
		changeDomain(null, null);
		// assert
		assertText(EMPTY, false);

		// act
		changeDomain(merchandise(NAME_INIT), null);
		// assert
		assertText(NAME_INIT, true);
	}

	@Ignore
	@Test
	public void testAddMissingReferencedElement() {
		// setup
		addFavPlayerNameControl();
		render();
		assertText(EMPTY, true);
		// act
		changeDomain(null, player(NAME_INIT));
		// assert
		assertText(NAME_INIT, true);
	}

	@Ignore
	@Test
	public void testReplaceContainmentElement() {
		// setup
		addFavMerchNameControl();
		changeDomain(merchandise(NAME_INIT), null);
		render();
		assertText(NAME_INIT, true);
		// act
		changeDomain(merchandise(NAME_OTHER), null);
		// assert
		assertText(NAME_OTHER, true);
	}

	@Ignore
	@Test
	public void testReplaceReferencedElement() {
		// setup
		addFavPlayerNameControl();
		changeDomain(null, player(NAME_INIT));
		render();
		assertText(NAME_INIT, true);
		// act
		changeDomain(null, player(NAME_OTHER));
		// assert
		assertText(NAME_OTHER, true);
	}

	private void render() {
		try {
			control = SWTViewTestHelper.render(view, domain, shell);
		} catch (final NoRendererFoundException e) {
			fail("Could not render view: " + e.getMessage());
		} catch (final NoPropertyDescriptorFoundExeption e) {
			fail("Could not render view: " + e.getMessage());
		}
	}

	private void changeDomain(Merchandise favMerchandise, Player favPlayer,
		Merchandise... merchandise) {
		domain.setFavouriteMerchandise(favMerchandise);
		domain.setFavouritePlayer(favPlayer);
		domain.getFanMerchandise().addAll(Arrays.asList(merchandise));
	}

	private Merchandise merchandise(String name) {
		final Merchandise merchandise = BowlingFactory.eINSTANCE.createMerchandise();
		merchandise.setName(name);
		return merchandise;
	}

	private Player player(String name) {
		final Player player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName(name);
		return player;
	}

	private void addFavMerchNameControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		initControl(control, BowlingPackage.eINSTANCE.getMerchandise_Name(),
			BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		view.getChildren().add(control);
	}

	private void addFavPlayerNameControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		initControl(control, BowlingPackage.eINSTANCE.getPlayer_Name(),
			BowlingPackage.eINSTANCE.getFan_FavouritePlayer());
		view.getChildren().add(control);
	}

	private void initControl(VControl control, EStructuralFeature feature,
		EReference... references) {
		control.setDomainModelReference(feature, Arrays.asList(references));
	}

	private Text getText() {
		final Composite composite = (Composite) control;
		final Control text = composite.getChildren()[2];
		return (Text) text;
	}

	private void assertText(String message, boolean enabled) {
		final Text text = getText();
		assertEquals("Enablement of text control: ", enabled, text.isEnabled() && text.getEditable());
		assertEquals("Text of text control: ", message, text.getText());
	}

}
