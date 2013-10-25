package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VView;

@SuppressWarnings("restriction")
// TODO no api
public class ViewRendererFX implements RendererFX<VView> {

	@Override
	public Node render(VView renderable) {
		VBox box = new VBox();
		for (VContainedElement composite : renderable.getChildren()) {
			Node node = RendererFactory.INSTANCE.render(composite);
			box.getChildren().add(node);
		}
		return box;
	}

}
