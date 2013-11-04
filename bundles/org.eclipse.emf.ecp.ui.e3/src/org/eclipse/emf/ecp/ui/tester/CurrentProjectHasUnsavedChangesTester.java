/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Tests if the currently selected project has unsaved changes.
 * 
 * @author jfaltermeier
 * 
 */
public class CurrentProjectHasUnsavedChangesTester extends PropertyTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
	 *      java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return false;
		}
		final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return false;
		}
		final ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
		if (selection instanceof TreeSelection) {
			final TreeSelection treeSelection = (TreeSelection) selection;
			final Object firstElement = treeSelection.getFirstElement();

			if (firstElement instanceof ECPProject) {
				return ((ECPProject) firstElement).hasDirtyContents();
			}

			if (firstElement instanceof EObject) {
				final TreePath[] treePaths = treeSelection.getPathsFor(firstElement);

				if (treePaths.length != 1) {
					return false;
				}

				final Object root = treePaths[0].getFirstSegment();
				if (root instanceof ECPProject) {
					final ECPProject project = (ECPProject) root;
					return project.hasDirtyContents();
				}
			}
		}
		return false;
	}

}
