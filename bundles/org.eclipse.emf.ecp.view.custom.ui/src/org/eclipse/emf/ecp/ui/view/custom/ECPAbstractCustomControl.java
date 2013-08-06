package org.eclipse.emf.ecp.ui.view.custom;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public abstract class ECPAbstractCustomControl implements ECPCustomControl {

	private final CustomControlHelper helper = new CustomControlHelper();

	private final Set<ECPCustomControlFeature> editableFeatures;
	private final Set<ECPCustomControlFeature> referencedFeatures;
	private ECPControlContext modelElementContext = null;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	private final ECPControlFactory controlFactory;

	protected final HashMap<EStructuralFeature, ECPControl> controlMap = new HashMap<EStructuralFeature, ECPControl>();

	public ECPAbstractCustomControl(Set<ECPCustomControlFeature> editableFeatures,
		Set<ECPCustomControlFeature> referencedFeatures) {
		super();
		this.editableFeatures = editableFeatures;
		this.referencedFeatures = referencedFeatures;
		controlFactory = Activator.getECPControlFactory();
	}

	public final void init(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		// move to some cleanup service
		createNecessaryObjects();
	}

	/**
	 * 
	 */
	private void createNecessaryObjects() {
		// TODO Auto-generated method stub

	}

	public final Set<ECPCustomControlFeature> getEditableFeatures() {
		return editableFeatures;
	}

	public final Set<ECPCustomControlFeature> getReferencedFeatures() {
		return referencedFeatures;
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
			customControlFeature.getRelevantEObject(modelElementContext.getModelElement()),
			customControlFeature.getTargetFeature());
	}

	private String getHelp(ECPCustomControlFeature customControlFeature) {
		if (!getReferencedFeatures().contains(customControlFeature)
			&& !getEditableFeatures().contains(customControlFeature)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(customControlFeature).getDescription(null);
	}

	private String getLabel(ECPCustomControlFeature customControlFeature) {
		if (!getReferencedFeatures().contains(customControlFeature)
			&& !getEditableFeatures().contains(customControlFeature)) {
			throw new IllegalArgumentException("The feature must have been registered before!");
		}
		return getItemPropertyDescriptor(customControlFeature).getDisplayName(null);
	}

	/**
	 * Returns the {@link DataBindingContext} set in the constructor.
	 * 
	 * @return the {@link DataBindingContext}
	 */
	private DataBindingContext getDataBindingContext() {
		return getModelElementContext().getDataBindingContext();
	}

	/**
	 * The model value used for databinding.
	 * 
	 * @return the {@link IObservableValue}
	 */
	private IObservableValue getModelValue(ECPCustomControlFeature customControlFeature) {
		return EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
			customControlFeature.getRelevantEObject(getModelElementContext().getModelElement()),
			customControlFeature.getTargetFeature());
	}

	private Binding bindTargetToModel(IObservableValue targetValue, UpdateValueStrategy targetToModel,
		UpdateValueStrategy modelToTarget, ECPCustomControlFeature customControlFeature) {
		if (!editableFeatures.contains(customControlFeature)) {
			throw new IllegalArgumentException("Feature is not registered as editable");
		}
		return getDataBindingContext().bindValue(targetValue, getModelValue(customControlFeature), targetToModel,
			modelToTarget);
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
		for (final ECPCustomControlFeature feature : editableFeatures) {
			feature.dispose();
		}
		for (final ECPCustomControlFeature feature : referencedFeatures) {
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

		public Binding bindTargetToModel(IObservableValue targetValue, UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget, ECPCustomControlFeature customControlFeature) {
			return ECPAbstractCustomControl.this.bindTargetToModel(targetValue, targetToModel, modelToTarget,
				customControlFeature);
		}
	}
}
