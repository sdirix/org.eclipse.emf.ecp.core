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
 * Edgar Mueller - additional test cases
 *******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.internal.rule.RuleService;
import org.eclipse.emf.ecp.view.internal.rule.RuleServiceHelper;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
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
import org.eclipse.emf.emfstore.bowling.Fan;
import org.eclipse.emf.emfstore.bowling.League;
import org.eclipse.emf.emfstore.bowling.Merchandise;
import org.eclipse.emf.emfstore.bowling.Player;
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

	/**
	 * @author Jonas
	 * 
	 */
	private class ViewModelContextStub implements ViewModelContext {

		private boolean hasRegisteredViewListener;
		private boolean hasRegisteredDomainListener;

		public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
			hasRegisteredViewListener = false;
		}

		public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
			hasRegisteredDomainListener = false;
		}

		public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
			hasRegisteredViewListener = true;
		}

		public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
			hasRegisteredDomainListener = true;
		}

		public View getViewModel() {
			return view;
		}

		public EObject getDomainModel() {
			return null;
		}

		public void dispose() {

		}

		public <T> boolean hasService(Class<T> serviceType) {
			// not used in tests
			return false;
		}

		public <T> T getService(Class<T> serviceType) {
			// not used in tests
			return null;
		}
	}

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

	private TestViewModelContext context;

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
		if (context != null) {
			context.dispose();
		}
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
		final RuleServiceHelper ruleServiceHelper = new RuleServiceHelper();
		context = new TestViewModelContext(view, domainModel);
		ruleService.instantiate(context);
		ruleServiceHelper.instantiate(context);
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
	 * @param showOnRightValue the visible on right value
	 */
	private void addLeagueShowRule(Renderable control, boolean showOnRightValue) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!showOnRightValue);
		rule.setCondition(createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"));
		control.getAttachments().add(rule);
	}

	private void addShowRule(Renderable control, boolean showOnRightValue, EAttribute attribute,
		Object expectedValue) {
		final ShowRule rule = RuleFactory.eINSTANCE.createShowRule();
		rule.setHide(!showOnRightValue);
		rule.setCondition(createLeafCondition(attribute, expectedValue));
		control.getAttachments().add(rule);
	}

	private void addEnableRule(Renderable control, boolean enableOnRightValue, EAttribute attribute,
		Object expectedValue) {
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setDisable(!enableOnRightValue);
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
	 * @param enableOnRightValue the enable on right value
	 */
	private void addLeagueEnableRule(Renderable control, boolean enableOnRightValue) {
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setDisable(!enableOnRightValue);
		final LeafCondition condition = RuleFactory.eINSTANCE.createLeafCondition();
		rule.setCondition(condition);
		condition.setAttribute(BowlingPackage.eINSTANCE.getLeague_Name());
		condition.setExpectedValue("League");
		control.getAttachments().add(rule);
	}

	@Test
	public void testInitialization() {
		final RuleService ruleService = new RuleService();
		final ViewModelContextStub contextStub = new ViewModelContextStub() {
			@Override
			public EObject getDomainModel() {
				return view;
			}
		};
		ruleService.instantiate(contextStub);
		assertTrue(contextStub.hasRegisteredViewListener);
		assertTrue(contextStub.hasRegisteredDomainListener);
	}

	@Test(expected = IllegalStateException.class)
	public void testInitializationWithNullDomainModel() {
		final RuleService ruleService = new RuleService();
		final ViewModelContextStub contextStub = new ViewModelContextStub();
		ruleService.instantiate(contextStub);
	}

	@Test(expected = IllegalStateException.class)
	public void testInitializationWithNullViewModel() {
		final RuleService ruleService = new RuleService();
		final ViewModelContextStub contextStub = new ViewModelContextStub();
		ruleService.instantiate(contextStub);
	}

	@Test
	public void testUnregisterOnViewModelContext() {
		final RuleService ruleService = new RuleService();
		final ViewModelContextStub contextStub = new ViewModelContextStub() {
			@Override
			public EObject getDomainModel() {
				return view;
			}
		};
		ruleService.instantiate(contextStub);
		assertTrue(contextStub.hasRegisteredViewListener);
		assertTrue(contextStub.hasRegisteredDomainListener);
		ruleService.dispose();
		assertFalse(contextStub.hasRegisteredViewListener);
		assertFalse(contextStub.hasRegisteredDomainListener);
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
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with first condition being true.
	 * Controls should be visible.
	 */
	@Test
	public void testShowRuleWithOrConditionFirstConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToRight();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with the second condition being true.
	 * Controls should be visible.
	 */
	@Test
	public void testShowRuleWithOrConditionSecondConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		league.setName("League2");
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with none of the conditions being true.
	 * Controls should not be visible.
	 */
	@Test
	public void testShowRuleWithOrConditionNoConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with first condition being true while initializing the rule service.
	 * Controls should be visible.
	 */
	@Test
	public void testInitShowRuleWithOrConditionFirstConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToRight();
		instantiateRuleService();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with second condition being true while initializing the rule service.
	 * Controls should be visible.
	 */
	@Test
	public void testInitShowRuleWithOrConditionSecondConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		league.setName("League2");
		instantiateRuleService();
		assertTrue(column.isVisible());
		assertTrue(controlPName.isVisible());
	}

	/**
	 * Test {@link OrCondition} with both conditions being true while initializing the rule service.
	 * Controls should be visible.
	 */
	@Test
	public void testInitShowRuleWithOrConditionBothConditionsApply() {
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

	/**
	 * Test {@link OrCondition} with both conditions being true.
	 * Controls should be visible.
	 */
	@Test
	public void testShowRuleWithOrConditionBothConditionsApply() {
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

	/**
	 * Test {@link OrCondition} with none of the conditions being true.
	 * Controls should not be visible.
	 */
	@Test
	public void testInitShowRuleWithOrConditionNoConditionApplies() {
		addLeagueShowRuleWithOrCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToWrong();
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with first condition being true.
	 * Controls should not be visible.
	 */
	@Test
	public void testShowRuleWithAndConditionFirstConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToRight();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with second condition being true.
	 * Controls should not be visible.
	 */
	@Test
	public void testShowRuleWithAndConditionSecondConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		league.setName("League2");
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with none of the conditions being true.
	 * Controls should not be visible.
	 */
	@Test
	public void testShowRuleWithAndConditionNoConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		instantiateRuleService();
		setLeagueToWrong();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with both conditions being true.
	 * Controls should be visible.
	 */
	@Test
	public void testShowRuleWithAndConditionBothConditionsApply() {
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

	/**
	 * Test {@link AndCondition} with first condition being true while initializing the rule service.
	 * Controls should not be visible.
	 */
	@Test
	public void testInitShowRuleInitWithAndConditionFirstConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		setLeagueToRight();
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with second condition being true while initializing the rule service.
	 * Controls should not be visible.
	 */
	@Test
	public void testInitShowRuleWithAndConditionSecondConditionApplies() {
		addLeagueShowRuleWithAndCondition(column, true,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));
		league.setName("League2");
		instantiateRuleService();
		assertFalse(column.isVisible());
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test {@link AndCondition} with none of the conditions being true while initializing the rule service.
	 * Controls should not be visible.
	 */
	@Test
	public void testInitShowRuleWithAndConditionNoConditionApplies() {
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
		final Fan fan = BowlingFactory.eINSTANCE.createFan();
		final Merchandise merchandise = BowlingFactory.eINSTANCE.createMerchandise();
		view.setRootEClass(fan.eClass());
		fan.setFavouriteMerchandise(merchandise);

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		control1.getPathToFeature().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		column.getComposites().add(control1);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getMerchandise_Name(), "foo");

		Rule rule = null;
		for (final Attachment attachment : control1.getAttachments()) {
			if (Rule.class.isInstance(attachment)) {
				rule = (Rule) attachment;
			}
		}

		((LeafCondition) rule.getCondition()).getPathToAttribute().add(
			BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());

		instantiateRuleService(fan);
		merchandise.setName("foo");
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
		assertFalse(controlPName.isVisible());
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
	 * 
	 * Control should not be visible since its parent will be hidden.
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isEnabled());
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
		assertFalse(controlPName.isEnabled());
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isEnabled());
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
		assertFalse(controlPName.isEnabled());
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
		assertFalse(controlPName.isEnabled());
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
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testParentShowVsChildShow() {
		addShowRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertTrue(parentColumn.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testParentShowVsChildHide() {
		addShowRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertTrue(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testParentHideVsChildHide() {
		addShowRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertFalse(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testParentHideVsChildShow() {
		addShowRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertFalse(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testParentEnableVsChildEnable() {
		addEnableRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertTrue(parentColumn.isEnabled());
		assertTrue(controlPName.isEnabled());
	}

	@Test
	public void testParentEnableVsChildDisable() {
		addEnableRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertTrue(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testParentDisableVsChildDisable() {
		addEnableRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertFalse(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testParentDisableVsChildEnable() {
		addEnableRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		instantiateRuleService();
		league.setName("foo");
		assertFalse(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testInitParentShowVsChildShow() {
		addShowRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertTrue(parentColumn.isVisible());
		assertTrue(controlPName.isVisible());
	}

	@Test
	public void testInitParentShowVsChildHide() {
		addShowRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertTrue(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitParentHideVsChildHide() {
		addShowRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertFalse(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitParentHideVsChildShow() {
		addShowRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addShowRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertFalse(parentColumn.isVisible());
		assertFalse(controlPName.isVisible());
	}

	@Test
	public void testInitParentEnableVsChildEnable() {
		addEnableRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertTrue(parentColumn.isEnabled());
		assertTrue(controlPName.isEnabled());
	}

	@Test
	public void testInitParentEnableVsChildDisable() {
		addEnableRule(parentColumn, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertTrue(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testInitParentDisableVsChildDisable() {
		addEnableRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertFalse(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	@Test
	public void testInitParentDisableVsChildEnable() {
		addEnableRule(parentColumn, false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		addEnableRule(controlPName, true, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		league.setName("foo");
		instantiateRuleService();
		assertFalse(parentColumn.isEnabled());
		assertFalse(controlPName.isEnabled());
	}

	/**
	 * Test dispose.
	 */
	@Test
	public void testDispose() {
		addLeagueEnableRule(controlPName, false);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();
		ruleService.dispose();
		setLeagueToWrong();
		assertTrue(controlPName.isEnabled());
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

		final Map<Renderable, Boolean> involvedEObjects = ruleService.getDisabledRenderables(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League");
		assertTrue(involvedEObjects.isEmpty());
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

		ruleService.getDisabledRenderables(((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League");
		assertTrue(controlPName.isEnabled());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectChange() {

		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();

		final Map<Renderable, Boolean> disabledRenderables = ruleService.getDisabledRenderables(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League_Wrong");
		final Map<Renderable, Boolean> hiddenRenderables = ruleService.getHiddenRenderables(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League_Wrong");

		assertEquals(3, hiddenRenderables.size());
		assertTrue(hiddenRenderables.containsKey(parentColumn));
		assertTrue(hiddenRenderables.containsKey(column));
		assertTrue(hiddenRenderables.containsKey(controlPName));

		assertEquals(1, disabledRenderables.size());
		assertTrue(disabledRenderables.containsKey(controlPName));
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectChangeWithoutChangedValue() {
		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		final RuleService ruleService = instantiateRuleService();

		final Map<Renderable, Boolean> disabledRenderables = ruleService.getDisabledRenderables(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League");
		final Map<Renderable, Boolean> hiddenRenderables = ruleService.getHiddenRenderables(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()), "League");

		assertEquals(0, disabledRenderables.size());
		assertEquals(0, hiddenRenderables.size());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectNoVanishingRenderables() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League", Control.class);
		assertEquals(0, involvedEControls.size());
	}

	/**
	 * Should return the control. Changing the league's name to
	 * 'League2' should not alter the disabled/show state of the
	 * control.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperBothRulesApply() {
		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Control.class);
		assertEquals(1, involvedEControls.size());
	}

	/**
	 * Should return the parent columns of the control.
	 * Changing the league's name to 'League2' should not alter
	 * the disabled/show state of the control.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperNoRuleAppliesFilterForComposite() {
		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Column> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Column.class);
		assertEquals(2, involvedEControls.size());
	}

	/**
	 * Should return nothing since the correct value is set.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperNoRuleApplies() {
		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League", Control.class);
		assertEquals(0, involvedEControls.size());
	}

	/**
	 * Should return the control since the disable rule applies because
	 * of the wrong value.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperEnableRuleAppliesWrongValue() {
		addLeagueEnableRule(controlPName, false);
		addLeagueShowRule(parentColumn, true);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Control.class);
		assertEquals(1, involvedEControls.size());
	}

	/**
	 * Should return the control because of the {@link EnableRule} on the control.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperEnableRuleAppliesCorrectValue() {
		addLeagueEnableRule(controlPName, false);
		addLeagueShowRule(parentColumn, true);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> hiddenOrDisabledControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League", Control.class);

		assertEquals(1, hiddenOrDisabledControls.size());
	}

	/**
	 * Should return nothing because the column should be visible, since
	 * 'League2' is the wrong value and the we have a disable rule.
	 */

	/**
	 * Should return the control because the column should be hidden, since
	 * 'League' is the right value and the we have a disable rule.
	 */
	@Test
	public void testGetInvolvedEObjectsHelperShowRuleAppliesCorrectValue() {
		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, false);
		setLeagueToWrong();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedEControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League", Control.class);
		assertEquals(1, involvedEControls.size());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectParentAffectedIfChildChanged() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Column> involvedColumns = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Column.class);
		assertEquals(1, involvedColumns.size());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectParentAffectedIfChildNotChanged() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Column> involvedColumns = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League", Column.class);
		assertEquals(0, involvedColumns.size());
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectWithVanishingRenderableShowRuleApplies() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Control.class);
		assertEquals(1, involvedControls.size());
		assertTrue(involvedControls.contains(controlPName));
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectWithVanishingRenderableEnableRuleApplies() {
		addLeagueEnableRule(controlPName, true);

		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Control.class);
		assertEquals(1, involvedControls.size());
		assertTrue(involvedControls.contains(controlPName));
	}

	/**
	 * Test get involved e object change.
	 */
	@Test
	public void testGetInvolvedEObjectWithVanishingRenderableTwoRulesApply() {
		// if the expected value equals the model value, then the control should be enabled

		addLeagueEnableRule(controlPName, true);
		addLeagueShowRule(parentColumn, true);
		setLeagueToRight();
		instantiateRuleService();
		final RuleServiceHelper helper = context.getService(RuleServiceHelper.class);

		final Set<Control> involvedControls = helper.getInvolvedEObjects(
			((LeagueImpl) league).eSetting(BowlingPackage.eINSTANCE.getLeague_Name()),
			"League2", Control.class);
		assertEquals(1, involvedControls.size());
		assertTrue(involvedControls.contains(controlPName));
	}

	@Test
	public void testUnset() {
		final Fan fan = BowlingFactory.eINSTANCE.createFan();
		final Merchandise merchandise = BowlingFactory.eINSTANCE.createMerchandise();
		view.setRootEClass(fan.eClass());
		fan.setFavouriteMerchandise(merchandise);

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		control1.getPathToFeature().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		column.getComposites().add(control1);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getFan_Name(), "foo");

		fan.setName("foo");
		merchandise.setName("bar");
		instantiateRuleService(fan);
		fan.setName("quux");
		assertFalse(control1.isVisible());
		assertEquals(BowlingPackage.eINSTANCE.getMerchandise_Name().getDefaultValue(), merchandise.getName());
	}

	@Test
	public void testMultiUnset() {
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(BowlingPackage.eINSTANCE.getLeague_Players());
		column.getComposites().add(control);

		addShowRule(control, true, BowlingPackage.eINSTANCE.getLeague_Name(), "League");
		setLeagueToRight();
		instantiateRuleService();
		assertEquals(1, league.getPlayers().size());
		setLeagueToWrong();
		assertEquals(0, league.getPlayers().size());
	}

	@Test
	public void testInitUnset() {
		final Fan fan = BowlingFactory.eINSTANCE.createFan();
		final Merchandise merchandise = BowlingFactory.eINSTANCE.createMerchandise();
		view.setRootEClass(fan.eClass());
		fan.setFavouriteMerchandise(merchandise);

		final Control control1 = ViewFactory.eINSTANCE.createControl();
		control1.setTargetFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		control1.getPathToFeature().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		column.getComposites().add(control1);

		addShowRule(control1, true, BowlingPackage.eINSTANCE.getFan_Name(), "foo");

		merchandise.setName("bar");
		fan.setName("quux");
		instantiateRuleService(fan);
		assertFalse(control1.isVisible());
		assertEquals(BowlingPackage.eINSTANCE.getMerchandise_Name().getDefaultValue(), merchandise.getName());
	}
}
