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

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
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
public class CheckoutDialog extends TitleAreaDialog
{
  private final ECPCheckoutSource checkoutSource;

  private String projectName;

  private ECPProperties projectProperties = ECPUtil.createProperties();

  private Text projectNameText;

  private Composite providerStack;

  public CheckoutDialog(Shell parentShell, ECPCheckoutSource checkoutSource)
  {
    super(parentShell);
    setShellStyle(SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
    this.checkoutSource = checkoutSource;

    projectName = checkoutSource.getDefaultCheckoutName();
    if (projectName == null)
    {
      projectName = "";
    }
  }

  public final ECPCheckoutSource getCheckoutSource()
  {
    return checkoutSource;
  }

  public String getProjectName()
  {
    return projectName;
  }

  public ECPProperties getProjectProperties()
  {
    return projectProperties;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText("Checkout");
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle("Checkout");
    setTitleImage(Activator.getImage("icons/checkout_project_wiz.png"));

    UIProvider uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(checkoutSource);

    ECPRepository repository = checkoutSource.getRepository();
    if (checkoutSource == repository)
    {
      setMessage("Checkout " + repository.getLabel() + ".");
    }
    else
    {
      setMessage("Checkout " + uiProvider.getText(checkoutSource) + " from " + repository.getLabel() + ".");
    }

    Composite area = (Composite)super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(1, false));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    composite.setLayout(new GridLayout(2, false));

    Label lblNewLabel = new Label(composite, SWT.NONE);
    lblNewLabel.setText("Project name:");

    projectNameText = new Text(composite, SWT.BORDER);
    projectNameText.setText(projectName);
    projectNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    projectNameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        projectName = projectNameText.getText();
      }
    });
    StackLayout providerStackLayout = new StackLayout();
    providerStack = new Composite(composite, SWT.NONE);
    providerStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    providerStack.setLayout(providerStackLayout);
    Control checkoutUI = uiProvider.createCheckoutUI(providerStack, checkoutSource, projectProperties);
    if (checkoutUI != null)
    {
      // checkoutUI.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      providerStackLayout.topControl = checkoutUI;
      providerStack.layout();
    }

    return area;
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 450);
  }

  @Override
  protected void cancelPressed()
  {
    projectName = null;
    projectProperties = null;
    super.cancelPressed();
  }
}
