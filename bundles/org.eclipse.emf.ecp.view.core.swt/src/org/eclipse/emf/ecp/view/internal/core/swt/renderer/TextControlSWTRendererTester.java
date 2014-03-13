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

/**
 * Tester for Text Renderer.
 * 
 * @author Eugen Neufeld
 * 
 */
public class TextControlSWTRendererTester extends SimpleControlSWTRendererTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#isSingleValue()
	 */
	@Override
	protected boolean isSingleValue() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#getPriority()
	 */
	@Override
	protected int getPriority() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#getSupportedClassType()
	 */
	@Override
	protected Class<?> getSupportedClassType() {
		return String.class;
	}
}
