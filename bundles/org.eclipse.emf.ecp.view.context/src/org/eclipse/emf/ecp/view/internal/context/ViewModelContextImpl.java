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
package org.eclipse.emf.ecp.view.internal.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.common.UniqueSetting;
import org.eclipse.emf.ecp.view.spi.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;

/**
 * The Class ViewModelContextImpl.
 * 
 * @author Eugen Neufeld
 */
public class ViewModelContextImpl implements ViewModelContext {

	private static final String NO_VIEW_SERVICE_OF_TYPE_FOUND = "No view service of type '%1$s' found."; //$NON-NLS-1$

	private static final String MODEL_CHANGE_LISTENER_MUST_NOT_BE_NULL = "ModelChangeListener must not be null."; //$NON-NLS-1$

	private static final String THE_VIEW_MODEL_CONTEXT_WAS_ALREADY_DISPOSED = "The ViewModelContext was already disposed."; //$NON-NLS-1$

	/** The view. */
	private final VElement view;

	/** The domain object. */
	private final EObject domainObject;

	/** The view model change listener. Needs to be thread safe. */
	private final List<ModelChangeListener> viewModelChangeListener = new CopyOnWriteArrayList<ModelChangeListener>();

	/** The domain model change listener. */
	private final List<ModelChangeListener> domainModelChangeListener = new ArrayList<ModelChangeListener>();

	/** The domain model content adapter. */
	private EContentAdapter domainModelContentAdapter;

	/** The view model content adapter. */
	private EContentAdapter viewModelContentAdapter;

	/** The view services. */
	private final SortedSet<ViewModelService> viewServices = new TreeSet<ViewModelService>(
		new Comparator<ViewModelService>() {

			public int compare(ViewModelService arg0, ViewModelService arg1) {
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
	 * A mapping between settings and controls.
	 */
	private final Map<UniqueSetting, Set<VControl>> settingToControlMap = new LinkedHashMap<UniqueSetting, Set<VControl>>();

	/**
	 * Instantiates a new view model context impl.
	 * 
	 * @param view the view
	 * @param domainObject the domain object
	 */
	public ViewModelContextImpl(VElement view, EObject domainObject) {
		this.view = view;
		this.domainObject = domainObject;

		instantiate();
	}

	/**
	 * Instantiates a new view model context impl.
	 * 
	 * @param view the view
	 * @param domainObject the domain object
	 * @param modelServices an array of services to use in the {@link ViewModelContext}
	 */
	public ViewModelContextImpl(VElement view, EObject domainObject, ViewModelService... modelServices) {
		this.view = view;
		this.domainObject = domainObject;

		for (final ViewModelService vms : modelServices) {
			viewServices.add(vms);
		}
		instantiate();
	}

	/**
	 * Instantiate.
	 */
	private void instantiate() {

		ViewModelUtil.resolveDomainReferences(getViewModel(), getDomainModel());

		viewModelContentAdapter = new ViewModelContentAdapter();

		view.eAdapters().add(viewModelContentAdapter);

		domainModelContentAdapter = new DomainModelContentAdapter();
		domainObject.eAdapters().add(domainModelContentAdapter);

		createSettingToControlMapping();

		readAbstractViewServices();

		for (final ViewModelService viewService : viewServices) {
			viewService.instantiate(this);
		}
	}

	private void createSettingToControlMapping() {
		checkAndUpdateSettingToControlMapping(view);

		final TreeIterator<EObject> eAllContents = view.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject eObject = eAllContents.next();
			checkAndUpdateSettingToControlMapping(eObject);
		}
	}

	private void checkAndUpdateSettingToControlMapping(EObject eObject) {
		if (VControl.class.isInstance(eObject)) {
			final VControl vControl = (VControl) eObject;
			if (vControl.getDomainModelReference() == null) {
				return;
			}
			final Iterator<Setting> iterator = vControl.getDomainModelReference().getIterator();
			while (iterator.hasNext()) {
				final Setting setting = iterator.next();
				final UniqueSetting uniqueSetting = UniqueSetting.createSetting(setting);
				if (!settingToControlMap.containsKey(uniqueSetting)) {
					settingToControlMap.put(uniqueSetting, new LinkedHashSet<VControl>());
				}
				settingToControlMap.get(uniqueSetting).add(vControl);
			}
		}
	}

	private void vControlRemoved(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}
		final Iterator<Setting> iterator = vControl.getDomainModelReference().getIterator();
		while (iterator.hasNext()) {
			final Setting next = iterator.next();
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(next);
			if (settingToControlMap.containsKey(uniqueSetting)) {
				settingToControlMap.get(uniqueSetting).remove(vControl);
				if (settingToControlMap.get(uniqueSetting).size() == 0) {
					settingToControlMap.remove(uniqueSetting);
				}
			}
		}

	}

	private void vControlAdded(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}
		final Iterator<Setting> iterator = vControl.getDomainModelReference().getIterator();
		while (iterator.hasNext()) {
			final Setting next = iterator.next();
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(next);
			if (!settingToControlMap.containsKey(uniqueSetting)) {
				settingToControlMap.put(uniqueSetting, new LinkedHashSet<VControl>());
			}
			settingToControlMap.get(uniqueSetting).add(vControl);
		}

	}

	private void eObjectRemoved(EObject eObject) {
		for (final EStructuralFeature eStructuralFeature : eObject.eClass().getEAllStructuralFeatures()) {
			final Setting setting = InternalEObject.class.cast(eObject).eSetting(eStructuralFeature);
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(setting);
			if (settingToControlMap.containsKey(uniqueSetting)) {
				settingToControlMap.remove(uniqueSetting);
			}
		}
	}

	private void eObjectAdded(EObject eObject) {
		final InternalEObject internalParent = InternalEObject.class.cast(eObject.eContainer());
		if (internalParent == null) {
			return;
		}
		// FIXME how to add??
		// TODO hack:
		for (final EReference eReference : internalParent.eClass().getEAllContainments()) {
			if (eReference.getEReferenceType().isInstance(eObject)) {
				final Setting setting = internalParent.eSetting(eReference);
				final UniqueSetting uniqueSetting = UniqueSetting.createSetting(setting);
				final Set<VControl> controls = settingToControlMap.get(uniqueSetting);
				if (controls == null) {
					continue;
				}
				final Set<VControl> iterateControls = new LinkedHashSet<VControl>(controls);
				for (final VControl control : iterateControls) {
					vControlAdded(control);
				}

			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getControlsFor(org.eclipse.emf.ecore.EStructuralFeature.Setting)
	 */
	public Set<VControl> getControlsFor(Setting setting) {
		return settingToControlMap.get(UniqueSetting.createSetting(setting));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getControlsFor(org.eclipse.emf.ecp.common.UniqueSetting)
	 */
	public Set<VControl> getControlsFor(UniqueSetting setting) {
		return settingToControlMap.get(setting);
	}

	/**
	 * Read abstract view services.
	 */
	private void readAbstractViewServices() {

		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] controls = extensionRegistry
			.getConfigurationElementsFor("org.eclipse.emf.ecp.view.context.viewServices"); //$NON-NLS-1$
		for (final IConfigurationElement e : controls) {
			try {
				final ViewModelService viewService = (ViewModelService) e.createExecutableExtension("class"); //$NON-NLS-1$
				viewServices.add(viewService);
			} catch (final CoreException e1) {
				Activator.log(e1);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getViewModel()
	 */
	public VElement getViewModel() {
		if (isDisposed) {
			throw new IllegalStateException(THE_VIEW_MODEL_CONTEXT_WAS_ALREADY_DISPOSED);
		}
		return view;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getDomainModel()
	 */
	public EObject getDomainModel() {
		if (isDisposed) {
			throw new IllegalStateException(THE_VIEW_MODEL_CONTEXT_WAS_ALREADY_DISPOSED);
		}
		return domainObject;
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		if (isDisposed) {
			return;
		}
		isDisposing = true;
		view.eAdapters().remove(viewModelContentAdapter);
		domainObject.eAdapters().remove(domainModelContentAdapter);

		viewModelChangeListener.clear();
		domainModelChangeListener.clear();

		for (final ViewModelService viewService : viewServices) {
			viewService.dispose();
		}
		viewServices.clear();
		settingToControlMap.clear();

		isDisposing = false;
		isDisposed = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#registerViewChangeListener(org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener)
	 */
	public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException(THE_VIEW_MODEL_CONTEXT_WAS_ALREADY_DISPOSED);
		}
		if (modelChangeListener == null) {
			throw new IllegalArgumentException(MODEL_CHANGE_LISTENER_MUST_NOT_BE_NULL);
		}
		viewModelChangeListener.add(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#unregisterViewChangeListener(org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener)
	 */
	public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
		// if (isDisposed) {
		// throw new IllegalStateException("The ViewModelContext was already disposed.");
		// }
		viewModelChangeListener.remove(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#registerDomainChangeListener(org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener)
	 */
	public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
		if (isDisposed) {
			throw new IllegalStateException(THE_VIEW_MODEL_CONTEXT_WAS_ALREADY_DISPOSED);
		}
		if (modelChangeListener == null) {
			throw new IllegalArgumentException(MODEL_CHANGE_LISTENER_MUST_NOT_BE_NULL);
		}
		domainModelChangeListener.add(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#unregisterDomainChangeListener(org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener)
	 */
	public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
		// if (isDisposed) {
		// throw new IllegalStateException("The ViewModelContext was already disposed.");
		// }
		domainModelChangeListener.remove(modelChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#hasService(java.lang.Class)
	 */
	public <T> boolean hasService(Class<T> serviceType) {
		for (final ViewModelService service : viewServices) {
			if (serviceType.isInstance(service)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceType) {
		for (final ViewModelService service : viewServices) {
			if (serviceType.isInstance(service)) {
				return (T) service;
			}
		}

		Activator.log(new IllegalArgumentException(String.format(NO_VIEW_SERVICE_OF_TYPE_FOUND,
			serviceType.getCanonicalName())));
		return null;
	}

	/**
	 * The content adapter for the view model.
	 */
	private class ViewModelContentAdapter extends EContentAdapter {

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
			// FIXME possible side effects?
			if (notification.getEventType() == Notification.ADD) {
				if (VControl.class.isInstance(notification.getNotifier())) {
					checkAndUpdateSettingToControlMapping(VControl.class.cast(notification.getNotifier()));
				}
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
			if (VElement.class.isInstance(notifier)) {
				ViewModelUtil.resolveDomainReferences((VElement) notifier, getDomainModel());
			}
			if (VControl.class.isInstance(notifier)) {
				vControlAdded((VControl) notifier);
			}
			if (VDomainModelReference.class.isInstance(notifier)
				&& VControl.class.isInstance(VDomainModelReference.class.cast(notifier).eContainer())) {
				vControlAdded((VControl) VDomainModelReference.class.cast(notifier).eContainer());
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
			if (VControl.class.isInstance(notifier)) {
				vControlRemoved((VControl) notifier);
			}
			for (final ModelChangeListener modelChangeListener : viewModelChangeListener) {
				modelChangeListener.notifyRemove(notifier);
			}
		}

	}

	/**
	 * The content adapter for the domain model.
	 */
	private class DomainModelContentAdapter extends EContentAdapter {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);

			// do not notify while being disposed
			if (isDisposing) {
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
			if (EObject.class.isInstance(notifier)) {
				eObjectAdded((EObject) notifier);
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
			if (EObject.class.isInstance(notifier)) {
				eObjectRemoved((EObject) notifier);
			}
			for (final ModelChangeListener modelChangeListener : domainModelChangeListener) {
				modelChangeListener.notifyRemove(notifier);
			}
		}

	}

}
