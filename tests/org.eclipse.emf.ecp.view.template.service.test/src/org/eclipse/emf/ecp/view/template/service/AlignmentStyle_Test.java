package org.eclipse.emf.ecp.view.template.service;

import static org.junit.Assert.*;

import org.eclipse.emf.ecp.view.template.style.alignment.model.AlignmentType;
import org.eclipse.emf.ecp.view.template.style.alignment.model.VTAlignmentFactory;
import org.eclipse.emf.ecp.view.template.style.alignment.model.VTAlignmentStyleProperty;
import org.junit.Before;
import org.junit.Test;

public class AlignmentStyle_Test {

	@Before
	public void setup(){
		
	}
	
	@Test
	public void testEqualitySameType() {
		VTAlignmentStyleProperty alignmentStyleProperty=VTAlignmentFactory.eINSTANCE.createAlignmentStyleProperty();
		VTAlignmentStyleProperty alignmentStyleProperty2=VTAlignmentFactory.eINSTANCE.createAlignmentStyleProperty();
		alignmentStyleProperty.setType(AlignmentType.LEFT);
		alignmentStyleProperty2.setType(AlignmentType.LEFT);
		assertTrue(alignmentStyleProperty.equalStyles(alignmentStyleProperty2));
	}
	@Test
	public void testEqualityDifferentType() {
		VTAlignmentStyleProperty alignmentStyleProperty=VTAlignmentFactory.eINSTANCE.createAlignmentStyleProperty();
		VTAlignmentStyleProperty alignmentStyleProperty2=VTAlignmentFactory.eINSTANCE.createAlignmentStyleProperty();
		alignmentStyleProperty.setType(AlignmentType.LEFT);
		alignmentStyleProperty2.setType(AlignmentType.RIGHT);
		assertFalse(alignmentStyleProperty.equalStyles(alignmentStyleProperty2));
	}
}
