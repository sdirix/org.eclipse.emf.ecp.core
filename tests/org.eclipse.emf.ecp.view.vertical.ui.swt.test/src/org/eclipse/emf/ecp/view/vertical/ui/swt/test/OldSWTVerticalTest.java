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
package org.eclipse.emf.ecp.view.vertical.ui.swt.test;

import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.vertical.ui.test.AbstractVerticalTest;
import org.junit.BeforeClass;

/**
 * @author Jonas
 * 
 */
public class OldSWTVerticalTest extends AbstractSWTVerticalTest {

	@BeforeClass
	public static void classInit() {
		AbstractVerticalTest.setVerticalClass(ViewPackage.eINSTANCE.getColumn());
	}

}
