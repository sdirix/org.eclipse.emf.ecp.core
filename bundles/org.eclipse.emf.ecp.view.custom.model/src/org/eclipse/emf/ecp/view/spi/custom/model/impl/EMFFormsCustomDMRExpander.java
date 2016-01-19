/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.custom.model.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.custom.model.ECPHardcodedReferences;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDomainExpander;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsExpandingFailedException;
import org.eclipse.emfforms.spi.localization.LocalizationServiceHelper;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * DMR Expander for VCustomDMR implementation of {@link EMFFormsDMRExpander}.
 *
 * @author Eugen Neufeld
 * @since 1.8
 *
 */
@Component
public class EMFFormsCustomDMRExpander implements EMFFormsDMRExpander {
	private EMFFormsDomainExpander domainExpander;

	/**
	 * Called by the framework to set the {@link EMFFormsDomainExpander}.
	 *
	 * @param emfFormsDomainExpander The {@link EMFFormsDomainExpander}
	 */
	@Reference
	protected void setEMFFormsDomainExpander(EMFFormsDomainExpander emfFormsDomainExpander) {
		domainExpander = emfFormsDomainExpander;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander#prepareDomainObject(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void prepareDomainObject(VDomainModelReference domainModelReference, EObject domainObject)
		throws EMFFormsExpandingFailedException {
		Assert.create(domainModelReference).notNull();
		Assert.create(domainObject).notNull();
		Assert.create(domainModelReference).ofClass(VCustomDomainModelReference.class);

		final VCustomDomainModelReference customReference = VCustomDomainModelReference.class
			.cast(domainModelReference);

		if (customReference.getBundleName() == null || customReference.getClassName() == null) {
			throw new EMFFormsExpandingFailedException(
				"The custom domain model reference's bundle name and classname must not be null."); //$NON-NLS-1$
		}
		final ECPHardcodedReferences customControl = loadObject(customReference.getBundleName(),
			customReference.getClassName());
		if (customControl == null) {
			throw new EMFFormsExpandingFailedException(
				"The custom domain model cannot be loaded."); //$NON-NLS-1$
		}
		if (!customReference.isControlChecked()) {
			// read stuff from control
			final Set<VDomainModelReference> controlReferences = new LinkedHashSet<VDomainModelReference>();
			controlReferences.addAll(customControl.getNeededDomainModelReferences());
			controlReferences.addAll(customReference.getDomainModelReferences());
			customReference.getDomainModelReferences().clear();
			customReference.getDomainModelReferences().addAll(controlReferences);
			customReference.setControlChecked(true);
		}
		// resolve references from control
		for (final VDomainModelReference dmr : customReference.getDomainModelReferences()) {
			domainExpander.prepareDomainObject(dmr, domainObject);
		}
	}

	private static ECPHardcodedReferences loadObject(String bundleName, String clazz) {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			new ClassNotFoundException(String.format(LocalizationServiceHelper.getString(
				VCustomDomainModelReferenceImpl.class, "BundleNotFound_ExceptionMessage"), clazz, bundleName)); //$NON-NLS-1$
			return null;
		}
		try {
			final Class<?> loadClass = bundle.loadClass(clazz);
			if (!ECPHardcodedReferences.class.isAssignableFrom(loadClass)) {
				return null;
			}
			return ECPHardcodedReferences.class.cast(loadClass.newInstance());
		} catch (final ClassNotFoundException ex) {
			return null;
		} catch (final InstantiationException ex) {
			return null;
		} catch (final IllegalAccessException ex) {
			return null;
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public double isApplicable(VDomainModelReference domainModelReference) {
		if (VCustomDomainModelReference.class.isInstance(domainModelReference)) {
			return 5;
		}
		return NOT_APPLICABLE;
	}

}
