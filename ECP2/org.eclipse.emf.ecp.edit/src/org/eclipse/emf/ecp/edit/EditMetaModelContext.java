package org.eclipse.emf.ecp.edit;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

//FIXME: is this needed
public interface EditMetaModelContext {

	boolean isNonDomainElement(EClass eClass);

	Set<EClass> getAllSubEClasses(EClass clazz, boolean b);

	EReference getPossibleContainingReference(EObject newMEInstance,
			EObject parent);

	ECPAssociationClassElement getAssociationClassElement(EObject link);

	boolean isAssociationClassElement(EObject modelElement);
}
