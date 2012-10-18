/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPDisposable;
import org.eclipse.emf.ecp.core.util.ECPDisposable.DisposeListener;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.IFilterProvider;
import org.eclipse.emf.ecp.internal.core.util.PropertiesElement;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class ECPProjectImpl extends PropertiesElement implements InternalProject, DisposeListener {

	private InternalRepository repository;

	private InternalProvider provider;

	private Object providerSpecificData;

	private Set<EPackage> filteredEPackages = Collections.emptySet();

	private Set<EClass> filteredEClasses = Collections.emptySet();

	private EditingDomain editingDomain;

	private boolean open;

	public ECPProjectImpl(InternalProvider provider, String name, ECPProperties properties) {
		super(name, properties);
		this.provider = provider;
		open = true;
		setupFilteredEPackages();
		notifyProvider(LifecycleEvent.INIT);
	}

	public ECPProjectImpl(ECPRepository repository, String name, ECPProperties properties) {
		super(name, properties);

		if (repository == null) {
			throw new IllegalArgumentException("Repository is null");
		}

		setRepository((InternalRepository) repository);
		provider = getRepository().getProvider();
		notifyProvider(LifecycleEvent.INIT);
		open = true;
	}

	public ECPProjectImpl(ObjectInput in) throws IOException {
		super(in);

		boolean shared = in.readBoolean();
		if (shared) {
			String repositoryName = in.readUTF();
			InternalRepository repository = (InternalRepository) ECPRepositoryManager.INSTANCE
				.getRepository(repositoryName);
			if (repository == null) {
				repository = new Disposed(repositoryName);
			}

			setRepository(repository);
			provider = repository.getProvider();
		} else {
			String providerName = in.readUTF();
			provider = (InternalProvider) ECPProviderRegistry.INSTANCE.getProvider(providerName);
			if (provider == null) {
				throw new IllegalStateException("Provider not found: " + providerName);
			}
		}

		open = in.readBoolean();

		int filteredPackageSize = in.readInt();
		filteredEPackages = new HashSet<EPackage>();
		for (int i = 0; i < filteredPackageSize; i++) {
			EPackage ePackage = Registry.INSTANCE.getEPackage(in.readUTF());
			filteredEPackages.add(ePackage);
		}
		int filteredEClassSize = in.readInt();
		filteredEClasses = new HashSet<EClass>();
		for (int i = 0; i < filteredEClassSize; i++) {
			EPackage ePackage = Registry.INSTANCE.getEPackage(in.readUTF());
			EClassifier eClassifier = ePackage.getEClassifier(in.readUTF());
			if (eClassifier instanceof EClass) {
				filteredEClasses.add((EClass) eClassifier);
			}
		}

		// do not initialize on startup, will be initializes by view
		// notifyProvider(LifecycleEvent.INIT);
	}

	/**
	 * this method sets all known {@link EPackage}s as the filter.
	 */
	private void setupFilteredEPackages() {
		List<IFilterProvider> filterProviders = new ArrayList<IFilterProvider>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			"org.eclipse.emf.ecp.core.filters");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				IFilterProvider filterProvider = (IFilterProvider) configurationElement
					.createExecutableExtension("class");
				filterProviders.add(filterProvider);
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}

		Set<EPackage> ePackages = new HashSet<EPackage>();
		Set<String> filteredNsUris = new HashSet<String>();
		for (IFilterProvider filterProvider : filterProviders) {
			filteredNsUris.addAll(filterProvider.getFilteredPackages());
		}

		for (String nsUri : Registry.INSTANCE.keySet()) {
			if (filteredNsUris.contains(nsUri)) {
				continue;
			}
			EPackage ePackage = Registry.INSTANCE.getEPackage(nsUri);
			ePackages.add(ePackage);
		}

		setFilteredPackages(ePackages);
	}

	@Override
	public void write(ObjectOutput out) throws IOException {
		super.write(out);
		if (isShared()) {
			out.writeBoolean(true);
			out.writeUTF(repository.getName());
		} else {
			out.writeBoolean(false);
			out.writeUTF(provider.getName());
		}

		out.writeBoolean(open);

		out.writeInt(filteredEPackages.size());
		for (EPackage ePackage : filteredEPackages) {
			out.writeUTF(ePackage.getNsURI());
		}
		out.writeInt(filteredEClasses.size());
		for (EClass eClass : filteredEClasses) {

			out.writeUTF(eClass.getEPackage().getNsURI());
			out.writeUTF(eClass.getName());
		}
	}

	public String getType() {
		return TYPE;
	}

	public void disposed(ECPDisposable disposable) {
		if (disposable == repository) {
			dispose();
		}
	}

	public boolean isStorable() {
		return true;
	}

	public InternalProject getProject() {
		return this;
	}

	public InternalRepository getRepository() {
		return repository;
	}

	public boolean isShared() {
		return repository != null;
	}

	private void setRepository(InternalRepository repository) {
		if (this.repository != null) {
			this.repository.removeDisposeListener(this);
		}

		this.repository = repository;

		if (this.repository != null) {
			this.repository.addDisposeListener(this);
		}
	}

	public InternalProvider getProvider() {
		return provider;
	}

	public Object getProviderSpecificData() {
		return providerSpecificData;
	}

	public void setProviderSpecificData(Object providerSpecificData) {
		this.providerSpecificData = providerSpecificData;
	}

	public void notifyObjectsChanged(Object[] objects) {
		if (objects != null && objects.length != 0) {
			ECPProjectManagerImpl.INSTANCE.notifyObjectsChanged(this, objects);
		}
	}

	public synchronized EditingDomain getEditingDomain() {
		if (editingDomain == null) {
			editingDomain = getProvider().createEditingDomain(this);
		}

		return editingDomain;
	}

	/**
	 * Returns an object which is an instance of the given class associated with this object. Returns <code>null</code>
	 * if
	 * no such object can be found.
	 * <p>
	 * This implementation of the method declared by <code>IAdaptable</code> passes the request along to the platform's
	 * adapter manager; roughly <code>Platform.getAdapterManager().getAdapter(this, adapter)</code>. Subclasses may
	 * override this method (however, if they do so, they should invoke the method on their superclass to ensure that
	 * the Platform's adapter manager is consulted).
	 * </p>
	 * 
	 * @param adapterType
	 *            the class to adapt to
	 * @return the adapted object or <code>null</code>
	 * @see IAdaptable#getAdapter(Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAdapter(Class adapterType) {
		InternalProvider provider = getProvider();
		if (provider != null && !provider.isDisposed()) {
			Object result = provider.getAdapter(this, adapterType);
			if (result != null) {
				return result;
			}
		}

		return Platform.getAdapterManager().getAdapter(this, adapterType);
	}

	public ECPModelContext getContext() {
		return this;
	}

	public boolean canDelete() {
		return true;
	}

	public void delete() {
		try {
			getProvider().handleLifecycle(this, LifecycleEvent.REMOVE);
		} catch (Exception ex) {
			Activator.log(ex);
		}

		ECPProjectManagerImpl.INSTANCE.changeElements(Collections.singleton(getName()), null);
	}

	public synchronized boolean isOpen() {
		return !isRepositoryDisposed() && open;
	}

	public synchronized void open() {
		if (!isRepositoryDisposed()) {
			setOpen(true);
		}
	}

	public synchronized void close() {
		if (!isRepositoryDisposed()) {
			setOpen(false);
		}
	}

	private boolean isRepositoryDisposed() {
		return repository != null && repository.isDisposed();
	}

	private void setOpen(boolean open) {
		boolean modified = false;
		synchronized (this) {
			if (open != this.open) {
				this.open = open;
				modified = true;

				notifyProvider(open ? LifecycleEvent.INIT : LifecycleEvent.DISPOSE);

				if (!open) {
					providerSpecificData = null;
					editingDomain = null;
				}
			}
		}

		if (modified) {
			ECPProjectManagerImpl.INSTANCE.changeProject(this, open, true);
		}
	}

	private void notifyProvider(LifecycleEvent event) {
		InternalProvider provider = getProvider();
		provider.handleLifecycle(this, event);
	}

	public void undispose(InternalRepository repository) {
		setRepository(repository);
		notifyProvider(LifecycleEvent.INIT);

		if (open) {
			ECPProjectManagerImpl.INSTANCE.changeProject(this, true, true);
		}
	}

	private void dispose() {
		notifyProvider(LifecycleEvent.DISPOSE);
		if (repository != null) {
			setRepository(new Disposed(repository.getName()));
		}

		providerSpecificData = null;
		editingDomain = null;

		ECPProjectManagerImpl.INSTANCE.changeProject(this, false, false);
	}

	/**
	 * @author Eike Stepper
	 */
	private static final class Disposed implements InternalRepository {
		private final String name;

		public Disposed(String name) {
			this.name = name;
		}

		public String getType() {
			return TYPE;
		}

		public String getName() {
			return name;
		}

		public boolean isDisposed() {
			return true;
		}

		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			return null;
		}

		public String getLabel() {
			return null;
		}

		public String getDescription() {
			return null;
		}

		public ECPProject[] getOpenProjects() {
			return null;
		}

		public int compareTo(ECPElement o) {
			return 0;
		}

		public ECPModelContext getContext() {
			return null;
		}

		public ECPProperties getProperties() {
			return null;
		}

		public boolean canDelete() {
			return false;
		}

		public void delete() {
		}

		public boolean isStorable() {
			return false;
		}

		public void write(ObjectOutput out) throws IOException {
		}

		public void setLabel(String label) {
		}

		public void setDescription(String description) {
		}

		public void dispose() {
		}

		public void addDisposeListener(DisposeListener listener) {
		}

		public void removeDisposeListener(DisposeListener listener) {
		}

		public InternalProvider getProvider() {
			return null;
		}

		public Object getProviderSpecificData() {
			return null;
		}

		public void setProviderSpecificData(Object data) {
		}

		public void notifyObjectsChanged(Object[] objects) {
		}
	}

	public EList<EObject> getElements() {
		return getProvider().getElements(this);
	}

	public Collection<EPackage> getUnsupportedEPackages() {
		return getProvider().getUnsupportedEPackages(ECPUtil.getAllRegisteredEPackages(), getRepository());
	}

	public void setFilteredPackages(Set<EPackage> filteredPackages) {
		filteredEPackages = filteredPackages;
		ECPProjectManagerImpl.INSTANCE.changeProject(this, open, true);
	}

	public Set<EPackage> getFilteredPackages() {
		return filteredEPackages;
	}

	/**
	 * @return the filteredEClasses
	 */
	public Set<EClass> getFilteredEClasses() {
		return filteredEClasses;
	}

	/**
	 * @param filteredEClasses
	 *            the filteredEClasses to set
	 */
	public void setFilteredEClasses(Set<EClass> filteredEClasses) {
		this.filteredEClasses = filteredEClasses;
		ECPProjectManagerImpl.INSTANCE.changeProject(this, open, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#getLinkElements(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EReference)
	 */
	public Iterator<EObject> getLinkElements(EObject modelElement, EReference eReference) {
		return getProvider().getLinkElements(this, modelElement, eReference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#doSave()
	 */
	public void doSave() {
		getProvider().doSave(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#isDirty()
	 */
	public boolean isDirty() {
		return getProvider().isDirty(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#hasAutosave()
	 */
	public boolean hasAutosave() {
		return getProvider().hasAutosave(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#delete(org.eclipse.emf.ecore.EObject[])
	 */
	public void delete(EObject... eObjects) {
		getProvider().delete(this, Arrays.asList(eObjects));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#delete(java.util.Collection)
	 */
	public void delete(Collection<EObject> eObjects) {
		getProvider().delete(this, eObjects);
		notifyObjectsChanged(new Object[] { this });
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.core.ECPProject#addModelElement(org.eclipse.emf.ecore.EObject)
	 */
	public void addModelElement(EObject eObject) {
		getProvider().getElements(this).add(eObject);
		notifyObjectsChanged(new Object[] { this });
	}

}
