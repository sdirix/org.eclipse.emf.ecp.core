package org.eclipse.emf.ecp.internal.ui.view.renderer;


public abstract class TreeRendererNodeVisitor<CONTROL> {
		public abstract void executeOnNode(RendererNode<CONTROL> node);
	}