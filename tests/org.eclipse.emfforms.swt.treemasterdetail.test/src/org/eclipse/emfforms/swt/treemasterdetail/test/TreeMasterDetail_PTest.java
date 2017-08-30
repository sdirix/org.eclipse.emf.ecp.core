/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.swt.treemasterdetail.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.emfstore.bowling.Area;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.League;
import org.eclipse.emf.emfstore.bowling.Matchup;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.emf.emfstore.bowling.Tournament;
import org.eclipse.emfforms.spi.swt.treemasterdetail.InitialSelectionProvider;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailSWTFactory;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TreeMasterDetail_PTest {

	private static final String ALICE = "Alice"; //$NON-NLS-1$
	private static final String BOB = "Bob"; //$NON-NLS-1$
	private static Shell shell;
	private Display display;
	private SWTBot bot;

	@Before
	public void setUp() {
		display = Display.getDefault();
		bot = new SWTBot();
		shell = UIThreadRunnable.syncExec(display, new Result<Shell>() {
			@Override
			public Shell run() {
				final Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				return shell;
			}
		});
	}

	private Resource createResource() {
		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extToFactoryMap = registry
			.getExtensionToFactoryMap();
		extToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
			new ResourceFactoryImpl());
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI,
			EcorePackage.eINSTANCE);

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(
			new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		final Resource resource = resourceSet
			.createResource(URI.createURI("VIRTUAL")); //$NON-NLS-1$
		return resource;

	}

	@After
	public void after() {
		// realm.dispose();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				shell.dispose();
			}
		});
	}

	@Test
	public void renderTree() {
		// arrange
		final League league = BowlingFactory.eINSTANCE.createLeague();
		final Player alice = BowlingFactory.eINSTANCE.createPlayer();
		alice.setName(ALICE);
		league.getPlayers().add(alice);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				TreeMasterDetailSWTFactory.createTreeMasterDetail(
					shell,
					SWT.NONE,
					league);
				// act
				shell.open();
			}
		});

		// assert
		assertEquals(1, bot.tree().getAllItems().length);
	}

	@Test
	public void updateDetailBySelectingAnotherPlayer() throws InterruptedException {

		// arrange
		final League league = BowlingFactory.eINSTANCE.createLeague();
		final Player alice = BowlingFactory.eINSTANCE.createPlayer();
		final Player bob = BowlingFactory.eINSTANCE.createPlayer();
		alice.setName(ALICE);
		bob.setName(BOB);
		league.getPlayers().add(alice);
		league.getPlayers().add(bob);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				TreeMasterDetailSWTFactory
					.createTreeMasterDetail(shell, SWT.NONE, league);
				shell.open();

				// act
				bot.tree().getTreeItem("Player Bob").doubleClick(); //$NON-NLS-1$
			}
		});

		// assert
		assertEquals(BOB, bot.text().getText());
	}

	@Test
	public void createElement() throws InterruptedException {

		// arrange
		// create an area as it already has child descriptors
		final Area area = BowlingFactory.eINSTANCE.createArea();
		final Tournament t1 = BowlingFactory.eINSTANCE.createTournament();
		final Tournament t2 = BowlingFactory.eINSTANCE.createTournament();

		area.getTournaments().add(t1);
		area.getTournaments().add(t2);

		// necessary in order to generate menu items of child descriptors
		final Resource resource = createResource();
		resource.getContents().add(area);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				TreeMasterDetailSWTFactory
					.fillDefaults(shell, SWT.NONE, area)
					.customizeCildCreation(new CreateElementCallback() {

						@Override
						public void initElement(EObject parent, EReference reference, EObject newObject) {
						}

						@Override
						public boolean beforeCreateElement(Object newElement) {
							return true;
						}

						@Override
						public void afterCreateElement(Object newElement) {
							Matchup.class.cast(newElement).setNrSpectators(BigInteger.TEN);
						}
					})
					.create();
				shell.open();

				// act
				bot.tree().select(0).contextMenu("Matchup").click(); //$NON-NLS-1$
				bot.text().setText(BigInteger.TEN.toString());
			}
		});

		// assert
		final EList<Matchup> matchups = area.getTournaments().get(0).getMatchups();
		assertEquals(1, matchups.size());
		assertEquals(BigInteger.TEN, matchups.get(0).getNrSpectators());
	}

	@Test
	public void initSelection() throws InterruptedException {

		// arrange
		final League league = BowlingFactory.eINSTANCE.createLeague();
		final Player alice = BowlingFactory.eINSTANCE.createPlayer();
		final Player bob = BowlingFactory.eINSTANCE.createPlayer();
		alice.setName(ALICE);
		bob.setName(BOB);
		league.getPlayers().add(alice);
		league.getPlayers().add(bob);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				TreeMasterDetailSWTFactory
					.fillDefaults(shell, SWT.NONE, league)
					.customizeInitialSelection(new InitialSelectionProvider() {
						@Override
						public EObject getInitialSelection(Object input) {
							return bob;
						}
					})
					.create();

				// act
				shell.open();
			}
		});

		// assert
		assertEquals("Player Bob", bot.tree().selection().get(0, 0)); //$NON-NLS-1$
	}

	@Test
	public void labelDecorator() throws InterruptedException {

		final League league = BowlingFactory.eINSTANCE.createLeague();
		final Player alice = BowlingFactory.eINSTANCE.createPlayer();
		final Player bob = BowlingFactory.eINSTANCE.createPlayer();
		alice.setName(ALICE);
		bob.setName(BOB);
		league.getPlayers().add(alice);
		league.getPlayers().add(bob);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				TreeMasterDetailSWTFactory
					.fillDefaults(shell, SWT.NONE, league)
					.customizeLabelDecorator(new MyLabelDecorator())
					.create();
				shell.open();
			}
		});

		// act
		bot.tree().getTreeItem(ALICE);
	}

	class MyLabelDecorator implements ILabelDecorator {

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public String decorateText(String text, Object element) {
			return text.replace("Player", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Image decorateImage(Image image, Object element) {
			return null;
		}
	}
}
