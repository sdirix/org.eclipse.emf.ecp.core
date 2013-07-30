package org.eclipse.emf.ecp.view.separator.ui;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.separator.model.Separator;

import java.util.LinkedHashMap;
import java.util.Map;

public class SeparatorNodeBuilder implements CustomNodeBuilder {

	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>> builders;
		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>>() {
			{
				put(Separator.class, new RenderableNodeBuilder<Separator>());
			}
		};
		return builders;
	}

}
