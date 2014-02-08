package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Collections;
import java.util.Set;

import javafx.scene.Node;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public abstract class RendererFX<T extends VElement> {

	public abstract Set<RenderingResultRow<Node>> render(T renderable,
			final ViewModelContext viewContext);

	protected Set<RenderingResultRow<Node>> createSingletonRow(final Node node) {

		RenderingResultRow<Node> row = new RenderingResultRow<Node>() {

			@Override
			public Set<Node> getControls() {
				return Collections.singleton(node);
			}

			@Override
			public Node getMainControl() {
				return node;
			}
		};
		return Collections.singleton(row);
	}
}
