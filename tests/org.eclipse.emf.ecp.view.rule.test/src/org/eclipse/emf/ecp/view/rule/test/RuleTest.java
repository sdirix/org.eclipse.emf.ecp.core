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
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.RuleFactory;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Fan;
import org.eclipse.emf.emfstore.bowling.Merchandise;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Rule Evaluation and its impact on node with conditions.
 * 
 * @author jonas
 */
@RunWith(DatabindingClassRunner.class)
public class RuleTest {

	@After
	public void after() {
		// ViewTestHelper.getViewModelContext().dispose();
	}

	@Test
	public void testEnableRuleNodeRendererWithDisableRule() {

		final RuleHandle ruleHandle = createDisabledEnableRule();

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnableRule() {

		final RuleHandle ruleHandle = createEnabledEnableRule();

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		// no condition defaults to false
		assertFalse(node.isEnabled());
	}

	@Test
	public void testShowRuleNodeRendererWithVisibleRule() {

		final RuleHandle ruleHandle = createVisibleShowRule();

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		// no condition defaults to false
		assertFalse(node.isVisible());
	}

	@Test
	public void testShowRuleNodeRendererWithInvisibleRule() {

		final RuleHandle ruleHandle = createInvisibleShowRule();

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testEnableRuleNodeRendererWithDisableRuleAndTrueLeafCondition() {

		final RuleHandle ruleHandle = createDisabledEnableRule();
		addTrueLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithDisableRuleAndFalseLeafCondition() {

		final RuleHandle ruleHandle = createDisabledEnableRule();
		addFalseLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnabledRuleAndTrueLeafCondition() {

		final RuleHandle ruleHandle = createEnabledEnableRule();
		addTrueLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isEnabled());
	}

	@Test
	public void testEnableRuleNodeRendererWithEnabledRuleAndFalseLeafCondition() {

		final RuleHandle ruleHandle = createEnabledEnableRule();
		addFalseLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isEnabled());
	}

	@Test
	public void testVisibleRuleNodeRendererWithInvisibleRuleAndTrueLeafCondition() {

		final RuleHandle ruleHandle = createInvisibleShowRule();
		addTrueLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithInvisibleRuleAndFalseLeafCondition() {

		final RuleHandle ruleHandle = createInvisibleShowRule();
		addFalseLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueLeafCondition() {

		final RuleHandle ruleHandle = createVisibleShowRule();
		addTrueLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseLeafCondition() {

		final RuleHandle ruleHandle = createVisibleShowRule();
		addFalseLeafCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueAndCondition() {

		final RuleHandle ruleHandle = createVisibleShowRule();
		addTrueAndCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseAndCondition() {

		final RuleHandle ruleHandle = createVisibleShowRule();
		addFalseAndCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueOrCondition() {

		final RuleHandle ruleHandle = createVisibleShowRule();
		addTrueOrCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndFalseOrCondition() {
		final RuleHandle ruleHandle = createVisibleShowRule();
		addFalseOrCondition(ruleHandle.getRule());

		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueComplexOrCondition() {
		final RuleHandle ruleHandle = createVisibleShowRule();
		addComplexTrueOrCondition(ruleHandle.getRule());
		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertTrue(node.isVisible());
	}

	@Test
	public void testVisibleRuleNodeRendererWithVisibleRuleAndTrueComplexAndCondition() {
		final RuleHandle ruleHandle = createVisibleShowRule();
		addComplexTrueAndCondition(ruleHandle.getRule());
		final Node<Renderable> node = ViewTestHelper.build(ruleHandle.getParent(), ruleHandle.getDomainObject());
		checkNodes(node, ruleHandle);
		assertFalse(node.isVisible());
	}

	public static void addFalseLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createFalseLeafCondition();
		rule.setCondition(leafCondition);
	}

	private static LeafCondition createFalseLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue("bar");
		return leafCondition;
	}

	private static LeafCondition createTrueLeafCondition() {
		final LeafCondition leafCondition = createLeafCondition();
		leafCondition.setExpectedValue("foo");
		return leafCondition;
	}

	private void addTrueAndCondition(Rule rule) {
		final AndCondition andCondition = createAndCondition();
		andCondition.getConditions().add(createTrueLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(andCondition);
	}

	private void addFalseAndCondition(Rule rule) {
		final AndCondition andCondition = createAndCondition();
		andCondition.getConditions().add(createFalseLeafCondition());
		andCondition.getConditions().add(createFalseLeafCondition());
		andCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(andCondition);
	}

	private void addTrueOrCondition(Rule rule) {
		final OrCondition orCondition = createOrCondition();
		orCondition.getConditions().add(createTrueLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createTrueLeafCondition());
		orCondition.getConditions().add(createTrueLeafCondition());
		rule.setCondition(orCondition);
	}

	private void addFalseOrCondition(Rule rule) {
		final OrCondition orCondition = createOrCondition();
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		orCondition.getConditions().add(createFalseLeafCondition());
		rule.setCondition(orCondition);
	}

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

	public static void addTrueLeafCondition(Rule rule) {
		final LeafCondition leafCondition = createTrueLeafCondition();
		rule.setCondition(leafCondition);
	}

	private static LeafCondition createLeafCondition() {
		final LeafCondition leafCondition = RuleFactory.eINSTANCE.createLeafCondition();
		final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		modelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		modelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		leafCondition.setDomainModelReference(modelReference);

		return leafCondition;
	}

	private void checkNodes(Node<Renderable> node, RuleHandle ruleHandle) {
		// final RuleService ruleService = new RuleService();
		// ruleService.instantiate(ViewTestHelper.getViewModelContext());
		// ruleService.dispose();
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(ruleHandle.getParent(), node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 0, node.getChildren().size());

	}

	public static RuleHandle createDisabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(true);
		return createRuleHandle(enableRule);
	}

	public static RuleHandle createEnabledEnableRule() {
		final EnableRule enableRule = createEnableRule();
		enableRule.setDisable(false);
		return createRuleHandle(enableRule);
	}

	private static RuleHandle createRuleHandle(Rule enableRule) {
		final Renderable renderable = createRuleContainerAndAddRule(enableRule);
		final Fan fan = BowlingFactory.eINSTANCE.createFan();
		final Merchandise merchandise = BowlingFactory.eINSTANCE.createMerchandise();
		merchandise.setName("foo");
		fan.setFavouriteMerchandise(merchandise);
		return new RuleHandle(enableRule, renderable, fan);
	}

	private static Renderable createRuleContainerAndAddRule(Rule rule) {
		final Renderable renderable = ViewFactory.eINSTANCE.createView();
		renderable.getAttachments().add(rule);
		return renderable;
	}

	private static EnableRule createEnableRule() {
		return RuleFactory.eINSTANCE.createEnableRule();
	}

	public static RuleHandle createVisibleShowRule() {
		final ShowRule showRule = RuleFactory.eINSTANCE.createShowRule();
		showRule.setHide(false);
		return createRuleHandle(showRule);
	}

	public static RuleHandle createInvisibleShowRule() {
		final ShowRule showRule = RuleFactory.eINSTANCE.createShowRule();
		showRule.setHide(true);
		return createRuleHandle(showRule);
	}

}
