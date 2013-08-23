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
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.cachetree.IExcludedObjectsCallback;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;

/**
 * Validation service that, once instantiated, synchronizes the validation result of a model element with its
 * Renderable.
 * 
 * @author jfaltermeier
 * 
 */
public class ValidationService extends AbstractViewService {

	private ViewModelContext context;
	private View view;

	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private ViewValidationCachedTree viewValidationCachedTree;
	private ValidationRegistry validationRegistry;

	private final LinkedHashSet<ViewValidationListener> validationListener;

	/**
	 * Default constructor.
	 */
	public ValidationService() {
		validationListener = new LinkedHashSet<ViewValidationListener>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;

		view = context.getViewModel();

		if (view == null) {
			throw new IllegalStateException("View model must not be null");
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null");
		}

		domainChangeListener = new ModelChangeListener() {
			public void notifyChange(ModelChangeNotification notification) {
				final Notification rawNotification = notification.getRawNotification();
				switch (rawNotification.getEventType()) {
				case Notification.ADD:
				case Notification.REMOVE:
				case Notification.ADD_MANY:
				case Notification.REMOVE_MANY:
					// TODO don't reinit everytime
					init(view, domainModel);
					viewValidationCachedTree.validate(getAllEObjects(domainModel));
					notifyListeners();
					break;
				default:
					viewValidationCachedTree.validate(notification.getNotifier());
					notifyListeners();
				}
			}
		};
		context.registerDomainChangeListener(domainChangeListener);

		viewChangeListener = new ModelChangeListener() {
			public void notifyChange(ModelChangeNotification notification) {
				// do nothing for now, not supported
			}
		};
		context.registerViewChangeListener(viewChangeListener);

		init(view, domainModel);

		viewValidationCachedTree.validate(getAllEObjects(domainModel));

		notifyListeners();
	}

	private void init(Renderable view, EObject domainModel) {
		validationRegistry = new ValidationRegistry();
		validationRegistry.register(domainModel, view);

		viewValidationCachedTree = new ViewValidationCachedTree(new IExcludedObjectsCallback() {
			public boolean isExcluded(Object object) {
				return false;
			}
		}, validationRegistry);
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
	 * Registers a listener that will be informed once the validation result's severity is higher than
	 * {@link Diagnostic#OK}.
	 * 
	 * @param listener the listener to register
	 */
	public void registerValidationListener(ViewValidationListener listener) {
		validationListener.add(listener);
	}

	/**
	 * Deregisters the given listener.
	 * 
	 * @param listener the listener to deregister
	 */
	public void deregisterValidationListener(ViewValidationListener listener) {
		validationListener.remove(listener);
	}

	@SuppressWarnings("unchecked")
	private void notifyListeners() {
		if (validationListener.size() > 0) {
			final Set<Diagnostic> result = new HashSet<Diagnostic>();
			if (view.getDiagnostic().getHighestSeverity() > Diagnostic.OK) {
				for (final Object o : view.getDiagnostic().getDiagnostics()) {
					if (((Diagnostic) o).getSeverity() > Diagnostic.OK) {
						result.add((Diagnostic) o);
					}
				}
			}

			if (result.size() > 0) {
				for (final ViewValidationListener l : validationListener) {
					l.onValidationErrors(result);
				}
			}
		}
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

}
