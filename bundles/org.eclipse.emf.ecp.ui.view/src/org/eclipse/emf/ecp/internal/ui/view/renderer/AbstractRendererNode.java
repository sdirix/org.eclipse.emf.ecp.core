package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.List;

import org.eclipse.emf.ecp.view.model.Renderable;

public abstract class AbstractRendererNode<U extends Renderable> implements WithRenderedObject {

	private Node<U> node;

	public AbstractRendererNode(Node<U> node) {
		this.node = node;
	}
	
	public List<Node> getChildren() {
		return node.getChildren();
	}
	
	public void addChild(Node child) {
		node.addChild(child);
	}
}
