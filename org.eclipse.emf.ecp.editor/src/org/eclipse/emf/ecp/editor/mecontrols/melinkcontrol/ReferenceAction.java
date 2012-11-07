/*******************************************************************************
 * Copyright 2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract class that models a action involving a reference. 
 *  
 */
public abstract class ReferenceAction extends Action {

	private EReference eReference;
	private EObject modelElement;

	/**
	 * Checks whether the multiplicity of the reference is valid.
	 * 
	 * @param silent
	 * 		Whether a message box should be shown in case the multiplicity is invalid
	 * @return true, if the multiplicity is valid, false otherwise
	 */
	protected boolean checkMultiplicity(boolean silent) {
		if (eReference.getUpperBound() == 1 || eReference.getUpperBound() == -1) {
			return true;
		}
		Object object = getModelElement().eGet(eReference);
		if (object instanceof EList<?>) {
			@SuppressWarnings("unchecked")
			EList<EObject> eList = (EList<EObject>) object;
			if (eList.size() < eReference.getUpperBound()) {
				return true;
			} else {
				MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				box.setMessage("Reference " + eReference.getName() + " has a multiplicity of "
					+ eReference.getUpperBound() + ". Please remove referenced elements before you add new.");
				box.open();
				return false;
			}

		}
		return false;

	}

	/**
	 * Returns the reference involved in this action.
	 * 
	 * @return the reference
	 */
	public EReference getReference() {
		return eReference;
	}

	/**
	 * Sets the reference involved in this action.
	 * 
	 * @param eReference
	 * 			the reference to be set
	 */
	public void setReference(EReference eReference) {
		this.eReference = eReference;
	}

	/**
	 * Returns the model element that is affected by this reference action.
	 * 
	 * @return the model element belonging to this action
	 */
	public EObject getModelElement() {
		return modelElement;
	}

	/**
	 * Sets the affected model element.
	 * 
	 * @param modelElement
	 * 			the model element to be set
	 */
	public void setModelElement(EObject modelElement) {
		this.modelElement = modelElement;
	}

}
