package org.eclipse.emf.ecp.view.custom.ui.swt.test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class CustomControlStubTester implements ECPApplicableTester {

	public CustomControlStubTester() {
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		return NOT_APPLICABLE;
	}

	public int isApplicable(VDomainModelReference domainModelReference) {
		// TODO Auto-generated method stub
		return !domainModelReference.getIterator().hasNext() ? 1 : NOT_APPLICABLE;
	}

}
