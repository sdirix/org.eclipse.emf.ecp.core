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

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.junit.Test;

public class ECPProviderRegistry_PTest extends AbstractTest {

	// @Test
	// public void getProviderByItselfTest(){
	// ECPProvider ecpProvider=ECPUtil.getECPProviderRegistry().getProvider(getProvider());
	// assertEquals(ecpProvider,getProvider());
	// }
	//
	@Test
	public void removeAddProviderTest() {
		ECPUtil.getECPProviderRegistry().removeProvider(EMFStoreProvider.NAME);
		assertEquals(0, ECPUtil.getECPProviderRegistry().getProviders().size());
		ECPUtil.getECPProviderRegistry().addProvider(new EMFStoreProvider());
		assertEquals(1, ECPUtil.getECPProviderRegistry().getProviders().size());
	}
}
