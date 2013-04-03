package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class LinkCellEditorTester implements ECPApplicableTester {

	public LinkCellEditorTester() {
		// TODO Auto-generated constructor stub
	}

	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(null);
		if (EReference.class.isInstance(feature)) {
			return 1;
		}
		return NOT_APPLICABLE;
	}

}
