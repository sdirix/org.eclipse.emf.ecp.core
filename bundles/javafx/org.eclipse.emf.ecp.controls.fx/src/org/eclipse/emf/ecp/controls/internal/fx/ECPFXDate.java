package org.eclipse.emf.ecp.controls.internal.fx;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.fx.util.ECPDateFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class ECPFXDate extends ECPFXText {

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(
			final VControl control) {
		return new EMFUpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value == null)
					return null;
				DateFormat dateInstance = DateFormat.getDateInstance(
						DateFormat.MEDIUM, Locale.getDefault());
				return dateInstance.format(value);
			}
		};
	}

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new ECPDateFieldToModelUpdateValueStrategy();
	}

	@Override
	public Set<RenderingResultRow<Node>> render(VControl control) {
		Set<RenderingResultRow<Node>> render = super.render(control);
		Node textNode = render.iterator().next().getControls().iterator()
				.next();

		Button button = new Button("Bla");
		HBox box = new HBox();
		box.setId("dateBox");
		box.getChildren().add(textNode);
		box.getChildren().add(button);
		HBox.setHgrow(textNode, Priority.ALWAYS);
		return Collections.singleton(getControlsRow(box));
	}

}
