package org.eclipse.emf.ecp.controls.internal.fx;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.fx.util.ECPXMLDateFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class ECPFXXmlDate extends ECPFXText {

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

				final XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) value;
				final Date date = gregorianCalendar.toGregorianCalendar()
						.getTime();

				return dateInstance.format(date);
			}
		};
	}

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new ECPXMLDateFieldToModelUpdateValueStrategy();
	}

	@Override
	public Set<RenderingResultRow<Node>> render(VControl control) {
		Set<RenderingResultRow<Node>> render = super.render(control);
		Node textNode = render.iterator().next().getControls().iterator()
				.next();

		Button button = new Button("Bla");
		HBox box = new HBox();
		box.setId("xmlDateBox");
		box.getChildren().add(textNode);
		box.getChildren().add(button);
		HBox.setHgrow(textNode, Priority.ALWAYS);
		return Collections.singleton(getControlsRow(box));
	}

}
