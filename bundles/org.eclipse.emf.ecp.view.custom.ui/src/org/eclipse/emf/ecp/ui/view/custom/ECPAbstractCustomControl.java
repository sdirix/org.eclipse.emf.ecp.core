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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.view.custom.internal.ui.Activator;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature;
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

	private final Set<ECPCustomControlFeature> features;
	private ECPControlContext modelElementContext;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	private final ECPControlFactory controlFactory;
	/**
	 * This map manages the mapping between an {@link EStructuralFeature} and a corresponding {@link ECPControl}.
	 */
	private final HashMap<EStructuralFeature, ECPControl> controlMap = new HashMap<EStructuralFeature, ECPControl>();

	/**
	 * Constructor for instantiating the {@link ECPAbstractCustomControl}. The constructor maps the features and sets
	 * the ECPControlFactory.
	 * 
	 * @param features the features which can be used in this {@link ECPAbstractCustomControl}. If the Set is null, an
	 *            empty set is used instead.
	 */
	public ECPAbstractCustomControl(Set<ECPCustomControlFeature> features) {
		super();
		if (features == null) {
			features = new LinkedHashSet<ECPCustomControlFeature>();
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

		initFeatures();

		// move to some cleanup service
		createNecessaryObjects();
	}

	private void initFeatures() {
		for (final ECPCustomControlFeature feature : features) {
			feature.init(modelElementContext.getModelElement(), modelElementContext.getDataBindingContext(),
				modelElementContext.getEditingDomain());
		}
	}

	private void createNecessaryObjects() {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.custom.model.ECPCustomControl#getECPCustomControlFeatures()
	 */
	public final Set<ECPCustomControlFeature> getECPCustomControlFeatures() {
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
	private IItemPropertyDescriptor getItemPropertyDescriptor(ECPCustomControlFeature customControlFeature) {
		return adapterFactoryItemDelegator.getPropertyDescriptor(
			customControlFeature.getRelevantEObject(),
			customControlFeature.getTargetFeature());
	}

	private String getHelp(ECPCustomControlFeature customControlFeature) {
		if (!getECPCustomControlFeatures().contains(customControlFeature)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(customControlFeature).getDescription(null);
	}

	private String getLabel(ECPCustomControlFeature customControlFeature) {
		if (!getECPCustomControlFeatures().contains(customControlFeature)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(customControlFeature).getDisplayName(null);
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
		for (final ECPCustomControlFeature feature : features) {
			feature.dispose();
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
	 * @param feature the {@link ECPCustomControlFeature} to retrieve a control for
	 * @param <T> the type of the control to retrieve
	 * @return the {@link ECPControl} that is fitting the most for the {@link ECPCustomControlFeature}. Can also be
	 *         null.
	 */
	protected final <T extends ECPControl> T getControl(Class<T> clazz, ECPCustomControlFeature feature) {
		final T createControl = controlFactory.createControl(clazz, getItemPropertyDescriptor(feature),
			getModelElementContext());
		controlMap.put(feature.getTargetFeature(), createControl);
		return createControl;
	}

	/**
	 * The {@link CustomControlHelper} allows the retrieval of information based on an
	 * {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature ECPCustomControlFeature}.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	public final class CustomControlHelper {

		/**
		 * This return a text providing a long helpful description of the feature. Can be used for example in a ToolTip.
		 * 
		 * @param customControlFeature the
		 *            {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature
		 *            ECPCustomControlFeature} to retrieve the help text for
		 * @return the String containing the helpful description or null if no description is found
		 */
		public String getHelp(ECPCustomControlFeature customControlFeature) {
			return ECPAbstractCustomControl.this.getHelp(customControlFeature);
		}

		/**
		 * This return a text providing a short label of the feature. Can be used for example as a label in front of the
		 * edit field.
		 * 
		 * @param customControlFeature the
		 *            {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature
		 *            ECPCustomControlFeature} to retrieve the text for
		 * @return the String containing the label null if no label is found
		 */
		public String getLabel(ECPCustomControlFeature customControlFeature) {
			return ECPAbstractCustomControl.this.getLabel(customControlFeature);
		}
	}
}
