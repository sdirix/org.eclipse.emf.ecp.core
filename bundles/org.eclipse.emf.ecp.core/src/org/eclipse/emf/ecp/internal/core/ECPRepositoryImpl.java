/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 * Eugen Neufeld - JavaDoc
 *******************************************************************************/

package org.eclipse.emf.ecp.internal.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPContainer;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.core.util.Disposable;
import org.eclipse.emf.ecp.internal.core.util.PropertiesElement;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.util.ECPDisposable;
import org.eclipse.emf.ecp.spi.core.util.ECPDisposable.DisposeListener;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.runtime.Platform;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This Class describes a repository.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class ECPRepositoryImpl extends PropertiesElement implements InternalRepository, DisposeListener {
	private final Disposable disposable = new Disposable(this) {
		@Override
		protected void doDispose() {
			provider = null;
			providerSpecificData = null;
		}
	};

	private String label;

	private String description;

	private InternalProvider provider;

	private Object providerSpecificData;

	/**
	 * Constructor used to create an instance through user input.
	 * 
	 * @param provider the {@link ECPProvider} for this repository
	 * @param name the name of this repository
	 * @param properties the {@link ECPProperties} of this repository
	 */
	public ECPRepositoryImpl(ECPProvider provider, String name, ECPProperties properties) {
		super(name, properties);
		label = name;
		description = "";

		if (provider == null) {
			throw new IllegalArgumentException("Provider is null");
		}

		this.provider = (InternalProvider) provider;
		this.provider.addDisposeListener(this);
	}

	/**
	 * Constructor used by the {@link org.eclipse.emf.ecp.core.ECPRepositoryManager ECPRepositoryManager} when loading
	 * existing repositories during startup.
	 * 
	 * @param in the {@link ObjectInput} to parse
	 * @throws IOException when an error occurs
	 */
	public ECPRepositoryImpl(ObjectInput in) throws IOException {
		super(in);

		label = in.readUTF();
		description = in.readUTF();

		String providerName = in.readUTF();
		provider = (InternalProvider) ECPUtil.getECPProviderRegistry().getProvider(providerName);
		if (provider == null) {
			throw new IllegalStateException("Provider not found: " + providerName);
		}

		provider.addDisposeListener(this);
	}

	/** {@inheritDoc} **/
	@Override
	public String getType() {
		return ECPRepository.TYPE;
	}

	/** {@inheritDoc} **/
	public void disposed(ECPDisposable disposable) throws DisposeException {
		if (disposable == provider) {
			dispose();
		}
	}

	/** {@inheritDoc} **/
	public boolean isStorable() {
		return true;
	}

	@Override
	public void write(ObjectOutput out) throws IOException {
		super.write(out);
		out.writeUTF(label);
		out.writeUTF(description);
		out.writeUTF(provider.getName());
	}

	/** {@inheritDoc} **/
	public String getLabel() {
		return label;
	}

	/** {@inheritDoc} **/
	public void setLabel(String label) {
		this.label = label;
	}

	/** {@inheritDoc} **/
	public String getDescription() {
		return description;
	}

	/** {@inheritDoc} **/
	public void setDescription(String description) {
		this.description = description;
	}

	/** {@inheritDoc} **/
	public boolean isDisposed() {
		return disposable.isDisposed();
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
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAdapter(Class adapterType) {
		InternalProvider provider = getProvider();
		if (!provider.isDisposed()) {
			Object result = provider.getAdapter(this, adapterType);
			if (result != null) {
				return result;
			}
		}

		return Platform.getAdapterManager().getAdapter(this, adapterType);
	}

	/** {@inheritDoc} **/
	public void dispose() {
		disposable.dispose();
	}

	/** {@inheritDoc} **/
	public void addDisposeListener(DisposeListener listener) {
		disposable.addDisposeListener(listener);
	}

	/** {@inheritDoc} **/
	public void removeDisposeListener(DisposeListener listener) {
		disposable.removeDisposeListener(listener);
	}

	/** {@inheritDoc} **/
	public InternalProvider getProvider() {
		return provider;
	}

	/** {@inheritDoc} **/
	public Object getProviderSpecificData() {
		return providerSpecificData;
	}

	/** {@inheritDoc} **/
	public void setProviderSpecificData(Object providerSpecificData) {
		this.providerSpecificData = providerSpecificData;
	}

	/** {@inheritDoc} **/
	public ECPContainer getContext() {
		return this;
	}

	/** {@inheritDoc} **/
	public boolean canDelete() {
		return isStorable();
	}

	/** {@inheritDoc} **/
	public void delete() {
		if (!canDelete()) {
			throw new UnsupportedOperationException();
		}

		try {
			provider.handleLifecycle(this, LifecycleEvent.REMOVE);
		} catch (Exception ex) {
			Activator.log(ex);
		}

		ECPRepositoryManagerImpl.INSTANCE.changeElements(Collections.singleton(getName()), null);
	}

	/** {@inheritDoc} **/
	public void notifyObjectsChanged(Collection<Object> objects) {
		if (objects != null && objects.size() != 0) {
			ECPRepositoryManagerImpl.INSTANCE.notifyObjectsChanged(this, objects);
		}
	}

	/**
	 * Return all open projects of that are shared on this repository.
	 * 
	 * @return array of currently open {@link ECPProject ECPProjects} that are shared on this repository
	 */
	public InternalProject[] getOpenProjects() {
		List<InternalProject> result = new ArrayList<InternalProject>();
		for (ECPProject project : ECPUtil.getECPProjectManager().getProjects()) {
			if (project.isOpen() && project.getRepository().equals(this)) {
				result.add((InternalProject) project);
			}
		}

		// TODO Consider to cache the result
		return result.toArray(new InternalProject[result.size()]);
	}
}
