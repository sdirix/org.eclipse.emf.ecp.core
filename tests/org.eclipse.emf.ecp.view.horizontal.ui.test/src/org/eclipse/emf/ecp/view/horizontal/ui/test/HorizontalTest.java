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
package org.eclipse.emf.ecp.view.horizontal.ui.test;

import org.eclipse.emf.ecp.view.horizontal.model.VHorizontalPackage;
import org.junit.BeforeClass;

/**
 * @author Jonas
 * 
 */
public class HorizontalTest extends AbstractHorizontalTest {

	@BeforeClass
	public static void init() {
		setHorizontalClass(VHorizontalPackage.eINSTANCE.getHorizontalLayout());
	}
}
