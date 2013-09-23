/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.view.context.internal.Activator;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VSingleDomainModelReference;
import org.eclipse.emf.ecp.view.model.util.ViewModelUtil;

/**
 * The Class ViewModelContextImpl.
 * 
 * @author Eugen Neufeld
 */
public class ViewModelContextImpl implements ViewModelContext {

	/** The view. */
	private final Renderable view;

	/** The domain object. */
	private final EObject domainObject;

	/** The view model change listener. */
	private final List<ModelChangeListener> viewModelChangeListener = new ArrayList<ModelChangeListener>();

	/** The domain model change listener. */
	private final List<ModelChangeListener> domainModelChangeListener = new ArrayList<ModelChangeListener>();

	/** The domain model content adapter. */
	private EContentAdapter domainModelContentAdapter;

	/** The view model content adapter. */
	private EContentAdapter viewModelContentAdapter;

	/** The view services. */
	private final SortedSet<AbstractViewService> viewServices = new TreeSet<AbstractViewService>(
		new Comparator<AbstractViewService>() {

			public int compare(AbstractViewService arg0, AbstractViewService arg1) {
				return arg0.getPriority() - arg1.getPriority();
			}
		});

	/**
	 * The disposed state.
	 */
	private boolean isDisposed;

	/**
	 * Whether the context is being disposed.
	 */
	private boolean isDisposing;

	/**
	 * Instantiates a new view model context impl.
	 * 
	 * @param view the view
	 * @param domainObject the domain object
	 */
	public ViewModelContextImpl(Renderable view, EObject domainObject) {
		this.view = view;
		this.domainObject = domainObject;

		instantiate();
	}

	/**
	 * Instantiate.
	 */
	private void instantiate() {

		ViewModelUtil.resolveDomainReferences(getViewModel(), getDomainModel());

		viewModelContentAdapter = new EContentAdapter() {

			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);

				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				if (notification.isTouch()) {
					return;
				}

				final ModelChangeNotification modelChangeNotification = new ModelChangeNotification(notification);
				for (final ModelChangeListener modelChangeListener : viewModelChangeListener) {
					modelChangeListener.notifyChange(modelChangeNotification);
				}
			}

			@Override
			protected void addAdapter(Notifier notifier) {
				super.addAdapter(notifier);
				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				if (AbstractControl.class.isInstance(notifier)) {
					final AbstractControl control = (AbstractControl) notifier;
					for (final VSingleDomainModelReference domainModelReference : control.getDomainModelReferences()) {
						if (domainModelReference.getDomainModel() == null) {
							domainModelReference.resolve(domainObject);
						}
					}
				}
				for (final ModelChangeListener modelChangeListener : viewModelChangeListener) {
					modelChangeListener.notifyAdd(notifier);
				}
			}

			@Override
			protected void removeAdapter(Notifier notifier) {
				super.removeAdapter(notifier);
				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				for (final ModelChangeListener modelChangeListener : viewModelChangeListener) {
					modelChangeListener.notifyRemove(notifier);
				}
			}

		};

		view.eAdapters().add(viewModelContentAdapter);

		// TODO extract contentadapter into shared class for both models

		domainModelContentAdapter = new EContentAdapter() {

			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);

				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				if (notification.isTouch()) {
					return;
				}

				final ModelChangeNotification modelChangeNotification = new ModelChangeNotification(notification);
				for (final ModelChangeListener modelChangeListener : domainModelChangeListener) {
					modelChangeListener.notifyChange(modelChangeNotification);
				}
			}

			@Override
			protected void addAdapter(Notifier notifier) {
				super.addAdapter(notifier);
				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				for (final ModelChangeListener modelChangeListener : domainModelChangeListener) {
					modelChangeListener.notifyAdd(notifier);
				}
			}

			@Override
			protected void removeAdapter(Notifier notifier) {
				super.removeAdapter(notifier);
				// do not notify while being disposed
				if (isDisposing) {
					return;
				}
				for (final ModelChangeListener modelChangeListener : domainModelChangeListener) {
					modelChangeListener.notifyRemove(notifier);
				}
			}

		};
		domainObject.eAdapters().add(domainModelContentAdapter);

		readAbstractViewServices();

		for (final AbstractViewService viewService : viewServices) {
			viewService.instantiate(this);
		}
	}

	/**
	 * Read abstract view services.
	 */
	private void readAbstractViewServices() {

		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] controls = extensionRegistry.getConfigurationElementsFor(
			"org.eclipse.emf.ecp.view.context.viewServices");
		for (final IConfigurationElement e : controls) {
			try {
				final AbstractViewService viewService = (AbstractViewService) e.createExecutableExtension("class");
				viewServices.add(viewService);
			} catch (final CoreException e1) {
				Activator.log(e1);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#getViewModel()
	 */
	public Renderable getViewModel() {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		return view;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#getDomainModel()
	 */
	public EObject getDomainModel() {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		return domainObject;
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		isDisposing = true;
		view.eAdapters().remove(viewModelContentAdapter);
		domainObject.eAdapters().remove(domainModelContentAdapter);

		viewModelChangeListener.clear();
		domainModelChangeListener.clear();

		for (final AbstractViewService viewService : viewServices) {
			viewService.dispose();
		}
		viewServices.clear();

		isDisposing = false;
		isDisposed = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#registerViewChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)
	 */
	public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		if (modelChangeListener == null) {
			throw new IllegalArgumentException("ModelChangeListener must not be null.");
		}
		viewModelChangeListener.add(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#unregisterViewChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)
	 */
	public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		viewModelChangeListener.remove(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#registerDomainChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)
	 */
	public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		if (modelChangeListener == null) {
			throw new IllegalArgumentException("ModelChangeListener must not be null.");
		}
		domainModelChangeListener.add(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#unregisterDomainChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)
	 */
	public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException("The ViewModelContext was already disposed.");
		}
		domainModelChangeListener.remove(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#hasService(java.lang.Class)
	 */
	public <T> boolean hasService(Class<T> serviceType) {
		for (final AbstractViewService service : viewServices) {
			if (serviceType.isInstance(service)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext#getService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceType) {
		for (final AbstractViewService service : viewServices) {
			if (serviceType.isInstance(service)) {
				return (T) service;
			}
		}

		throw new RuntimeException("No view service of type " + serviceType.getCanonicalName() + " found");
	}
}
