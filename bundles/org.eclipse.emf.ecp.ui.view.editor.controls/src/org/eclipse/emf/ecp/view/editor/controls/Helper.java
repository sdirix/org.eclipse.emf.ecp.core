package org.eclipse.emf.ecp.view.editor.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.view.model.View;

public final class Helper {

	public static EClass getRootEClass(ECPProject project) {
		return ((View) project.getContents().get(0)).getRootEClass();
	}

	public static EClass getRootEClass(EObject eObject) {
		EObject testObject = eObject;
		// while (!(View.class.isInstance(testObject) || TreeCategory.class.isInstance(testObject)
		// && ((TreeCategory) testObject).getTargetFeature() != null)
		// && testObject != null) {
		while (!View.class.isInstance(testObject)
			&& testObject != null) {
			testObject = testObject.eContainer();
		}
		if (View.class.isInstance(testObject)) {
			return ((View) testObject).getRootEClass();
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
}
