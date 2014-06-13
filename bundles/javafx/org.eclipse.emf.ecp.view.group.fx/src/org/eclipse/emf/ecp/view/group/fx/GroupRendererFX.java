package org.eclipse.emf.ecp.view.group.fx;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.eclipse.emf.ecp.view.model.internal.fx.RendererFX;
import org.eclipse.emf.ecp.view.model.internal.fx.RendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class GroupRendererFX extends RendererFX<VGroup> {
	@Override
	public Set<RenderingResultRow<Node>> render(VGroup renderable,
			ViewModelContext viewModelContext) {
		TitledPane groupPane = new TitledPane();
		groupPane.setCollapsible(false);
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
				// TODO hack
				if (column == 1)
					GridPane.setColumnSpan(
							grid.getChildren().get(
									grid.getChildren().size() - 1),
							GridPane.REMAINING);
			}
		}
		if (grid.getChildren().size() == 1) {
			GridPane.setVgrow(grid.getChildren().get(0), Priority.ALWAYS);
		}
		String text = renderable.getName();
		if (text == null)
			text = "";
		groupPane.setText(text);
		groupPane.setContent(grid);
		groupPane.setMaxHeight(Double.MAX_VALUE);

		return createSingletonRow(groupPane);
	}
}
