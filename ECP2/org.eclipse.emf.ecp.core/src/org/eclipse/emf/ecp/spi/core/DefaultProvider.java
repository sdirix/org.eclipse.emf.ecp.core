/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextAdapter;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.core.util.Disposable;
import org.eclipse.emf.ecp.internal.core.util.Element;
import org.eclipse.emf.ecp.spi.core.util.AdapterProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.ecp.spi.core.util.ModelWrapper;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class DefaultProvider extends Element implements InternalProvider {
	private final Disposable disposable = new Disposable(this) {
		@Override
		protected void doDispose() {
			uiProvider = null;

			for (ECPRepository repository : ECPRepositoryManager.INSTANCE.getRepositories()) {
				if (repository.getProvider().getName().equals(getName())) {
					handleLifecycle(repository, LifecycleEvent.DISPOSE);
				}
			}

			for (ECPProject project : ECPProjectManager.INSTANCE.getProjects()) {
				if (project.getProvider().getName().equals(getName())) {
					handleLifecycle(project, LifecycleEvent.DISPOSE);
				}
			}

			DefaultProvider.this.doDispose();
		}
	};

	private String label;

	private String description;

	private AdapterProvider uiProvider;

	protected DefaultProvider(String name) {
		super(name);
		label = name;
		description = "";
	}

	public final String getType() {
		return TYPE;
	}

	public final InternalProvider getProvider() {
		return this;
	}

	public final String getLabel() {
		return label;
	}

	public final void setLabel(String label) {
		this.label = label;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final AdapterProvider getUIProvider() {
		return uiProvider;
	}

	public final void setUIProvider(AdapterProvider uiProvider) {
		this.uiProvider = uiProvider;
	}

	public final InternalRepository[] getAllRepositories() {
		List<InternalRepository> result = new ArrayList<InternalRepository>();
		for (ECPRepository repository : ECPRepositoryManager.INSTANCE.getRepositories()) {
			if (!ECPUtil.isDisposed(repository)) {
				ECPProvider provider = repository.getProvider();
				if (provider.equals(this)) {
					result.add((InternalRepository) repository);
				}
			}
		}

		// TODO Consider to cache the result
		return result.toArray(new InternalRepository[result.size()]);
	}

	public final InternalProject[] getOpenProjects() {
		List<InternalProject> result = new ArrayList<InternalProject>();
		for (ECPProject project : ECPProjectManager.INSTANCE.getProjects()) {
			if (project.isOpen()) {
				ECPRepository repository = project.getRepository();
				if (!ECPUtil.isDisposed(repository)) {
					if (repository.getProvider().equals(this)) {
						result.add((InternalProject) project);
					}
				}
			}
		}

		// TODO Consider to cache the result
		return result.toArray(new InternalProject[result.size()]);
	}

	public final boolean isDisposed() {
		return disposable.isDisposed();
	}

	public final void dispose() {
		disposable.dispose();
	}

	public final void addDisposeListener(DisposeListener listener) {
		disposable.addDisposeListener(listener);
	}

	public final void removeDisposeListener(DisposeListener listener) {
		disposable.removeDisposeListener(listener);
	}

	protected void doDispose() {
	}

	public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		if (uiProvider != null) {
			return uiProvider.getAdapter(adaptable, adapterType);
		}

		return null;
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
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType) {
		return Platform.getAdapterManager().getAdapter(this, adapterType);
	}

	public EditingDomain createEditingDomain(InternalProject project) {
		CommandStack commandStack = createCommandStack(project);
		EditingDomain editingDomain = new AdapterFactoryEditingDomain(InternalProvider.EMF_ADAPTER_FACTORY,
			commandStack);
		editingDomain.getResourceSet().eAdapters().add(new ECPModelContextAdapter(project));
		return editingDomain;
	}

	protected CommandStack createCommandStack(InternalProject project) {
		return new BasicCommandStack();
	}

	public boolean canAddRepositories() {
		return true;
	}

	public boolean isSlow(Object parent) {
		return false;
	}

	public ECPModelContext getModelContext(Object element) {
		if (element instanceof ECPModelContext) {
			return (ECPModelContext) element;
		}

		if (element instanceof ECPModelContextProvider) {
			return ((ECPModelContextProvider) element).getModelContext(element);
		}

		if (element instanceof ModelWrapper) {
			return ((ModelWrapper<?, ?>) element).getContext();
		}

		if (element instanceof EObject) {
			EObject eObject = (EObject) element;
			ECPModelContext context = getModelContextFromAdapter(eObject);
			if (context != null) {
				return context;
			}

			element = eObject.eResource();
		}

		if (element instanceof Resource) {
			Resource resource = (Resource) element;
			ECPModelContext context = getModelContextFromAdapter(resource);
			if (context != null) {
				return context;
			}

			element = resource.getResourceSet();
		}

		if (element instanceof ResourceSet) {
			ResourceSet resourceSet = (ResourceSet) element;
			ECPModelContext context = getModelContextFromAdapter(resourceSet);
			if (context != null) {
				return context;
			}
		}

		return null;
	}

	private ECPModelContext getModelContextFromAdapter(Notifier notifier) {
		ECPModelContextAdapter adapter = (ECPModelContextAdapter) EcoreUtil.getAdapter(notifier.eAdapters(),
			ECPModelContextAdapter.class);
		if (adapter != null) {
			return adapter.getContext();
		}

		return null;
	}

	public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList) {
		if (parent == ECPProjectManager.INSTANCE) {
			childrenList.addChildren(ECPProjectManager.INSTANCE.getProjects());
		} else if (parent == ECPRepositoryManager.INSTANCE) {
			childrenList.addChildren(ECPRepositoryManager.INSTANCE.getRepositories());
		} else {
			// Get the adapter from the factory.
			ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider) EMF_ADAPTER_FACTORY.adapt(
				parent, ITreeItemContentProvider.class);

			// Either delegate the call or return nothing.
			if (treeItemContentProvider != null) {
				Collection<?> children = treeItemContentProvider.getChildren(parent);
				childrenList.addChildren(children);
			}
		}
	}

	public void handleLifecycle(ECPModelContext context, LifecycleEvent event) {
		// TODO Trace properly
		String providerClass = getClass().getSimpleName();
		String contextClass = context.getClass().getSimpleName();
		System.out.println(providerClass + " received " + event + " for " + contextClass + " " + context);
	}

	/**
	 * Convenient implementation of the {@link #getUnsupportedEPackages(Collection)} method to return an empty list. The
	 * provider has to {@link Override} this method if not all {@link EPackage}s are supported.
	 */
	@SuppressWarnings("javadoc")
	public Collection<EPackage> getUnsupportedEPackages(Collection<EPackage> packages, InternalRepository repository) {
		return Collections.emptyList();
	}

	/**
	 * Convenient implementation of the {@link #getLinkElements(ECPProject, EObject, EReference)} method to use the
	 * {@link ItemPropertyDescriptor} to get all object of an object.
	 */
	public Iterator<EObject> getLinkElements(ECPProject ecpProject, EObject modelElement, EReference eReference) {
		return ItemPropertyDescriptor.getReachableObjectsOfType(modelElement, eReference.getEType()).iterator();
	}

	/**
	 * Convenient implementation where nothing happens
	 */
	public void doSave(InternalProject project) {
		// do nothing
	}

	/**
	 * Convenient implementation where the project is autosaved
	 */
	public boolean isDirty(InternalProject project) {
		return false;
	}

	/**
	 * Convenient implementation where the project has autosave
	 */
	public boolean hasAutosave(InternalProject project) {
		return true;
	}

	public void cloneProject(InternalProject projectToClone, InternalProject targetProject) {
		// TODO change
		throw new UnsupportedOperationException();
	}

}
