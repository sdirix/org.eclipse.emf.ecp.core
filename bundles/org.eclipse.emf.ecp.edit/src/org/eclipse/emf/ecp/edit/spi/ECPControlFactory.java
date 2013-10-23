/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.spi;

import java.util.Set;

import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * The ControlFactory provides a
 * method ({@link ECPControlFactory#createControl(Class, IItemPropertyDescriptor, ECPControlContext)}) for creating a
 * suitable
 * control with the known widgets.
 * 
 * @author Eugen Neufeld
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ECPControlFactory {
	/**
	 * Creates an {@link ECPAbstractControl} from the provided {@link IItemPropertyDescriptor} and the
	 * {@link ECPControlContext}.
	 * 
	 * @param <T> the type of the control to create
	 * @param controlTypeClass the class of the control to create
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param context the {@link ECPControlContext}
	 * @return the created {@link ECPControl} or null if nothing fitting could be created
	 */
	@Deprecated
	<T extends ECPControl> T createControl(Class<T> controlTypeClass, IItemPropertyDescriptor itemPropertyDescriptor,
		ECPControlContext context);

	/**
	 * Creates an {@link ECPAbstractControl} based on a provided id.
	 * 
	 * @param <T> the type of the control to create
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param context the {@link ECPControlContext}
	 * @param controlId the id of the control to create
	 * @return the created {@link ECPControl} or null if id is unknown
	 * 
	 * @deprecated
	 */

	@Deprecated
	<T extends ECPControl> T createControl(IItemPropertyDescriptor itemPropertyDescriptor,
		ECPControlContext context, String controlId);

	/**
	 * @since 1.1
	 */
	<T> T createControl(Class<T> controlTypeClass, VDomainModelReference domainModelReference);

	/**
	 * @since 1.1
	 */
	<T> T createControl(String controlId);

	/**
	 * A copy of all known {@link ECPControlDescription}.
	 * 
	 * @return a copy of the set of all known controlDescriptions
	 */
	Set<ECPControlDescription> getControlDescriptors();
}
