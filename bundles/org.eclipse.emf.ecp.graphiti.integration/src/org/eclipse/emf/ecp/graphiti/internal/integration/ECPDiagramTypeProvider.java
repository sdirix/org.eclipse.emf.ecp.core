package org.eclipse.emf.ecp.graphiti.internal.integration;

import org.eclipse.emf.ecp.internal.graphiti.feature.ECPFeatureProvider;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class ECPDiagramTypeProvider extends AbstractDiagramTypeProvider {


	private IToolBehaviorProvider[] toolBehaviorProviders;

	public ECPDiagramTypeProvider() {
		super();
		setFeatureProvider(new ECPFeatureProvider(this));
	}

	@Override
	public boolean isAutoUpdateAtRuntimeWhenEditorIsSaved() {
		return true;
	}
	
	@Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders =
                new IToolBehaviorProvider[] { new ECPTutorialToolBehaviorProvider(
                    this) };
        }
        return toolBehaviorProviders;
    }
}
