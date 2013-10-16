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
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.GCCollectable;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
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

// import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

@RunWith(SWTBotJunit4ClassRunner.class)
// @RunWith(SWTBotDatabindingClassRunner.class)
// @RunWith(DatabindingClassRunner.class)
public class OpenEditorTest extends SWTBotTestCase {

	// private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@SuppressWarnings("unused")
	private static EObject domainObject;
	private static Shell shell;
	private static View view;
	private Display display;
	@SuppressWarnings("unused")
	private GCCollectable collectable;

	@Before
	public void init() {
		// setup model
		domainObject = createDomainObject();
		//
		display = Display.getDefault();// /new Display();

		shell = UIThreadRunnable.syncExec(display, new Result<Shell>() {

			public Shell run() {
				final Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				return shell;
			}
		});

		view = ViewFactory.eINSTANCE.createView();
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

		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {

			public void run() {

				final Control nameCtrl = createControl(BowlingPackage.eINSTANCE
					.getPlayer_Name());
				final Control genderCtrl = createControl(BowlingPackage.eINSTANCE
					.getPlayer_Gender());
				final Control heightCtrl = createControl(BowlingPackage.eINSTANCE
					.getPlayer_Height());
				final Control victoriesCtrl = createControl(BowlingPackage.eINSTANCE
					.getPlayer_NumberOfVictories());

				view.getCategorizations().add(
					createCategorizations(nameCtrl, genderCtrl, heightCtrl,
						victoriesCtrl));

				display.syncExec(new Runnable() {

					public void run() {
						try {
							SWTViewTestHelper.render(view, createDomainObject(), shell);
						} catch (final NoRendererFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (final NoPropertyDescriptorFoundExeption e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// TODO Auto-generated method stub
						shell.open();
					}
				});

				final SWTBotTree tree = bot.tree();
				final SWTBotTreeItem select = tree.getTreeItem("parent").getNode("foo").getNode("2").select();

				// display.syncExec(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// while (!shell.isDisposed()) {
				// if (!Display.getCurrent().readAndDispatch()) {
				// Display.getCurrent().sleep();
				// }
				// }
				// }
				// });
				//

				display.syncExec(new Runnable() {

					public void run() {
						final TreeItem item = select.widget;
						final Node<?> node = (Node<?>) item.getData();
						collectable = new GCCollectable(node.getRenderable());
					}
				});
				tree.getTreeItem("parent").getNode("foo").getNode("1").select();

				// SWTViewTestHelper.dispose();
				assertTrue(tree.getTreeItem("parent").getNode("foo").getNode("1").isSelected());
				// assertTrue(collectable.isCollectable());
				// Thread.sleep(1000);
			}
		});
	}

	private Control createControl(EStructuralFeature feature) {
		final org.eclipse.emf.ecp.view.model.Control control = ViewFactory.eINSTANCE
			.createControl();
		final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(feature);
		control.setDomainModelReference(domainModelReference);
		return control;
	}

	private Categorization createCategorizations(
		org.eclipse.emf.ecp.view.model.Composite composite1,
		org.eclipse.emf.ecp.view.model.Composite composite2,
		org.eclipse.emf.ecp.view.model.Composite composite3,
		org.eclipse.emf.ecp.view.model.Composite composite4) {

		final Categorization parentCategorization = ViewFactory.eINSTANCE
			.createCategorization();
		final Categorization fooCategorization = ViewFactory.eINSTANCE
			.createCategorization();
		final Categorization barCategorization = ViewFactory.eINSTANCE
			.createCategorization();
		final Category category1 = ViewFactory.eINSTANCE.createCategory();
		final Category category2 = ViewFactory.eINSTANCE.createCategory();
		final Category category3 = ViewFactory.eINSTANCE.createCategory();
		final Category category4 = ViewFactory.eINSTANCE.createCategory();
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

}
