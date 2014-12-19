/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.indexdmr.databinding;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecp.view.model.common.internal.databinding.FeaturePathDatabindingProviderService;
import org.eclipse.emf.ecp.view.model.common.spi.databinding.DatabindingProviderService;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * The {@link DatabindingProviderService} for a
 * {@link VIndexDomainModelReference}.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class IndexDatabindingProviderService extends
		FeaturePathDatabindingProviderService {

	@SuppressWarnings("unchecked")
	@Override
	public <O extends IObservable> O getObservable(
			VFeaturePathDomainModelReference domainModelReference,
			Class<O> observableClass) {
		final IObservableValue value = (IObservableValue) super.getObservable(
				domainModelReference, observableClass);

		return (O) value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends IProperty> P getProperty(
			VFeaturePathDomainModelReference domainModelReference,
			Class<P> propertyClass) {
		// IEMFValueProperty property = (IEMFValueProperty)
		// super.getProperty(domainModelReference, propertyClass);
		final VIndexDomainModelReference indexDomainModelReference = (VIndexDomainModelReference) domainModelReference;

		final EMFIndexedValueProperty valueProperty = new EMFIndexedValueProperty(
				indexDomainModelReference.getIndex(),
				domainModelReference.getDomainModelEFeature());
		final IValueProperty value = valueProperty
				.value(getPropertyOfChild(indexDomainModelReference
						.getTargetDMR()));

		// property.value(valueProperty);
		return (P) value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IValueProperty getPropertyOfChild(
			VDomainModelReference domainModelReference) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(getClass())
				.getBundleContext();
		ServiceReference<DatabindingProviderService> databindingProviderServiceReference = null;
		try {
			final Collection<ServiceReference<DatabindingProviderService>> serviceReferences = bundleContext
					.getServiceReferences(
							DatabindingProviderService.class,
							String.format(
									"(domainModelReference=%s)", domainModelReference.getClass().getName())); //$NON-NLS-1$
			final Iterator<ServiceReference<DatabindingProviderService>> iterator = serviceReferences
					.iterator();
			if (iterator.hasNext()) {
				databindingProviderServiceReference = iterator.next();
			}
			if (databindingProviderServiceReference == null) {
				throw new IllegalStateException(
						"No DatabindingProviderService available."); //$NON-NLS-1$
			}
		} catch (final InvalidSyntaxException e) {
			throw new IllegalStateException(e);
		}
		final DatabindingProviderService<VDomainModelReference> service = bundleContext
				.getService(databindingProviderServiceReference);

		final IValueProperty property = service.getProperty(
				domainModelReference, IValueProperty.class);

		bundleContext.ungetService(databindingProviderServiceReference);

		return property;
	}

}
