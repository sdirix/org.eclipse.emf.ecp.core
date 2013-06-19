package org.eclipse.emf.ecp.internal.ui.view.renderer;


public abstract class TreeRendererNodeVisitor<T> {
		public abstract void executeOnNode(RendererNode<T> node);
	}