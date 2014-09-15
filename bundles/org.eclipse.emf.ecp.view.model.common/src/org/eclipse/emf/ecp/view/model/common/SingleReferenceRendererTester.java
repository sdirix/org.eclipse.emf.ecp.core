/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.common;

import java.util.Iterator;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * @author Lucas Koehler
 *
 */
public class SingleReferenceRendererTester implements ECPRendererTester {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.ECPRendererTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final VControl control = (VControl) vElement;
		final Setting setting = getSetting(control);
		if (setting == null) {
			return NOT_APPLICABLE;
		}

		final EStructuralFeature feature = setting.getEStructuralFeature();
		if (feature.isMany()) {
			return NOT_APPLICABLE;
		}
		// if we have a reference
		if (!EReference.class.isInstance(feature)) {
			return NOT_APPLICABLE;
		}
		final EReference eReference = EReference.class.cast(feature);
		if (EcorePackage.eINSTANCE.getEObject().isInstance(eReference.getEType())) {
			return 3;
		}
		return NOT_APPLICABLE;
	}

	private Setting getSetting(VControl control) {
		final Iterator<Setting> iterator = control.getDomainModelReference().getIterator();
		int count = 0;
		Setting setting = null;
		while (iterator.hasNext()) {
			count++;
			setting = iterator.next();
		}
		if (count != 1) {
			return null;
		}
		return setting;
	}

}
