/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.wizards;

//TODO: Revise
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.SelectModelElementHelper;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.Collection;

/**
 * @author Hodaie This is the first page of NewModelElementWizard. On this page the model packages and their class (only
 *         those who inherit ModelElement and are not abstract) are shown in a TreeViewer. If user selects a class in
 *         this tree, the wizard can finish.
 * @author Eugen Neufeld
 */
public class ModelTreePage extends WizardPage implements Listener
{

  private static final String PAGE_TITLE = "Add new model element";

  private static final String PAGE_DESCRIPTION = "Select model element type";

  private final SelectModelElementHelper helper;

  /**
   * Constructor.
   * 
   * @param selected
   *          the selected EClass
   * @param pageName
   *          page name
   */
  protected ModelTreePage(String pageName, ECPProject project)
  {
    super(pageName);
    helper = new SelectModelElementHelper(project);
    setTitle(PAGE_TITLE);
    setDescription(PAGE_DESCRIPTION);

  }

  protected ModelTreePage(String pageName, Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
      Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses)
  {
    super(pageName);
    helper = new SelectModelElementHelper(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
    setTitle(PAGE_TITLE);
    setDescription(PAGE_DESCRIPTION);

  }

  /**
   * {@inheritDoc}
   */
  public void createControl(Composite parent)
  {

    Composite composite = helper.createUI(parent);
    helper.getTreeViewer().getTree().addListener(SWT.Selection, this);
    helper.getTreeViewer().addDoubleClickListener(new IDoubleClickListener()
    {

      public void doubleClick(DoubleClickEvent event)
      {
        if (getWizard().canFinish())
        {
          getWizard().performFinish();
          getWizard().getContainer().getShell().close();
        }

      }

    });

    setControl(composite);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canFlipToNextPage()
  {

    return false;

  }

  /**
   * Check if tree selection is a ME and wizard can complete. This Method sets the newMEType and treeCompleted fields in
   * NewModelElementWizard
   * 
   * @return
   */
  private boolean checkSelection()
  {

    NewModelElementWizard wizard = (NewModelElementWizard)getWizard();
    boolean canFinish = false;
    ISelection sel = helper.getTreeViewer().getSelection();
    if (sel == null)
    {
      canFinish = false;
    }

    if (!(sel instanceof IStructuredSelection))
    {
      canFinish = false;
    }

    IStructuredSelection ssel = (IStructuredSelection)sel;
    if (ssel.isEmpty())
    {
      canFinish = false;
    }
    Object o = ssel.getFirstElement();
    if (o instanceof EClass)
    {
      canFinish = true;
    }

    else
    {
      canFinish = false;
    }

    if (canFinish)
    {
      EClass newMEType = (EClass)o;
      wizard.setNewMEType(newMEType);
      wizard.setTreePageCompleted(true);
      return true;
    }

    wizard.setNewMEType(null);
    wizard.setTreePageCompleted(false);
    return false;

  }

  /**
   * {@inheritDoc} On selection change in TreeViewer updates wizard buttons accordingly.
   */
  public void handleEvent(Event event)
  {

    checkSelection();
    getWizard().getContainer().updateButtons();

  }

}
