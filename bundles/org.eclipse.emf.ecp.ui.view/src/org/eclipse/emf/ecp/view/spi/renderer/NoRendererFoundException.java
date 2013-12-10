/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.renderer;

import org.eclipse.emf.ecp.ui.view.ECPRendererException;

public class NoRendererFoundException extends ECPRendererException {

	private static final long serialVersionUID = -8540544811118107575L;

	public NoRendererFoundException(String string) {
		super(string);
	}

}
