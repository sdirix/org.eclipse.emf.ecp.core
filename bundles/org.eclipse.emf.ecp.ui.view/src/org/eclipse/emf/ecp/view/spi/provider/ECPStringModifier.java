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
package org.eclipse.emf.ecp.view.spi.provider;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * The TooltipModifier allows to manipulate a provided String.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface ECPStringModifier {

	/**
	 * Modifies a string.
	 * 
	 * @param text the text to modify
	 * @param vElement the {@link VElement} which was rendered for the tooltip
	 * @param viewModelContext the {@link ViewModelContext} used
	 * @return the modified text
	 */
	String modifyString(String text, VElement vElement, ViewModelContext viewModelContext);
}
