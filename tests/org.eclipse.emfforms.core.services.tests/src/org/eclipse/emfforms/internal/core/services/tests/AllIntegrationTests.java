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
package org.eclipse.emfforms.internal.core.services.tests;

import org.eclipse.emfforms.internal.core.services.scoped.EMFFormsScopedServicesFactoryImpl_ITest;
import org.eclipse.emfforms.internal.core.services.scoped.SettingToControlMapper_ITest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	EMFFormsScopedServicesFactoryImpl_ITest.class,
	SettingToControlMapper_ITest.class
})
public class AllIntegrationTests {

}
