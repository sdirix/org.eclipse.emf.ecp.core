package org.eclipse.emf.ecp.graphiti.internal.integration;

import org.eclipse.emf.ecp.internal.graphiti.feature.ECPDoubleClickFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;

public class ECPTutorialToolBehaviorProvider extends DefaultToolBehaviorProvider{

	public ECPTutorialToolBehaviorProvider(
			IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}
	
	@Override
    public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
        ICustomFeature customFeature =
            new ECPDoubleClickFeature(getFeatureProvider());
        // canExecute() tests especially if the context contains a EClass
        if (customFeature.canExecute(context)) {
            return customFeature;
        }
 
        return super.getDoubleClickFeature(context);
    }
}
