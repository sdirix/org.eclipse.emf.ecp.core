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
package org.eclipse.emf.ecp.view.ui.editor.test.controls;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.emf.ecp.view.test.common.GCCollectable;
import org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Game;
import org.eclipse.emf.emfstore.bowling.Gender;
import org.eclipse.emf.emfstore.bowling.Matchup;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.emf.emfstore.bowling.TournamentType;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotDateTime;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author jfaltermeier
 * 
 */
@RunWith(Parameterized.class)
public class ControlsSWTBotTest extends ECPCommonSWTBotTest {

	private static double memBefore;
	private static double memAfter;
	private static EObject domainObject;

	private final boolean isDomainCollectable;
	private final Boolean[] configuration;

	private GCCollectable viewCollectable;
	private GCCollectable contextCollectable;
	private GCCollectable domainCollectable;

	public ControlsSWTBotTest(boolean isDomainCollectable, Boolean[] b) {
		this.isDomainCollectable = isDomainCollectable;
		configuration = b;
	}

	@Parameters
	public static Collection<Object[]> data() {
		final List<Object[]> data = new ArrayList<Object[]>();

		data.addAll(createConfig(10, new Boolean[] { true, false, false, false, false, false, false, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, true, false, false, false, false, false, false, false,
			false, false }));
		data.addAll(createConfig(100, new Boolean[] { false, false, true, false, false, false, false, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, true, false, false, false, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, true, false, false, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, true, false, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, false, true, false, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, false, false, true, false,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, false, false, false, true,
			false, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, false, false, false, false,
			true, false }));
		data.addAll(createConfig(10, new Boolean[] { false, false, false, false, false, false, false, false, false,
			false, true }));
		data.addAll(createConfig(10, new Boolean[] { true, true, true, true, true, true, true, true, true, true, true
		}));

		return data;
	}

	@AfterClass
	public static void afterClass() {
		final double diff = Math.abs((memBefore - memAfter) / memBefore);
		assertTrue(diff < 0.01);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#createContext(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecp.view.model.VView)
	 */
	@Override
	public ECPControlContext createContext(EObject domainObject, VView view) {
		// new ViewModelContextImpl(view, domainObject);
		final ECPControlContext context = super.createContext(domainObject, view);
		contextCollectable = new GCCollectable(context);
		return context;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#logic()
	 */
	// BEGIN COMPLEX CODE
	@Override
	public void logic() {
		if (configuration[0]) {
			doActionStringControl(0);
		}
		if (configuration[1]) {
			doActionDateTimeControl(1);
		}
		if (configuration[2]) {
			doActionNumericalControlDouble(2);
		}
		if (configuration[3]) {
			doActionBooleanControl(3);
		}
		if (configuration[4]) {
			doActionAttributeMultiControl(4);
		}
		if (configuration[5]) {
			doActionNumericalControlInteger(5);
		}
		if (configuration[6]) {
			doActionMultiControlEEnum(6);
		}
		if (configuration[7]) {
			doActionNumericalControlBigDec(7);
		}
		if (configuration[8]) {
			doActionEEnumControl(8);
		}
		if (configuration[9]) {
			doActionReferenceMultiControl(9);
		}
		if (configuration[10]) {
			doActionLinkControl(10);
		}
	}

	// END COMPLEX CODE

	private void doActionStringControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotText text = bot.textWithLabel("Name");
				text.setFocus();
				text.setText("Maxl Morlock");
				bot.label("Name").setFocus();
			}
		});
	}

	private void doActionDateTimeControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotDateTime date = bot.dateTime(0);
				final SWTBotDateTime time = bot.dateTime(1);
				date.setFocus();
				date.setDate(new Date());
				time.setFocus();
				time.setDate(new Date());
				date.setFocus();
			}
		});
	}

	private void doActionNumericalControlDouble(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotText text = bot.textWithLabel("Height");
				text.setFocus();
				final Locale locale = Locale.getDefault(Category.FORMAT);
				final NumberFormat formatter = NumberFormat.getInstance(locale);
				text.setText(formatter.format(1.7d));
				bot.label("Height").setFocus();
			}
		});
	}

	private void doActionBooleanControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotCheckBox check = bot.checkBox();
				check.setFocus();
				check.click();
				bot.label("Is Professional").setFocus();
			}
		});
	}

	private void doActionAttributeMultiControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final int addEntryIndex = getFirstButtonIndex(myIndex);
				final SWTBotButton addEntry = bot.button(addEntryIndex);
				addEntry.click();
				final SWTBotButton delete = bot.button(addEntryIndex + 1);
				delete.click();
				bot.label("EMails*").setFocus();
			}
		});
	}

	private void doActionNumericalControlInteger(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotText text = bot.textWithLabel("Number Of Victories");
				text.setFocus();
				text.setText(String.valueOf(250));
				bot.label("Number Of Victories").setFocus();
			}
		});
	}

	private void doActionMultiControlEEnum(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final int addEntryIndex = getFirstButtonIndex(myIndex);
				final SWTBotButton addEntry = bot.button(addEntryIndex);
				addEntry.click();
				final SWTBotButton delete = bot.button(addEntryIndex + 1);
				delete.click();
				bot.label("Played Tournament Types").setFocus();
			}
		});
	}

	private void doActionNumericalControlBigDec(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotText text = bot.textWithLabel("Win Loss Ratio");
				text.setFocus();
				final Locale locale = Locale.getDefault(Category.FORMAT);
				final NumberFormat formatter = NumberFormat.getInstance(locale);
				text.setText(formatter.format(new BigDecimal(0.9)));
				bot.label("Win Loss Ratio").setFocus();
			}
		});
	}

	private void doActionEEnumControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotCombo combo = bot.comboBox("Male");
				combo.setSelection(0);
				combo.setSelection(1);
				bot.label("Gender").setFocus();
			}
		});
	}

	private void doActionReferenceMultiControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				// delete game2
				final SWTBotButton button = bot.button(getFirstButtonIndex(myIndex) + 5);
				button.click();
				bot.label("Games*").setFocus();
			}
		});
	}

	private void doActionLinkControl(final int myIndex) {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				final SWTBotButton button = bot.button(getFirstButtonIndex(myIndex));
				button.click();
				bot.label("Matchup*").setFocus();
			}
		});
	}

	private int getFirstButtonIndex(int myIndex) {
		final int[] possibleTextFields = new int[] { 4, 6, 9, 10 };
		int index = 0;
		for (final int i : possibleTextFields) {
			if (i == myIndex) {
				break;
			}
			if (configuration[i]) {
				switch (i) {
				case 4:
				case 6:
					index = index + 4;
					break;
				case 9:
					index = index + 5;
					break;
				default:
					break;
				}
			}
		}
		return index;
	}

	@Override
	public void assertions(double before, double after) {
		ControlsSWTBotTest.memBefore += before;
		ControlsSWTBotTest.memAfter += after;

		assertTrue("More than four adapter left on domain model element after dispose of ECPSWTView: "
			+ getViewNode().getControlContext().getModelElement().eAdapters().size()
			+ " adapters. Not all adapters can be removed, but it's maybe time to get suspicious.", getViewNode()
			.getControlContext().getModelElement().eAdapters().size() < 5);

		assertTrue(getSWTViewCollectable().isCollectable());
		setSWTViewCollectable(null);
		setViewNode(null);
		assertTrue(contextCollectable.isCollectable());
		assertTrue(viewCollectable.isCollectable());
		if (isDomainCollectable) {
			assertTrue(domainCollectable.isCollectable());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#createDomainObject()
	 */
	@Override
	public EObject createDomainObject() {
		Game game = (Game) domainObject;

		if (isDomainCollectable) {
			// remove reference to domain object, since gc will be tested
			domainObject = null;
		}

		if (game == null) {
			game = BowlingFactory.eINSTANCE.createGame();
			final Player player = createPlayer();
			final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
			game.setPlayer(player);
			matchup.getGames().add(game);
			final Game game2 = BowlingFactory.eINSTANCE.createGame();
			matchup.getGames().add(game2);
			final ResourceSet resourceSet = new ResourceSetImpl();
			final Resource resource = resourceSet.createResource(URI.createFileURI("foo.xmi"));
			resource.getContents().add(game);
			resource.getContents().add(game2);
			resource.getContents().add(player);
			resource.getContents().add(matchup);
			addEditingDomain(resourceSet);
			memBefore = 0d;
			memAfter = 0d;
		} else {
			game.getPlayer().setName("Max Morlock");
			game.getPlayer().setHeight(1.8);
			game.getPlayer().getEMails().clear();
			game.getPlayer().getEMails().add("maxl@foobar.com");
			game.getPlayer().setNumberOfVictories(249);
			game.getPlayer().getPlayedTournamentTypes().clear();
			game.getPlayer().getPlayedTournamentTypes().add(TournamentType.AMATEUR);
			game.getPlayer().setWinLossRatio(new BigDecimal(0.8));
			game.getPlayer().setGender(Gender.MALE);
			game.setMatchup((Matchup) game.eResource().getContents().get(3));
			game.getMatchup().getGames().add((Game) game.eResource().getContents().get(1));
		}

		if (!isDomainCollectable) {
			domainObject = game;
		}

		domainCollectable = new GCCollectable(game);
		return game;
	}

	private Player createPlayer() {
		final Player player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Max Morlock");
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(11, 5, 1925);
		player.setDateOfBirth(calendar.getTime());
		player.setHeight(1.80d);
		player.setIsProfessional(true);
		player.getEMails().add("maxl@foobar.com");
		player.setNumberOfVictories(249);
		player.getPlayedTournamentTypes().add(TournamentType.AMATEUR);
		player.setWinLossRatio(new BigDecimal(0.8));
		player.setGender(Gender.MALE);
		return player;
	}

	private void addEditingDomain(ResourceSet resourceSet) {
		AdapterFactory adapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] { adapterFactory,
			new ReflectiveItemProviderAdapterFactory() });
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory,
			new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#createView()
	 */
	@Override
	public VView createView() {
		final VView view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(BowlingPackage.eINSTANCE.getGame());
		createControls(view);
		viewCollectable = new GCCollectable(view);
		return view;
	}

	// BEGIN COMPLEX CODE
	private void createControls(VView view) {
		if (configuration[0]) {
			createStringControl(view);
		}
		if (configuration[1]) {
			createDateTimeControl(view);
		}
		if (configuration[2]) {
			createNumericalControlDouble(view);
		}
		if (configuration[3]) {
			createBooleanControl(view);
		}
		if (configuration[4]) {
			createAttributeMultiControl(view);
		}
		if (configuration[5]) {
			createNumericalControlInteger(view);
		}
		if (configuration[6]) {
			createMultiControlEEnum(view);
		}
		if (configuration[7]) {
			createNumericalControlBigDec(view);
		}
		if (configuration[8]) {
			createEEnumControl(view);
		}
		if (configuration[9]) {
			createReferenceMultiControl(view);
		}
		if (configuration[10]) {
			createLinkControl(view);
		}
	}

	// END COMPLEX CODE

	private void createStringControl(VView view) {
		final VControl stringControl = VViewFactory.eINSTANCE.createControl();
		stringControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_Name()));
		view.getChildren().add(stringControl);
	}

	private void createDateTimeControl(VView view) {
		final VControl dateTimeControl = VViewFactory.eINSTANCE.createControl();
		dateTimeControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_DateOfBirth()));
		view.getChildren().add(dateTimeControl);
	}

	private void createNumericalControlDouble(VView view) {
		final VControl doubleControl = VViewFactory.eINSTANCE.createControl();
		doubleControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_Height()));
		view.getChildren().add(doubleControl);
	}

	private void createBooleanControl(VView view) {
		final VControl booleanControl = VViewFactory.eINSTANCE.createControl();
		booleanControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_IsProfessional()));
		view.getChildren().add(booleanControl);
	}

	private void createAttributeMultiControl(VView view) {
		final VControl attributeMultiControl = VViewFactory.eINSTANCE.createControl();
		attributeMultiControl
			.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
				.getPlayer_EMails()));
		view.getChildren().add(attributeMultiControl);
	}

	private void createNumericalControlInteger(VView view) {
		final VControl integerControl = VViewFactory.eINSTANCE.createControl();
		integerControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_NumberOfVictories()));
		view.getChildren().add(integerControl);
	}

	private void createMultiControlEEnum(VView view) {
		final VControl eEnumMultiControl = VViewFactory.eINSTANCE.createControl();
		eEnumMultiControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_PlayedTournamentTypes()));
		view.getChildren().add(eEnumMultiControl);
	}

	private void createNumericalControlBigDec(VView view) {
		final VControl bigDecControl = VViewFactory.eINSTANCE.createControl();
		bigDecControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_WinLossRatio()));
		view.getChildren().add(bigDecControl);
	}

	private void createEEnumControl(VView view) {
		final VControl eEnumControl = VViewFactory.eINSTANCE.createControl();
		eEnumControl.setDomainModelReference(createPlayerVFeaturePathDomainModelReference(BowlingPackage.eINSTANCE
			.getPlayer_Gender()));
		view.getChildren().add(eEnumControl);
	}

	private void createReferenceMultiControl(VView view) {
		final VControl referenceMultiControl = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getMatchup_Games());
		domainModelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getGame_Matchup());
		referenceMultiControl.setDomainModelReference(domainModelReference);
		view.getChildren().add(referenceMultiControl);
	}

	private void createLinkControl(VView view) {
		final VControl linkControl = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getGame_Matchup());
		linkControl.setDomainModelReference(domainModelReference);
		view.getChildren().add(linkControl);
	}

	private VFeaturePathDomainModelReference createPlayerVFeaturePathDomainModelReference(EStructuralFeature feature) {
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(feature);
		domainModelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getGame_Player());
		return domainModelReference;
	}

	private static Collection<Object[]> createConfig(int loopsWithSameDomain, Boolean[] usedControls) {
		final Collection<Object[]> config = new ArrayList<Object[]>();
		for (int i = 0; i < loopsWithSameDomain - 1; i++) {
			config.add(new Object[] { false, usedControls });
		}
		config.add(new Object[] { true, usedControls });
		return config;
	}

}
