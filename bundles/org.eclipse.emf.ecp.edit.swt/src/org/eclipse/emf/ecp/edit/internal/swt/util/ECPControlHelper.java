/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Jonas
 * 
 */
public abstract class ECPControlHelper {
	/**
	 * @param eObject the object to add a reference to
	 * @param newMEInstance the object which is added as a reference
	 * @param eReference the reference to be modified
	 * @param editingDomain the editing domain to execute commands on
	 */
	public static void addModelElementInReference(EObject eObject, EObject newMEInstance, EReference eReference,
		EditingDomain editingDomain) {

		// add the new object to the reference
		// Object object = modelElement.eGet(eReference);
		if (eReference.getUpperBound() == 1) {
			editingDomain.getCommandStack().execute(
				SetCommand.create(editingDomain, eObject, eReference, newMEInstance));
		} else {
			editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, eObject, eReference, newMEInstance));
		}
	}

}
