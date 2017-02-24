/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.editor.controls;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.spi.ReferenceService;
import org.eclipse.emf.ecp.spi.common.ui.SelectModelElementWizardFactory;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.LinkControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration;
import org.eclipse.emf.ecp.view.spi.util.swt.ImageRegistryService;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;
import org.eclipse.swt.widgets.Composite;

/**
 * Special {@link LinkControlSWTRenderer} which will streamline the column dmr selection.
 *
 * @author Johannes Faltermeier
 *
 */
public class TableWidthConfigurationDMRRenderer extends LinkControlSWTRenderer {

	private TableWidthConfigurationDMRRendererReferenceService referenceService;

	/**
	 * @param vElement the element to render
	 * @param viewContext the view model context
	 * @param reportService the report service
	 * @param emfFormsDatabinding the data binding service
	 * @param emfFormsLabelProvider the label provider
	 * @param vtViewTemplateProvider the view template provider
	 * @param localizationService the localization service
	 * @param imageRegistryService the image registry service
	 * @param emfFormsEditSuppport the edit support
	 */
	@Inject
	// CHECKSTYLE.OFF: ParameterNumber
	public TableWidthConfigurationDMRRenderer(
		VControl vElement,
		ViewModelContext viewContext,
		ReportService reportService,
		EMFFormsDatabinding emfFormsDatabinding,
		EMFFormsLabelProvider emfFormsLabelProvider,
		VTViewTemplateProvider vtViewTemplateProvider,
		EMFFormsLocalizationService localizationService,
		ImageRegistryService imageRegistryService,
		EMFFormsEditSupport emfFormsEditSuppport) {
		// CHECKSTYLE.ON: ParameterNumber
		super(
			vElement,
			viewContext,
			reportService,
			emfFormsDatabinding,
			emfFormsLabelProvider,
			vtViewTemplateProvider,
			localizationService,
			imageRegistryService,
			emfFormsEditSuppport);
	}

	@Override
	protected void createNewReferenceButton(Composite parent, String elementDisplayName) {
		/* we only want users to select existing DMRs -> no-op */
	}

	@Override
	protected ReferenceService getReferenceService() {
		if (referenceService == null) {
			referenceService = new TableWidthConfigurationDMRRendererReferenceService();
		}
		return referenceService;
	}

	/**
	 * Special {@link ReferenceService} allowing stream lined DMR selection for the width configuration.
	 *
	 * @author Johannes Faltermeier
	 *
	 */
	private class TableWidthConfigurationDMRRendererReferenceService implements ReferenceService {

		@Override
		public void instantiate(ViewModelContext context) {
			/* no-op */
		}

		@Override
		public void dispose() {
			/* no-op */
		}

		@Override
		public int getPriority() {
			/* no-op */
			return 0;
		}

		@Override
		public void addNewModelElements(EObject eObject, EReference eReference) {
			/* no-op */
		}

		@SuppressWarnings("restriction")
		@Override
		public void addExistingModelElements(EObject eObject, EReference eReference) {
			if (!VTableControl.class.isInstance(eObject.eContainer())) {
				return;
			}
			final VTableControl tableControl = VTableControl.class.cast(eObject.eContainer());
			if (!VTableDomainModelReference.class.isInstance(tableControl.getDomainModelReference())) {
				return;
			}
			final VTableDomainModelReference tableDMR = VTableDomainModelReference.class
				.cast(tableControl.getDomainModelReference());
			final Set<EObject> unconfiguredColumns = new LinkedHashSet<EObject>(
				tableDMR.getColumnDomainModelReferences());
			for (final VTableColumnConfiguration columnConfiguration : tableControl.getColumnConfigurations()) {
				if (!VWidthConfiguration.class.isInstance(columnConfiguration)) {
					continue;
				}
				unconfiguredColumns
					.remove(VWidthConfiguration.class.cast(columnConfiguration).getColumnDomainReference());
			}

			final Set<EObject> selectedColumns = SelectModelElementWizardFactory
				.openModelElementSelectionDialog(
					unconfiguredColumns,
					eReference.isMany());

			org.eclipse.emf.ecp.internal.edit.ECPControlHelper.addModelElementsInReference(
				eObject,
				selectedColumns,
				eReference,
				AdapterFactoryEditingDomain.getEditingDomainFor(eObject));
		}

		@Override
		public void openInNewContext(EObject eObject) {
			/* no-op */
		}

	}

}
