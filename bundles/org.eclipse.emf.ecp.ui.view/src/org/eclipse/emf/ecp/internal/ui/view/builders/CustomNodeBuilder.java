package org.eclipse.emf.ecp.internal.ui.view.builders;

import java.util.Map;

import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Renderable;

public interface CustomNodeBuilder {

	Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>
	getCustomNodeBuilders();
}
