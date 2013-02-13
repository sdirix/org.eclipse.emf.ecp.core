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

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.internal.ui.dialogs.ProjectPropertiesDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Eike Stepper
 */
public class ProjectPropertiesAction extends AbstractAction
{
  public ProjectPropertiesAction()
  {
  }

  @Override
  protected void run(IAction action, IStructuredSelection selection)
  {
    ECPProject project = (ECPProject)selection.getFirstElement();
    if (project != null)
    {
      new ProjectPropertiesDialog(getShell(), false, project).open();
    }
  }
}
