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
package org.eclipse.emf.ecp.view.rule.ui.swt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.test.RuleHandle;
import org.eclipse.emf.ecp.view.rule.test.RuleTest;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class RuleSWTTest {

	private org.eclipse.emf.ecp.view.model.Control control;
	private Shell shell;
	private EObject input;
	private org.eclipse.swt.widgets.Control renderedControl;

	@Before
	public void init() {
		control = createControl();
		input = createControl();
		control.setTargetFeature(ViewPackage.eINSTANCE.getControl_LabelAlignment());
		shell = SWTViewTestHelper.createShell();
		shell.setVisible(true);
	}

	@Test
	public void testEnableRuleWithFalse() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		addEnableRule();
		render();
		assertFalse(isControlEnabled());
	}

	@Test
	public void testDisableRuleWithFalse() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		addDisableRule();
		render();
		assertTrue(isControlEnabled());
	}

	@Test
	public void testVisibleRuleWithFalse() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		addVisibleRule();
		render();
		assertFalse(isControlVisible());
	}

	@Test
	public void testInVisibleRuleWithFalse() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		addInVisibleRule();
		render();
		assertTrue(isControlVisible());
	}

	@Test
	public void testDisableRuleAndTrueLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addDisableRule();
		RuleTest.addTrueLeafCondition(ruleHandle.getRule());
		render();
		assertFalse(isControlEnabled());
	}

	@Test
	public void testDisableRuleAndFalseLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addDisableRule();
		RuleTest.addFalseLeafCondition(ruleHandle.getRule());
		render();
		assertTrue(isControlEnabled());
	}

	@Test
	public void testEnableRuleAndFalseLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addEnableRule();
		RuleTest.addFalseLeafCondition(ruleHandle.getRule());
		render();
		assertFalse(isControlEnabled());
	}

	@Test
	public void testEnabledRuleAndTrueLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addEnableRule();
		RuleTest.addTrueLeafCondition(ruleHandle.getRule());
		render();
		assertTrue(isControlEnabled());
	}

	@Test
	public void testInvisibleRuleAndFalseLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addInVisibleRule();
		RuleTest.addFalseLeafCondition(ruleHandle.getRule());
		render();
		assertTrue(isControlVisible());
	}

	@Test
	public void testInvisibleRuleAndTrueLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addInVisibleRule();
		RuleTest.addTrueLeafCondition(ruleHandle.getRule());
		render();
		assertFalse(isControlVisible());
	}

	@Test
	public void testVisibleRuleAndTrueLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addVisibleRule();
		RuleTest.addTrueLeafCondition(ruleHandle.getRule());
		render();
		assertTrue(isControlVisible());
	}

	@Test
	public void testVisibleRuleAndFalseLeafCondition() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final RuleHandle ruleHandle = addVisibleRule();
		RuleTest.addFalseLeafCondition(ruleHandle.getRule());
		render();
		assertFalse(isControlVisible());
	}

	/**
	 * @return
	 * 
	 */
	private RuleHandle addInVisibleRule() {
		final RuleHandle invisibleShowRule = RuleTest.createInvisibleShowRule();
		addRuleToElement(invisibleShowRule.getRule(), control);
		return invisibleShowRule;

	}

	/**
	 * @return
	 */
	private boolean isControlVisible() {
		final org.eclipse.swt.widgets.Control control = getControlWhichIsEnabled(renderedControl);
		return control.isVisible();
	}

	/**
	 * 
	 */
	private RuleHandle addVisibleRule() {
		final RuleHandle visibleShowRule = RuleTest.createVisibleShowRule();
		addRuleToElement(visibleShowRule.getRule(), control);
		return visibleShowRule;

	}

	private RuleHandle addDisableRule() {
		final RuleHandle enabledEnableRule = RuleTest.createDisabledEnableRule();
		addRuleToElement(enabledEnableRule.getRule(), control);
		return enabledEnableRule;
	}

	/**
	 * @throws NoPropertyDescriptorFoundExeption
	 * @throws NoRendererFoundException
	 * 
	 */
	private void render() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderedControl = SWTViewTestHelper.render(control, input, shell);

	}

	/**
	 * @return
	 */
	private boolean isControlEnabled() {
		final org.eclipse.swt.widgets.Control control = getControlWhichIsEnabled(renderedControl);
		return control.isEnabled();
	}

	/**
	 * @param control
	 */
	private org.eclipse.swt.widgets.Control getControlWhichIsEnabled(org.eclipse.swt.widgets.Control control) {
		assertTrue(control instanceof Composite);

		return control;
	}

	/**
	 * @param control
	 */
	private RuleHandle addEnableRule() {
		final RuleHandle enabledEnableRule = RuleTest.createEnabledEnableRule();
		addRuleToElement(enabledEnableRule.getRule(), control);
		return enabledEnableRule;
	}

	/**
	 * @param enabledEnableRule
	 * @param control2
	 */
	private void addRuleToElement(Rule enabledEnableRule, Control control2) {
		control.getAttachments().add(enabledEnableRule);

	}

	/**
	 * 
	 */
	private org.eclipse.emf.ecp.view.model.Control createControl() {
		final Control createControl = ViewFactory.eINSTANCE.createControl();
		createControl.setLabelAlignment(Alignment.LEFT);
		return createControl;

	}

}
