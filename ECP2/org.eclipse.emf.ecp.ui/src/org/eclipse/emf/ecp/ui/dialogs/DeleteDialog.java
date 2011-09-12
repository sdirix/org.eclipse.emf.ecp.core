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

import org.eclipse.emf.ecp.core.util.ECPDeletable;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import java.util.List;

/**
 * @author Eike Stepper
 */
public class DeleteDialog extends TitleAreaDialog
{
  private final List<ECPDeletable> deletables;

  public DeleteDialog(Shell parentShell, List<ECPDeletable> deletables)
  {
    super(parentShell);
    this.deletables = deletables;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText("Delete");
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    int count = deletables.size();

    setTitle("Delete");
    setTitleImage(ResourceManager.getPluginImage("org.eclipse.emf.ecp.ui", "icons/delete_wiz.png"));
    setMessage("Are you sure you want to delete " + count + " element" + (count == 1 ? "" : "s") + "?");

    Composite area = (Composite)super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(1, false));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    composite.setLayout(new GridLayout(1, false));

    Button btnDontAskAgain = new Button(composite, SWT.CHECK);
    btnDontAskAgain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
    btnDontAskAgain.setText("Don't ask again");

    return area;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL, false);
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(381, 182);
  }
}
