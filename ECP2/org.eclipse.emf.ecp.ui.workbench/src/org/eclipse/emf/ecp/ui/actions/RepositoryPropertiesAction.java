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
import org.eclipse.emf.ecp.internal.ui.dialogs.RepositoryPropertiesDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Eike Stepper
 */
public class RepositoryPropertiesAction extends AbstractAction
{
  public RepositoryPropertiesAction()
  {
  }

  @Override
  protected void run(IAction action, IStructuredSelection selection)
  {
    ECPRepository repository = (ECPRepository)selection.getFirstElement();
    if (repository != null)
    {
      new RepositoryPropertiesDialog(getShell(), false, repository).open();
    }
  }
}
