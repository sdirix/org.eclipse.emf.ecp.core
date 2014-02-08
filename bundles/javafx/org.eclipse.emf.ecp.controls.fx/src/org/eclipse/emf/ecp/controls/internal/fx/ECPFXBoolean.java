package org.eclipse.emf.ecp.controls.internal.fx;

import java.util.Collections;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class ECPFXBoolean extends ECPControlFactoryFX {

	@Override
	public Set<RenderingResultRow<Node>> render(VControl control) {
		CheckBox checkBox = new CheckBox();
		IObservableValue targetValue = getTargetObservable(checkBox, "selected");
		IObservableValue modelValue = getModelObservable(control
				.getDomainModelReference().getIterator().next());
		bindModelToTarget(new EMFDataBindingContext(), targetValue, modelValue,
				null, null);
		return Collections.singleton(getControlsRow(checkBox));
	}
}
