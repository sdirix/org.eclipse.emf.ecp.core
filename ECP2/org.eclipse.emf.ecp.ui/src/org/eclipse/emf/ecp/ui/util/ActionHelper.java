/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas Helming - initial API and implementation
 * Eugen Neufeld - simplification of class
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.internal.ui.Messages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author helming
 * @author Eugen Neufeld
 */
public final class ActionHelper {

	private ActionHelper() {

	}

	/**
	 * This opens the model element.
	 * 
	 * @param me
	 *            ModelElement to open
	 * @param sourceView
	 *            the view that requested the open model element
	 * @param ecpProject the {@link ECPProject} of the model element
	 */
	public static void openModelElement(final EObject me, final String sourceView, ECPProject ecpProject) {
		if (me == null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
				Messages.ActionHelper_ErrorTitle_ElementDeleted, Messages.ActionHelper_ErrorMessage_ElementDeleted);
			return;
		}
		IConfigurationElement[] modelelementopener = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.ui.modelelementopener"); //$NON-NLS-1$
		ModelElementOpener bestCandidate = null;
		int bestValue = -1;
		for (IConfigurationElement element : modelelementopener) {
			modelelementopener = null;
			try {
				ModelElementOpener modelelementOpener = (ModelElementOpener) element.createExecutableExtension("class"); //$NON-NLS-1$
				int value = modelelementOpener.canOpen(me);
				if (value > bestValue) {
					bestCandidate = modelelementOpener;
					bestValue = value;
				}
			} catch (CoreException e) {

				Activator.log(e);
			}
		}
		// TODO: find solution
		// ECPWorkspaceManager.getObserverBus().notify(ModelElementOpenObserver.class).onOpen(me, sourceView, name);
		// BEGIN SUPRESS CATCH EXCEPTION
		try {
			bestCandidate.openModelElement(me, ecpProject);
		} catch (RuntimeException e) {
			Activator.log(e);
		}
		// END SUPRESS CATCH EXCEPTION

	}

}
