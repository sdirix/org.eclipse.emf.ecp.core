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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.ui.common.SelectModelElementHelper;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Hodaie
 * @author Eugen Neufeld This is implementation of New Model Element wizard. This wizard is show through
 *         "Add new model element..." command in context menu of Navigator (only on right click on LeafSection). The
 *         wizard shows a tree of model packages and their classes. The user can select a Model Element type in this
 *         tree and on finish the model element is created, added to Leaf- or CompositeSection and opend for editing.
 */
public class NewModelElementWizard extends ECPWizard<SelectModelElementHelper>
{

  public NewModelElementWizard(String title)
  {
    setWindowTitle(title);
  }

  /**
   * . ({@inheritDoc})
   */
  @Override
  public void addPages()
  {
    super.addPages();
    WizardPage wp = new WizardPage(Messages.NewModelElementWizard_WizardTitle_AddModelElement)
    {

      public void createControl(Composite parent)
      {
        Composite composite = getUIProvider().createUI(parent);
        getUIProvider().getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener()
        {

          public void selectionChanged(SelectionChangedEvent event)
          {
            IStructuredSelection sel = (IStructuredSelection)getUIProvider().getTreeViewer().getSelection();
            if (sel != null && sel.getFirstElement() instanceof EClass)
            {
              setPageComplete(true);
            }
            else
            {
              setPageComplete(false);
            }
          }
        });
        getUIProvider().getTreeViewer().addDoubleClickListener(new IDoubleClickListener()
        {

          public void doubleClick(DoubleClickEvent event)
          {
            if (isPageComplete() && performFinish())
            {
              ((WizardDialog)getContainer()).close();
            }

          }

        });
        setPageComplete(false);
        setControl(composite);
      }
    };
    addPage(wp);
    wp.setTitle(Messages.NewModelElementWizard_PageTitle_AddModelElement);
    wp.setDescription(Messages.NewModelElementWizard_PageDescription_AddModelElement);

  }

  /**
   * . ({@inheritDoc}) This method creates a model element instance from selected type, adds it to Leaf- or
   * CompositeSection, and opens it.
   */
  @Override
  public boolean performFinish()
  {
    return true;
  }
}
