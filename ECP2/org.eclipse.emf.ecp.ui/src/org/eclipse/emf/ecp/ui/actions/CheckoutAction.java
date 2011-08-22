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

import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.ui.dialogs.CheckoutDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public class CheckoutAction extends AbstractAction
{
  public CheckoutAction()
  {
  }

  @Override
  protected void run(IAction action, IStructuredSelection selection)
  {
    for (Iterator<?> it = selection.iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof ECPCheckoutSource)
      {
        ECPCheckoutSource checkoutSource = (ECPCheckoutSource)element;
        CheckoutDialog dialog = new CheckoutDialog(getShell(), checkoutSource);
        if (dialog.open() == CheckoutDialog.OK)
        {
          String projectName = dialog.getProjectName();
          ECPProperties projectProperties = dialog.getProjectProperties();
          checkoutSource.checkout(projectName, projectProperties);
        }
      }
    }
  }
}
