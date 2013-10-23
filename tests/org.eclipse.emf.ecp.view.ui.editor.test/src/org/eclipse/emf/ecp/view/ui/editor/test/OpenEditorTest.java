/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.ui.editor.test;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.internal.ECPSWTViewRendererImpl;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.VCategorization;
import org.eclipse.emf.ecp.view.model.VCategory;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.emf.ecp.view.test.common.GCCollectable;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class OpenEditorTest extends SWTBotTestCase {

	@SuppressWarnings("unused")
	private static EObject domainObject;
	private static Shell shell;
	private static VView view;
	private Display display;
	@SuppressWarnings("unused")
	private GCCollectable collectable;

	@Before
	public void init() {
		domainObject = createDomainObject();
		display = Display.getDefault();

		shell = UIThreadRunnable.syncExec(display, new Result<Shell>() {

			public Shell run() {
				final Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				return shell;
			}
		});

		view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(BowlingPackage.eINSTANCE.getPlayer());
	}

	private static Player createDomainObject() {
		final Player player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Test");
		return player;
	}

	@Test
	public void testViewWithControls() throws ECPRendererException,
		InterruptedException {

		Realm.runWithDefault(SWTObservables.getRealm(display), new TestRunnable());
	}

	private VControl createControl(EStructuralFeature feature) {
		final org.eclipse.emf.ecp.view.model.VControl control = VViewFactory.eINSTANCE
			.createControl();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(feature);
		control.setDomainModelReference(domainModelReference);
		return control;
	}

	private VCategorization createCategorizations(
		org.eclipse.emf.ecp.view.model.VContainedElement composite1,
		org.eclipse.emf.ecp.view.model.VContainedElement composite2,
		org.eclipse.emf.ecp.view.model.VContainedElement composite3,
		org.eclipse.emf.ecp.view.model.VContainedElement composite4) {

		final VCategorization parentCategorization = VViewFactory.eINSTANCE
			.createCategorization();
		final VCategorization fooCategorization = VViewFactory.eINSTANCE
			.createCategorization();
		final VCategorization barCategorization = VViewFactory.eINSTANCE
			.createCategorization();
		final VCategory category1 = VViewFactory.eINSTANCE.createCategory();
		final VCategory category2 = VViewFactory.eINSTANCE.createCategory();
		final VCategory category3 = VViewFactory.eINSTANCE.createCategory();
		final VCategory category4 = VViewFactory.eINSTANCE.createCategory();
		parentCategorization.setName("parent");
		fooCategorization.setName("foo");
		barCategorization.setName("bar");
		category1.setName("1");
		category2.setName("2");
		category3.setName("3");
		category4.setName("4");
		category1.setComposite(composite1);
		category2.setComposite(composite2);
		category3.setComposite(composite3);
		category4.setComposite(composite4);
		fooCategorization.getCategorizations().add(category1);
		fooCategorization.getCategorizations().add(category2);
		barCategorization.getCategorizations().add(category3);
		barCategorization.getCategorizations().add(category4);
		parentCategorization.getCategorizations().add(fooCategorization);
		parentCategorization.getCategorizations().add(barCategorization);
		return parentCategorization;
	}

	private class TestRunnable implements Runnable {

		public void run() {

			final VControl nameCtrl = createControl(BowlingPackage.eINSTANCE
				.getPlayer_Name());
			final VControl genderCtrl = createControl(BowlingPackage.eINSTANCE
				.getPlayer_Gender());
			final VControl heightCtrl = createControl(BowlingPackage.eINSTANCE
				.getPlayer_Height());
			final VControl victoriesCtrl = createControl(BowlingPackage.eINSTANCE
				.getPlayer_NumberOfVictories());

			view.getCategorizations().add(
				createCategorizations(nameCtrl, genderCtrl, heightCtrl,
					victoriesCtrl));

			display.syncExec(new Runnable() {

				public void run() {
					try {
						final ECPControlContext context = ViewTestHelper.createECPControlContext(
							createDomainObject(), shell, view);
						ECPSWTViewRendererImpl.render(shell, context, view);
					} catch (final NoRendererFoundException e) {
						fail(e.getMessage());
					} catch (final NoPropertyDescriptorFoundExeption e) {
						fail(e.getMessage());
					} catch (final ECPRendererException ex) {
						fail(ex.getMessage());
					}
					shell.open();
				}
			});

			final SWTBotTree tree = bot.tree();
			final SWTBotTreeItem select = tree.getTreeItem("parent").getNode("foo").getNode("2").select();

			display.syncExec(new Runnable() {

				public void run() {
					final TreeItem item = select.widget;
					final Node<?> node = (Node<?>) item.getData();
					collectable = new GCCollectable(node.getRenderable());
				}
			});
			tree.getTreeItem("parent").getNode("foo").getNode("1").select();

			assertTrue(tree.getTreeItem("parent").getNode("foo").getNode("1").isSelected());
		}
	}

}
