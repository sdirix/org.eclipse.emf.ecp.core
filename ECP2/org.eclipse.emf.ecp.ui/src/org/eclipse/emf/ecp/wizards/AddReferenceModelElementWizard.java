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

package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.composites.SelectModelElementComposite;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class AddReferenceModelElementWizard extends ECPWizard<SelectModelElementComposite> {
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		WizardPage page = new WizardPage("AddReferencePage") {

			public void createControl(Composite parent) {
				Composite composite = getCompositeProvider().createUI(parent);

				setPageComplete(true);
				setControl(composite);
			}
		};
		addPage(page);
		page.setTitle("Reference to set");
		page.setDescription("Select the EObject to set as the reference.");
		setWindowTitle("Add Reference");
	}

}
