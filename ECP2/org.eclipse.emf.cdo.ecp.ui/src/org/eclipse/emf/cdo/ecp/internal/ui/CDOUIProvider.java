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
package org.eclipse.emf.cdo.ecp.internal.ui;

import org.eclipse.emf.cdo.ecp.internal.core.CDOBranchWrapper;
import org.eclipse.emf.cdo.ecp.internal.core.CDOProjectData;
import org.eclipse.emf.cdo.ecp.internal.core.CDOProvider;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.workspace.CDOWorkspace;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceUtil;

import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.ui.DefaultPropertySource;
import org.eclipse.net4j.util.ui.container.ElementWizardComposite;

import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

/**
 * @author Eike Stepper
 */
public class CDOUIProvider extends DefaultUIProvider
{
  public CDOUIProvider()
  {
    super(CDOProvider.NAME);
  }

  @Override
  public String getText(Object element)
  {
    if (element instanceof CDOResourceNode)
    {
      CDOResourceNode node = (CDOResourceNode)element;
      return node.getName();
    }

    return super.getText(element);
  }

  @Override
  public Image getImage(Object element)
  {
    if (element instanceof CDOBranchWrapper)
    {
      return Activator.getImage("icons/branch.gif");
    }

    if (element instanceof CDOResource)
    {
      return Activator.getImage("icons/resource.gif");
    }

    if (element instanceof CDOResourceFolder)
    {
      return Activator.getImage("icons/folder.gif");
    }

    return super.getImage(element);
  }

  @Override
  public Object getAdapter(Object adaptable, Class<?> adapterType)
  {
    if (adapterType == IPropertySourceProvider.class && adaptable instanceof InternalProject)
    {
      final InternalProject project = (InternalProject)adaptable;
      if (project.getProvider().getName().equals(CDOProvider.NAME))
      {
        return new IPropertySourceProvider()
        {
          public IPropertySource getPropertySource(Object object)
          {
            CDOProjectData data = CDOProvider.getProjectData(project);
            CDOWorkspace workspace = data.getWorkspace();
            return new DefaultPropertySource<CDOWorkspace>(workspace, CDOWorkspaceUtil.PROPERTIES);
          }
        };
      }
    }

    return super.getAdapter(adaptable, adapterType);
  }

  @Override
  public Control createAddRepositoryUI(Composite parent, final ECPProperties repositoryProperties,
      final Text repositoryNameText, Text repositoryLabelText, Text repositoryDescriptionText)
  {
    GridLayout mainLayout = new GridLayout(1, false);
    mainLayout.marginWidth = 0;
    mainLayout.marginHeight = 0;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(mainLayout);

    GridLayout group1Layout = new GridLayout(1, false);
    group1Layout.marginWidth = 0;
    group1Layout.marginHeight = 0;

    Group group1 = new Group(composite, SWT.NONE);
    group1.setLayout(group1Layout);
    group1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    group1.setText("Connection");
    final ElementWizardComposite connectorWizard = new ElementWizardComposite.WithRadios(group1, SWT.NONE,
        "org.eclipse.net4j.connectors", "Type:");
    connectorWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    connectorWizard.getNotifier().addListener(new IListener()
    {
      public void notifyEvent(IEvent event)
      {
        repositoryProperties.addProperty(CDOProvider.PROP_CONNECTOR_TYPE, connectorWizard.getFactoryType());
        repositoryProperties.addProperty(CDOProvider.PROP_CONNECTOR_DESCRIPTION, connectorWizard.getDescription());
      }
    });

    Group group2 = new Group(composite, SWT.NONE);
    group2.setLayout(new GridLayout(1, false));
    group2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    group2.setText("Repository");
    final Text remoteRepositoryNameText = new Text(group2, SWT.BORDER);
    remoteRepositoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    remoteRepositoryNameText.addModifyListener(new ModifyListener()
    {
      private String oldText = "";

      public void modifyText(ModifyEvent e)
      {
        if (oldText.equals(repositoryNameText.getText()))
        {
          oldText = remoteRepositoryNameText.getText();
          repositoryNameText.setText(oldText);
          repositoryProperties.addProperty(CDOProvider.PROP_REPOSITORY_NAME, oldText);
        }
      }
    });

    return composite;
  }
}
