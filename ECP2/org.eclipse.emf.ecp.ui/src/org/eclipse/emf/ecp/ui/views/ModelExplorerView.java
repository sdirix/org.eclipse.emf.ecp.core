/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.common.TreeViewerFactory;
import org.eclipse.emf.ecp.ui.model.ModelContentProvider;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public class ModelExplorerView extends TreeView {
	/**
	 * @author Jonas
	 * @author Eugen Neufeld
	 */
	public class DoubleClickListener implements IDoubleClickListener {

		/**
		 * Opens an EObject using the ActionHelper
		 */
		public void doubleClick(DoubleClickEvent event) {
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				Object firstElement = structuredSelection.getFirstElement();
				if (firstElement instanceof EObject) {
					ECPModelContext context = ECPUtil.getModelContext(contentProvider, structuredSelection.toArray());
					ActionHelper.openModelElement((EObject) firstElement, "modelexplorer", (ECPProject) context);
				}
			}

		}
	}

	public static final String ID = "org.eclipse.emf.ecp.ui.ModelExplorerView";

	private ModelContentProvider contentProvider;

	public ModelExplorerView() {
		super(ID);
	}

	@Override
	protected TreeViewer createViewer(final Composite parent) {
		final TreeViewer viewer = TreeViewerFactory.createModelExplorerViewer(parent, true, createLabelDecorator());
		contentProvider=(ModelContentProvider)viewer.getContentProvider();
		viewer.addDoubleClickListener(new DoubleClickListener());
		return viewer;
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		Object[] elements = getSelection().toArray();

		ECPModelContext context = ECPUtil.getModelContext(contentProvider, elements);
		if (context != null) {
			UIProvider provider = UIProviderRegistry.INSTANCE.getUIProvider(context);
			if (provider != null) {
				provider.fillContextMenu(manager, context, elements);
			}
		}

		super.fillContextMenu(manager);
	}
}
