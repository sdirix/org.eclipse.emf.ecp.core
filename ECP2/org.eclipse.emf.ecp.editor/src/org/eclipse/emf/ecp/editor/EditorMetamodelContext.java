package org.eclipse.emf.ecp.editor;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.ui.model.ECPAssociationClassElement;

public interface EditorMetamodelContext {

	//TODO: Check if these methods are really needed
	
	boolean isNonDomainElement(EClass eClass);

	Set<EClass> getAllSubEClasses(EClass clazz, boolean b);

	EReference getPossibleContainingReference(EObject newMEInstance,
			EObject parent);

	ECPAssociationClassElement getAssociationClassElement(EObject link);

	boolean isAssociationClassElement(EObject modelElement);

}
