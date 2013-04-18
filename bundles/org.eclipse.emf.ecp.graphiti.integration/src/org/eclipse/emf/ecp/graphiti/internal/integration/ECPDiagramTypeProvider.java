package org.eclipse.emf.ecp.graphiti.internal.integration;

import org.eclipse.emf.ecp.internal.graphiti.feature.ECPFeatureProvider;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;

public class ECPDiagramTypeProvider extends AbstractDiagramTypeProvider {

	public ECPDiagramTypeProvider() {
		super();
		setFeatureProvider(new ECPFeatureProvider(this));
	}

}
