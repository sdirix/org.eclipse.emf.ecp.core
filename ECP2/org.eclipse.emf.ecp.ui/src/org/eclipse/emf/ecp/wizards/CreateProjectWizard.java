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

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.common.CreateProjectComposite;
import org.eclipse.emf.ecp.ui.common.CreateProjectComposite.CreateProjectChangeListener;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Neufeld
 */
public class CreateProjectWizard extends ECPWizard<CreateProjectComposite> {

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
		List<ECPProvider> providers = new ArrayList<ECPProvider>();
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			if (provider.hasUnsharedProjectSupport()) {
				providers.add(provider);
			}
		}
		WizardPage wp = new WizardPage("CreateProject") //$NON-NLS-1$
		{

			public void createControl(Composite parent) {
				Composite composite = getUIProvider().createUI(parent);
				getUIProvider().setListener(new CreateProjectChangeListener() {

					public void providerChanged(ECPProvider provider) {
					}

					public void projectNameChanged(String projectName) {
						if (projectName != null) {
							setPageComplete(true);
						} else {
							setPageComplete(false);
						}
					}
				});
				setPageComplete(false);
				setControl(composite);
			}
		};
		addPage(wp);
		String title = Messages.CreateProjectWizard_PageTitle_CreateProjectDefault;
		String message = Messages.CreateProjectWizard_PageMessage_CreateProjectDefault;
		if (providers.size() == 1) {
			title = Messages.CreateProjectWizard_PageTitle_Create + providers.get(0).getLabel()
				+ Messages.CreateProjectWizard_PageTitle_Project;
			message = Messages.CreateProjectWizard_PageMessage_CreateProject;
		}
		wp.setTitle(title);
		wp.setImageDescriptor(Activator.getImageDescriptor("icons/checkout_project_wiz.png")); //$NON-NLS-1$
		wp.setMessage(message);
		setWindowTitle(title);
	}

}
