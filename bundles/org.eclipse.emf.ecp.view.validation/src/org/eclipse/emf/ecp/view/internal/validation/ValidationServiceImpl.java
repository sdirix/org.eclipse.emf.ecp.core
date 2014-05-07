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
package org.eclipse.emf.ecp.view.internal.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.UniqueSetting;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.DomainModelChangeNotifier.DomainModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDiagnostic;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Validation service that, once instantiated, synchronizes the validation result of a model element with its
 * Renderable.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ValidationServiceImpl implements ValidationService {

	/**
	 * The {@link ValidationDomainModelChangeListener} for the view model.
	 * 
	 */
	private class ViewModelChangeListener implements ModelChangeListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification)
		 */
		@Override
		public void notifyChange(ModelChangeNotification notification) {
			if (VViewPackage.eINSTANCE.getElement_Enabled() == notification.getRawNotification()
				.getFeature()
				|| VViewPackage.eINSTANCE.getElement_Visible() == notification.getRawNotification()
					.getFeature()) {
				if (VViewPackage.eINSTANCE.getControl().isInstance(notification.getNotifier())) {
					final VControl control = (VControl) notification.getNotifier();
					// final EObject controlDomainModel = validationRegistry.resolveDomainModel(domainModel,
					// control.getDomainModelReference().());
					// REFACTORING test
					final VDomainModelReference domainModelReference = control.getDomainModelReference();
					if (domainModelReference == null) {
						return;
					}
					final Iterator<Setting> settings = domainModelReference.getIterator();
					final Set<EObject> eObjectsToValidate = new LinkedHashSet<EObject>();
					while (settings.hasNext()) {
						eObjectsToValidate.add(settings.next().getEObject());
					}
					for (final EObject eObject : eObjectsToValidate) {
						validate(eObject);
					}
				}
			}
			switch (notification.getRawNotification().getEventType()) {
			case Notification.REMOVE:
			case Notification.REMOVE_MANY:
				reevaluateToTop(notification.getNotifier());
				break;
			default:
				break;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyAdd(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		public void notifyAdd(Notifier notifier) {
			if (VControl.class.isInstance(notifier)) {
				final VDomainModelReference domainModelReference = VControl.class.cast(notifier)
					.getDomainModelReference();
				if (domainModelReference == null) {
					return;
				}
				final Iterator<Setting> settings = domainModelReference.getIterator();
				final Set<EObject> eObjectsToValidate = new LinkedHashSet<EObject>();
				while (settings.hasNext()) {
					eObjectsToValidate.add(settings.next().getEObject());
				}
				for (final EObject eObject : eObjectsToValidate) {
					validate(eObject);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyRemove(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		public void notifyRemove(Notifier notifier) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * The {@link ValidationDomainModelChangeListener} for the domain model.
	 * 
	 */
	private class ValidationDomainModelChangeListener implements DomainModelChangeListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification)
		 */
		@Override
		public void notifyChange(ModelChangeNotification notification) {
			if (ValidationNotification.class.isInstance(notification.getRawNotification())) {
				validate(notification.getNotifier());
				return;
			}
			final Notification rawNotification = notification.getRawNotification();
			switch (rawNotification.getEventType()) {
			case Notification.ADD:
			case Notification.ADD_MANY:
				validate(getAllEObjects(notification.getNotifier()));
				break;
			case Notification.REMOVE:
				if (EReference.class.isInstance(rawNotification.getFeature())) {
					cleanControlDiagnostics(EObject.class.cast(notification.getNotifier()),
						EReference.class.cast(rawNotification.getFeature()),
						EObject.class.cast(rawNotification.getOldValue()));
				}
				//$FALL-THROUGH$
			case Notification.REMOVE_MANY:
				validate(getAllEObjects(notification.getNotifier()));

				break;
			case Notification.REMOVING_ADAPTER:
				break;
			default:
				validate(getAllEObjects(notification.getNotifier()));
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyAdd(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		public void notifyAdd(Notifier notifier) {
			// do nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext.DomainModelChangeListener#notifyRemove(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		public void notifyRemove(Notifier notifier) {
			// do nothing
		}

	}

	private final Set<ValidationProvider> validationProviders = new LinkedHashSet<ValidationProvider>();
	private ValidationDomainModelChangeListener domainChangeListener;
	private ViewModelChangeListener viewChangeListener;
	private ViewModelContext context;
	private final Queue<EObject> validationQueue = new LinkedList<EObject>();
	private final Set<EObject> validated = new LinkedHashSet<EObject>();
	private boolean validationRunning;
	private final Map<UniqueSetting, VDiagnostic> currentUpdates = new LinkedHashMap<UniqueSetting, VDiagnostic>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#instantiate(org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;
		final VElement renderable = context.getViewModel();

		if (renderable == null) {
			throw new IllegalStateException("View model must not be null"); //$NON-NLS-1$
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null"); //$NON-NLS-1$
		}

		readValidationProvider();

		domainChangeListener = new ValidationDomainModelChangeListener();
		viewChangeListener = new ViewModelChangeListener();

		context.registerDomainChangeListener(domainChangeListener);
		context.registerViewChangeListener(viewChangeListener);

		validate(getAllEObjects(domainModel));
	}

	private void cleanControlDiagnostics(EObject parent, EReference parentReference, EObject removedEObject) {
		final Set<VControl> controls = context.getControlsFor(UniqueSetting.createSetting(parent, parentReference));
		if (controls == null) {
			return;
		}
		for (final VControl vControl : controls) {
			if (vControl == null) {
				continue;
			}
			if (vControl.getDiagnostic() == null) {
				continue;
			}
			final Set<Object> diagnosticsToRemove = new LinkedHashSet<Object>();
			for (final Object diagnosticObject : vControl.getDiagnostic().getDiagnostics()) {
				final Diagnostic diagnostic = Diagnostic.class.cast(diagnosticObject);
				if (diagnostic.getData().size() < 1) {
					continue;
				}
				if (removedEObject.equals(diagnostic.getData().get(0))) {
					diagnosticsToRemove.add(diagnostic);
				}
			}
			vControl.getDiagnostic().getDiagnostics().removeAll(diagnosticsToRemove);
		}
	}

	private void readValidationProvider() {
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] controls = extensionRegistry
			.getConfigurationElementsFor("org.eclipse.emf.ecp.view.validation.validationProvider"); //$NON-NLS-1$
		for (final IConfigurationElement e : controls) {
			try {
				final ValidationProvider validationProvider = (ValidationProvider) e.createExecutableExtension("class"); //$NON-NLS-1$
				validationProviders.add(validationProvider);
			} catch (final CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#dispose()
	 */
	@Override
	public void dispose() {
		context.unregisterDomainChangeListener(domainChangeListener);
		context.unregisterViewChangeListener(viewChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#getPriority()
	 */
	@Override
	public int getPriority() {
		return 3;
	}

	/**
	 * Returns a collection of all direct and indirect child-EObjects including the parent.
	 * 
	 * @param eObject the parent
	 * @return all eobjects
	 */
	private Collection<EObject> getAllEObjects(EObject eObject) {
		final List<EObject> result = new ArrayList<EObject>();
		result.add(eObject);
		final TreeIterator<EObject> iterator = EcoreUtil.getAllContents(eObject, false);
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.validation.ValidationService#validate(java.util.Collection)
	 */
	@Override
	public void validate(Collection<EObject> eObjects) {

		EObject firstObject = null;
		for (final EObject eObject : eObjects) {
			if (firstObject == null) {
				firstObject = eObject;
			} else {
				validationQueue.offer(eObject);
			}
		}
		if (firstObject != null) {
			validate(firstObject);
		}

	}

	/**
	 * Validate the given eObject.
	 * 
	 * @param eObject the eObject to validate
	 */
	public void validate(EObject eObject) {
		if (validationRunning) {
			validationQueue.offer(eObject);
		} else {
			validationRunning = true;
			validated.add(eObject);
			validateAndCollectSettings(eObject);

			EObject toValidate = validationQueue.poll();
			while (toValidate != null) {
				if (!validated.contains(toValidate)) {
					validated.add(toValidate);
					validateAndCollectSettings(toValidate);
				}
				toValidate = validationQueue.poll();
			}
			update();
			currentUpdates.clear();
			validated.clear();
			notifyListeners();
			validationRunning = false;
		}

	}

	private void notifyListeners() {
		if (validationListener.size() > 0) {
			final Set<Diagnostic> result = getDiagnosticResult();
			for (final ViewValidationListener l : validationListener) {
				l.onNewValidation(result);
			}

		}
	}

	private void update() {
		final Map<VControl, VDiagnostic> controlDiagnosticMap = new LinkedHashMap<VControl, VDiagnostic>();
		for (final UniqueSetting uniqueSetting : currentUpdates.keySet()) {
			final Set<VControl> controls = context.getControlsFor(uniqueSetting);
			if (controls == null) {
				continue;
			}
			for (final VControl control : controls) {
				if (!controlDiagnosticMap.containsKey(control)) {
					controlDiagnosticMap.put(control, VViewFactory.eINSTANCE.createDiagnostic());
				}
				if (!control.isEnabled() || !control.isVisible() || control.isReadonly()) {
					continue;
				}
				controlDiagnosticMap.get(control).getDiagnostics()
					.addAll(currentUpdates.get(uniqueSetting).getDiagnostics());

				// add all diagnostics of control which are not in the currentUpdates
				if (control.getDiagnostic() == null) {
					continue;
				}

				for (final Object diagnosticObject : control.getDiagnostic().getDiagnostics()) {
					final Diagnostic diagnostic = Diagnostic.class.cast(diagnosticObject);
					if (diagnostic.getData().size() < 2 || !EObject.class.isInstance(diagnostic.getData().get(0))
						|| !EStructuralFeature.class.isInstance(diagnostic.getData().get(1))) {
						continue;
					}
					final UniqueSetting uniqueSetting2 = UniqueSetting.createSetting(
						EObject.class.cast(diagnostic.getData().get(0)),
						EStructuralFeature.class.cast(diagnostic.getData().get(1)));
					if (!currentUpdates.containsKey(uniqueSetting2)) {
						controlDiagnosticMap.get(control).getDiagnostics().add(diagnosticObject);
					}

				}

			}
		}

		updateAndPropagate(controlDiagnosticMap);
	}

	private void updateAndPropagate(Map<VControl, VDiagnostic> controlDiagnosticMap) {
		for (final VControl control : controlDiagnosticMap.keySet()) {

			// control.setDiagnostic(VDiagnosticHelper.clean2(controlDiagnosticMap.get(control)));
			control.setDiagnostic(controlDiagnosticMap.get(control));

			reevaluateToTop(control.eContainer());
		}
	}

	private void reevaluateToTop(EObject parent) {

		while (parent != null) {
			final EObject newParent = parent.eContainer();
			if (!VElement.class.isInstance(parent)) {
				parent = newParent;
				continue;
			}
			final VElement vElement = (VElement) parent;

			final VDiagnostic vDiagnostic = VViewFactory.eINSTANCE.createDiagnostic();

			for (final EObject eObject : vElement.eContents()) {
				if (!VElement.class.isInstance(eObject)) {
					continue;
				}
				final VElement childElement = (VElement) eObject;
				if (childElement.getDiagnostic() != null) {
					vDiagnostic.getDiagnostics().addAll(childElement.getDiagnostic().getDiagnostics());
				}
			}
			vElement.setDiagnostic(vDiagnostic);
			parent = newParent;
		}
	}

	private void validateAndCollectSettings(EObject eObject) {
		final Diagnostic diagnostic = getDiagnosticForEObject(eObject);
		// unset everything for the new EObject
		for (final EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(eObject, feature);
			if (!currentUpdates.containsKey(uniqueSetting)) {
				currentUpdates.put(uniqueSetting, VViewFactory.eINSTANCE.createDiagnostic());
			}
		}
		analyzeDiagnostic(diagnostic);

	}

	private void analyzeDiagnostic(Diagnostic diagnostic) {
		if (diagnostic.getData().size() > 1) {
			if (InternalEObject.class.isInstance(diagnostic.getData().get(0))
				&& EStructuralFeature.class.isInstance(diagnostic.getData().get(1))) {
				final InternalEObject internalEObject = (InternalEObject) diagnostic.getData().get(0);
				final EStructuralFeature eStructuralFeature = (EStructuralFeature) diagnostic.getData().get(1);
				final Setting setting = internalEObject.eSetting(eStructuralFeature);
				final UniqueSetting uniqueSetting = UniqueSetting.createSetting(setting);
				if (!currentUpdates.containsKey(uniqueSetting)) {
					currentUpdates.put(uniqueSetting, VViewFactory.eINSTANCE.createDiagnostic());
				}
				currentUpdates.get(uniqueSetting).getDiagnostics().add(diagnostic);
			}

		}
		else {
			for (final Diagnostic childDiagnostic : diagnostic.getChildren()) {
				analyzeDiagnostic(childDiagnostic);
			}
		}
	}

	/**
	 * Computes the {@link Diagnostic} for the given eObject.
	 * 
	 * @param object the eObject to validate
	 * @return the diagnostic
	 */
	public Diagnostic getDiagnosticForEObject(EObject object) {
		EValidator validator = EValidator.Registry.INSTANCE.getEValidator(object.eClass().getEPackage());
		final BasicDiagnostic diagnostics = Diagnostician.INSTANCE.createDefaultDiagnostic(object);

		if (validator == null) {
			validator = new EObjectValidator();
		}
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final Map<Object, Object> context = new LinkedHashMap<Object, Object>();
		context.put(EValidator.SubstitutionLabelProvider.class, new ECPSubstitutionLabelProvider(adapterFactory));
		context.put(EValidator.class, validator);

		validator.validate(object, diagnostics, context);

		adapterFactory.dispose();

		final Map<EStructuralFeature, DiagnosticChain> diagnosticMap = new LinkedHashMap<EStructuralFeature, DiagnosticChain>();
		for (final Diagnostic child : diagnostics.getChildren()) {
			if (DiagnosticChain.class.isInstance(child)) {
				diagnosticMap.put((EStructuralFeature) child.getData().get(1), (DiagnosticChain) child);
			}
		}

		for (final ValidationProvider validationProvider : validationProviders) {
			final List<Diagnostic> additionValidation = validationProvider.validate(object);
			for (final Diagnostic additionDiagnostic : additionValidation) {
				if (diagnosticMap.containsKey(additionDiagnostic.getData().get(1))) {
					diagnosticMap.get(additionDiagnostic.getData().get(1)).add(additionDiagnostic);
				} else {
					diagnostics.add(additionDiagnostic);
				}

			}
		}
		return diagnostics;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.validation.ValidationService#addValidationProvider(org.eclipse.emf.ecp.view.internal.validation.ValidationProvider)
	 */
	@Override
	public void addValidationProvider(ValidationProvider validationProvider) {
		validationProviders.add(validationProvider);
		validate(getAllEObjects(context.getDomainModel()));
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.validation.ValidationService#removeValidationProvider(org.eclipse.emf.ecp.view.internal.validation.ValidationProvider)
	 */
	@Override
	public void removeValidationProvider(ValidationProvider validationProvider) {
		validationProviders.remove(validationProvider);
		validate(getAllEObjects(context.getDomainModel()));
	}

	private final Set<ViewValidationListener> validationListener = new LinkedHashSet<ViewValidationListener>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.validation.ValidationService#registerValidationListener(org.eclipse.emf.ecp.view.internal.validation.ViewValidationListener)
	 */
	@Override
	public void registerValidationListener(ViewValidationListener listener) {
		validationListener.add(listener);

		listener.onNewValidation(getDiagnosticResult());
	}

	private Set<Diagnostic> getDiagnosticResult() {
		final Set<Diagnostic> result = new LinkedHashSet<Diagnostic>();
		final VDiagnostic diagnostic = context.getViewModel().getDiagnostic();
		if (diagnostic != null) {
			for (final Object diagObject : diagnostic.getDiagnostics()) {
				result.add((Diagnostic) diagObject);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.validation.ValidationService#deregisterValidationListener(org.eclipse.emf.ecp.view.internal.validation.ViewValidationListener)
	 */
	@Override
	public void deregisterValidationListener(ViewValidationListener listener) {
		validationListener.add(listener);
		listener.onNewValidation(getDiagnosticResult());
	}

}
