/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.domainexpander.table;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDomainExpander;
import org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsExpandingFailedException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * An {@link EMFFormsDMRExpander} for expanding {@link VTableDomainModelReference VTableDomainModelReferences}.
 *
 * @author Lucas Koehler
 *
 */
@Component(name = "EMFFormsTableDMRExpander")
public class EMFFormsTableDMRExpander implements EMFFormsDMRExpander {

	private ReportService reportService;
	private EMFFormsDomainExpander domainExpander;
	private EMFFormsDatabinding emfFormsDatabinding;

	/**
	 * Called by the framework to set the {@link ReportService}.
	 *
	 * @param reportService The {@link ReportService}
	 */
	@Reference
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;

	}

	/**
	 * Called by the framework to set the {@link EMFFormsDomainExpander}.
	 *
	 * @param emfFormsDomainExpander The {@link EMFFormsDomainExpander}
	 */
	@Reference
	protected void setEMFFormsDomainExpander(EMFFormsDomainExpander emfFormsDomainExpander) {
		domainExpander = emfFormsDomainExpander;
	}

	/**
	 * Called by the framework to set the {@link EMFFormsDatabinding}.
	 *
	 * @param emfFormsDatabinding The {@link EMFFormsDatabinding}
	 */
	@Reference
	protected void setEMFFormsDatabinding(EMFFormsDatabinding emfFormsDatabinding) {
		this.emfFormsDatabinding = emfFormsDatabinding;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander#prepareDomainObject(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void prepareDomainObject(VDomainModelReference domainModelReference, EObject domainObject)
		throws EMFFormsExpandingFailedException {

		Assert.create(domainModelReference).notNull();
		Assert.create(domainObject).notNull();
		Assert.create(domainModelReference).ofClass(VTableDomainModelReference.class);

		final VTableDomainModelReference tableDMR = (VTableDomainModelReference) domainModelReference;

		// expand first part of the table dmr.
		final VDomainModelReference firstReference;
		if (tableDMR.getDomainModelReference() == null) {
			final VFeaturePathDomainModelReference featureDMR = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
			featureDMR.setDomainModelEFeature(tableDMR.getDomainModelEFeature());
			featureDMR.getDomainModelEReferencePath().addAll(tableDMR.getDomainModelEReferencePath());
			firstReference = featureDMR;
		} else {
			firstReference = tableDMR.getDomainModelReference();
		}
		domainExpander.prepareDomainObject(firstReference, domainObject);

		IObservableList observableList;
		try {
			observableList = emfFormsDatabinding.getObservableList(firstReference, domainObject);
		} catch (final DatabindingFailedException ex) {
			throw new EMFFormsExpandingFailedException(
				"Could not expand the given table domain model reference due to an DatabindingFailedException: " //$NON-NLS-1$
					+ ex.getMessage());
		}

		// Expand every object in the table for every column dmr.
		for (final VDomainModelReference columnDMR : tableDMR.getColumnDomainModelReferences()) {
			for (final Object object : observableList) {
				final EObject currentTableDomainObject = EObject.class.cast(object);
				domainExpander.prepareDomainObject(columnDMR, currentTableDomainObject);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.domainexpander.EMFFormsDMRExpander#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public double isApplicable(VDomainModelReference domainModelReference) {
		if (domainModelReference == null) {
			reportService.report(new AbstractReport("Warning: The given domain model reference was null.")); //$NON-NLS-1$
			return NOT_APPLICABLE;
		}
		if (VTableDomainModelReference.class.isInstance(domainModelReference)) {
			return 5d;
		}
		return NOT_APPLICABLE;
	}

}