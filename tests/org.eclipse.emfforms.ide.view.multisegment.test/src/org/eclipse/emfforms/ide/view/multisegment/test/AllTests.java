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

import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiSegmentGenerator_Test;
import org.eclipse.emfforms.internal.view.multisegment.tooling.MultiSelectionValidator_Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All unit tests for bundle org.eclipse.emfforms.ide.view.multisegment.
 *
 * @author Lucas Koehler
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MultiSegmentGenerator_Test.class, MultiSelectionValidator_Test.class })
public class AllTests {

}
