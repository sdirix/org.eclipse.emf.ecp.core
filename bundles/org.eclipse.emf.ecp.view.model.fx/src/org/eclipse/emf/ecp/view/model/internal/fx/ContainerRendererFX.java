package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VContainer;

@SuppressWarnings("restriction")
// TODO no api
public abstract class ContainerRendererFX<T extends VContainer> implements
		RendererFX<T> {

	@Override
	public Node render(T renderable) {
		Pane pane = getPane();
		for (VContainedElement composite : renderable.getChildren()) {
			Node node = RendererFactory.INSTANCE.render(composite);
			pane.getChildren().add(node);
		}
		return pane;
	}

	protected abstract Pane getPane();

}
