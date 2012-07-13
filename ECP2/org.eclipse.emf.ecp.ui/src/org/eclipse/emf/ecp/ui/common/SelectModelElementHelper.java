package org.eclipse.emf.ecp.ui.common;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;

public class SelectModelElementHelper extends AbstractModelElementHelper {

	/**
	 * @param ePackages
	 * @param unsupportedEPackages
	 * @param filteredEPackages
	 * @param filteredEClasses
	 */
	public SelectModelElementHelper(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
		Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses) {
		super(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
	}

	/**
	 * @param project
	 */
	public SelectModelElementHelper(ECPProject project) {
		super(project);
	}

	@Override
	protected boolean isCheckedTree() {
		return false;
	}
}
