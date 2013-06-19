package org.eclipse.emf.ecp.internal.ui.view;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.View;

public interface IViewProvider {

	int NOT_APPLICABLE=-1;
	
	int canRender(EObject eObject);
	View generate(EObject eObject);
}
