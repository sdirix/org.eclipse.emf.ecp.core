/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.editor.ecore.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emfforms.spi.swt.treemasterdetail.actions.MasterDetailAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * @author Lucas Koehler
 *
 */
public class CreateNewInstanceAction extends MasterDetailAction {

	// TODO proper icon
	private static final String ICON_PATH = "icons/EcoreModelFile.gif";
	private static final String ACTION_NAME = "Create New Dynamic Instance";

	/**
	 * Default constructor.
	 */
	public CreateNewInstanceAction() {
		setLabel(ACTION_NAME);
		setImagePath(ICON_PATH);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.treemasterdetail.actions.MasterDetailAction#shouldShow(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean shouldShow(EObject eObject) {
		return EClass.class.isInstance(eObject);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.treemasterdetail.actions.MasterDetailAction#execute(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void execute(EObject object) {
		final EClass eClass = EClass.class.cast(object);
		final Diagnostic validate = Diagnostician.INSTANCE.validate(eClass);
		if (validate.getSeverity() == Diagnostic.OK) {
			final CreateNewInstaceWizard wizard = new CreateNewInstaceWizard(eClass);
			new WizardDialog(getTreeViewer().getTree().getShell(), wizard).open();
		} else {
			final MultiStatus status = new MultiStatus("org.eclipse.emfforms.editor.ecore", 4,
				"Can not create a new instance file for EClass " + eClass.getName() + " because it is invalid.", null);
			for (final Diagnostic d : validate.getChildren()) {
				status.add(new Status(IStatus.ERROR, "org.eclipse.emfforms.editor.ecore", d.getMessage()));
			}
			ErrorDialog.openError(getTreeViewer().getTree().getShell(), "Error", null, status);
		}
	}

}
