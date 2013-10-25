package org.eclipse.emf.ecp.view.horizontal.fx;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import org.eclipse.emf.ecp.view.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.model.internal.fx.ContainerRendererFX;

@SuppressWarnings("restriction")
//TODO no api
public class HorizontalLayoutRendererFX extends
		ContainerRendererFX<VHorizontalLayout> {

	@Override
	protected Pane getPane() {
		return new HBox();
	}

}
