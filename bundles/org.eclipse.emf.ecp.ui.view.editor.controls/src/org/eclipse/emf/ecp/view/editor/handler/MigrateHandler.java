package org.eclipse.emf.ecp.view.editor.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.editor.controls.Helper;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class MigrateHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Object selection = ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		final View view = (View) selection;

		final Map<EClass, EReference> childParentReferenceMap = new HashMap<EClass, EReference>();
		final EClass rootClass = Helper.getRootEClass(view);

		Helper.getReferenceMap(rootClass, childParentReferenceMap);

		final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(view);

		final TreeIterator<EObject> eAllContents = view.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject eObject = eAllContents.next();
			if (!Control.class.isInstance(eObject)) {
				continue;
			}
			final Control control = (Control) eObject;

			final List<EReference> bottomUpPath = Helper.getReferencePath(control.getDomainModelReference()
				.getModelFeature().getEContainingClass(),
				childParentReferenceMap);
			// control.getPathToFeature().addAll(bottomUpPath);
			((VFeaturePathDomainModelReference) control.getDomainModelReference()).getDomainModelEReferencePath()
				.clear();
			editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, control.getDomainModelReference(),
					ViewPackage.eINSTANCE.getVFeaturePathDomainModelReference_DomainModelEReferencePath(),
					bottomUpPath));
		}
		return null;
	}

}
