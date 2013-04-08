/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.emfstore.ui.search;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author Eugen Neufeld
 *         This class shows the search model element dialog for emfstore projects
 */
public class SearchModelElementHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		InternalProject project = (InternalProject)ECPProjectManager.INSTANCE.getProject(selection.getFirstElement());

		ESLocalProject projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
		
		if (projectSpace == null) {
			return null;
		}

		Set<EObject> eObjects = projectSpace.getAllModelElements();

		if (project == null) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information",
				"You must first select the Project.");
		} else {
			ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			AdapterFactoryLabelProvider adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(HandlerUtil.getActiveShell(event),
				adapterFactoryLabelProvider);
			dialog.setElements(eObjects.toArray());
			dialog.setMultipleSelection(false);
			dialog.setMessage("Enter model element name prefix or pattern (e.g. *Trun?)");
			dialog.setTitle("Search Model Element");
			if (dialog.open() == Dialog.OK) {
				Object[] selections = dialog.getResult();

				if (selections != null && selections.length == 1 && selections[0] instanceof EObject) {
					ECPHandlerHelper.openModelElement((EObject) selections[0],
						 project);
				}
			}
			adapterFactoryLabelProvider.dispose();
			composedAdapterFactory.dispose();
		}

		return null;
	}

}
