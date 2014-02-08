package org.eclipse.emf.ecp.view.horizontal.fx;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.eclipse.emf.ecp.view.model.internal.fx.RendererFX;
import org.eclipse.emf.ecp.view.model.internal.fx.RendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class HorizontalLayoutRendererFX extends RendererFX<VHorizontalLayout> {

	@Override
	public Set<RenderingResultRow<Node>> render(VHorizontalLayout renderable,
			ViewModelContext viewModelContext) {
		GridPane grid = new GridPane();
		grid.getStyleClass().add("horizontal");
		int column = 0;
		for (VContainedElement composite : renderable.getChildren()) {
			Set<RenderingResultRow<Node>> renderResult = RendererFactory.INSTANCE
					.render(composite, viewModelContext);
			for (RenderingResultRow<Node> controlsRow : renderResult) {
				HBox hbox = new HBox();
				int controlRowCount = 0;
				for (Node node : controlsRow.getControls()) {
					hbox.getChildren().add(node);
					if (controlsRow.getControls().size() == ++controlRowCount)
						HBox.setHgrow(node, Priority.ALWAYS);
				}

				grid.add(hbox, column++, 0);
				GridPane.setHgrow(hbox, Priority.ALWAYS);
				if (VContainer.class.isInstance(composite))
					GridPane.setVgrow(hbox, Priority.ALWAYS);
				addColumnConstraint(grid);
			}
		}
		return createSingletonRow(grid);
	}

	private void addColumnConstraint(GridPane grid) {

		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100);
		column.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(column);
	}

}
