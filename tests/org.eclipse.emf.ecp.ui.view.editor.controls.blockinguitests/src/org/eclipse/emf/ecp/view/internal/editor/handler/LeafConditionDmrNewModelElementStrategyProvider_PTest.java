/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.editor.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.test.common.MultiTry;
import org.eclipse.emf.ecp.test.common.MultiTryTestRule;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy;
import org.eclipse.emf.ecp.view.internal.editor.handler.LeafConditionDmrNewModelElementStrategyProvider_PTest.CreateLeafConditionDmrTestProvider;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.rule.model.RulePackage;
import org.eclipse.emf.ecp.view.test.common.swt.spi.SWTTestUtil;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link LeafConditionDmrNewModelElementStrategyProvider}.
 *
 * @author Lucas Koehler
 */
public class LeafConditionDmrNewModelElementStrategyProvider_PTest
	extends AbstractLeafConditionDmrStrategyProviderTest<CreateLeafConditionDmrTestProvider> {

	@Rule
	public final MultiTryTestRule multiTryRule = new MultiTryTestRule(3, false);

	@Override
	protected void initStrategyProvider() {
		setStrategyProvider(new CreateLeafConditionDmrTestProvider());
	}

	@Override
	protected boolean executeHandles(EObject owner, EReference reference) {
		return getStrategyProvider().handles(owner, reference);
	}

	@Test
	@MultiTry
	public void strategy() {
		final CreateNewModelElementStrategy strategy = getStrategyProvider().createCreateNewModelElementStrategy();
		assertNotNull(strategy);

		@SuppressWarnings("unchecked")
		final Optional<EObject>[] result = new Optional[1];
		UIThreadRunnable.asyncExec(() -> {
			result[0] = strategy.createNewModelElement(getLeafCondition(),
				RulePackage.Literals.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE);
		});

		waitForShell("New Domain Model Reference"); //$NON-NLS-1$

		final boolean r = UIThreadRunnable.syncExec(() -> {
			final Shell wShell = Display.getDefault().getActiveShell();
			final Tree tree = SWTTestUtil.findControl(wShell, 0, Tree.class);
			// select EReference -> Finish button should be disabled
			SWTTestUtil.selectTreeItem(tree, 0);
			final Button finish = SWTTestUtil.findControl(wShell, 4, Button.class);
			final boolean finishState = finish.isEnabled();

			SWTTestUtil.selectTreeItem(tree, 1);
			SWTTestUtil.clickButton(finish);
			return finishState;
		});
		assertFalse("Dmr wizard finish button enablement after an EReference was selected in simple mode.", r);

		final Optional<EObject> strategyResult = waitUntilNotNull(result);
		assertTrue(strategyResult.isPresent());
		assertTrue("Result must be a VDomainModelReference.", result[0].get() instanceof VDomainModelReference); //$NON-NLS-1$
		// strategy should not add the dmr to the table control.
		assertNull(getLeafCondition().getDomainModelReference());
	}

	@Test
	@MultiTry
	public void strategy_cancel() {
		final CreateNewModelElementStrategy strategy = getStrategyProvider().createCreateNewModelElementStrategy();
		assertNotNull(strategy);

		@SuppressWarnings("unchecked")
		final Optional<EObject>[] result = new Optional[1];
		UIThreadRunnable.asyncExec(() -> {
			result[0] = strategy.createNewModelElement(getLeafCondition(),
				RulePackage.Literals.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE);
		});

		waitForShell("New Domain Model Reference"); //$NON-NLS-1$

		UIThreadRunnable.syncExec(() -> {
			final Shell wShell = Display.getDefault().getActiveShell();
			final Tree tree = SWTTestUtil.findControl(wShell, 0, Tree.class);
			final Button cancel = SWTTestUtil.findControl(wShell, 3, Button.class);
			SWTTestUtil.selectTreeItem(tree, 1);
			SWTTestUtil.clickButton(cancel);
		});

		final Optional<EObject> strategyResult = waitUntilNotNull(result);
		assertFalse(strategyResult.isPresent());
		assertNull(getLeafCondition().getDomainModelReference());
	}

	/** Allows to mock the segment tooling enabled flag without the need to provide a runtime parameter. */
	class CreateLeafConditionDmrTestProvider extends LeafConditionDmrNewModelElementStrategyProvider
		implements TestableStrategyProvider {
		private boolean segmentToolingEnabled;

		/**
		 * @param segmentToolingEnabled true to enable segment mode
		 */
		@Override
		public void setSegmentToolingEnabled(boolean segmentToolingEnabled) {
			this.segmentToolingEnabled = segmentToolingEnabled;
		}

		@Override
		boolean isSegmentToolingEnabled() {
			return segmentToolingEnabled;
		}
	}

}
