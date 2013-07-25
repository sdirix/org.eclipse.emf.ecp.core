package org.eclipse.emf.ecp.view.editor.handler;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.view.editor.controls.Helper;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.Set;

public class GenerateControlsHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object selection = ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event)).getFirstElement();
		if (selection == null) {
			return null;
		}
		final ECPProject project = ECPUtil.getECPProjectManager().getProject(selection);
		final EClass rootClass = Helper.getRootEClass((EObject) selection);
		SelectAttributesDialog sad = new SelectAttributesDialog(project, rootClass, HandlerUtil.getActiveShell(event));
		int result = sad.open();
		if (result == Window.OK) {
			final EClass datasegment = sad.getDataSegment();
			final Set<EStructuralFeature> features = sad.getSelectedFeatures();
			final CompositeCollection compositeCollection = (CompositeCollection) selection;
			AdapterFactoryEditingDomain.getEditingDomainFor(compositeCollection).getCommandStack()
				.execute(new ChangeCommand(compositeCollection) {

					@Override
					protected void doExecute() {
						ControlGenerator.addControls(rootClass, compositeCollection, datasegment, features);
					}
				});

		}

		return null;
	}

}
