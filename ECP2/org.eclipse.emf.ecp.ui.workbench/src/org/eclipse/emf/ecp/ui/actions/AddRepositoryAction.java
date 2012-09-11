/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.actions;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.common.UICreateProject;
import org.eclipse.emf.ecp.ui.platform.Activator;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.AddRepositoryWizard;
import org.eclipse.emf.ecp.wizards.CreateProjectWizard;
import org.eclipse.emf.ecp.wizards.WizardUICallback;
import org.eclipse.emf.ecp.wizards.page.AddRepositoryPage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public class AddRepositoryAction extends Action
{
  private final Shell shell;

  public AddRepositoryAction(Shell shell)
  {
    super("Add Repository", Activator.getImageDescriptor("icons/add.gif"));
    setToolTipText("Add a new model repository");
    this.shell = shell;
  }

  @Override
  public void run()
  {
	  
	  HandlerHelper.createRepository(new WizardUICallback<AddRepositoryComposite>(shell, new AddRepositoryWizard()));
  }

}
