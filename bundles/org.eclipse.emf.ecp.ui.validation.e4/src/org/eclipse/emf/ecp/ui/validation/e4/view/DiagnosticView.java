/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.validation.e4.view;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecp.internal.ui.validation.ValidationTreeViewerFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * View to display validation results.
 * 
 * @author jfaltermeier
 * 
 */
public class DiagnosticView {

	private static TreeViewer diagnosticTree;

	/**
	 * set input.
	 * 
	 * @param object the input
	 */
	public static void setInput(Object object) {
		// TODO better to use some kind of bridge?
		if (diagnosticTree != null) {
			diagnosticTree.setInput(object);
		}
	}

	/**
	 * Creates the diagnostic view.
	 * 
	 * @param composite the parent {@link Composite}
	 * @param selectionService the selection service to publish the selection of the tree viewer.
	 */
	@PostConstruct
	public void create(Composite composite, final ESelectionService selectionService) {
		diagnosticTree = ValidationTreeViewerFactory.createValidationViewer(composite);
		diagnosticTree
			.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					final ISelection selection = event.getSelection();
					if (IStructuredSelection.class.isInstance(selection)) {
						final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
						if (structuredSelection != null && !structuredSelection.isEmpty()) {
							if (structuredSelection.size() == 1) {
								selectionService
									.setSelection(structuredSelection.getFirstElement());
							}
							else {
								selectionService
									.setSelection(structuredSelection.toList());
							}
						}
						else {
							selectionService.setSelection(null);
						}
					}
				}
			});
	}

	/**
	 * Sets the focus to the tree.
	 */
	@Focus
	public void setFocus() {
		diagnosticTree.getTree().setFocus();
	}

}
