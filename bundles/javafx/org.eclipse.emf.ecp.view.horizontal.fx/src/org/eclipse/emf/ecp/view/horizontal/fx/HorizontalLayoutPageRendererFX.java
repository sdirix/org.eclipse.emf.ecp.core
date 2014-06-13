package org.eclipse.emf.ecp.view.horizontal.fx;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.eclipse.emf.ecp.view.model.internal.fx.RendererFX;
import org.eclipse.emf.ecp.view.model.internal.fx.RendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public class HorizontalLayoutPageRendererFX extends
		RendererFX<VHorizontalLayout> {

	@Override
	public Set<RenderingResultRow<Node>> render(
			final VHorizontalLayout renderable,
			final ViewModelContext viewModelContext) {
		final Pagination pagination = new Pagination(renderable.getChildren()
				.size(), 0);
		pagination.setPageFactory(new Callback<Integer, Node>() {

			public Node call(Integer param) {
				VBox box = new VBox();
				Set<RenderingResultRow<Node>> render = RendererFactory.INSTANCE
						.render(renderable.getChildren().get(param),
								viewModelContext);

				for (RenderingResultRow<Node> row : render) {
					HBox hBox = new HBox();
					for (Node node : row.getControls())
						hBox.getChildren().add(node);
					box.getChildren().add(hBox);
				}

				return box;

			}
		});
		return createSingletonRow(pagination);
	}

}
