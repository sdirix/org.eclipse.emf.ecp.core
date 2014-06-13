package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public abstract class ContainerRendererFX<T extends VContainer> extends
		RendererFX<T> {

	@Override
	public Set<RenderingResultRow<Node>> render(T renderable,
			final ViewModelContext viewContext) {
		Pane pane = getPane();
		for (VContainedElement composite : renderable.getChildren()) {
			Set<RenderingResultRow<Node>> renderResult = RendererFactory.INSTANCE
					.render(composite, viewContext);
			for (RenderingResultRow<Node> controlsRow : renderResult) {
				HBox box = new HBox();
				for (Node node : controlsRow.getControls()) {
					box.getChildren().add(node);
				}
				pane.getChildren().add(box);
			}
		}
		return createSingletonRow(pane);
	}

	protected abstract Pane getPane();

}
