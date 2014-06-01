package org.eclipse.emf.ecp.controls.renderer.fx;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecp.view.model.internal.fx.SimpleControlRendererFX;
import org.eclipse.emf.ecp.view.spi.model.VControl;

public class BooleanRendererFX extends SimpleControlRendererFX {

	@Override
	protected Node createControl() {
		VControl control = getVElement();
		CheckBox checkBox = new CheckBox();
		IObservableValue targetValue = getTargetObservable(checkBox, "selected");
		IObservableValue modelValue = getModelObservable(control
				.getDomainModelReference().getIterator().next());
		bindModelToTarget(targetValue, modelValue, null,
				null);
		return checkBox;
	}

}
