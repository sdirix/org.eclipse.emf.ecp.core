/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.editor.handler;

import java.text.MessageFormat;
import java.util.Optional;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.ui.view.swt.reference.OpenInNewContextStrategy;
import org.eclipse.emf.ecp.ui.view.swt.reference.OpenInNewContextStrategy.Provider;
import org.eclipse.emf.ecp.ui.view.swt.reference.ReferenceServiceCustomizationVendor;
import org.eclipse.emf.ecp.view.spi.editor.controls.EStructuralFeatureSelectionValidator;
import org.eclipse.emf.ecp.view.spi.editor.controls.Helper;
import org.eclipse.emf.ecp.view.spi.editor.controls.ToolingModeUtil;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.spi.rule.model.RulePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emfforms.bazaar.Create;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * This provider registers a {@link OpenInNewContextStrategy} to edit <strong>segment based</strong> domain model
 * references for {@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition LeafConditions}.
 *
 * @author Lucas Koehler
 */
@Component(name = "LeafConditionDmrOpenInNewContextStrategyProvider", property = "service.ranking:Integer=50")
public class LeafConditionDmrOpenInNewContextStrategyProvider
	extends ReferenceServiceCustomizationVendor<OpenInNewContextStrategy> implements Provider {

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
	 * Creates the {@link OpenInNewContextStrategy}.
	 *
	 * @return The created {@link OpenInNewContextStrategy}
	 */
	@Create
	public OpenInNewContextStrategy createOpenInNewContextStrategy() {
		return new Strategy();
	}

	/**
	 * This strategy allows to edit a segment-based child dmr in a {@link CreateSegmentDmrWizard}.
	 *
	 * @author Lucas Koehler
	 */
	class Strategy implements OpenInNewContextStrategy {

		@Override
		public boolean openInNewContext(EObject owner, EReference reference, EObject object) {
			// Root EClass is the root EClass configured in the VView which contains the LeafCondition
			final EClass rootEClass = Helper.getRootEClass(owner);
			if (rootEClass == null) {
				final String message = MessageFormat.format(
					"Could not edit the domain model reference for leaf condition ''{0}'' because the VView containing it has no root EClass.", //$NON-NLS-1$
					owner);
				reportService.report(new AbstractReport(message, IStatus.WARNING));
				return false;
			}

			final EStructuralFeatureSelectionValidator validator = feature -> feature instanceof EAttribute ? null
				: "A leaf condition's domain model reference must point to an attribute."; //$NON-NLS-1$
			final CreateSegmentDmrWizard dmrWizard = new CreateSegmentDmrWizard(rootEClass,
				"Edit Domain Model Reference", (VDomainModelReference) object, validator); //$NON-NLS-1$

			final WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), dmrWizard);
			wizardDialog.setBlockOnOpen(true);
			wizardDialog.open();

			// Replace old dmr with edited version
			final Optional<VDomainModelReference> dmr = dmrWizard.getDomainModelReference();
			if (dmr.isPresent()) {
				final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(owner);
				// The DMR is always set in a single reference
				final Command command = SetCommand.create(domain, owner, reference, dmr.get());
				domain.getCommandStack().execute(command);
			}

			return true;
		}
	}
}
