/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.custom;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.view.custom.internal.ui.Activator;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * Abstract class implementing {@link ECPCustomControl} providing necessary common access methods.
 * 
 * @author emueller
 * @author eneufeld
 * 
 */
public abstract class ECPAbstractCustomControl implements ECPCustomControl {

	private final CustomControlHelper helper = new CustomControlHelper();

	private final Set<VDomainModelReference> features;
	private ECPControlContext modelElementContext;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	private final ECPControlFactory controlFactory;
	/**
	 * This map manages the mapping between an {@link EStructuralFeature} and a corresponding {@link ECPControl}.
	 */
	private final Map<EStructuralFeature, ECPControl> controlMap = new LinkedHashMap<EStructuralFeature, ECPControl>();

	private final Map<VDomainModelReference, Adapter> adapterMap = new LinkedHashMap<VDomainModelReference, Adapter>();

	/**
	 * Constructor for instantiating the {@link ECPAbstractCustomControl}. The constructor maps the features and sets
	 * the ECPControlFactory.
	 * 
	 * @param features the features which can be used in this {@link ECPAbstractCustomControl}. If the Set is null, an
	 *            empty set is used instead.
	 */
	public ECPAbstractCustomControl(Set<VDomainModelReference> features) {
		super();
		if (features == null) {
			features = new LinkedHashSet<VDomainModelReference>();
		}
		this.features = features;
		controlFactory = Activator.getECPControlFactory();
	}

	/**
	 * Retrieves the control used for an {@link EStructuralFeature}.
	 * 
	 * @param eStructuralFeature the {@link EStructuralFeature} to get the control for
	 * @return the {@link ECPControl} or null if none is used inside this {@link ECPCustomControl}
	 */
	protected final ECPControl getRetrievedControl(EStructuralFeature eStructuralFeature) {
		return controlMap.get(eStructuralFeature);
	}

	/**
	 * Reset all validation errors on all {@link ECPControl ECPControls} used in this {@link ECPCustomControl}.
	 */
	protected final void resetControlValidation() {
		final Set<EStructuralFeature> keySet = controlMap.keySet();

		for (final EStructuralFeature eStructuralFeature : keySet) {
			final ECPControl ecpControl = controlMap.get(eStructuralFeature);
			ecpControl.resetValidation();
		}
	}

	/**
	 * This method is called by the framework to instantiate the {@link ECPCustomControl}.
	 * 
	 * @param modelElementContext the {@link ECPControlContext} to use by this {@link ECPCustomControl}.
	 */
	public final void init(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		// initFeatures();
	}

	// private void initFeatures() {
	// for (final ECPCustomControlFeature feature : features) {
	// feature.init(modelElementContext.getModelElement(), modelElementContext.getDataBindingContext(),
	// modelElementContext.getEditingDomain());
	// }
	// }

	/**
	 * Method for enabling databinding on the reference/attribute of the referenced object.
	 * Throws an {@link IllegalStateException} if the {@link VDomainModelReference} doesn't resolve to exactly one
	 * {@link Setting}.
	 * 
	 * 
	 * @param modelFeature the {@link VDomainModelReference} to bind
	 * @param targetValue the target observerable
	 * @param targetToModel update strategy target to model
	 * @param modelToTarget update strategy model to target
	 * @return the resulting binding
	 */
	protected Binding bindTargetToModel(VDomainModelReference modelFeature, IObservableValue targetValue,
		UpdateValueStrategy targetToModel,
		UpdateValueStrategy modelToTarget) {
		final Setting setting = getSetting(modelFeature);
		final IObservableValue modelValue = EMFEditObservables.observeValue(modelElementContext.getEditingDomain(),
			setting.getEObject(), setting.getEStructuralFeature());
		return modelElementContext.getDataBindingContext().bindValue(targetValue, modelValue, targetToModel,
			modelToTarget);
	}

	/**
	 * Sets the value of the feature to the new value.
	 * Throws an {@link IllegalStateException} if the {@link VDomainModelReference} doesn't resolve to exactly one
	 * {@link Setting}.
	 * 
	 * @param modelReference the {@link VDomainModelReference} to get the value for
	 * @param newValue the value to be set
	 */
	protected void setValue(VDomainModelReference modelReference, Object newValue) {
		// if (!isEditable) {
		// throw new UnsupportedOperationException(
		// "Set value is not supported on ECPCustomControlFeatures that are not editable.");
		// }
		final Setting setting = getSetting(modelReference);
		setting.set(newValue);
	}

	/**
	 * Registers a change listener on the referenced object. {@link ECPCustomControlChangeListener#notifyChanged()} will
	 * be called when a change on the referenced object is noticed.
	 * 
	 * Throws an {@link IllegalStateException} if the {@link VDomainModelReference} doesn't resolve to exactly one
	 * {@link Setting}.
	 * 
	 * @param modelReference the {@link VDomainModelReference} to register a listener for
	 * @param changeListener the change listener to register
	 */
	protected void registerChangeListener(VDomainModelReference modelReference,
		final ECPCustomControlChangeListener changeListener) {
		if (adapterMap.containsKey(modelReference)) {
			// FIXME handling if an listener already exists
		}
		final Setting setting = getSetting(modelReference);
		final Adapter newAdapter = new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				if (msg.isTouch()) {
					return;
				}
				if (msg.getFeature().equals(setting.getEStructuralFeature())) {
					super.notifyChanged(msg);
					changeListener.notifyChanged();
				}
			}

		};
		setting.getEObject().eAdapters().add(newAdapter);
		adapterMap.put(modelReference, newAdapter);
	}

	/**
	 * Returns the current set value of the feature.
	 * Throws an {@link IllegalStateException} if the {@link VDomainModelReference} doesn't resolve to exactly one
	 * {@link Setting}.
	 * 
	 * @param modelReference the {@link VDomainModelReference} to get the value for
	 * @return the value
	 */
	protected Object getValue(VDomainModelReference modelReference) {
		final Setting setting = getSetting(modelReference);
		return setting.get(false);
	}

	/**
	 * @param modelFeature the {@link VDomainModelReference} to get the Setting for
	 * @return the setting or throws an {@link IllegalStateException} if too many or too few elements are found.
	 */
	private Setting getSetting(VDomainModelReference modelFeature) {
		final Iterator<Setting> iterator = modelFeature.getIterator();
		int numElments = 0;
		Setting setting = null;
		while (iterator.hasNext()) {
			setting = iterator.next();
			numElments++;
		}
		if (numElments == 0) {
			throw new IllegalStateException("The VDomainModelReference was not initialised.");
		}
		else if (numElments > 1) {
			throw new IllegalStateException(
				"The VDomainModelReference is ambigous, please use VDomainModelReference which resolve to exactly one setting.");
		}
		// if (!isEditable()) {
		// throw new IllegalArgumentException("Feature is not registered as editable");
		// }
		return setting;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.custom.ECPCustomControl#getNeededDomainModelReferences()
	 */
	public final Set<VDomainModelReference> getNeededDomainModelReferences() {
		return features;
	}

	/**
	 * Returns the {@link ECPControlContext} to use.
	 * 
	 * @return the {@link ECPControlContext}
	 */
	private ECPControlContext getModelElementContext() {
		return modelElementContext;
	}

	/**
	 * Return the {@link IItemPropertyDescriptor} describing this {@link EStructuralFeature}.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 */
	private IItemPropertyDescriptor getItemPropertyDescriptor(VDomainModelReference domainModelReference) {
		final Iterator<Setting> settings = domainModelReference.getIterator();
		if (settings.hasNext()) {
			final Setting setting = settings.next();
			return adapterFactoryItemDelegator.getPropertyDescriptor(setting.getEObject(),
				setting.getEStructuralFeature());
		}
		return null;
	}

	private String getHelp(VDomainModelReference domainModelReference) {
		if (!getNeededDomainModelReferences().contains(domainModelReference)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(domainModelReference).getDescription(null);
	}

	private String getLabel(VDomainModelReference domainModelReference) {
		if (!getNeededDomainModelReferences().contains(domainModelReference)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(domainModelReference).getDisplayName(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean showLabel() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		Activator.ungetECPControlFactory();
		if (composedAdapterFactory != null) {
			composedAdapterFactory.dispose();
		}
		for (final VDomainModelReference domainModelReference : adapterMap.keySet()) {
			final Setting setting = getSetting(domainModelReference);
			setting.getEObject().eAdapters().remove(adapterMap.get(domainModelReference));
		}
		disposeCustomControl();
	}

	/**
	 * This method is called during dispose and allows to dispose necessary objects.
	 */
	protected abstract void disposeCustomControl();

	/**
	 * This is a helper method which provides an {@link CustomControlHelper}. It allows to get the help description as
	 * well as the label of an {@link ECPCustomControlFeature}.
	 * 
	 * @return the {@link CustomControlHelper} to use to retrieve addition descriptions of an
	 *         {@link ECPCustomControlFeature}.
	 */
	protected final CustomControlHelper getHelper() {
		return helper;
	}

	/**
	 * Use this method to get an {@link ECPControl} which can be used inside the {@link ECPCustomControl}.
	 * 
	 * @param clazz the {@link Class} of the {@link ECPControl} to retrieve
	 * @param domainModelReference the {@link VDomainModelReference} to retrieve a control for
	 * @param <T> the type of the control to retrieve
	 * @return the {@link ECPControl} that is fitting the most for the {@link ECPCustomControlFeature}. Can also be
	 *         null.
	 */
	protected final <T extends ECPControl> T getControl(Class<T> clazz, VDomainModelReference domainModelReference) {
		final T createControl = controlFactory.createControl(clazz, getItemPropertyDescriptor(domainModelReference),
			getModelElementContext());
		final Iterator<Setting> iterator = domainModelReference.getIterator();
		final boolean hasNext = iterator.hasNext();
		if (hasNext) {
			controlMap.put(iterator.next().getEStructuralFeature(), createControl);
			return createControl;
		}
		return null;
	}

	/**
	 * The {@link CustomControlHelper} allows the retrieval of information based on an
	 * {@link org.eclipse.emf.ecp.ui.view.custom.ECPCustomControlFeature ECPCustomControlFeature}.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	public final class CustomControlHelper {

		/**
		 * This return a text providing a long helpful description of the feature. Can be used for example in a ToolTip.
		 * 
		 * @param domainModelReference the {@link VDomainModelReference} to retrieve the help text for
		 * @return the String containing the helpful description or null if no description is found
		 */
		public String getHelp(VDomainModelReference domainModelReference) {
			return ECPAbstractCustomControl.this.getHelp(domainModelReference);
		}

		/**
		 * This return a text providing a short label of the feature. Can be used for example as a label in front of the
		 * edit field.
		 * 
		 * @param domainModelReference the {@link VDomainModelReference} to retrieve the text for
		 * @return the String containing the label null if no label is found
		 */
		public String getLabel(VDomainModelReference domainModelReference) {
			return ECPAbstractCustomControl.this.getLabel(domainModelReference);
		}
	}
}
