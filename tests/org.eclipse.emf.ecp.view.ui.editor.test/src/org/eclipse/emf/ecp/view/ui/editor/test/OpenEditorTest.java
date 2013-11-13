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

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.internal.DefaultControlContext;
import org.eclipse.emf.ecp.ui.view.swt.internal.ECPSWTViewRendererImpl;
import org.eclipse.emf.ecp.view.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationFactory;
import org.eclipse.emf.ecp.view.categorization.model.VCategory;
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
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test case that renders a view, disposes it and checks whether the view
 * is garbage collectable.
 * 
 * @author emueller
 * 
 */
@RunWith(Parameterized.class)
public class OpenEditorTest extends SWTBotTestCase {

	private static Shell shell;
	private static Display display;
	private GCCollectable collectable;
	private GCCollectable contextCollectable;
	private GCCollectable viewCollectable;
	private static GCCollectable domainCollectable;

	private static Wrapper<Player> domainObjectWrapper;

	private static double memBeforeFirstRender = -1;
	private static double memBeforeRender;
	private static double memAfterRender;

	public OpenEditorTest(Object o) {
	}

	@Parameters
	public static Collection<Object[]> data() {
		final Object[][] data = new Object[100][1];
		return Arrays.asList(data);
	}

	@BeforeClass
	public static void beforeClass() {
		display = Display.getDefault();
		domainObjectWrapper = new Wrapper<Player>(createDomainObject());
		domainCollectable = new GCCollectable(domainObjectWrapper.getObject());
	}

	@AfterClass
	public static void afterClass() {
		// check if domain object has no adapter left and remove strong coupling from thread
		assertEquals(0, domainObjectWrapper.removeObject().eAdapters().size());

		final double memoryGrowth = (memAfterRender - memBeforeFirstRender) / memBeforeFirstRender;
		assertTrue("Memory growth bigger than 15%: " + memoryGrowth, memoryGrowth < 0.15);

		assertTrue(domainCollectable.isCollectable());
	}

	@Before
	public void init() {
		// display = Display.getDefault();

		shell = UIThreadRunnable.syncExec(display, new Result<Shell>() {

			public Shell run() {
				final Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				return shell;
			}
		});

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

	private VCategorizationElement createCategorizations(
		org.eclipse.emf.ecp.view.model.VContainedElement composite1,
		org.eclipse.emf.ecp.view.model.VContainedElement composite2,
		org.eclipse.emf.ecp.view.model.VContainedElement composite3,
		org.eclipse.emf.ecp.view.model.VContainedElement composite4) {

		final VCategorizationElement categorizationElement = VCategorizationFactory.eINSTANCE
			.createCategorizationElement();

		final VCategorization parentCategorization = VCategorizationFactory.eINSTANCE
			.createCategorization();
		final VCategorization fooCategorization = VCategorizationFactory.eINSTANCE
			.createCategorization();
		final VCategorization barCategorization = VCategorizationFactory.eINSTANCE
			.createCategorization();
		final VCategory category1 = VCategorizationFactory.eINSTANCE.createCategory();
		final VCategory category2 = VCategorizationFactory.eINSTANCE.createCategory();
		final VCategory category3 = VCategorizationFactory.eINSTANCE.createCategory();
		final VCategory category4 = VCategorizationFactory.eINSTANCE.createCategory();
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
		categorizationElement.getCategorizations().add(parentCategorization);
		return categorizationElement;
	}

	private VView createView() {
		final VView view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(BowlingPackage.eINSTANCE.getPlayer());

		final VControl nameControl = createControl(BowlingPackage.eINSTANCE
			.getPlayer_Name());
		final VControl genderControl = createControl(BowlingPackage.eINSTANCE
			.getPlayer_Gender());
		final VControl heightControl = createControl(BowlingPackage.eINSTANCE
			.getPlayer_Height());
		final VControl victoriesControl = createControl(BowlingPackage.eINSTANCE
			.getPlayer_NumberOfVictories());

		view.getChildren().add(
			createCategorizations(nameControl, genderControl, heightControl,
				victoriesControl));

		return view;
	}

	private double usedMemory() {
		return 0d + Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	private class TestRunnable implements Runnable {

		public void run() {
			final Wrapper<ECPSWTView> viewWrapper = new Wrapper<ECPSWTView>(
				UIThreadRunnable.syncExec(new Result<ECPSWTView>() {
					public ECPSWTView run() {
						try {

							if (memBeforeFirstRender == -1) {
								memBeforeFirstRender = usedMemory();
							}
							memBeforeRender = usedMemory();

							final VView view = createView();
							collectable = new GCCollectable(view);

							final ECPControlContext context = new DefaultControlContext(domainObjectWrapper
								.getObject(), view);
							contextCollectable = new GCCollectable(context);
							final ECPSWTView renderedView = ECPSWTViewRendererImpl.render(shell, context, view);
							viewCollectable = new GCCollectable(renderedView);
							shell.open();
							return renderedView;
						} catch (final NoRendererFoundException e) {
							fail(e.getMessage());
						} catch (final NoPropertyDescriptorFoundExeption e) {
							fail(e.getMessage());
						} catch (final ECPRendererException ex) {
							fail(ex.getMessage());
						}
						return null;
					}
				}));

			final SWTBotTree tree = bot.tree();
			tree.getTreeItem("parent").getNode("foo").getNode("2").select();

			UIThreadRunnable.syncExec(new VoidResult() {
				public void run() {
					viewWrapper.removeObject().dispose();
					memAfterRender = usedMemory();
					shell.close();
					shell.dispose();
				}
			});

			assertTrue(viewCollectable.isCollectable());
			assertTrue(contextCollectable.isCollectable());
			assertTrue(collectable.isCollectable());
			final double memoryGrowth = (memAfterRender - memBeforeRender) / memBeforeRender;
			assertTrue("Memory growth bigger than 15%: " + memoryGrowth, memoryGrowth < 0.15);
		}
	}

	/**
	 * Wrapper class for avoiding strong references on objects from this thread which would prevent garbage collection.
	 * 
	 * @author jfaltermeier
	 * 
	 * @param <T> The type of the object to be wrapped.
	 */
	public static class Wrapper<T> {

		private T object;

		public Wrapper(T object) {
			this.object = object;
		}

		public T getObject() {
			return object;
		}

		public T removeObject() {
			final T result = object;
			object = null;
			return result;
		}
	}

}
