package org.eclipse.emf.ecp.internal.graphiti.feature;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;

public class ECPFeatureProvider extends DefaultFeatureProviderWithPatterns {

	public ECPFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
		addPattern(new EObjectPattern());
		addConnectionPattern(new ContainmentPattern());
		addConnectionPattern(new ReferencePattern());
	}

}
