package org.eclipse.emf.ecp.view.model.generator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;

public class ViewProvider implements IViewProvider {

	public ViewProvider() {
	}

	public View generate(EObject eObject) {
		View view = ViewFactory.eINSTANCE.createView();
		Category category = ViewFactory.eINSTANCE.createCategory();
		view.getCategorizations().add(category);
		
		Column column = ViewFactory.eINSTANCE.createColumn();
		category.setComposite(column);
		
		for(EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
			
			if (isInvalidFeature(feature)) {
				continue;
			}
			
			Control control = ViewFactory.eINSTANCE.createControl();
			control.setTargetFeature(feature);
			column.getComposites().add(control);
		}
		
		return view;
	}
	
	
	private boolean isInvalidFeature(EStructuralFeature feature) {
		return isContainerReference(feature)
				|| isTransient(feature)
				|| isVolatile(feature);
	}

	private boolean isContainerReference(EStructuralFeature feature) {
		if (feature instanceof EReference){
			EReference reference = (EReference) feature;
			if (reference.isContainer()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isTransient(EStructuralFeature feature) {
		return feature.isTransient();
	}
	
	private boolean isVolatile(EStructuralFeature feature) {
		return feature.isVolatile();
	}

	@Override
	public int canRender(EObject eObject) {
		return 1;
	}
}
