package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;

import org.eclipse.emf.ecp.view.model.VElement;

@SuppressWarnings("restriction")
// TODO no api
public interface RendererFX<T extends VElement> {

	Node render(T renderable);
}
