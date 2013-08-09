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
 *******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.rule.RuleService;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.RuleFactory;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.League;
import org.eclipse.emf.emfstore.bowling.Matchup;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.emf.emfstore.bowling.Tournament;
import org.eclipse.emf.emfstore.bowling.impl.LeagueImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * Things to be tested.
 * 
 * tests on control with rule:
 * init wrong value , right value
 * lifecycle wrong -> right; right -> wrong; wrong -> wrong; right -> right
 * types: show, enable, hide, disable
 * (2+4)*4
 * 
 * propagation tests:
 * - test propagation from parent to child (no rule) [combinations same as rule on control]
 * - test propagation from parent to child (rule != parent.rule) [combinations same as rule on control]
 * 
 * 
 * domain wrong
 * parent (s: disabled r:enable)
 * child1 (s:visible r:hidden) child2 (s:enabled r:disable)
 * 
 * init
 * parent (s: disabled r:enable)
 * child1 (s:visible r:hidden) child2 (s:disabled r:disable)
 * 
 * parent(s:enabled)
 * child (s:hidden) child2(s:disabled)
 * 
 * 
 * dispose
 * 
 * getInvolvedObjects
 * objects if no change -> none, empty list
 * objects if change -> only container of rule
 * no side effects
 */

public class RuleServiceTest {

	/** The player. */
	private Player player;

	/** The league. */
	private League league;

	/** The view. */
	private View view;

	/** The control p name. */
	private Control controlPName;

	/** The column. */
	private Column column;

	/** The parent column. */
	private Column parentColumn;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		player = BowlingFactory.eINSTANCE.createPlayer();
		league = BowlingFactory.eINSTANCE.createLeague();
		league.getPlayers().add(player);

		view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(league.eClass());

		parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		column = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(column);

		controlPName = ViewFactory.eINSTANCE.createControl();
		controlPName.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Name());
		controlPName.getPathToFeature().add(BowlingPackage.eINSTANCE.getLeague_Players());
		column.getComposites().add(controlPName);

	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private RuleService instantiateRuleService() {
		return instantiateRuleService(league);
	}

	/**
	 * Instantiate rule service.
	 * 
	 * @return the rule service
	 */
	private RuleService instantiateRuleService(final EObject domainModel) {
		final RuleService ruleService = new RuleService();
		ruleService.instantiate(new ViewModelContext() {

			private EContentAdapter viewContentAdapter;
			private EContentAdapter domainContentAdapter;
			private final List<ModelChangeListener> domainChangeListeners = new ArrayList<ViewModelContext.ModelChangeListener>();
			private final List<ModelChangeListener> viewChangeListeners = new ArrayList<ViewModelContext.ModelChangeListener>();

			public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
				viewChangeListeners.remove(modelChangeListener);
				view.eAdapters().remove(viewContentAdapter);
			}

			public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
				domainChangeListeners.remove(modelChangeListener);
				domainModel.eAdapters().remove(domainContentAdapter);
			}

			public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
				viewContentAdapter = new EContentAdapter() {
					@Override
					public void notifyChanged(Notification notification) {
						super.notifyChanged(notification);
						for (final ModelChangeListener listener : viewChangeListeners) {
							listener.notifyChange(new ModelChangeNotification(notification));
						}
					}
				};
				view.eAdapters().add(viewContentAdapter);
			}

			public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
				domainChangeListeners.add(modelChangeListener);
				domainContentAdapter = new EContentAdapter() {
					@Override
					public void notifyChanged(Notification notification) {
						super.notifyChanged(notification);
						for (final ModelChangeListener listener : domainChangeListeners) {
							listener.notifyChange(new ModelChangeNotification(notification));
						}
					}
				};
				domainModel.eAdapters().add(domainContentAdapter);
			}

			public View getViewModel() {
				return view;
			}

			public EObject getDomainModel() {
				return domainModel;
			}

			public void dispose() {
				domainModel.eAdapters().remove(domainContentAdapter);
				domainModel.eAdapters().remove(viewContentAdapter);
			}
		});
		return ruleService;
	}

	/**
	 * Sets the league to wrong.
	 */
	private void setLeagueToWrong() {
		league.setName("League_Wrong");
	}

	/**
	 * Sets the league to right.
	 */
	private void setLeagueToRight() {
		league.setName("League");
	}

	/**
	 * Adds the league show rule.
	 * 
	 * @param control the control
	 * @param hideOnRightValue the visible on right value
	 */
	private void addLeagueShowRule(Renderable control, boolean hideOnRightValue) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!hideOnRightValue);
		rule.setCondition(createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"));
		control.getAttachments().add(rule);
	}

	private void addShowRule(Renderable control, boolean hideOnRightValue, EAttribute attribute,
		Object expectedValue) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!hideOnRightValue);
		rule.setCondition(createLeafCondition(attribute, expectedValue));
		control.getAttachments().add(rule);
	}

	private LeafCondition createLeafCondition(EAttribute attribute, Object expectedValue) {
		final LeafCondition condition = RuleFactory.eINSTANCE.createLeafCondition();
		condition.setAttribute(attribute);
		condition.setExpectedValue(expectedValue);
		return condition;
	}

	private void addLeagueShowRuleWithOrCondition(Renderable control, boolean hideOnRightValue,
		Condition... childConditions) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!hideOnRightValue);
		final OrCondition condition = RuleFactory.eINSTANCE.createOrCondition();
		for (final Condition childCondition : childConditions) {
			condition.getConditions().add(childCondition);
		}
		rule.setCondition(condition);
		control.getAttachments().add(rule);
	}

	private void addLeagueShowRuleWithAndCondition(Renderable control, boolean hideOnRightValue,
		Condition... childConditions) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!hideOnRightValue);
		final AndCondition condition = RuleFactory.eINSTANCE.createAndCondition();
		for (final Condition childCondition : childConditions) {
			condition.getConditions().add(childCondition);
		}
		rule.setCondition(condition);
		control.getAttachments().add(rule);
	}

	/**
	 * Adds the league enable rule.
	 * 
	 * @param control the control
	 * @param disableOnRightValue the enable on right value
	 */
	private void addLeagueEnableRule(Renderable control, boolean disableOnRightValue) {
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setDisable(!disableOnRightValue);
		final LeafCondition condition = RuleFactory.eINSTANCE.createLeafCondition();
		rule.setCondition(condition);
		condition.setAttribute(BowlingPackage.eINSTANCE.getLeague_Name());
		condition.setExpectedValue("League");
		control.getAttachments().add(rule);
	}

	private boolean registeredViewListener = false;
	private boolean registeredDomainListener = false;

	@Test
	public void testInitialization() {
		final RuleService ruleService = new RuleService();

		ruleService.instantiate(new ViewModelContext() {

			public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
				registeredViewListener = true;
			}

			public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
				registeredDomainListener = true;
			}

			public View getViewModel() {
				return view;
			}

			public EObject getDomainModel() {
				return league;
			}

			public void dispose() {

			}
		});
		assertTrue(registeredDomainListener);
		assertTrue(registeredViewListener);
	}

	@Test(expected = IllegalStateException.class)
	public void testInitializationWithNullDomainModel() {
		final RuleService ruleService = new RuleService();

		ruleService.instantiate(new ViewModelContext() {

			public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
				registeredViewListener = true;
			}

			public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
				registeredDomainListener = true;
			}

			public View getViewModel() {
				return view;
			}

			public EObject getDomainModel() {
				return null;
			}

			public void dispose() {

			}
		});
	}

	@Test(expected = IllegalStateException.class)
	public void testInitializationWithNullViewModel() {
		final RuleService ruleService = new RuleService();

		ruleService.instantiate(new ViewModelContext() {

			public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
			}

			public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
				registeredViewListener = true;
			}

			public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
				registeredDomainListener = true;
			}

			public View getViewModel() {
				return null;
			}

			public EObject getDomainModel() {
				return league;
			}

			public void dispose() {

			}
		});
	}

	@Test
	public void testUnregisterOnViewModelContext() {
		final RuleService ruleService = new RuleService();

		ruleService.instantiate(new ViewModelContext() {

			public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
				registeredViewListener = false;
			}

			public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
				registeredDomainListener = false;
			}

			public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
				registeredViewListener = true;
			}

			public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
				registeredDomainListener = true;
			}

			public View getViewModel() {
				return view;
			}

			public EObject getDomainModel() {
				return league;
			}

			public void dispose() {

			}
		});
		ruleService.dispose();
		assertFalse(registeredDomainListener);
		assertFalse(registeredViewListener);
	}

	/**
	 * Test init right show rule.
	 */
	@Test
	public void testInitRightShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test init wrong show rule.
	 */
	@Test
	public void testInitWrongShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule wrong init.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleWrongInit() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule right init.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleRightInit() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule wrong init.
	 */
	@Test
	public void testPropagationShowRuleChildRuleWrongInit() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule right init.
	 */
	@Test
	public void testPropagationShowRuleChildRuleRightInit() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithOrCondition_FirstConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithOrCondition_SecondConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		league.setName("League2");
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithOrCondition_NoConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitShowRuleWithOrCondition_FirstConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testInitShowRuleWithOrCondition_SecondConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		league.setName("League2");
		instantiateRuleService();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testInitShowRuleWithOrCondition_BothConditionsApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		addLeagueShowRuleWithOrCondition(control1, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_Name(), "foo"),
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3));
		player.setName("foo");
		player.setNumberOfVictories(3);
		instantiateRuleService(player);
		assertTrue(column.isVisible());
		assertTrue(control1.isVisible());
	}

	@Test
	public void testShowRuleWithOrCondition_BothConditionsApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		addLeagueShowRuleWithOrCondition(control1, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_Name(), "foo"),
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3));
		instantiateRuleService(player);
		player.setName("foo");
		player.setNumberOfVictories(3);
		assertTrue(column.isVisible());
		assertTrue(control1.isVisible());
	}

	@Test
	public void testInitShowRuleWithOrCondition_NoConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithAndCondition_FirstConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithAndCondition_SecondConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		league.setName("League2");
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithAndCondition_NoConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithAndCondition_BothConditionsApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_Name(), "foo"),
			createLeafCondition(BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3));
		instantiateRuleService(player);
		player.setName("foo");
		player.setNumberOfVictories(3);
		assertTrue(column.isVisible());
		assertTrue(control1.isVisible());
	}

	@Test
	public void testInitShowRuleInitWithAndCondition_FirstConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitShowRuleWithAndCondition_SecondConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		league.setName("League2");
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitShowRuleWithAndCondition_NoConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testShowRuleWithTwoPossibleTargetsWhereOnlyOneSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		instantiateRuleService(player);
		player.setName("foo");
		assertTrue(control1.isVisible());
		assertFalse(control2.isVisible());
	}

	@Test
	public void testShowRuleWithTwoPossibleTargetsWhereTheOtherSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		instantiateRuleService(player);
		player.setNumberOfVictories(3);
		assertFalse(control1.isVisible());
		assertTrue(control2.isVisible());
	}

	@Test
	public void testShowRuleWhereConditionReferencesAnotherTarget() {
		final Tournament tournament = BowlingFactory.eINSTANCE.createTournament();
		view.setRootEClass(tournament.eClass());
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		tournament.getMatchups().add(matchup);

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getMatchup_NrSpectators());
		control1.getPathToFeature().add(BowlingPackage.eINSTANCE.getTournament_Matchups());
		column.getComposites().add(control1);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getMatchup_NrSpectators(), new BigInteger("3"));

		Rule rule = null;
		for (final Attachment attachment : control1.getAttachments()) {
			if (Rule.class.isInstance(attachment)) {
				rule = (Rule) attachment;
			}
		}

		((LeafCondition) rule.getCondition()).getPathToAttribute().add(
			BowlingPackage.eINSTANCE.getTournament_Matchups());

		instantiateRuleService(tournament);
		matchup.setNrSpectators(new BigInteger("3"));
		assertTrue(control1.isVisible());
	}

	@Test
	public void testShowRuleWithTwoPossibleTargetsWhereBothSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		instantiateRuleService(player);
		player.setName("foo");
		player.setNumberOfVictories(3);
		assertTrue(control1.isVisible());
		assertTrue(control2.isVisible());
	}

	@Test
	public void testInitShowRuleWithTwoPossibleTargetsWhereOnlyOneSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		player.setName("foo");
		instantiateRuleService(player);
		assertTrue(control1.isVisible());
		assertFalse(control2.isVisible());
	}

	@Test
	public void testInitShowRuleWithTwoPossibleTargetsWhereTheOtherSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		player.setNumberOfVictories(3);
		instantiateRuleService(player);
		assertFalse(control1.isVisible());
		assertTrue(control2.isVisible());
	}

	@Test
	public void testInitShowRuleWithTwoPossibleTargetsWhereBothSettingShouldApply() {
		view.setRootEClass(player.eClass());

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control1);

		final Control control2 = ViewFactory.eINSTANCE.createControl();
		control2.setTargetFeature(BowlingPackage.eINSTANCE.getPlayer_Height());
		column.getComposites().add(control2);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getPlayer_Name(), "foo");
		addShowRule(control2, true, BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories(), 3);

		player.setName("foo");
		player.setNumberOfVictories(3);
		instantiateRuleService(player);
		assertTrue(control1.isVisible());
		assertTrue(control2.isVisible());
	}

	/**
	 * Test default wrong to right show rule.
	 */
	@Test
	public void testDefaultWrongToRightShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test default wrong to wrong show rule.
	 */
	@Test
	public void testDefaultWrongToWrongShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test default right to wrong show rule.
	 */
	@Test
	public void testDefaultRightToWrongShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test default right to right show rule.
	 */
	@Test
	public void testDefaultRightToRightShowRule() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule wrong to right.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule wrong to wrong.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule right to wrong.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule no child rule right to right.
	 */
	@Test
	public void testPropagationShowRuleNoChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule wrong to right.
	 */
	@Test
	public void testPropagationShowRuleChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule wrong to wrong.
	 */
	@Test
	public void testPropagationShowRuleChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule right to wrong.
	 */
	@Test
	public void testPropagationShowRuleChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation show rule child rule right to right.
	 */
	@Test
	public void testPropagationShowRuleChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be visible

		addLeagueShowRule(column, true);
		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test init right enable rule.
	 */
	@Test
	public void testInitRightEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test init wrong enable rule.
	 */
	@Test
	public void testInitWrongEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule wrong init.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleWrongInit() {
		addLeagueEnableRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule right init.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleRightInit() {
		addLeagueEnableRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation enable rule child rule wrong init.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleWrongInit() {
		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule child rule right init.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleRightInit() {
		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test default wrong to right enable rule.
	 */
	@Test
	public void testDefaultWrongToRightEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test default wrong to wrong enable rule.
	 */
	@Test
	public void testDefaultWrongToWrongEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test default right to wrong enable rule.
	 */
	@Test
	public void testDefaultRightToWrongEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test default right to right enable rule.
	 */
	@Test
	public void testDefaultRightToRightEnableRule() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule wrong to right.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule wrong to wrong.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule right to wrong.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule no child rule right to right.
	 */
	@Test
	public void testPropagationEnableRuleNoChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule child rule wrong to right.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule child rule wrong to wrong.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule child rule right to wrong.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation enable rule child rule right to right.
	 */
	@Test
	public void testPropagationEnableRuleChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(column, true);
		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test init right hide rule.
	 */
	@Test
	public void testInitRightHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test init wrong hide rule.
	 */
	@Test
	public void testInitWrongHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule right init.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleRightInit() {
		addLeagueShowRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule wrong init.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleWrongInit() {
		addLeagueShowRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule right init.
	 */
	@Test
	public void testPropagationHideRuleChildRuleRightInit() {
		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule wrong init.
	 */
	@Test
	public void testPropagationHideRuleChildRuleWrongInit() {
		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test default wrong to right hide rule.
	 */
	@Test
	public void testDefaultWrongToRightHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test default wrong to wrong hide rule.
	 */
	@Test
	public void testDefaultWrongToWrongHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test default right to wrong hide rule.
	 */
	@Test
	public void testDefaultRightToWrongHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test default right to right hide rule.
	 */
	@Test
	public void testDefaultRightToRightHideRule() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule wrong to right.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule wrong to wrong.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule right to wrong.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule no child rule right to right.
	 */
	@Test
	public void testPropagationHideRuleNoChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule wrong to right.
	 */
	@Test
	public void testPropagationHideRuleChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule wrong to wrong.
	 */
	@Test
	public void testPropagationHideRuleChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule right to wrong.
	 */
	@Test
	public void testPropagationHideRuleChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test propagation hide rule child rule right to right.
	 */
	@Test
	public void testPropagationHideRuleChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be invisible

		addLeagueShowRule(column, false);
		addLeagueShowRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test init right disable rule.
	 */
	@Test
	public void testInitRightDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test init wrong disable rule.
	 */
	@Test
	public void testInitWrongDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule right init.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleRightInit() {
		addLeagueEnableRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule wrong init.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleWrongInit() {
		addLeagueEnableRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule right init.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleRightInit() {
		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule wrong init.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleWrongInit() {
		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test default right to wrong disable rule.
	 */
	@Test
	public void testDefaultRightToWrongDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test default right to right disable rule.
	 */
	@Test
	public void testDefaultRightToRightDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test default wrong to right disable rule.
	 */
	@Test
	public void testDefaultWrongToRightDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test default wrong to wrong disable rule.
	 */
	@Test
	public void testDefaultWrongToWrongDisableRule() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule right to wrong.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule right to right.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule wrong to right.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule no child rule wrong to wrong.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule right to wrong.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleRightToWrong() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule right to right.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleRightToRight() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule wrong to right.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleWrongToRight() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test propagation disable rule child rule wrong to wrong.
	 */
	@Test
	public void testPropagationDisableRuleChildRuleWrongToWrong() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(column, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToWrong();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test multi level propagation on init.
	 */
	@Test
	public void testMultiLevelPropagationOnInit() {
		addLeagueEnableRule(parentColumn, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test multi level propagation on lifecycle.
	 */
	@Test
	public void testMultiLevelPropagationOnLifecycle() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(parentColumn, false);
		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test dispose.
	 */
	@Test
	public void testDispose() {
		// if the expected value equals the model value, then the control should be disabled

		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();
		ruleService.dispose();
		setLeagueToWrong();
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test get involved e object no change.
	 */
	@Test
	public void testGetInvolvedEObjectNoChange() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();

		final Set<Renderable> involvedEObject = ruleService.getInvolvedEObject(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League");
		assertTrue(involvedEObject.isEmpty());
	}

	/**
	 * Test get involved e object no side effects.
	 */
	@Test
	public void testGetInvolvedEObjectNoSideEffects() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();

		ruleService.getInvolvedEObject(((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League");
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectChange() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();

		final Set<Renderable> involvedEObject = ruleService.getInvolvedEObject(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League_Wrong");
		assertEquals(2, involvedEObject.size());
		assertTrue(involvedEObject.contains(parentColumn));
		assertTrue(involvedEObject.contains(controlPName));
	}
}
