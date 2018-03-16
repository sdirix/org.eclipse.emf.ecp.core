/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * Christian W. Damus - bug 529138
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.reference;

import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.spi.EMFUtils;
import org.eclipse.emf.ecp.spi.common.ui.CompositeFactory;
import org.eclipse.emf.ecp.spi.common.ui.SelectModelElementWizardFactory;
import org.eclipse.emf.ecp.spi.common.ui.composites.SelectionComposite;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy.Provider;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emfforms.bazaar.Bazaar;
import org.eclipse.emfforms.bazaar.BazaarContext;
import org.eclipse.emfforms.bazaar.Create;
import org.eclipse.emfforms.bazaar.StaticBid;
import org.eclipse.emfforms.bazaar.Vendor;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Default strategy that allows creating a new model element based on the sub classes of the reference type. If
 * there is more than one, a selection dialog is shown.
 * This implementation utilizes the {@link EClassSelectionStrategy} to filter the EClass to offer.
 *
 * @author Eugen Neufeld
 * @since 1.17
 *
 */
@Component(name = "DefaultCreateNewModelElementStrategyProvider", property = "service.ranking:Integer=1")
public class DefaultCreateNewModelElementStrategyProvider
	extends ReferenceServiceCustomizationVendor<CreateNewModelElementStrategy> implements Provider {
	private static <T> Bazaar<T> createBazaar(T defaultStrategy) {
		final Bazaar.Builder<T> builder = builder(defaultStrategy);
		return builder.build();
	}

	private static <T> Bazaar.Builder<T> builder(final T defaultStrategy) {
		/**
		 * A vendor of the default, that tries to lose every bid.
		 */
		@StaticBid(bid = Double.NEGATIVE_INFINITY)
		class DefaultVendor implements Vendor<T> {
			/**
			 * Return the default strategy.
			 *
			 * @return the default strategy
			 */
			@Create
			public T createDefault() {
				return defaultStrategy;
			}
		}

		final Bazaar.Builder<T> result = Bazaar.Builder.empty();
		return result.threadSafe().add(new DefaultVendor());
	}

	private final Bazaar<EClassSelectionStrategy> eclassSelectionStrategyBazaar = createBazaar(
		EClassSelectionStrategy.NULL);
	private ComponentContext context;
	private ReportService reportService;

	/**
	 * Add an {@code EClass} selection strategy provider.
	 *
	 * @param provider the provider to add
	 */
	@Reference(cardinality = MULTIPLE, policy = DYNAMIC)
	public void addEClassSelectionStrategyProvider(EClassSelectionStrategy.Provider provider) {
		eclassSelectionStrategyBazaar.addVendor(provider);
	}

	/**
	 * Remove an {@code EClass} selection strategy provider.
	 *
	 * @param provider the provider to remove
	 */
	void removeEClassSelectionStrategyProvider(EClassSelectionStrategy.Provider provider) {
		eclassSelectionStrategyBazaar.removeVendor(provider);
	}

	/**
	 * The {@link ReportService} to use for errors
	 *
	 * @param reportService The {@link ReportService}
	 */
	@Reference
	void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Activates me.
	 *
	 * @param context my component context
	 */
	@Activate
	void activate(ComponentContext context) {
		this.context = context;
	}

	/**
	 * Deactivates me.
	 */
	@Deactivate
	void deactivate() {
		context = null;
	}

	private BazaarContext getBazaarContext(EObject owner, EReference reference) {
		return baseContext(owner, reference).build();
	}

	private BazaarContext.Builder baseContext(EObject owner, EReference reference) {
		return BazaarContext.Builder.with(adapt(context.getProperties()))
			.put(EObject.class, owner)
			.put(EReference.class, reference);
	}

	/**
	 * Adapt a {@code dictionary} as a map.
	 *
	 * @param dictionary a dictionary
	 * @return the {@code dictionary}, as a map
	 */
	static Map<String, ?> adapt(Dictionary<String, ?> dictionary) {
		// The OSGi implementation of the read-only properties for all sorts of
		// things is a map
		if (dictionary instanceof Map<?, ?>) {
			@SuppressWarnings("unchecked")
			final Map<String, ?> result = (Map<String, ?>) dictionary;
			return result;
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		for (final Enumeration<String> keys = dictionary.keys(); keys.hasMoreElements();) {
			final String next = keys.nextElement();
			result.put(next, dictionary.get(next));
		}
		return result;
	}

	@Override
	protected boolean handles(EObject owner, EReference reference) {
		return true;
	}

	private EClassSelectionStrategy createDynamicEClassSelectionStrategy() {
		return new EClassSelectionStrategy() {

			@Override
			public Collection<EClass> collectEClasses(EObject owner, EReference reference,
				Collection<EClass> eclasses) {

				Collection<EClass> result = eclasses;

				final List<EClassSelectionStrategy> delegates = eclassSelectionStrategyBazaar.createProducts(
					getBazaarContext(owner, reference));
				for (final EClassSelectionStrategy next : delegates) {
					result = next.collectEClasses(owner, reference, result);
				}

				return result;
			}
		};
	}

	/**
	 * Creates the {@link CreateNewModelElementStrategy}.
	 *
	 * @return The created {@link CreateNewModelElementStrategy}
	 */
	@Create
	public CreateNewModelElementStrategy createCreateNewModelElementStrategy() {
		final EClassSelectionStrategy classSelectionStrategy = createDynamicEClassSelectionStrategy();
		return new DefaultStrategy(classSelectionStrategy);
	}

	//
	// Nested types
	//

	/**
	 * The default creation strategy implementation.
	 */
	private class DefaultStrategy implements CreateNewModelElementStrategy {
		private final EClassSelectionStrategy classSelectionStrategy;

		DefaultStrategy(final EClassSelectionStrategy classSelectionStrategy) {
			super();

			this.classSelectionStrategy = classSelectionStrategy;
		}

		@Override
		public Optional<EObject> createNewModelElement(EObject owner, EReference reference) {
			// Ask the edit provider for available children. If it provides none (which is
			// usually the case for cross-reference features, not containments) then fall back
			// to the release 1.16 behaviour of just finding all subclasses of the reference type
			final Map<EClass, EObject> availableChildren = getNewObjectsByDescriptors(owner, reference);
			final Collection<EClass> availableClasses = availableChildren.isEmpty()
				? EMFUtils.getSubClasses(reference.getEReferenceType())
				: availableChildren.keySet();

			final Collection<EClass> classes = classSelectionStrategy.collectEClasses(owner, reference,
				availableClasses);
			if (classes.isEmpty()) {
				final String errorMessage = String.format("No concrete classes for the type %1$s were found!", //$NON-NLS-1$
					reference.getEReferenceType().getName());
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", //$NON-NLS-1$
					errorMessage);
				reportService.report(new AbstractReport(errorMessage));
				return Optional.empty();
			}
			if (classes.size() == 1) {
				final EClass only = classes.iterator().next();
				EObject result = availableChildren.get(only);
				if (result == null) {
					// Create one in the release 1.16 way
					result = EcoreUtil.create(only);
				}
				return Optional.of(result);
			}
			return Optional.ofNullable(getModelElementInstanceFromList(classes, availableChildren));
		}

		/**
		 * Getting a mapping of new objects provided by an {@code owner}'s edit provider, by class.
		 *
		 * @param owner the owner of a reference in which to create an object
		 * @param reference the reference in which to create an object
		 * @return a mapping of edit-provider supplied possible children
		 */
		private Map<EClass, EObject> getNewObjectsByDescriptors(EObject owner, EReference reference) {
			final Collection<CommandParameter> descriptors = getNewChildDescriptors(owner, reference);
			if (descriptors.isEmpty()) {
				return Collections.emptyMap();
			}

			final Map<EClass, EObject> result = new LinkedHashMap<EClass, EObject>();
			for (final CommandParameter next : descriptors) {
				final EObject object = next.getEValue();
				result.put(object.eClass(), object);
			}
			return result;
		}

		/**
		 * Obtain new child descriptors from the {@code owner}'s edit provider.
		 *
		 * @param owner the owner of a reference in which to create an object
		 * @param reference the reference in which to create an object
		 * @return the new child descriptors
		 */
		private Collection<CommandParameter> getNewChildDescriptors(EObject owner, EReference reference) {
			final EClass referenceType = reference.getEReferenceType();

			final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(owner);
			if (!(domain instanceof AdapterFactoryEditingDomain)) {
				return Collections.emptyList();
			}

			final AdapterFactory factory = ((AdapterFactoryEditingDomain) domain).getAdapterFactory();
			if (factory == null) {
				return Collections.emptyList();
			}

			final IEditingDomainItemProvider itemProvider = (IEditingDomainItemProvider) factory.adapt(owner,
				IEditingDomainItemProvider.class);
			if (itemProvider == null) {
				return Collections.emptyList();
			}

			// The item provider offers new child descriptors for probably multiple containment references.
			// We want only those compatible with our reference
			final Collection<?> descriptors = itemProvider.getNewChildDescriptors(owner, domain, null);
			final Collection<CommandParameter> result = new ArrayList<CommandParameter>(descriptors.size());
			for (final Object next : descriptors) {
				if (next instanceof CommandParameter) {
					final CommandParameter newChild = (CommandParameter) next;
					if (referenceType.isInstance(newChild.getEValue())) {
						result.add(newChild);
					}
				}
			}

			return result;
		}

		/**
		 * Prompt the user to select one of the given {@code classes} to instantiate. If the
		 * available children includes an instance of the selected class, then use it, otherwise
		 * create a new unconfigured instance.
		 *
		 * @param classes the classes to chose from
		 * @param availableChildren available instances of some intersection of those {@code classes}
		 *
		 * @return an instance of the class selected by the user, or {@code null} if the user cancelled
		 *         the prompt
		 */
		private EObject getModelElementInstanceFromList(Collection<EClass> classes,
			Map<EClass, EObject> availableChildren) {

			final SelectionComposite<TreeViewer> helper = CompositeFactory.getSelectModelClassComposite(
				new HashSet<EPackage>(),
				new HashSet<EPackage>(), classes);

			EObject result = SelectModelElementWizardFactory.openCreateNewModelElementDialog(helper);
			if (result != null) {
				final EObject fromAvailable = availableChildren.get(result.eClass());
				if (fromAvailable != null) {
					// Use the object provided by the edit provider, instead, as it may be
					// pre-configured in some important way
					result = fromAvailable;
				}
			}
			return result;
		}
	}
}
