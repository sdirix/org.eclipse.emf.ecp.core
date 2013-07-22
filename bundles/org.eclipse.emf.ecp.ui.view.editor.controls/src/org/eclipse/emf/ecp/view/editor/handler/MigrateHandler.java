package org.eclipse.emf.ecp.view.editor.handler;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.editor.controls.Helper;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MigrateHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object selection = ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		View view = (View) selection;

		Map<EClass, EReference> childParentReferenceMap = new HashMap<EClass, EReference>();
		EClass rootClass = Helper.getRootEClass(view);

		Helper.getReferenceMap(rootClass, childParentReferenceMap);

		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(view);

		TreeIterator<EObject> eAllContents = view.eAllContents();
		while (eAllContents.hasNext()) {
			EObject eObject = eAllContents.next();
			if (!Control.class.isInstance(eObject)) {
				continue;
			}
			Control control = (Control) eObject;

			List<EReference> bottomUpPath = Helper.getReferencePath(control.getTargetFeature().getEContainingClass(),
				childParentReferenceMap);
			// control.getPathToFeature().addAll(bottomUpPath);
			control.getPathToFeature().clear();
			editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, control, ViewPackage.eINSTANCE.getControl_PathToFeature(),
					bottomUpPath));
		}
		return null;
	}

}
