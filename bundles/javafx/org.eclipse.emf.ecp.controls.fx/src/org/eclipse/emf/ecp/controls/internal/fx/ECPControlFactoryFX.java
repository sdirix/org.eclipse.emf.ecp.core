package org.eclipse.emf.ecp.controls.internal.fx;

import java.util.LinkedHashSet;
import java.util.Set;

import javafx.scene.Node;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EObjectObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;
import org.eclipse.fx.core.databinding.JFXBeanProperties;

public abstract class ECPControlFactoryFX extends ECPAbstractControl {

	public abstract Set<RenderingResultRow<Node>> render(VControl control);

	protected IObservableValue getTargetObservable(Object source,
			String property) {
		return JFXBeanProperties.value(property).observe(source);
	}

	protected IObservableValue getModelObservable(Setting setting) {
		return new EObjectObservableValue(setting.getEObject(),
				setting.getEStructuralFeature()) {

			@Override
			public Object getValueType() {
				// TODO Auto-generated method stub
				return ((EStructuralFeature) super.getValueType()).getEType()
						.getInstanceClass();
			}

		};
	}

	protected Binding bindModelToTarget(DataBindingContext dataBindingContext,
			IObservableValue target, IObservableValue model,
			UpdateValueStrategy targetToModelStrategy,
			UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = dataBindingContext.bindValue(target, model,
				targetToModelStrategy, modelToTargetStrategy);
		binding.getValidationStatus().addValueChangeListener(
				new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						IStatus statusNew = (IStatus) event.diff.getNewValue();
						if (IStatus.ERROR == statusNew.getSeverity()) {
							binding.updateModelToTarget();
						}
					}
				});

		return binding;
	}

	protected RenderingResultRow<Node> getControlsRow(final Node... nodes) {
		final Set<Node> result = new LinkedHashSet<>();
		for (Node node : nodes) {
			result.add(node);
		}
		return new RenderingResultRow<Node>() {

			@Override
			public Set<Node> getControls() {
				return result;
			}

			@Override
			public Node getMainControl() {
				if (nodes.length == 0)
					return null;
				else
					return nodes[0];
			}
		};
	}

	protected void applyValidation(VControl control, Node node) {
		if(control.getDiagnostic()==null){
			node.setId("ok");
			return;
		}
		switch (control.getDiagnostic().getHighestSeverity()) {
		case Diagnostic.ERROR:
			node.setId("error");
			break;
		case Diagnostic.OK:
			node.setId("ok");
			break;
		}
	}

	// FIXME delete methods from ECPPControl
	@Override
	public void dispose() {
		// do nothing
	}

}
