package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class ViewRendererFX extends RendererFX<VView> {

	@Override
	public Set<RenderingResultRow<Node>> render(VView renderable,
			ViewModelContext viewModelContext) {
		GridPane grid = new GridPane();
		grid.getStyleClass().add("vertical");

		int row = -1;
		for (VContainedElement composite : renderable.getChildren()) {
			Set<RenderingResultRow<Node>> renderResult = RendererFactory.INSTANCE
					.render(composite, viewModelContext);
			for (RenderingResultRow<Node> controlsRow : renderResult) {
				row++;
				int column = 0;
				for (Node node : controlsRow.getControls()) {
					grid.add(node, column++, row);
					GridPane.setHgrow(node, Priority.ALWAYS);
					if (VContainer.class.isInstance(composite))
						GridPane.setVgrow(node, Priority.ALWAYS);
				}
			}
		}
		if (grid.getChildren().size() == 1) {
			GridPane.setVgrow(grid.getChildren().get(0), Priority.ALWAYS);
		}
		return createSingletonRow(grid);
	}

}
