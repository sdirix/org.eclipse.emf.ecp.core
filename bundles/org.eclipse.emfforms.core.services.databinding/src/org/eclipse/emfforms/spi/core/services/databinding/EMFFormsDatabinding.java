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
package org.eclipse.emfforms.spi.core.services.databinding;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;

/**
 * {@link EMFFormsDatabinding} provides a databinding service. It provides two methods for getting a
 * {@link IValueProperty} from a {@link VDomainModelReference} and getting a {@link IObservableValue} from a
 * {@link VDomainModelReference} and a {@link EObject}.
 *
 * @author Lucas Koehler
 *
 */
public interface EMFFormsDatabinding {

	/**
	 * Returns an {@link IObservableValue} by observing the value described by the given {@link VDomainModelReference}
	 * of the given {@link EObject}.
	 *
	 * @param domainModelReference The domain model reference pointing to the desired value
	 * @param object The object containing the value of the reference
	 * @return The resulting {@link IObservableValue}, does not return <code>null</code>.
	 */
	IObservableValue getObservableValue(VDomainModelReference domainModelReference, EObject object);

	/**
	 * Returns an {@link IValueProperty} described by the given {@link VDomainModelReference}.
	 *
	 * @param domainModelReference The domain model reference pointing to the desired value
	 * @return The resulting {@link IValueProperty}, does not return <code>null</code>.
	 */
	IValueProperty getValueProperty(VDomainModelReference domainModelReference);
}
