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
package org.eclipse.emf.ecp.view.model.common.spi.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;

/**
 * A service providing a conversion from a VDomainModelReference to databinding classes.
 * 
 * @author Eugen Neufeld
 * @param <DMR> the {@link VDomainModelReference} this service is working for
 */
public interface DatabindingProviderService<DMR extends VDomainModelReference> {

	/**
	 * Get the {@link IObservable} for the provided {@link VDomainModelReference}. A {@link IllegalArgumentException} is
	 * thrown if the passed {@link VDomainModelReference} can't provide the {@link IObservable}.
	 * 
	 * @param domainModelReference the {@link VDomainModelReference} to get the {@link IObservable} for
	 * @param observableClass the type of the {@link IObservable}
	 * @param <O> the type of the {@link IObservable}
	 * @return the {@link IObservable}
	 */
	<O extends IObservable> O getObservable(DMR domainModelReference, Class<O> observableClass);

	/**
	 * Get the {@link IProperty} for the provided {@link VDomainModelReference}. A {@link IllegalArgumentException} is
	 * thrown if the passed {@link VDomainModelReference} can't provide the {@link IProperty}.
	 * 
	 * @param domainModelReference the {@link VDomainModelReference} to get the {@link IProperty} for
	 * @param propertyClass the type of the {@link IProperty}
	 * @param <P> the type of the {@link IProperty}
	 * @return the {@link IProperty}
	 */
	<P extends IProperty> P getProperty(DMR domainModelReference, Class<P> propertyClass);
}
