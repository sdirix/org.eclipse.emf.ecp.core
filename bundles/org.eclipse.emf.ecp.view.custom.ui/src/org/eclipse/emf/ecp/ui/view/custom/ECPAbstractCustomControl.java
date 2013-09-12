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
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
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
	private ECPControlContext modelElementContext = null;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	private final ECPControlFactory controlFactory;

	protected final HashMap<EStructuralFeature, ECPControl> controlMap = new HashMap<EStructuralFeature, ECPControl>();

	public ECPAbstractCustomControl(Set<ECPCustomControlFeature> features) {
		super();
		if (features == null) {
			features = new LinkedHashSet<ECPCustomControlFeature>();
		}
		this.features = features;
		controlFactory = Activator.getECPControlFactory();
	}

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

	/**
	 * 
	 */
	private void createNecessaryObjects() {
		// TODO Auto-generated method stub

	}

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

	protected abstract void disposeCustomControl();

	protected final CustomControlHelper getHelper() {
		return helper;
	}

	protected final <T extends ECPControl> T getControl(Class<T> clazz, ECPCustomControlFeature feature) {
		final T createControl = controlFactory.createControl(clazz, getItemPropertyDescriptor(feature),
			getModelElementContext());
		controlMap.put(feature.getTargetFeature(), createControl);
		return createControl;
	}

	public final class CustomControlHelper {

		public String getHelp(ECPCustomControlFeature customControlFeature) {
			return ECPAbstractCustomControl.this.getHelp(customControlFeature);
		}

		public String getLabel(ECPCustomControlFeature customControlFeature) {
			return ECPAbstractCustomControl.this.getLabel(customControlFeature);
		}
	}
}
