/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.model;

/**
 * Wrapper for exceptions occuring during the inialisation of {@link ECPCustomControl}s.
 * 
 * @author jfaltermeier
 * 
 */
public class ECPCustomControlInitException extends Exception {

	private static final long serialVersionUID = 6739521641358060919L;

	/**
	 * @param string the message
	 * @param throwable the throwable causing this exception
	 */
	public ECPCustomControlInitException(String string, Throwable throwable) {

		super(string, throwable);
	}

}
