package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;

import org.eclipse.emf.ecp.view.model.VElement;

@SuppressWarnings("restriction")
//TODO no api
public interface RendererFactory {

	RendererFactory INSTANCE=RendererFactoryImpl.getInstance();
	
	<T extends VElement> Node render (T renderable);
}
