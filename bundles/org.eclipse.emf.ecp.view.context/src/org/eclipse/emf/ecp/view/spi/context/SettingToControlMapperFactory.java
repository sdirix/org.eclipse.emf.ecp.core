/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.context;

/**
 * A service that provides {@link ViewModelContext} specific {@link SettingToControlMapper} instances.
 *
 * @author Lucas Koehler
 * @since 1.8
 *
 */
// TODO move to another bundle
public interface SettingToControlMapperFactory {

	/**
	 * Creates a new {@link SettingToControlMapper} instance for the given {@link ViewModelContext}.
	 *
	 * @param viewModelContext The {@link ViewModelContext}
	 * @return The created {@link SettingToControlMapper} instance
	 */
	SettingToControlMapper createSettingToControlMapper(ViewModelContext viewModelContext);
}
