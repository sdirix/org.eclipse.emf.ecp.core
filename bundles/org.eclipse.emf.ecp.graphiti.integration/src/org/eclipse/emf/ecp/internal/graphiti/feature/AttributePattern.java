package org.eclipse.emf.ecp.internal.graphiti.feature;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.config.IPatternConfiguration;

public class AttributePattern extends AbstractPattern {

	public AttributePattern(IPatternConfiguration patternConfiguration) {
		super(patternConfiguration);
	}

	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof EAttribute;
	}

	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		// TODO Auto-generated method stub
		return false;
	}

}
