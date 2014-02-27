/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Tester for Text Renderer.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTXMLDateControlRendererTester extends SWTSimpleControlRendererTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SWTSimpleControlRendererTester#isSingleValue()
	 */
	@Override
	protected boolean isSingleValue() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SWTSimpleControlRendererTester#getPriority()
	 */
	@Override
	protected int getPriority() {
		return 3;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SWTSimpleControlRendererTester#getSupportedClassType()
	 */
	@Override
	protected Class<?> getSupportedClassType() {
		return XMLGregorianCalendar.class;
	}
}
