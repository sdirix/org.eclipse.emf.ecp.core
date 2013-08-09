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
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.RuleFactory;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class RuleTest {

	@Test
	public void testEnableRuleNodeRendererWithDisableRule() {
		// setup model
		final RuleHandel ruleHandel = createDisabledEnableRule();
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnableRule() {
		// setup model
		final RuleHandel ruleHandel = createEnabledEnableRule();
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isEnabled());
	}

	@Test
	public void testShowRuleNodeRendererWithVisibleRule() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	@Test
	public void testShowRuleNodeRendererWithInvisibleRule() {
		// setup model
		final RuleHandel ruleHandel = createInvisibleShowRule();
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testEnableRuleNodeRendererWithDisableRuleAndTrueLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createDisabledEnableRule();
		addTrueLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithDisableRuleAndFalseLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createDisabledEnableRule();
		addFalseLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnabledRuleAndTrueLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createEnabledEnableRule();
		addTrueLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnabledRuleAndFalseLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createEnabledEnableRule();
		addFalseLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isEnabled());
	}

	@Test
	public void testVisibleRuleNodeRendererWithInvisibleRuleAndTrueLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createInvisibleShowRule();
		addTrueLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithInvisibleRuleAndFalseLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createInvisibleShowRule();
		addFalseLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addTrueLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseLeafCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addFalseLeafCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueAndCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addTrueAndCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseAndCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addFalseAndCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueOrCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addTrueOrCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseOrCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addFalseOrCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueComplexOrCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addComplexTrueOrCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueComplexAndCondition() {
		// setup model
		final RuleHandel ruleHandel = createVisibleShowRule();
		addComplexTrueAndCondition(ruleHandel.rule);
		// Test NodeBuidlers
		final Node<Renderable> node = ViewTestHelper.build(ruleHandel.parent, ruleHandel.domainObject);
		checkNodes(node, ruleHandel);
		assertFalse(node.isVisible());
	}

	/**
	 * @param rule
	 */
	public static void addFalseLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createFalseLeafCondition();
		rule.setCondition(leafCondition);
	}

	private static LeafCondition createFalseLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue(Alignment.NONE);
		return leafCondition;
	}

	private static LeafCondition createTrueLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue(Alignment.LEFT);
		return leafCondition;
	}

	/**
	 * @param rule
	 */
	private void addTrueAndCondition(Rule rule) {
		final AndCondition andCondition = createAndCondition();
		andCondition.getConditions().add(createTrueLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(andCondition);
	}

	/**
	 * @param rule
	 */
	private void addFalseAndCondition(Rule rule) {
		final AndCondition andCondition = createAndCondition();
		andCondition.getConditions().add(createFalseLeafCondition());
		andCondition.getConditions().add(createFalseLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(andCondition);
	}

	/**
	 * @param rule
	 */
	private void addTrueOrCondition(Rule rule) {
		final OrCondition orCondition = createOrCondition();
		orCondition.getConditions().add(createTrueLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createTrueLeafCondition());
		orCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(orCondition);
	}

	/**
	 * @param rule
	 */
	private void addFalseOrCondition(Rule rule) {
		final OrCondition orCondition = createOrCondition();
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		rule.setCondition(orCondition);
	}

	/**
	 * @param rule
	 */
	private void addComplexTrueAndCondition(Rule rule) {
		final OrCondition subTrueOrCondition = createOrCondition();
		subTrueOrCondition.getConditions().add(createTrueLeafCondition());
		subTrueOrCondition.getConditions().add(createFalseLeafCondition());

		final OrCondition subEmptyOrCondition = createOrCondition();
		final AndCondition subEmptyAndCondition = createAndCondition();

		final AndCondition andCondition = createAndCondition();
		andCondition.getConditions().add(subEmptyOrCondition);
		andCondition.getConditions().add(subEmptyAndCondition);
		andCondition.getConditions().add(subTrueOrCondition);

		rule.setCondition(andCondition);

	}

	/**
	 * @param rule
	 */
	private void addComplexTrueOrCondition(Rule rule) {
		final AndCondition subFalseAndCondition = createAndCondition();
		subFalseAndCondition.getConditions().add(createTrueLeafCondition());
		subFalseAndCondition.getConditions().add(createFalseLeafCondition());

		final OrCondition subEmptyOrCondition = createOrCondition();
		final AndCondition subEmptyAndCondition = createAndCondition();

		final OrCondition orCondition = createOrCondition();
		orCondition.getConditions().add(subEmptyOrCondition);
		orCondition.getConditions().add(subEmptyAndCondition);
		orCondition.getConditions().add(subFalseAndCondition);
		rule.setCondition(orCondition);

	}

	private AndCondition createAndCondition() {
		final AndCondition andCondition = RuleFactory.eINSTANCE.createAndCondition();
		return andCondition;
	}

	private OrCondition createOrCondition() {
		final OrCondition andCondition = RuleFactory.eINSTANCE.createOrCondition();
		return andCondition;
	}

	/**
	 * @param rule
	 */
	public static void addTrueLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createTrueLeafCondition();
		rule.setCondition(leafCondition);
	}

	private static LeafCondition createLeafCondition() {
		final LeafCondition leafCondition = RuleFactory.eINSTANCE.createLeafCondition();
		leafCondition.setAttribute(ViewPackage.eINSTANCE.getControl_LabelAlignment());
		return leafCondition;
	}

	/**
	 * @param node
	 * @param ruleHandel
	 */
	private void checkNodes(Node<Renderable> node, RuleHandel ruleHandel) {
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(ruleHandel.parent, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 0, node.getChildren().size());

	}

	public static RuleHandel createDisabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(true);
		return createRuleHandel(enableRule);
	}

	public static RuleHandel createEnabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(false);
		return createRuleHandel(enableRule);
	}

	/**
	 * @param enableRule
	 * @return
	 */
	private static RuleHandel createRuleHandel(Rule enableRule) {
		final Renderable renderable = createRuleContainerAndAddRule(enableRule);
		final Control domainObject = ViewFactory.eINSTANCE.createControl();
		domainObject.setLabelAlignment(Alignment.LEFT);
		return new RuleHandel(enableRule, renderable, domainObject);
	}

	private static Renderable createRuleContainerAndAddRule(Rule rule) {
		final Renderable renderable = ViewFactory.eINSTANCE.createView();
		renderable.getAttachments().add(rule);
		return renderable;
	}

	private static EnableRule createEnableRule() {
		return RuleFactory.eINSTANCE.createEnableRule();
	}

	public static RuleHandel createVisibleShowRule() {
		final ShowRule showRule = RuleFactory.eINSTANCE.createShowRule();
		showRule.setHide(false);
		return createRuleHandel(showRule);
	}

	public static RuleHandel createInvisibleShowRule() {
		final ShowRule showRule = RuleFactory.eINSTANCE.createShowRule();
		showRule.setHide(true);
		return createRuleHandel(showRule);
	}

}
