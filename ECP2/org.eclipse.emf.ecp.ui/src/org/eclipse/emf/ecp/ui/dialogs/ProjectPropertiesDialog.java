/**
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecp.core.ECPProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eike Stepper
 */
public class ProjectPropertiesDialog extends PropertiesDialog
{
  private final ECPProject project;

  private Text nameText;

  private Text repositoryText;

  private Text providerText;

  public ProjectPropertiesDialog(Shell parentShell, boolean editable, ECPProject project)
  {
    super(parentShell, project.getName(), "The project is " + (project.isOpen() ? "open" : "closed") + ".", editable,
        project.getProperties());
    this.project = project;
  }

  public final ECPProject getProject()
  {
    return project;
  }

  public final Text getNameText()
  {
    return nameText;
  }

  public final Text getRepositoryText()
  {
    return repositoryText;
  }

  public final Text getProviderText()
  {
    return providerText;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText("Project Properties");
  }

  @Override
  protected void createSpecialProperties(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));

    repositoryText = addTextProperty(composite, "Repository:", project.getRepository().getLabel());
    providerText = addTextProperty(composite, "Provider:", project.getProvider().getLabel());
  }
}
