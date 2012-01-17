package org.eclipse.emf.ecp.explorereditorbridge;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.EditorMetamodelContext;
import org.eclipse.emf.ecp.ui.model.ECPAssociationClassElement;

public class MetaModeElementContext implements EditorMetamodelContext {

	

	@Override
	public Set<EClass> getAllSubEClasses(EClass eClass, boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNonDomainElement(EClass clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAssociationClassElement(EObject eObject) {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public EReference getPossibleContainingReference(EObject newMEInstance,
			EObject parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECPAssociationClassElement getAssociationClassElement(EObject link) {
		// TODO Auto-generated method stub
		return null;
	}


}
