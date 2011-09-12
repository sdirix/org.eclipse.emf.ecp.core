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

import org.eclipse.net4j.util.ObjectUtil;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ProvidersContentProvider;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class AddRepositoryDialog extends TitleAreaDialog
{
  private ECPProvider provider;

  private String repositoryName;

  private String repositoryLabel;

  private String repositoryDescription;

  private Map<String, Control> providerControls = new HashMap<String, Control>();

  private Map<String, ECPProperties> providerProperties = new HashMap<String, ECPProperties>();

  private ComboViewer providersViewer;

  private Composite providerStack;

  private StackLayout providerStackLayout;

  private Text repositoryNameText;

  private Text repositoryLabelText;

  private Text repositoryDescriptionText;

  public AddRepositoryDialog(Shell parentShell)
  {
    super(parentShell);
    setShellStyle(SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

    repositoryName = "";
    repositoryLabel = "";
    repositoryDescription = "";
  }

  public final ECPProvider getProvider()
  {
    return provider;
  }

  public final ECPProperties getProperties()
  {
    if (provider == null)
    {
      return null;
    }

    return providerProperties.get(provider.getName());
  }

  public final String getRepositoryName()
  {
    return repositoryName;
  }

  public final String getRepositoryLabel()
  {
    return repositoryLabel;
  }

  public final String getRepositoryDescription()
  {
    return repositoryDescription;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText("Add Repository");
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle("Add Repository");
    setTitleImage(ResourceManager.getPluginImage("org.eclipse.emf.ecp.ui", "icons/checkout_project_wiz.png"));
    setMessage("Select a provider and configure the new repository.");

    Composite area = (Composite)super.createDialogArea(parent);

    Composite composite = new Composite(area, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    composite.setLayout(new GridLayout(2, false));

    {
      Label label = new Label(composite, SWT.NONE);
      label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
      label.setText("Provider:");

      providersViewer = new ComboViewer(composite, SWT.NONE);
      Combo combo = providersViewer.getCombo();
      GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
      gd_combo.widthHint = 150;
      combo.setLayoutData(gd_combo);
      providersViewer.setContentProvider(new ProvidersContentProvider(true));
      providersViewer.setLabelProvider(new ProvidersLabelProvider());
      providersViewer.setSorter(new ViewerSorter());
      providersViewer.setInput(ECPProviderRegistry.INSTANCE);
      providersViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          provider = (ECPProvider)selection.getFirstElement();
          if (provider != null)
          {
            showProviderUI(provider);
          }
        }
      });
    }

    GridData gd_providerStack = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
    gd_providerStack.heightHint = 136;
    providerStackLayout = new StackLayout();
    providerStack = new Composite(composite, SWT.NONE);
    providerStack.setLayout(providerStackLayout);
    providerStack.setLayoutData(gd_providerStack);

    {
      Label label = new Label(composite, SWT.NONE);
      label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
      label.setText("Name:");

      repositoryNameText = new Text(composite, SWT.BORDER);
      repositoryNameText.setText(repositoryName);
      repositoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      repositoryNameText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          boolean labelUnchanged = ObjectUtil.equals(repositoryName, repositoryLabel);
          repositoryName = repositoryNameText.getText();
          if (labelUnchanged)
          {
            repositoryLabelText.setText(repositoryName);
          }
        }
      });
    }

    {
      Label label = new Label(composite, SWT.NONE);
      label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
      label.setText("Label:");

      repositoryLabelText = new Text(composite, SWT.BORDER);
      repositoryLabelText.setText(repositoryLabel);
      repositoryLabelText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      repositoryLabelText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          repositoryLabel = repositoryLabelText.getText();
        }
      });
    }

    {
      Label label = new Label(composite, SWT.NONE);
      label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
      label.setText("Description:");

      repositoryDescriptionText = new Text(composite, SWT.BORDER);
      repositoryDescriptionText.setText(repositoryDescription);
      GridData gd_repositoryDescriptionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
      gd_repositoryDescriptionText.heightHint = 36;
      repositoryDescriptionText.setLayoutData(gd_repositoryDescriptionText);
      repositoryDescriptionText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          repositoryDescription = repositoryDescriptionText.getText();
        }
      });
    }

    for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders())
    {
      if (provider.canAddRepositories())
      {
        providersViewer.setSelection(new StructuredSelection(provider));
        break;
      }
    }

    return area;
  }

  protected void showProviderUI(ECPProvider provider)
  {
    String name = provider.getName();
    Control control = providerControls.get(name);
    if (control == null)
    {
      ECPProperties properties = ECPUtil.createProperties();

      UIProvider uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(name);
      control = uiProvider.createAddRepositoryUI(providerStack, properties, repositoryNameText, repositoryLabelText,
          repositoryDescriptionText);
      providerControls.put(name, control);
      providerProperties.put(name, properties);
    }

    providerStackLayout.topControl = control;
    providerStack.layout();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(482, 528);
  }

  @Override
  protected void cancelPressed()
  {
    repositoryName = null;
    repositoryLabel = null;
    repositoryDescription = null;
    super.cancelPressed();
  }
}
