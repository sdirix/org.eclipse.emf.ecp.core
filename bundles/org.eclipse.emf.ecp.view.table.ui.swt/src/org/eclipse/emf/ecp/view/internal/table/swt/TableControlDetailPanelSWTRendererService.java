/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.table.swt;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.swt.TableControlDetailPanelRenderer;
import org.eclipse.emf.ecp.view.spi.util.swt.ImageRegistryService;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.swt.core.EMFFormsRendererService;

/**
 * TableControlSWTRendererService which provides the TableControlSWTRenderer.
 *
 * @author Eugen Neufeld
 *
 */
public class TableControlDetailPanelSWTRendererService implements EMFFormsRendererService<VTableControl> {

	private EMFFormsDatabinding databindingService;
	private EMFFormsLabelProvider labelProvider;
	private ReportService reportService;
	private VTViewTemplateProvider vtViewTemplateProvider;
	private ImageRegistryService imageRegistryService;
	private EMFFormsEditSupport editSupport;

	/**
	 * Called by the initializer to set the EMFFormsDatabinding.
	 *
	 * @param databindingService The EMFFormsDatabinding
	 */
	protected void setEMFFormsDatabinding(EMFFormsDatabinding databindingService) {
		this.databindingService = databindingService;
	}

	/**
	 * Called by the initializer to set the EMFFormsLabelProvider.
	 *
	 * @param labelProvider The EMFFormsLabelProvider
	 */
	protected void setEMFFormsLabelProvider(EMFFormsLabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * Called by the initializer to set the ReportService.
	 *
	 * @param reportService The ReportService
	 */
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Called by the initializer to set the VTViewTemplateProvider.
	 *
	 * @param vtViewTemplateProvider The VTViewTemplateProvider
	 */
	protected void setVTViewTemplateProvider(VTViewTemplateProvider vtViewTemplateProvider) {
		this.vtViewTemplateProvider = vtViewTemplateProvider;
	}

	/**
	 * Called by the initializer to set the ImageRegistryService.
	 *
	 * @param imageRegistryService The ImageRegistryService
	 */
	protected void setImageRegistryService(ImageRegistryService imageRegistryService) {
		this.imageRegistryService = imageRegistryService;
	}

	/**
	 * Called by the initializer to set the EMFFormsEditSupport.
	 *
	 * @param editSupport The EMFFormsEditSupport
	 */
	protected void setEMFFormsEditSupport(EMFFormsEditSupport editSupport) {
		this.editSupport = editSupport;
	}

	/**
	 * Called by the initializer to unset the EMFFormsEditSupport.
	 *
	 * @param editSupport The EMFFormsEditSupport
	 */
	protected void unsetEMFFormsEditSupport(EMFFormsEditSupport editSupport) {
		this.editSupport = null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.core.EMFFormsRendererService#isApplicable(VElement,ViewModelContext)
	 */
	@Override
	public double isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VTableControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		if (DetailEditing.WITH_PANEL == VTableControl.class.cast(vElement).getDetailEditing()) {
			return 10;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.core.EMFFormsRendererService#getRendererInstance(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public AbstractSWTRenderer<VTableControl> getRendererInstance(VTableControl vElement,
		ViewModelContext viewModelContext) {
		return new TableControlDetailPanelRenderer(vElement, viewModelContext, reportService, databindingService,
			labelProvider, vtViewTemplateProvider, imageRegistryService, editSupport);
	}

}
