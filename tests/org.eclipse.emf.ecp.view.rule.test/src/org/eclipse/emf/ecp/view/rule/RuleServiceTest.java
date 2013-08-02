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
package org.eclipse.emf.ecp.view.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.League;
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

	/**
	 * Instantiate rule service.
	 * 
	 * @return the rule service
	 */
	private RuleService instantiateRuleService() {
		return new RuleService(view, league);
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
	 * @param visibleOnRightValue the visible on right value
	 */
	private void addLeagueShowRule(Renderable control, boolean visibleOnRightValue) {
		final ShowRule rule = ViewFactory.eINSTANCE.createShowRule();
		rule.setHide(visibleOnRightValue);
		final LeafCondition condition = ViewFactory.eINSTANCE.createLeafCondition();
		rule.setCondition(condition);
		condition.setAttribute(BowlingPackage.eINSTANCE.getLeague_Name());
		condition.setExpectedValue("League");
		control.setRule(rule);
	}

	/**
	 * Adds the league enable rule.
	 * 
	 * @param control the control
	 * @param enableOnRightValue the enable on right value
	 */
	private void addLeagueEnableRule(Renderable control, boolean enableOnRightValue) {
		final EnableRule rule = ViewFactory.eINSTANCE.createEnableRule();
		rule.setDisable(enableOnRightValue);
		final LeafCondition condition = ViewFactory.eINSTANCE.createLeafCondition();
		rule.setCondition(condition);
		condition.setAttribute(BowlingPackage.eINSTANCE.getLeague_Name());
		condition.setExpectedValue("League");
		control.setRule(rule);
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
	}

	/**
	 * Test propagation disable rule no child rule wrong init.
	 */
	@Test
	public void testPropagationDisableRuleNoChildRuleWrongInit() {
		addLeagueEnableRule(column, false);
		setLeagueToWrong();
		instantiateRuleService();
		assertTrue(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
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
		assertTrue(controlPName.isVisible());
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
		assertFalse(controlPName.isVisible());
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
