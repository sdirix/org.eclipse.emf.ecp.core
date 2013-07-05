package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public interface NodeBuilder<T extends Renderable> {
	
	Node build(T renderable, ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator);
	
}
