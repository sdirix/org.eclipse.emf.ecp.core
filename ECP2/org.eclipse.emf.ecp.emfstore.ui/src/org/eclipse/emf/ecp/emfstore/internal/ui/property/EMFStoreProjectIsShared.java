package org.eclipse.emf.ecp.emfstore.internal.ui.property;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;

import org.eclipse.core.expressions.PropertyTester;

public class EMFStoreProjectIsShared extends PropertyTester {

	public EMFStoreProjectIsShared() {
		// TODO Auto-generated constructor stub
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		ProjectSpace ps = EMFStoreProvider.INSTANCE.getProjectSpace((InternalProject) receiver);
		Boolean result = Boolean.valueOf(ps.isShared());
		return result.equals(expectedValue);
	}

}
