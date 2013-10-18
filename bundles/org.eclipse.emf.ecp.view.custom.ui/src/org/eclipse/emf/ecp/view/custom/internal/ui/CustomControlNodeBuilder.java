/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.internal.ui;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.custom.model.VCustomControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * A node builder for custom controls.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CustomControlNodeBuilder extends RenderableNodeBuilder<VCustomControl> {
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder#build(org.eclipse.emf.ecp.view.model.Renderable,
	 *      org.eclipse.emf.ecp.edit.ECPControlContext, org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator)
	 */
	@Override
	public Node<VCustomControl> build(VCustomControl model, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		// final CustomControlImpl modelImpl = (CustomControlImpl) model;
		//
		// final Set<VDomainModelReference> result = new LinkedHashSet<VDomainModelReference>();
		// if (model.getBundle() == null || model.getClassName() == null) {
		//
		// }
		// else {
		// try {
		// final ECPCustomControl categoryComposite = model.getECPCustomControl();
		// for (final ECPCustomControlFeature editFeature : categoryComposite.getECPCustomControlFeatures()) {
		// final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
		// .createVFeaturePathDomainModelReference();
		// domainModelReference.setDomainModelEFeature(editFeature.getTargetFeature());
		// domainModelReference.getDomainModelEReferencePath().addAll(editFeature.geteReferencePath());
		// domainModelReference.resolve(context.getModelElement());
		// result.add(domainModelReference);
		// }
		// } catch (final ECPCustomControlInitException ex) {
		// // TODO activate?
		// // Activator.logException(e);
		// }
		// }

		// modelImpl.setVSingleDomainModelReferences(result);
		return new Node<VCustomControl>(model, context);
	}
}
