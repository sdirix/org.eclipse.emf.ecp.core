/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 * Edgar Mueller - adoptions in order to avoid memory leaks
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * Validation service that, once instantiated, synchronizes the validation result of a model element with its
 * Renderable.
 * 
 * @author jfaltermeier
 * @author emueller
 */
public class ValidationService extends AbstractViewService {

	private ViewModelContext context;
	private Renderable renderable;

	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private ViewValidator viewValidationGraph;
	private ValidationRegistry validationRegistry;

	private final List<ViewValidationListener> validationListener;

	/**
	 * Default constructor.
	 */
	public ValidationService() {
		validationListener = new ArrayList<ViewValidationListener>();
	}

	/**
	 * The {@link ModelChangeListener} for the view model.
	 * 
	 */
	class ViewModelChangeListener implements ModelChangeListener {

		private final EObject domainModel;

		/**
		 * Constructor.
		 * 
		 * @param domainModel
		 *            the domain model
		 */
		public ViewModelChangeListener(EObject domainModel) {
			this.domainModel = domainModel;
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.context.ModelChangeNotification)
		 */
		public void notifyChange(ModelChangeNotification notification) {
			// do nothing for now, not supported
			if (Renderable.class.isInstance(notification.getRawNotification().getNotifier())) {
				if (notification.getRawNotification().getFeature() == ViewPackage.eINSTANCE
					.getRenderable_Diagnostic()) {

				}
				else if (EReference.class.isInstance(notification.getRawNotification().getFeature())
					&& Renderable.class.isInstance(notification.getRawNotification().getNewValue())) {
				}
				else if (ViewPackage.eINSTANCE.getRenderable_Enabled() == notification.getRawNotification()
					.getFeature()
					|| ViewPackage.eINSTANCE.getRenderable_Visible() == notification.getRawNotification()
						.getFeature()) {
					if (ViewPackage.eINSTANCE.getControl() == notification.getNotifier().eClass()) {
						final Control control = (Control) notification.getNotifier();
						// final EObject controlDomainModel = validationRegistry.resolveDomainModel(domainModel,
						// control.getDomainModelReference().());
						// REFACTORING test
						final Iterator<Setting> settings = control.getDomainModelReference().getIterator();
						while (settings.hasNext()) {
							viewValidationGraph.validate(settings.next().getEObject());
						}
					}
				}
			}
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyAdd(org.eclipse.emf.common.notify.Notifier)
		 */
		public void notifyAdd(Notifier notifier) {
			if (Renderable.class.isInstance(notifier)) {
				final Renderable renderable = (Renderable) notifier;
				final EObject renderableParent = renderable.eContainer();
				if (Renderable.class.isInstance(renderableParent)
					&& validationRegistry.containsRenderable((Renderable) renderableParent)) {
					validationRegistry.register(domainModel, renderable);
					final Map<EObject, Set<AbstractControl>> map = validationRegistry.getDomainToControlMapping(
						domainModel,
						renderable);
					viewValidationGraph.validate(map.keySet());
				}
			}
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyRemove(org.eclipse.emf.common.notify.Notifier)
		 */
		public void notifyRemove(Notifier notifier) {
			if (Renderable.class.isInstance(notifier)) {
				validationRegistry.removeRenderable((Renderable) notifier);
				viewValidationGraph.removeRenderable((Renderable) notifier);
			}
		}
	}

	/**
	 * The {@link ModelChangeListener} for the domain model.
	 * 
	 */
	class DomainModelChangeListener implements ModelChangeListener {

		private final EObject domainModel;

		/**
		 * Constructor.
		 * 
		 * @param domainModel
		 *            the domain model
		 */
		public DomainModelChangeListener(EObject domainModel) {
			this.domainModel = domainModel;
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.context.ModelChangeNotification)
		 */
		public void notifyChange(ModelChangeNotification notification) {
			if (viewValidationGraph == null) {
				return; // ignore notifications during initialization
			}
			final Notification rawNotification = notification.getRawNotification();
			switch (rawNotification.getEventType()) {
			case Notification.ADD:
			case Notification.REMOVE:
				viewValidationGraph.validate(getAllEObjects(notification.getNotifier()));
				break;
			case Notification.ADD_MANY:
			case Notification.REMOVE_MANY:
				viewValidationGraph.validate(getAllEObjects(domainModel));

				break;
			default:
				viewValidationGraph.validate(getAllEObjects(notification.getNotifier()));
			}
			notifyListeners();
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyAdd(org.eclipse.emf.common.notify.Notifier)
		 */
		public void notifyAdd(Notifier notifier) {
			// maybe null while init
			if (viewValidationGraph != null) {
				viewValidationGraph.validate((EObject) notifier);
			}
		}

		/**
		 * 
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyRemove(org.eclipse.emf.common.notify.Notifier)
		 */
		public void notifyRemove(Notifier notifier) {
			final EObject eObject = (EObject) notifier;
			viewValidationGraph.removeDomainObject(eObject);
			validationRegistry.removeDomainObject(eObject);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(final ViewModelContext context) {
		this.context = context;

		renderable = context.getViewModel();

		if (renderable == null) {
			throw new IllegalStateException("View model must not be null");
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null");
		}

		domainChangeListener = new DomainModelChangeListener(domainModel);
		viewChangeListener = new ViewModelChangeListener(domainModel);

		context.registerDomainChangeListener(domainChangeListener);
		context.registerViewChangeListener(viewChangeListener);

		init(renderable, domainModel);

		viewValidationGraph.validate(getAllEObjects(domainModel));

		notifyListeners();
	}

	private void init(Renderable view, EObject domainModel) {
		validationRegistry = new ValidationRegistry();
		validationRegistry.register(domainModel, view);
		viewValidationGraph = new ViewValidator(view, domainModel, validationRegistry);
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
	 * Registers a listener that will receive {@link Diagnostic}s with severity higher than {@link Diagnostic#OK}. After
	 * registration the listener's {@link ViewValidationListener#onNewValidation(Set)} will be called with current
	 * results.
	 * 
	 * @param listener the listener to register
	 */
	public void registerValidationListener(ViewValidationListener listener) {
		validationListener.add(listener);
		listener.onNewValidation(getDiagnosticResult());
	}

	/**
	 * Deregisters the given listener.
	 * 
	 * @param listener the listener to deregister
	 */
	public void deregisterValidationListener(ViewValidationListener listener) {
		validationListener.remove(listener);
	}

	private void notifyListeners() {
		if (validationListener.size() > 0) {
			final Set<Diagnostic> result = getDiagnosticResult();
			for (final ViewValidationListener l : validationListener) {
				l.onNewValidation(result);
			}

		}
	}

	private Set<Diagnostic> getDiagnosticResult() {
		final Set<Diagnostic> result = new HashSet<Diagnostic>();
		if (renderable.getDiagnostic().getHighestSeverity() > Diagnostic.OK) {
			for (final Object o : renderable.getDiagnostic().getDiagnostics()) {
				if (((Diagnostic) o).getSeverity() > Diagnostic.OK) {
					result.add((Diagnostic) o);
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#dispose()
	 */
	@Override
	public void dispose() {
		context.unregisterDomainChangeListener(domainChangeListener);
		context.unregisterViewChangeListener(viewChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#getPriority()
	 */
	@Override
	public int getPriority() {
		return 3;
	}

}
