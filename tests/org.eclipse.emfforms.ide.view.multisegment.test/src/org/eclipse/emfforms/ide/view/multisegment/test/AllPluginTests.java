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
package org.eclipse.emfforms.ide.view.multisegment.test;

import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiDmrNewReferenceElementStrategyProvider_PTest;
import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiDmrOpenInNewContextStrategyProvider_PTest;
import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiSegmentChildDmrNewModelElementStrategyProvider_PTest;
import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiSegmentChildDmrOpenInNewContextStrategyProvider_PTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All plugin tests for bundle org.eclipse.emfforms.ide.view.multisegment.
 *
 * @author Lucas Koehler
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MultiSegmentChildDmrOpenInNewContextStrategyProvider_PTest.class,
	MultiSegmentChildDmrNewModelElementStrategyProvider_PTest.class,
	MultiDmrNewReferenceElementStrategyProvider_PTest.class, MultiDmrOpenInNewContextStrategyProvider_PTest.class })
public class AllPluginTests {

}
