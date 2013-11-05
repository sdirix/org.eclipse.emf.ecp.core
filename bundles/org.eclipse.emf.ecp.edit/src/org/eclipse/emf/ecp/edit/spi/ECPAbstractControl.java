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
package org.eclipse.emf.ecp.edit.spi;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * The {@link ECPAbstractControl} is the abstract class describing a control.
 * This class provides the necessary common access methods.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPAbstractControl implements ECPControl {

	private ECPControlContext modelElementContext;
	private boolean embedded;
	private EMFDataBindingContext dataBindingContext;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private VDomainModelReference domainModelReference;

	/**
	 * This method is called by the framework to instantiate the {@link ECPAbstractControl}.
	 * 
	 * @param controlContext the {@link ECPControlContext} to use by this {@link ECPAbstractControl}.
	 * @param domainModelReference the {@link VDomainModelReference} of this control
	 * @since 1.1
	 */
	public final void init(ECPControlContext controlContext, VDomainModelReference domainModelReference) {
		modelElementContext = controlContext;
		this.domainModelReference = domainModelReference;
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		postInit();
	}

	/**
	 * Override this method to perform actions after the initialization of the control.
	 */
	protected void postInit() {
		// do nothing
	}

	/**
	 * Return the {@link IItemPropertyDescriptor} describing this {@link Setting}.
	 * 
	 * @param setting the {@link Setting} to use for identifying the {@link IItemPropertyDescriptor}.
	 * @return the {@link IItemPropertyDescriptor}
	 * @since 1.1
	 */
	protected final IItemPropertyDescriptor getItemPropertyDescriptor(Setting setting) {
		return adapterFactoryItemDelegator.getPropertyDescriptor(setting.getEObject(),
			setting.getEStructuralFeature());
	}

	/**
	 * Returns a {@link DataBindingContext} for this control.
	 * 
	 * @return the {@link DataBindingContext}
	 * @since 1.1
	 */
	public final DataBindingContext getDataBindingContext() {
		if (dataBindingContext == null) {
			dataBindingContext = new EMFDataBindingContext();
		}
		return dataBindingContext;
	}

	/**
	 * Disposes the control.
	 * A control which needs specific dispose handling must still call super.dispose.
	 * 
	 * @since 1.1
	 */
	public void dispose() {
		composedAdapterFactory.dispose();
		modelElementContext = null;
		if (dataBindingContext != null) {
			dataBindingContext.dispose();
		}
		adapterFactoryItemDelegator = null;
		domainModelReference = null;
	}

	/**
	 * Whether a control is embedded. An embedded control can be rendered in an other fashion then an not embedded
	 * version.
	 * 
	 * @return true if the control is embedded in another control
	 */
	protected final boolean isEmbedded() {
		return embedded;
	}

	/**
	 * Sets whether this control is used as an embedded control.
	 * 
	 * @param embedded whether the control is used as an embedded control
	 */
	public final void setEmbedded(boolean embedded) {
		this.embedded = embedded;
	}

	/**
	 * Returns the {@link ECPControlContext} to use.
	 * 
	 * @return the {@link ECPControlContext}
	 */
	protected final ECPControlContext getModelElementContext() {
		return modelElementContext;
	}

	/**
	 * Returns the {@link VDomainModelReference} set for this control.
	 * 
	 * @return the domainModelReference the {@link VDomainModelReference} of this control
	 */
	protected final VDomainModelReference getDomainModelReference() {
		return domainModelReference;
	}

	/**
	 * Returns the {@link EditingDomain} for the set {@link VDomainModelReference}.
	 * 
	 * @return the {@link EditingDomain} for this control
	 */
	protected final EditingDomain getEditingDomain() {
		return getEditingDomain(getDomainModelReference());
	}

	/**
	 * Returns the {@link EditingDomain} for the provided {@link VDomainModelReference}.
	 * 
	 * @param domainModelReference the provided {@link VDomainModelReference}
	 * @return the {@link EditingDomain} of this {@link VDomainModelReference}
	 */
	protected final EditingDomain getEditingDomain(VDomainModelReference domainModelReference) {
		EditingDomain editingDomain = null;
		if (modelElementContext != null) {
			editingDomain = modelElementContext.getEditingDomain();
		}
		if (editingDomain == null) {
			editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(domainModelReference.getIterator().next());
		}
		return editingDomain;
	}

}
