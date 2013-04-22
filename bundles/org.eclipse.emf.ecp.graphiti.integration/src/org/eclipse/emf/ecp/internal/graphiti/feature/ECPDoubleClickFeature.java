package org.eclipse.emf.ecp.internal.graphiti.feature;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

public class ECPDoubleClickFeature extends AbstractCustomFeature {

	public ECPDoubleClickFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}

	public void execute(ICustomContext context) {
		EObject target = (EObject) getBusinessObjectForPictogramElement(context.getInnerPictogramElement());
		Object o = getBusinessObjectForPictogramElement(getDiagram());
		final ECPProject ecpProject = ECPProjectManager.INSTANCE.getProject(o);
		ECPHandlerHelper.openModelElement(target, ecpProject);

	}

}
