package org.eclipse.emf.ecp.controls.internal.fx;

import java.util.Collections;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ECPFXEnum extends ECPControlFactoryFX {

	@Override
	public Set<RenderingResultRow<Node>> render(final VControl control) {
		final ChoiceBox cb = new ChoiceBox();
		cb.setItems(FXCollections.observableArrayList(control
				.getDomainModelReference().getEStructuralFeatureIterator()
				.next().getEType().getInstanceClass().getEnumConstants()));
		cb.setMaxWidth(Double.MAX_VALUE);

		IObservableValue targetValue = getTargetObservable(cb, "value");
		IObservableValue modelValue = getModelObservable(control
				.getDomainModelReference().getIterator().next());
		bindModelToTarget(new EMFDataBindingContext(), targetValue, modelValue,
				null, null);

		control.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (msg.getFeature() == VViewPackage.eINSTANCE
						.getElement_Diagnostic()) {
					applyValidation(control, cb);
				}
			}

		});

		applyValidation(control, cb);

		return Collections.singleton(getControlsRow(cb));
	}

}
