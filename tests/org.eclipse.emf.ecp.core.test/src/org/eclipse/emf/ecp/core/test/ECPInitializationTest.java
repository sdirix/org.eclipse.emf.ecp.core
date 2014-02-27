/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.junit.Test;

/**
 * @author Eugen
 * 
 */
public class ECPInitializationTest {

	@Test
	public void createProjectAddElementTest() {
		try {
			final ECPProject project = ECPUtil.getECPProjectManager().createProject(
				ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME), "test");
			final long startTimeMillis = System.currentTimeMillis();
			for (int i = 0; i < 60000; i++) {
				project.getContents().add(EcoreFactory.eINSTANCE.createEClass());
				if (i % 1000 == 0) {
					if (System.currentTimeMillis() - startTimeMillis > 20000) {
						fail("Taking too long");
					}
					System.out
						.println("Added " + i + "Items, Time passed " + (System.currentTimeMillis() - startTimeMillis));
				}
			}
			assertTrue(System.currentTimeMillis() - startTimeMillis < 20000);
		} catch (final ECPProjectWithNameExistsException e) {
			fail();
		}
	}

}