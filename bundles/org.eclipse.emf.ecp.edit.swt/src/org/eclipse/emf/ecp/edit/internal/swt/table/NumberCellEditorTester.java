package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class NumberCellEditorTester implements ECPApplicableTester {

	public NumberCellEditorTester() {
	}

	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		EStructuralFeature feature=(EStructuralFeature)itemPropertyDescriptor.getFeature(null);
		if (EAttribute.class.isInstance(feature)) {
			Class<?> instanceClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			if(Number.class.isAssignableFrom(instanceClass)){
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}

}
