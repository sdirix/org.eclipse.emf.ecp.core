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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.util.ActionHelper;

import org.eclipse.jface.wizard.Wizard;

import java.util.Collection;

/**
 * @author Hodaie This is implementation of New Model Element wizard. This wizard is show through
 *         "Add new model element..." command in context menu of Navigator (only on right click on LeafSection). The
 *         wizard shows a tree of model packages and their classes. The user can select a Model Element type in this
 *         tree and on finish the model element is created, added to Leaf- or CompositeSection and opend for editing.
 */
public class NewModelElementWizard extends Wizard
{

  /**
   * . Through this field, the ModelTreePage tells the wizard which model element type is selected
   */
  private EClass newMEType;

  /**
   * Through this field, the ModelTreePage tells the wizard if it's ready to finish, i.e. if the selection a model
   * element is and not a package.
   */
  private boolean treePageCompleted;

  private ECPProject ecpProject;

  private Collection<EPackage> ePackages;

  private Collection<EPackage> unsupportedEPackages;

  private Collection<EPackage> filteredEPackages;

  private Collection<EClass> filteredEClasses;

  /**
   * . ({@inheritDoc})
   */
  @Override
  public void addPages()
  {
    ModelTreePage treePage = null;
    if (ecpProject != null)
    {
      treePage = new ModelTreePage("ModelTreePage", ecpProject);
    }
    else
    {
      treePage = new ModelTreePage("ModelTreePage", ePackages, unsupportedEPackages, filteredEPackages,
          filteredEClasses);
    }
    addPage(treePage);

  }

  /**
   * . ({@inheritDoc}) This method creates a model element instance from selected type, adds it to Leaf- or
   * CompositeSection, and opens it.
   */
  @Override
  public boolean performFinish()
  {
    final EObject newMEInstance;
    if (ecpProject != null && newMEType != null)
    {
      // 1.create ME
      EPackage ePackage = newMEType.getEPackage();
      newMEInstance = ePackage.getEFactoryInstance().create(newMEType);

      ecpProject.getElements().add(newMEInstance);

      // 3.open the newly created ME
      ActionHelper.openModelElement(newMEInstance, this.getClass().getName(), ecpProject);
    }

    return true;
  }

  public void init(ECPProject ecpProject)
  {
    this.ecpProject = ecpProject;
  }

  public void init(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
      Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses)
  {
    this.ePackages = ePackages;
    this.unsupportedEPackages = unsupportedEPackages;
    this.filteredEPackages = filteredEPackages;
    this.filteredEClasses = filteredEClasses;
  }

  // /**
  // * . ({@inheritDoc})
  // */
  // public void init(IWorkbench workbench, IStructuredSelection selection)
  // {
  // // get the in navigator selected ME
  // if (selection.getFirstElement() instanceof InternalProject)
  // {
  // ecpProject = (InternalProject)selection.getFirstElement();
  // }
  // else
  // {
  //
  // }
  //
  // }

  /**
   * . ({@inheritDoc})
   */
  @Override
  public boolean canFinish()
  {
    return treePageCompleted;

  }

  /**
   * @param newMEType
   *          The ME type that was in ModelTreePage selected.
   */
  public void setNewMEType(EClass newMEType)
  {
    this.newMEType = newMEType;
  }

  /**
   * @param treePageCompleted
   *          If ModelTreePage is complete (i.e. its selection is a ME)
   */
  public void setTreePageCompleted(boolean treePageCompleted)
  {
    this.treePageCompleted = treePageCompleted;
  }

  /**
   * @return
   */
  public EClass getNewMEType()
  {
    return newMEType;
  }

}
