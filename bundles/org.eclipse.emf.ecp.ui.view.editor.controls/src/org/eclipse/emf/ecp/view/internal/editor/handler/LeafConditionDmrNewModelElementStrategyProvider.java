/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * lucas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.editor.handler;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy.Provider;
import org.eclipse.emf.ecp.ui.view.swt.reference.ReferenceServiceCustomizationVendor;
import org.eclipse.emf.ecp.view.spi.editor.controls.EStructuralFeatureSelectionValidator;
import org.eclipse.emf.ecp.view.spi.editor.controls.Helper;
import org.eclipse.emf.ecp.view.spi.editor.controls.ToolingModeUtil;
import org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.spi.rule.model.RulePackage;
import org.eclipse.emfforms.bazaar.Create;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * This provider registers a {@link CreateNewModelElementStrategy} to create <strong>segment based</strong> domain model
 * references for {@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition LeafConditions}.
 *
 * @author Lucas Koehler
 */
@Component(name = "LeafConditionDmrNewModelElementStrategyProvider", property = "service.ranking:Integer=50")
public class LeafConditionDmrNewModelElementStrategyProvider
	extends ReferenceServiceCustomizationVendor<CreateNewModelElementStrategy> implements Provider {

	private ReportService reportService;

	@Override
	protected boolean handles(EObject owner, EReference reference) {
		return isSegmentToolingEnabled()
			&& owner instanceof LeafCondition
			&& reference == RulePackage.Literals.LEAF_CONDITION__DOMAIN_MODEL_REFERENCE;
	}

	/**
	 * @return true if segment based tooling is enabled
	 */
	boolean isSegmentToolingEnabled() {
		return ToolingModeUtil.isSegmentToolingEnabled();
	}

	/**
	 * Set the {@link ReportService}.
	 *
	 * @param reportService The {@link ReportService}
	 */
	@Reference(unbind = "-")
	void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Creates the {@link CreateNewModelElementStrategy}.
	 *
	 * @return The created {@link CreateNewModelElementStrategy}
	 */
	@Create
	public CreateNewModelElementStrategy createCreateNewModelElementStrategy() {
		return new Strategy();
	}

	/**
	 * This strategy allows to create a new segment-based child dmr in a {@link CreateSegmentDmrWizard}.
	 *
	 * @author Lucas Koehler
	 */
	class Strategy implements CreateNewModelElementStrategy {

		@Override
		public Optional<EObject> createNewModelElement(EObject owner, EReference reference) {
			// Root EClass is the root EClass configured in the VView which contains the LeafCondition
			final EClass rootEClass = Helper.getRootEClass(owner);
			if (rootEClass == null) {
				final String message = MessageFormat.format(
					"Could not create a new domain model reference for leaf condition ''{0}'' because the VView containing it has no root EClass.", //$NON-NLS-1$
					owner);
				reportService.report(new AbstractReport(message, IStatus.WARNING));
				return Optional.empty();
			}

			final EStructuralFeatureSelectionValidator validator = feature -> feature instanceof EAttribute ? null
				: "A leaf condition's domain model reference must point to an attribute."; //$NON-NLS-1$
			final CreateSegmentDmrWizard dmrWizard = new CreateSegmentDmrWizard(rootEClass,
				"New Domain Model Reference", null, validator); //$NON-NLS-1$

			final WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), dmrWizard);
			wizardDialog.setBlockOnOpen(true);
			wizardDialog.open();

			return Optional.fromJavaOptional(dmrWizard.getDomainModelReference().map(EObject.class::cast));
		}
	}

}
