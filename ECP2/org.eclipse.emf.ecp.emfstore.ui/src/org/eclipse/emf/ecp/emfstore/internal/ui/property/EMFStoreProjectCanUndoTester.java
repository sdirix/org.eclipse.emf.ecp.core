/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.property;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;

import org.eclipse.core.expressions.PropertyTester;

/**
 * @author Tobias Verhoeven
 * 
 */
public class EMFStoreProjectCanUndoTester extends PropertyTester {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
	 * java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		ProjectSpace ps = EMFStoreProvider.INSTANCE.getProjectSpace((InternalProject) receiver);
		Boolean result = Boolean.valueOf(ps.hasUncommitedChanges());
		return result;
	}
}
