package org.eclipse.emf.ecp.view.vertical.fx;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.eclipse.emf.ecp.view.model.internal.fx.ContainerRendererFX;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalLayout;

@SuppressWarnings("restriction")
//TODO no api
public class VerticalLayoutRendererFX extends
		ContainerRendererFX<VVerticalLayout> {

	@Override
	protected Pane getPane() {
		return new VBox();
	}

}
