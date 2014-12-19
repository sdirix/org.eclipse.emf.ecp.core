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
package org.eclipse.emf.ecp.view.spi.swt;

import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * The renderer for additional controls.
 *
 * @author Eugen Neufeld
 * @param <VELEMENT> the {@link VElement} this renderer is valid for
 * @since 1.3
 */
public abstract class AbstractAdditionalSWTRenderer<VELEMENT extends VElement> extends AbstractSWTRenderer<VELEMENT> {

}
