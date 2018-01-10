/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * lucas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.datatemplate;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.spi.common.ui.SelectModelElementWizardFactory;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy.Provider;
import org.eclipse.emf.ecp.ui.view.swt.reference.ReferenceServiceCustomizationVendor;
import org.eclipse.emfforms.bazaar.Create;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.core.services.datatemplate.TemplateProvider;
import org.eclipse.emfforms.datatemplate.Template;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * Provides a strategy to the {@link org.eclipse.emf.ecp.ui.view.swt.DefaultReferenceService DefaultReferenceService}
 * that allows creating new model elements based on a template with pre-defined values.
 *
 * @author Lucas Koehler
 *
 */
// Ranking as it was for the TemplateReferenceService
@Component(name = "TemplateCreateNewModelElementStrategyProvider", property = "service.ranking:Integer=10")
public class TemplateCreateNewModelElementStrategyProvider
	extends ReferenceServiceCustomizationVendor<CreateNewModelElementStrategy> implements Provider {
	private final Set<TemplateProvider> templateProviders = new LinkedHashSet<TemplateProvider>();

	/**
	 * Register a template provider implementation.
	 *
	 * @param templateProvider the {@link TemplateProvider} to add
	 */
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	void registerTemplateProvider(TemplateProvider templateProvider) {
		templateProviders.add(templateProvider);
	}

	/**
	 * Unregister a template provider registration.
	 *
	 * @param templateProvider the {@link TemplateProvider} to remove
	 */
	void unregisterTemplateProvider(TemplateProvider templateProvider) {
		templateProviders.remove(templateProvider);
	}

	@Override
	protected boolean handles(EObject owner, EReference reference) {
		return collectAvailableTemplates(owner, reference).size() > 0;
	}

	/**
	 * Collect a list of available templates for the given {@link EReference}.
	 *
	 * @param eObject the parent {@link EObject}
	 * @param eReference the {@link EReference} to find templates for
	 * @return list of available templates
	 */
	private Set<Template> collectAvailableTemplates(EObject eObject, EReference eReference) {
		final Set<Template> templates = new LinkedHashSet<Template>();
		for (final TemplateProvider provider : templateProviders) {
			if (!provider.canProvide(eReference.getEReferenceType())) {
				continue;
			}
			templates.addAll(provider.provide(eReference.getEReferenceType()));

		}
		return templates;
	}

	/**
	 * Creates the {@link CreateNewModelElementStrategy}.
	 *
	 * @return The created {@link CreateNewModelElementStrategy}
	 */
	@Create
	public CreateNewModelElementStrategy createCreateNewModelElementStrategy() {
		return new Strategy();
	}

	/**
	 * The actual {@link CreateNewModelElementStrategy strategy} that creates a new element based on a template selected
	 * by the user.
	 *
	 * @author Lucas Koehler
	 */
	private class Strategy implements CreateNewModelElementStrategy {
		/**
		 * {@inheritDoc}
		 * <p>
		 * Create the new model element based on a template.
		 */
		@Override
		public Optional<EObject> createNewModelElement(EObject owner, EReference reference) {
			final Set<Template> availableTemplates = collectAvailableTemplates(owner, reference);
			if (availableTemplates.isEmpty()) {
				// This should not happen because in case of no available templates, the provider should not bid during
				// the strategy selection
				return Optional.empty();
			}

			Template selected = availableTemplates.iterator().next();
			if (availableTemplates.size() > 1) {
				final Set<Template> selectedElements = showSelectModelInstancesDialog(availableTemplates);

				if (selectedElements == null || selectedElements.isEmpty()) {
					return Optional.empty();
				}
				selected = selectedElements.iterator().next();
			}

			return Optional.of(EcoreUtil.copy(selected.getInstance()));
		}

		/**
		 * Show a model instance selection dialog.
		 *
		 * @param availableInstances the instances to choose from
		 * @return the instances selected by the user
		 */
		protected <T extends EObject> Set<T> showSelectModelInstancesDialog(
			Set<T> availableInstances) {
			return SelectModelElementWizardFactory.openModelElementSelectionDialog(availableInstances, false);
		}
	}
}
