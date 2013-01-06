package org.eclipse.emf.ecp.explorereditorbridge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.ECPAssociationClassElement;
import org.eclipse.emf.ecp.edit.EditMetaModelContext;

import java.util.Set;

public class MetaModeElementContext implements EditMetaModelContext {

	public Set<EClass> getAllSubEClasses(EClass eClass, boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNonDomainElement(EClass clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAssociationClassElement(EObject eObject) {
		// TODO Auto-generated method stub
		return false;
	}

	public EReference getPossibleContainingReference(EObject newMEInstance, EObject parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public ECPAssociationClassElement getAssociationClassElement(EObject link) {
		// TODO Auto-generated method stub
		return null;
	}

}
