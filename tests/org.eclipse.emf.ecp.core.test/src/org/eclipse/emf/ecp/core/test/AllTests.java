/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all tests.
 * 
 * @author eneufeld
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	ECPProjectManagerTests.class,
	ECPProjectTests.class, 
	ECPProviderTests.class,
	ECPProviderRegistryTest.class,
	ECPRepositoryTest.class})
public class AllTests {

}