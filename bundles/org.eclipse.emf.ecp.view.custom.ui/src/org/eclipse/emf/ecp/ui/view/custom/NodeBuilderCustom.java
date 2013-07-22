package org.eclipse.emf.ecp.ui.view.custom;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.model.Renderable;

public class NodeBuilderCustom implements CustomNodeBuilder {

	public NodeBuilderCustom() {
	}

	@Override
	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {

		Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> builders = new HashMap<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>();
		builders.put(CustomControl.class,
				new RenderableNodeBuilder<CustomControl>());
		return builders;
	}

}
