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
package org.eclipse.emf.ecp.ui.view.editor.controls.blockinguitests;

import org.eclipse.emf.ecp.view.internal.editor.handler.RuleConditionDmrNewModelElementStrategyProvider_PTest;
import org.eclipse.emf.ecp.view.internal.editor.handler.RuleConditionDmrOpenInNewContextStrategyProvider_PTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All plugin tests for ui.view.editor.controls where tested classes block the UI.
 *
 * @author Lucas Koehler
 */
@RunWith(Suite.class)
@SuiteClasses({ RuleConditionDmrNewModelElementStrategyProvider_PTest.class,
	RuleConditionDmrOpenInNewContextStrategyProvider_PTest.class })
public class AllPluginTests {

}
