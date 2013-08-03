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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.AndCondition;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.OrCondition;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.Rule;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class RuleTest {

	/**
	 * @author Jonas
	 * 
	 */
	public class RuleHandel {

		public final Rule rule;
		private final Renderable parent;
		public final EObject domainObject;

		/**
		 * @param rule
		 * @param renderable
		 * @param domainObject
		 */
		public RuleHandel(Rule rule, Renderable renderable, EObject domainObject) {
			this.rule = rule;
			// TODO Auto-generated constructor stub
			parent = renderable;
			this.domainObject = domainObject;
		}

	}

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
	private void addFalseLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createFalseLeafCondition();
		rule.setCondition(leafCondition);
	}

	private LeafCondition createFalseLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue(true);
		return leafCondition;
	}

	private LeafCondition createTrueLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue(false);
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
		final AndCondition andCondition = ViewFactory.eINSTANCE.createAndCondition();
		return andCondition;
	}

	private OrCondition createOrCondition() {
		final OrCondition andCondition = ViewFactory.eINSTANCE.createOrCondition();
		return andCondition;
	}

	/**
	 * @param rule
	 */
	private void addTrueLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createTrueLeafCondition();
		leafCondition.setExpectedValue(false);
		rule.setCondition(leafCondition);
	}

	private LeafCondition createLeafCondition() {
		final LeafCondition leafCondition = ViewFactory.eINSTANCE.createLeafCondition();
		leafCondition.setAttribute(EcorePackage.eINSTANCE.getEClass_Abstract());
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

	/**
	 * @return
	 */
	private RuleHandel createDisabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(true);
		return createRuleHandel(enableRule);
	}

	/**
	 * @return
	 */
	private RuleHandel createEnabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(false);
		return createRuleHandel(enableRule);
	}

	/**
	 * @param enableRule
	 * @return
	 */
	private RuleHandel createRuleHandel(Rule enableRule) {
		final Renderable renderable = createRuleContainerAndAddRule(enableRule);
		final EObject domainObject = EcoreFactory.eINSTANCE.createEClass();
		return new RuleHandel(enableRule, renderable, domainObject);
	}

	private Renderable createRuleContainerAndAddRule(Rule rule) {
		final Renderable renderable = ViewFactory.eINSTANCE.createView();
		renderable.setRule(rule);
		return renderable;
	}

	private EnableRule createEnableRule() {
		return ViewFactory.eINSTANCE.createEnableRule();
	}

	/**
	 * @return
	 */
	private RuleHandel createVisibleShowRule() {
		final ShowRule showRule = ViewFactory.eINSTANCE.createShowRule();
		showRule.setHide(false);
		return createRuleHandel(showRule);
	}

	/**
	 * @return
	 */
	private RuleHandel createInvisibleShowRule() {
		final ShowRule showRule = ViewFactory.eINSTANCE.createShowRule();
		showRule.setHide(true);
		return createRuleHandel(showRule);
	}

}
