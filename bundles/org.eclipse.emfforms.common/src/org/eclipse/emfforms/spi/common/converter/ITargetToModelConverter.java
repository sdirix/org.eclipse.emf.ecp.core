/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.common.converter;

/**
 * Interface for converting any value during the target to model conversion phase.
 *
 * @since 1.12
 */
public interface ITargetToModelConverter {

	/**
	 * Converts the given value.
	 *
	 * @param value the value to be converted
	 * @return the converted value
	 */
	Object convert(Object value);
}