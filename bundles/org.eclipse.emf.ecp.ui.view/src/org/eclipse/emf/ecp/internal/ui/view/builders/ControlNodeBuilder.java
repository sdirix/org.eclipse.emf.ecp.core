package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Leaf;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class ControlNodeBuilder<C extends Control> implements NodeBuilder<C> {

	public Node<C> build(C renderable, ECPControlContext controlContext,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {

		final ECPControlContext subcontext = createSubcontext(renderable, controlContext);
		return new Leaf<C>(renderable, subcontext);
	}

	protected ECPControlContext createSubcontext(Control modelControl, ECPControlContext controlContext) {
		// EObject parent = controlContext.getModelElement();
		//
		// for (EReference eReference : modelControl.getPathToFeature()) {
		// EObject child = (EObject) parent.eGet(eReference);
		// if (child == null) {
		// child = EcoreUtil.create(eReference.getEReferenceType());
		// parent.eSet(eReference, child);
		// }
		// parent = child;
		// }
		// REFACTORING Test
		final ECPControlContext subContext = controlContext.createSubContext(modelControl.getDomainModelReference()
			.getDomainModel());
		return subContext;
	}
}
