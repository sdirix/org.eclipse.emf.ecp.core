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
package org.eclipse.emf.ecp.edit;

import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
	private final EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private VDomainModelReference domainModelReference;

	/**
	 * The Constructor containing all common parameters.
	 * 
	 * @param showLabel whether a label should be created for this control
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param feature the {@link EStructuralFeature}
	 * @param modelElementContext the {@link ECPControlContext}
	 * @param embedded whether this control will be embedded in another control e.g. multicontrol
	 */
	@Deprecated
	public ECPAbstractControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		this.modelElementContext = modelElementContext;
		this.embedded = embedded;
	}

	/**
	 * @since 1.1
	 */
	public ECPAbstractControl() {
	}

	/**
	 * This method is called by the framework to instantiate the {@link ECPCustomControl}.
	 * 
	 * @param modelElementContext the {@link ECPControlContext} to use by this {@link ECPCustomControl}.
	 * @since 1.1
	 */
	public final void init(ECPControlContext controlContext, VDomainModelReference domainModelReference) {
		modelElementContext = controlContext;
		this.domainModelReference = domainModelReference;
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	private final Setting getSetting(VDomainModelReference domainModelReference) {
		final Iterator<Setting> iterator = domainModelReference.getIterator();
		int count = 0;
		Setting lastSetting = null;
		while (iterator.hasNext()) {
			count++;
			if (count == 2) {
				throw new IllegalArgumentException(
					"The passed VDomainModelReference resolves to more then one setting.");
			}
			lastSetting = iterator.next();
		}
		if (count == 0) {
			throw new IllegalArgumentException("The passed VDomainModelReference resolves to no setting.");
		}
		return lastSetting;
	}

	/**
	 * @since 1.1
	 */
	protected final EditingDomain getEditingDomain(VDomainModelReference domainModelReference) {
		final Setting setting = getSetting(domainModelReference);
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	/**
	 * Return the {@link IItemPropertyDescriptor} describing this {@link EStructuralFeature}.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 * @since 1.1
	 */
	protected final IItemPropertyDescriptor getItemPropertyDescriptor(VDomainModelReference domainModelReference) {
		final Setting setting = getSetting(domainModelReference);
		return adapterFactoryItemDelegator.getPropertyDescriptor(setting.getEObject(),
			setting.getEStructuralFeature());
	}

	/**
	 * @since 1.1
	 */
	protected final boolean isEditable(VDomainModelReference domainModelReference) {
		final Setting setting = getSetting(domainModelReference);
		return getItemPropertyDescriptor(domainModelReference).canSetProperty(setting.getEObject());
	}

	/**
	 * Returns a {@link DataBindingContext} for this control.
	 * 
	 * @return the {@link DataBindingContext}
	 * @since 1.1
	 */
	protected final DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	/**
	 * @since 1.1
	 */
	protected final EObject getModelElement(VDomainModelReference domainModelReference) {
		return getSetting(domainModelReference).getEObject();
	}

	/**
	 * Return the {@link EStructuralFeature} of this control.
	 * 
	 * @return the {@link EStructuralFeature}
	 * @since 1.1
	 */
	protected final EStructuralFeature getStructuralFeature(VDomainModelReference domainModelReference) {
		return getSetting(domainModelReference).getEStructuralFeature();
	}

	/**
	 * @since 1.1
	 */
	public void dispose() {
		composedAdapterFactory.dispose();
	}

	/**
	 * Whether a control is embedded. An embedded control can be rendered in an other fashion then an not embedded
	 * version.
	 * 
	 * @return true if the control is embedded in another control
	 */
	protected boolean isEmbedded() {
		return embedded;
	}

	/**
	 * Returns the {@link ECPControlContext} to use.
	 * 
	 * @return the {@link ECPControlContext}
	 */
	protected ECPControlContext getModelElementContext() {
		return modelElementContext;
	}

	/**
	 * Return the {@link EStructuralFeature} of this control.
	 * 
	 * @return the {@link EStructuralFeature}
	 */
	@Deprecated
	protected EStructuralFeature getStructuralFeature() {
		return getSetting(domainModelReference).getEStructuralFeature();
	}

	/**
	 * Whether this control should be editable.
	 * 
	 * @return true if the {@link IItemPropertyDescriptor#canSetProperty(Object)} returns true, false otherwise
	 */
	@Deprecated
	protected boolean isEditable() {
		return getItemPropertyDescriptor(domainModelReference).canSetProperty(modelElementContext.getModelElement());
	}

	/**
	 * Checks whether this {@link EStructuralFeature} has an explicit unset state.
	 * 
	 * @return true if {@link EStructuralFeature#isUnsettable()} is true, false otherwise
	 */
	@Deprecated
	protected boolean hasUnsetState() {
		return getStructuralFeature().isUnsettable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public boolean showLabel() {
		return true;
	}

	@Deprecated
	protected IItemPropertyDescriptor getItemPropertyDescriptor() {
		return getItemPropertyDescriptor(domainModelReference);
	}

}
