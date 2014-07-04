/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.editor.handler;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction;

/**
 * @author Eugen
 * 
 */
public class GenerateTableColumnsHandler extends MasterDetailAction {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#shouldShow(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean shouldShow(EObject eObject) {
		if (VTableControl.class.isInstance(eObject)) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#execute(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void execute(EObject object) {
		final VTableControl tableControl = VTableControl.class.cast(object);
		final VDomainModelReference domainModelReference = tableControl.getDomainModelReference();
		if (domainModelReference == null || !VTableDomainModelReference.class.isInstance(domainModelReference)) {
			return;
		}

		final VTableDomainModelReference tableDMR = (VTableDomainModelReference) domainModelReference;
		final Iterator<EStructuralFeature> structuralFeatureIterator = tableDMR.getEStructuralFeatureIterator();
		if (structuralFeatureIterator == null || !structuralFeatureIterator.hasNext()) {
			return;
		}
		final EStructuralFeature eStructuralFeature = structuralFeatureIterator.next();
		if (!EReference.class.isInstance(eStructuralFeature)) {
			return;
		}
		final EReference eReference = (EReference) eStructuralFeature;
		for (final EAttribute attribute : eReference.getEReferenceType().getEAllAttributes()) {
			final VFeaturePathDomainModelReference dmr = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
			dmr.setDomainModelEFeature(attribute);
			tableDMR.getColumnDomainModelReferences().add(dmr);
		}
	}

}
