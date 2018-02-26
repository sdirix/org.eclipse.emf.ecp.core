/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.datatemplate;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.spi.EMFUtils;
import org.eclipse.emfforms.core.services.datatemplate.TemplateProvider;
import org.eclipse.emfforms.datatemplate.DataTemplateFactory;
import org.eclipse.emfforms.datatemplate.Template;
import org.eclipse.emfforms.internal.core.services.label.BundleResolver;
import org.eclipse.emfforms.internal.core.services.label.BundleResolver.NoBundleFoundException;
import org.eclipse.emfforms.internal.core.services.label.BundleResolverImpl;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * {@link TemplateProvider} that provides a templates simply containing empty instances of the corresponding EClass and
 * its concrete sub classes.
 *
 * @author Lucas Koehler
 *
 */
@SuppressWarnings("restriction")
@Component(name = "BlankTemplateProvider")
public class BlankTemplateProvider implements TemplateProvider {
	private BundleResolver bundleResolver;
	private EMFFormsLocalizationService localizationService;

	/**
	 * Creates a new {@link BlankTemplateProvider} instance.
	 */
	public BlankTemplateProvider() {
		setBundleResolver(new BundleResolverImpl());
	}

	/**
	 * Sets the {@link EMFFormsLocalizationService}.
	 * 
	 * @param localizationService The {@link EMFFormsLocalizationService}
	 */
	@Reference
	void setLocalizationService(EMFFormsLocalizationService localizationService) {
		this.localizationService = localizationService;
	}

	@Override
	public boolean canProvide(EClass superType) {
		// We can only provide a blank template if the type or at least one of its sub classes is concrete.
		for (final EClass subClass : EMFUtils.getSubClasses(superType)) {
			if (!subClass.isAbstract() && !subClass.isInterface()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Set<Template> provide(EClass superType) {
		final LinkedHashSet<Template> result = new LinkedHashSet<Template>();
		final Collection<EClass> subClasses = EMFUtils.getSubClasses(superType);
		for (final EClass subClass : subClasses) {
			if (!subClass.isAbstract() && !subClass.isInterface()) {
				result.add(createTemplate(subClass));
			}
		}

		return result;
	}

	/**
	 * Creates a blank template for the given type.
	 *
	 * @param type The {@link EClass} specifying the type
	 * @return The blank {@link Template}
	 */
	protected Template createTemplate(EClass type) {
		final EObject instance = EcoreUtil.create(type);
		final Template template = DataTemplateFactory.eINSTANCE.createTemplate();
		template.setInstance(instance);

		final String nameTemplate = localizationService.getString(BlankTemplateProvider.class,
			MessageKeys.BlankTemplateProvider_blankTemplateLabel);
		template.setName(NLS.bind(nameTemplate, getDisplayName(type)));

		return template;
	}

	/**
	 * Set the {@link BundleResolver}.
	 *
	 * @param bundleResolver The {@link BundleResolver}
	 */
	protected void setBundleResolver(BundleResolver bundleResolver) {
		this.bundleResolver = bundleResolver;
	}

	/**
	 * Fetch the display name for the given {@link EClass}.
	 *
	 * @param type the {@link EClass} to get the display name for
	 * @return the display name
	 */
	protected String getDisplayName(EClass type) {
		try {
			final Bundle editBundle = bundleResolver.getEditBundle(type);
			return localizationService.getString(editBundle, String.format("_UI_%s_type", type.getName())); //$NON-NLS-1$
		} catch (final NoBundleFoundException ex) {
			// Do nothing, the fall back of using the type's name will be used
		}

		return type.getName();

	}
}
