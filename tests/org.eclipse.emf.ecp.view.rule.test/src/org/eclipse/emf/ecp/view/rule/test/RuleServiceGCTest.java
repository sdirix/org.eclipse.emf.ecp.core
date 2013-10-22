/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.internal.rule.RuleService;
import org.eclipse.emf.ecp.view.internal.rule.RuleServiceHelper;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;
import org.eclipse.emf.ecp.view.test.common.GCCollectable;
import org.eclipse.emf.ecp.view.test.common.Tuple;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalFactory;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalLayout;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.League;
import org.eclipse.emf.emfstore.bowling.Player;
import org.junit.After;
import org.junit.Test;

public class RuleServiceGCTest extends CommonRuleTest {

	private ViewModelContext context;

	private Tuple<View, League> createView() {
		final Player player = BowlingFactory.eINSTANCE.createPlayer();
		final League league = BowlingFactory.eINSTANCE.createLeague();
		league.getPlayers().add(player);

		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(league.eClass());

		final VVerticalLayout parentColumn = VVerticalFactory.eINSTANCE.createVerticalLayout();
		view.getChildren().add(parentColumn);

		final VVerticalLayout column = VVerticalFactory.eINSTANCE.createVerticalLayout();
		parentColumn.getComposites().add(column);

		final Control controlPName = ViewFactory.eINSTANCE.createControl();

		final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getPlayer_Name());
		domainModelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getLeague_Players());
		controlPName.setDomainModelReference(domainModelReference);

		column.getComposites().add(controlPName);
		return Tuple.create(view, league);
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

	/**
	 * Instantiate rule service.
	 * 
	 * @return the rule service
	 */
	private RuleService instantiateRuleService(View view, final EObject domainModel) {
		final RuleService ruleService = new RuleService();
		final RuleServiceHelper ruleServiceHelper = new RuleServiceHelper();
		context = new ViewModelContextImpl(view, domainModel);
		ruleService.instantiate(context);
		ruleServiceHelper.instantiate(context);
		return ruleService;
	}

	@Test
	public void testRemoveShowRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		league.setName("foo");
		addShowRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().get(0).getAttachments().remove(0);

		assertTrue(collectable.isCollectable());
		assertTrue(view.getChildren().get(0).isVisible());
	}

	@Test
	public void testRemoveShowRuleWithoutCondition() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addShowRule(view.getChildren().get(0), false);
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().get(0).getAttachments().remove(0);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveEnableRuleWithoutCondition() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addEnableRule(view.getChildren().get(0), false);
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().get(0).getAttachments().remove(0);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveEnableRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		league.setName("foo");
		addEnableRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().get(0).getAttachments().remove(0);

		assertTrue(collectable.isCollectable());
		assertTrue(view.getChildren().get(0).isEnabled());
	}

	@Test
	public void testRemoveLeafConditionOfShowRuleReevaluate() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		league.setName("foo");
		addShowRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			((ShowRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final ShowRule showRule = (ShowRule) view.getChildren().get(0).getAttachments().get(0);

		assertFalse(view.getChildren().get(0).isVisible());
		showRule.setCondition(null);
		assertTrue(collectable.isCollectable());
		assertTrue(view.getChildren().get(0).isVisible());
	}

	@Test
	public void testRemoveAndConditionOfShowRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addLeagueShowRuleWithAndCondition(view.getChildren().get(0), false,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));

		final GCCollectable collectable = new GCCollectable(
			((ShowRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final ShowRule showRule = (ShowRule) view.getChildren().get(0).getAttachments().get(0);
		showRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveAndConditionOfEnableRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addLeagueEnableRuleWithAndCondition(view.getChildren().get(0), false,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));

		final GCCollectable collectable = new GCCollectable(
			((EnableRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final EnableRule enableRule = (EnableRule) view.getChildren().get(0).getAttachments().get(0);
		enableRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveOrConditionOfEnableRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addLeagueShowRuleWithOrCondition(view.getChildren().get(0), false,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));

		final GCCollectable collectable = new GCCollectable(
			((ShowRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final ShowRule showRule = (ShowRule) view.getChildren().get(0).getAttachments().get(0);
		showRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveLeafConditionOfEnableRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addEnableRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final EnableRule enableRule = (EnableRule) view.getChildren().get(0).getAttachments().get(0);
		final GCCollectable collectable = new GCCollectable(enableRule.getCondition());
		instantiateRuleService(view, league);
		enableRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveLeafConditionOfShowRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addShowRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final ShowRule showRule = (ShowRule) view.getChildren().get(0).getAttachments().get(0);
		final GCCollectable collectable = new GCCollectable(showRule.getCondition());
		instantiateRuleService(view, league);
		showRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveLeafConditionOfEnableRuleReevaluate() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		league.setName("foo");
		addEnableRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			((EnableRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final EnableRule enableRule = (EnableRule) view.getChildren().get(0).getAttachments().get(0);

		assertFalse(view.getChildren().get(0).isEnabled());
		enableRule.setCondition(null);
		assertTrue(collectable.isCollectable());
		assertTrue(view.getChildren().get(0).isEnabled());
	}

	@Test
	public void testRemoveOrConditionOfShowRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();

		addLeagueShowRuleWithOrCondition(view.getChildren().get(0), false,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));

		final GCCollectable collectable = new GCCollectable(
			((ShowRule) view.getChildren().get(0).getAttachments().get(0)).getCondition());
		instantiateRuleService(view, league);
		final ShowRule showRule = (ShowRule) view.getChildren().get(0).getAttachments().get(0);
		showRule.setCondition(null);
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveOrConditionBOfEnableRuleReevaluate() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		league.setName("foo");

		addLeagueEnableRuleWithOrCondition(view.getChildren().get(0), false,
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "foo"),
			createLeafCondition(BowlingPackage.eINSTANCE.getLeague_Name(), "League2"));

		final GCCollectable collectable = new GCCollectable(
			EnableRule.class.cast(view.getChildren().get(0).getAttachments().get(0)).getCondition());
		final GCCollectable ruleCollectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));

		instantiateRuleService(view, league);
		final EnableRule enableRule = (EnableRule) view.getChildren().get(0).getAttachments().get(0);

		assertFalse(view.getChildren().get(0).isEnabled());
		enableRule.setCondition(null);
		assertTrue(collectable.isCollectable());
		assertFalse(ruleCollectable.isCollectable());
		assertTrue(view.getChildren().get(0).isEnabled());
	}

	@Test
	public void testRemoveRenderableWithShowRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addShowRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().remove(view.getChildren().get(0));
		assertTrue(collectable.isCollectable());
	}

	@Test
	public void testRemoveRenderableWithEnableRule() {
		final Tuple<View, League> tuple = createView();
		final View view = tuple.first();
		final League league = tuple.second();
		addEnableRule(view.getChildren().get(0), false, BowlingPackage.eINSTANCE.getLeague_Name(), "foo");
		final GCCollectable collectable = new GCCollectable(
			view.getChildren().get(0).getAttachments().get(0));
		instantiateRuleService(view, league);
		view.getChildren().remove(view.getChildren().get(0));
		assertTrue(collectable.isCollectable());
	}

}
