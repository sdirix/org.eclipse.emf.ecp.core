package org.eclipse.emf.ecp.controls.renderer.fx;

import java.util.Collections;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.fx.util.ECPTextFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.view.model.internal.fx.SimpleControlRendererFX;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

public class TextRendererFX extends SimpleControlRendererFX {

	@Override
	protected Node createControl() {
		final VControl control = getVElement();
		final TextField text = new TextField();
		text.setEditable(!control.isReadonly());
		IObservableValue targetValue = getTargetObservable(text, "text");
		IObservableValue modelValue = getModelObservable(control
				.getDomainModelReference().getIterator().next());
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(control),
				getModelToTargetStrategy(control));

		control.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (msg.getFeature() == VViewPackage.eINSTANCE
						.getElement_Diagnostic()) {
					applyValidation(control, text);
				}
			}

		});

		applyValidation(control, text);
		text.setMaxWidth(Double.MAX_VALUE);

		return text;
	}

	protected UpdateValueStrategy getModelToTargetStrategy(VControl control) {
		return new EMFUpdateValueStrategy();
	}

	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new ECPTextFieldToModelUpdateValueStrategy();
	}
}
