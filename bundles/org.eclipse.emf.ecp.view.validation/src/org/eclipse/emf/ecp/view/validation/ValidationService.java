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
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
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

	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private ViewValidationCachedTree viewValidationCachedTree;
	private ValidationRegistry validationRegistry;

	/**
	 * Default constructor.
	 */
	public ValidationService() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;

		final View view = context.getViewModel();

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
					// TODO don't reinit everytime?
					init(view, domainModel);
					viewValidationCachedTree.validate(getAllEObjects(domainModel));
					break;
				default:
					viewValidationCachedTree.validate(notification.getNotifier());
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
