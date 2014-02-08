package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Set;

import javafx.scene.Node;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;

public interface RendererFactory {

	RendererFactory INSTANCE = RendererFactoryImpl.getInstance();

	<T extends VElement> Set<RenderingResultRow<Node>> render(T renderable,
			final ViewModelContext viewContext);
}
