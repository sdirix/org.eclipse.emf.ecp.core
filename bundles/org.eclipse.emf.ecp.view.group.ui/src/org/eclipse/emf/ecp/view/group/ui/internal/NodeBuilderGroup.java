package org.eclipse.emf.ecp.view.group.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CompositeCollectionNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.group.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;

public class NodeBuilderGroup implements CustomNodeBuilder {

	public NodeBuilderGroup() {
	}

	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {

		final Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> builders = new HashMap<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>();
		builders.put(Group.class, new CompositeCollectionNodeBuilder<Group>());
		return builders;
	}

}
