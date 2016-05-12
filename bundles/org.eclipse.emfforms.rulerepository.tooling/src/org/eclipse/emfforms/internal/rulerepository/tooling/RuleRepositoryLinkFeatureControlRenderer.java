package org.eclipse.emfforms.internal.rulerepository.tooling;

import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecp.view.internal.editor.controls.LinkFeatureControlRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emfforms.spi.common.report.ReportService;

@SuppressWarnings("restriction")
public class RuleRepositoryLinkFeatureControlRenderer extends LinkFeatureControlRenderer {

	@Inject
	public RuleRepositoryLinkFeatureControlRenderer(VControl vElement, ViewModelContext viewContext,
		ReportService reportService) {
		super(vElement, viewContext, reportService);
	}

	@Override
	protected EClass getRootEClass(Notifier notifier) {
		final EObject eObject = (EObject) notifier;

		final EList<Resource> resources = eObject.eResource().getResourceSet().getResources();
		for (final Resource resource : resources) {
			final EObject object = resource.getContents().get(0);
			if (VView.class.isInstance(object)) {
				return VView.class.cast(object).getRootEClass();
			}
		}
		throw new IllegalStateException("Please select a view model first."); //$NON-NLS-1$
	}

}
