/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.core.swt.tests;

import org.eclipse.emf.ecp.view.internal.core.swt.renderer.BooleanControlRenderer_Test;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.DateTimeControlRenderer_Test;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.EnumComboViewerRenderer_Test;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.NumberControlRenderer_Test;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.ViewRenderer_Test;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.XMLDateControlRenderer_Test;
import org.eclipse.emf.ecp.view.spi.core.swt.renderer.TextControlRenderer_Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite for Unit Tests.
 * 
 * @author Eugen Neufeld
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ BooleanControlRenderer_Test.class, DateTimeControlRenderer_Test.class,
	EnumComboViewerRenderer_Test.class,
	NumberControlRenderer_Test.class, ViewRenderer_Test.class, XMLDateControlRenderer_Test.class,
	TextControlRenderer_Test.class })
public class AllTests {
	// JUnit 4 Test Suite
}
