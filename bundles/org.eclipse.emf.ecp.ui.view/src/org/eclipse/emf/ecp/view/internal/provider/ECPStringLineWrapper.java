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
package org.eclipse.emf.ecp.view.internal.provider;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.provider.ECPStringModifier;

/**
 * An {@link ECPStringModifier} which wraps texts automatically after 80 chars.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ECPStringLineWrapper implements ECPStringModifier {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.provider.ECPStringModifier#modifyString(java.lang.String,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	public String modifyString(String text, VElement vElement, ViewModelContext viewModelContext) {
		final StringBuilder sb = new StringBuilder(text);
		final int maxLineLength = 80;
		int i = 0;
		while (i + maxLineLength < sb.length()) {
			i = sb.lastIndexOf(" ", i + maxLineLength); //$NON-NLS-1$
			if (i == -1) {
				break;
			}
			sb.replace(i, i + 1, "\n"); //$NON-NLS-1$
		}

		return sb.toString();
	}

}
