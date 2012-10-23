package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;

import java.util.Collection;

/**
 * Helper class for creating a dialog which allows to select an {@link EClass}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SelectModelElementHelper extends AbstractModelElementHelper {

	/**
	 * Constructor for providing the filter data manually.
	 * 
	 * @param ePackages all {@link EPackage}s that can be used
	 * @param unsupportedEPackages {@link EPackage}s that are not available for selection
	 * @param filteredEPackages {@link EPackage}s which are selectable
	 * @param filteredEClasses {@link EClass}es which are selectable
	 */
	public SelectModelElementHelper(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
		Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses) {
		super(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
	}

	/**
	 * Constructor for providing only the {@link ECPProject}.
	 * 
	 * @param project the {@link ECPProject} to read the settings from
	 */
	public SelectModelElementHelper(ECPProject project) {
		super(project);
	}

	@Override
	protected boolean isCheckedTree() {
		return false;
	}
}
