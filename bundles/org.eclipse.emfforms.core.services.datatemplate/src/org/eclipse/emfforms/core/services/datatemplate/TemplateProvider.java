/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.core.services.datatemplate;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emfforms.datatemplate.Template;

/**
 * Template provider interface.
 *
 * @author Mat Hansen <mhansen@eclipsesource.com>
 * @since 1.16
 *
 */
public interface TemplateProvider {

	/**
	 * Checks whether the template provider can provide templates matching the given (super) type.
	 *
	 * @param superType the type to match for
	 * @return true if the provider provides templates
	 */
	boolean canProvide(EClass superType);

	/**
	 * Provides a set of templates containing any available template for the given type or one of its sub types.
	 *
	 * @param superType The type for which templates will be provided
	 * @return a set of suitable templates (might be empty)
	 */
	Set<Template> provide(EClass superType);

}
