/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emf.ecp.view.editor.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.view.model.VView;

/**
 * Helper class for editor controls.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class Helper {

	private Helper() {

	}

	public static EClass getRootEClass(ECPProject project) {
		return ((VView) project.getContents().get(0)).getRootEClass();
	}

	public static EClass getRootEClass(EObject eObject) {
		EObject testObject = eObject;
		// while (!(View.class.isInstance(testObject) || TreeCategory.class.isInstance(testObject)
		// && ((TreeCategory) testObject).getTargetFeature() != null)
		// && testObject != null) {
		while (!VView.class.isInstance(testObject)
			&& testObject != null) {
			testObject = testObject.eContainer();
		}
		if (VView.class.isInstance(testObject)) {
			return ((VView) testObject).getRootEClass();
			// } else if (TreeCategory.class.isInstance(testObject)) {
			// return ((EReference) ((TreeCategory) testObject).getTargetFeature())
			// .getEReferenceType();
		}
		return getRootEClass(ECPUtil.getECPProjectManager().getProject(eObject));
	}

	public static void getReferenceMap(EClass parent,
		Map<EClass, EReference> childParentReferenceMap) {
		for (final EReference eReference : parent.getEAllContainments()) {
			if (eReference.getEReferenceType() != parent) {
				childParentReferenceMap.put(eReference.getEReferenceType(), eReference);
				getReferenceMap(eReference.getEReferenceType(), childParentReferenceMap);
			}
		}
	}

	public static List<EReference> getReferencePath(EClass selectedClass,
		Map<EClass, EReference> childParentReferenceMap) {
		final List<EReference> bottomUpPath = new ArrayList<EReference>();

		while (childParentReferenceMap.containsKey(selectedClass)) {
			final EReference parentReference = childParentReferenceMap.get(selectedClass);
			bottomUpPath.add(parentReference);
			selectedClass = parentReference.getEContainingClass();
		}
		Collections.reverse(bottomUpPath);
		return bottomUpPath;
	}

	/**
	 * Determines all EClasses that can be reached from this EClass.
	 * 
	 * @param root the EClass to analyze
	 * @return the Set of ECLasses which can be references from this control
	 */
	public static Set<EClass> getDatasegmentSubclasses(EClass root) {
		final Set<EClass> possibleSegments = new LinkedHashSet<EClass>();
		getDatasegmentSubclasses(root, possibleSegments);
		return possibleSegments;

	}

	private static void getDatasegmentSubclasses(EClass root, Set<EClass> possibleSegments) {
		if (possibleSegments.contains(root)) {
			return;
		}
		possibleSegments.add(root);
		for (final EReference eReference : root.getEAllContainments()) {
			getDatasegmentSubclasses(eReference.getEReferenceType(), possibleSegments);
		}
	}
}
