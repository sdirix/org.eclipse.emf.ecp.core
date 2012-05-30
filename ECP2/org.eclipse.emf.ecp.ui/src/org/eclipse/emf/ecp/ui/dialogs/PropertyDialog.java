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
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eike Stepper
 */
public class PropertyDialog extends TitleAreaDialog
{
  private final boolean keyEditable;

  private String key;

  private String value;

  private Text keyText;

  private Text valueText;

  public PropertyDialog(Shell parentShell, boolean keyEditable, String key, String value)
  {
    super(parentShell);
    this.keyEditable = keyEditable;
    this.key = key;
    this.value = value;
  }

  public PropertyDialog(Shell parentShell)
  {
    this(parentShell, true, null, null);
  }

  public final String getKey()
  {
    return key;
  }

  public final String getValue()
  {
    return value;
  }

  public final boolean isKeyEditable()
  {
    return keyEditable;
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setMessage("Message");
    setTitle("Title");
    Composite area = (Composite)super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    Label lblKey = new Label(container, SWT.NONE);
    lblKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblKey.setText("Key:");

    keyText = new Text(container, SWT.BORDER);
    keyText.setText(key == null ? "" : key);
    keyText.setEditable(keyEditable);
    keyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    keyText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        key = keyText.getText();
      }
    });

    Label lblValue = new Label(container, SWT.NONE);
    lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblValue.setText("Value:");

    valueText = new Text(container, SWT.BORDER);
    valueText.setText(value == null ? "" : value);
    valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    valueText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        value = valueText.getText();
      }
    });

    return area;
  }

  @Override
  protected void cancelPressed()
  {
    key = null;
    value = null;
    super.cancelPressed();
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 228);
  }
}
