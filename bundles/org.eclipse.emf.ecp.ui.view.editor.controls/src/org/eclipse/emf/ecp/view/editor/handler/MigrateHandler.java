/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
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

/**
 * The Handler for migrating existing models.
 * 
 * @author Eugen Neufeld
 * 
 */
// TODO needed?
public class MigrateHandler extends AbstractHandler {
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
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
			final Setting setting = control.getDomainModelReference().getIterator().next();
			final List<EReference> bottomUpPath = Helper.getReferencePath(setting.getEStructuralFeature()
				.getEContainingClass(),
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
