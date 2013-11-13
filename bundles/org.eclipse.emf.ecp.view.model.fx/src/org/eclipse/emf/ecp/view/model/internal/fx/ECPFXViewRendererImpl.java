/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.emf.ecp.view.model.fx.ECPFXView;
import org.eclipse.emf.ecp.view.model.fx.ECPFXViewRenderer;

/**
 * @author Jonas
 * 
 */
@SuppressWarnings("restriction")
// TODO no api
public class ECPFXViewRendererImpl implements ECPFXViewRenderer {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.fx.ECPFXViewRenderer#render(org.eclipse.emf.ecp.view.model.VView,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public ECPFXView render(VView view, EObject domainObject) { // throws
																// ECPRendererException
		
		init(view, domainObject);
		Node render = RendererFactory.INSTANCE.render(view);

		return new ECPFXViewImpl(render);
	}

	@Override
	public ECPFXView render(EObject domainObject) {
		VView view=ViewProviderHelper.getView(domainObject);
		init(view, domainObject);
		Node render = RendererFactory.INSTANCE.render(view);

		return new ECPFXViewImpl(render);
	}

	
//	private VView generate(EClass eClass) {
//		final VView view = VViewFactory.eINSTANCE.createView();
//		for (final EStructuralFeature feature : eClass
//				.getEAllStructuralFeatures()) {
//
//			final VControl control = VViewFactory.eINSTANCE.createControl();
//			final VFeaturePathDomainModelReference modelReference = VViewFactory.eINSTANCE
//					.createFeaturePathDomainModelReference();
//			modelReference.setDomainModelEFeature(feature);
//			control.setDomainModelReference(modelReference);
//
//			view.getChildren().add(control);
//
//		}
//
//		return view;
//	}
	
	private void init(VView view, EObject domainObject){
		TreeIterator<EObject> eAllContents = view.eAllContents();
		while (eAllContents.hasNext()) {
			EObject next = eAllContents.next();
			if (VDomainModelReference.class.isInstance(next)) {
				((VDomainModelReference) next).resolve(domainObject);
			}
		}
	}
}
