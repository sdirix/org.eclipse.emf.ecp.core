/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lcuas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.segments;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeAddRemoveListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReferenceSegment;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emfforms.spi.core.services.segments.EMFFormsSegmentGenerator;
import org.eclipse.emfforms.spi.core.services.view.EMFFormsViewContext;

/**
 *
 * A view service that converts all {@link VDomainModelReference domain model references} of a view context to
 * {@link VDomainModelReferenceSegment segments}. The generated segments are added to their respective DMRs.
 * <p>
 * <strong>Note:</strong> This behavior can be deactivated by setting the program argument
 * {@link DmrToSegmentsViewServiceFactory#LEGACY_DMR_RESOLVEMENT -enableLegacyDmrResolution}.
 *
 *
 * @author Lucas Koehler
 *
 */
public class DmrToSegmentsViewService implements ModelChangeAddRemoveListener {

	private final EMFFormsSegmentGenerator segmentGenerator;

	/**
	 * Creates a new instance.
	 *
	 * @param viewContext The {@link EMFFormsViewContext} that created me
	 */
	public DmrToSegmentsViewService(EMFFormsViewContext viewContext) {
		viewContext.registerViewChangeListener(this);
		segmentGenerator = viewContext.getService(EMFFormsSegmentGenerator.class);

		final VElement view = viewContext.getViewModel();
		view.eAllContents().forEachRemaining(this::addSegmentsIfNecessary);
	}

	/**
	 * Checks whether an object is a {@link VDomainModelReference}. If it is and it does not have any
	 * {@link VDomainModelReferenceSegment segments}, generate the segments and add them to the DMR.
	 *
	 * @param object The object to check and and add the segments to if applicable
	 */
	private void addSegmentsIfNecessary(Object object) {
		if (object instanceof VDomainModelReference) {
			final VDomainModelReference dmr = (VDomainModelReference) object;
			if (!dmr.getSegments().isEmpty()) {
				// DMRs that already use segments must not be changed
				return;
			}
			final List<VDomainModelReferenceSegment> segments = segmentGenerator.generateSegments(dmr);
			dmr.getSegments().addAll(segments);
		}
	}

	@Override
	public void notifyChange(ModelChangeNotification notification) {
		// Nothing to do
	}

	@Override
	public void notifyAdd(Notifier notifier) {
		addSegmentsIfNecessary(notifier);
	}

	@Override
	public void notifyRemove(Notifier notifier) {
		// Nothing to do
	}
}
