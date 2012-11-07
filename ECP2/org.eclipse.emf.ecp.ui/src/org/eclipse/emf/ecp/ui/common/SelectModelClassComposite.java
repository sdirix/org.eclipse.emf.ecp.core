/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
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
public class SelectModelClassComposite extends AbstractEClassTreeSelectionComposite {

	/**
	 * Constructor for providing the filter data manually.
	 * 
	 * @param ePackages all {@link EPackage}s that can be used
	 * @param unsupportedEPackages {@link EPackage}s that are not available for selection
	 * @param filteredEPackages {@link EPackage}s which are selectable
	 * @param filteredEClasses {@link EClass}es which are selectable
	 */
	public SelectModelClassComposite(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
		Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses) {
		super(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
	}

	/**
	 * Constructor for providing only the {@link ECPProject}.
	 * 
	 * @param project the {@link ECPProject} to read the settings from
	 */
	public SelectModelClassComposite(ECPProject project) {
		super(project);
	}

	@Override
	protected boolean isCheckedTree() {
		return false;
	}
}
