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
import org.eclipse.emf.ecp.ui.platform.Activator;
import org.eclipse.emf.ecp.wizards.AddRepositoryPage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

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
    RepositoryWizard repositoryWizard = new RepositoryWizard();
    repositoryWizard.setWindowTitle("Add Repository");
    WizardDialog wizardDialog = new WizardDialog(shell, repositoryWizard);
    // AddRepositoryDialog dialog = new AddRepositoryDialog(shell);
    if (wizardDialog.open() == WizardDialog.OK)
    {
      // ECPProvider provider = dialog.getProvider();
      // ECPProperties properties = dialog.getProperties();
      //
      // String name = dialog.getRepositoryName();
      // String label = dialog.getRepositoryLabel();
      // String description = dialog.getRepositoryDescription();
      //
      // ECPRepositoryManager.INSTANCE.addRepository(provider, name, label, description, properties);
    }
  }

  private class RepositoryWizard extends Wizard
  {
    private ECPRepository selectedRepository = null;

    private AddRepositoryPage addPage;

    /**
     * . ({@inheritDoc})
     */
    @Override
    public void addPages()
    {
      addPage = new AddRepositoryPage("AddRepository", null);
      addPage(addPage);
    }

    /**
     * . ({@inheritDoc})
     */
    @Override
    public boolean canFinish()
    {

      return selectedRepository != null || addPage.getProperties() != null && addPage.getRepositoryName() != null;

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
      if (selectedRepository == null)
      {
        selectedRepository = ECPRepositoryManager.INSTANCE.addRepository(addPage.getProvider(),
            addPage.getRepositoryName(), addPage.getRepositoryLabel() == null ? "" : addPage.getRepositoryLabel(),
            addPage.getRepositoryDescription() == null ? "" : addPage.getRepositoryDescription(),
            addPage.getProperties());
      }
      return true;
    }
  }
}
