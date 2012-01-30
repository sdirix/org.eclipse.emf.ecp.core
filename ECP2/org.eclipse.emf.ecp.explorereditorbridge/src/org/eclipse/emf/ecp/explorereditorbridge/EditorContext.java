package org.eclipse.emf.ecp.explorereditorbridge;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.editor.EditorMetamodelContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContextListener;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

public class EditorContext implements EditorModelelementContext {

	private final EObject modelElement;
	private MetaModeElementContext metaModeElementContext;

	public EditorContext(EObject modelElement) {
		this.modelElement = modelElement;
		metaModeElementContext = new MetaModeElementContext();
	}

	public void addModelElementContextListener(
			EditorModelelementContextListener modelElementContextListener) {
		// TODO Auto-generated method stub

	}

	public void removeModelElementContextListener(
			EditorModelelementContextListener modelElementContextListener) {
		// TODO Auto-generated method stub

	}



	public Collection<EObject> getAllModelElementsbyClass(EClass clazz,
			boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	public EditingDomain getEditingDomain() {
		// TODO Auto-generated method stub
		return AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);
	}

	public EditorMetamodelContext getMetaModelElementContext() {
		// TODO Auto-generated method stub
		return metaModeElementContext;
	}

	public boolean contains(EObject eObject) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}
