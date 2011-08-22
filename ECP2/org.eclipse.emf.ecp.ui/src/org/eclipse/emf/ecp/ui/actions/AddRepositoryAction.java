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
package org.eclipse.emf.ecp.ui.actions;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.dialogs.AddRepositoryDialog;

import org.eclipse.jface.action.Action;
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
    AddRepositoryDialog dialog = new AddRepositoryDialog(shell);
    if (dialog.open() == AddRepositoryDialog.OK)
    {
      ECPProvider provider = dialog.getProvider();
      ECPProperties properties = dialog.getProperties();

      String name = dialog.getRepositoryName();
      String label = dialog.getRepositoryLabel();
      String description = dialog.getRepositoryDescription();

      ECPRepositoryManager.INSTANCE.addRepository(provider, name, label, description, properties);
    }
  }
}
