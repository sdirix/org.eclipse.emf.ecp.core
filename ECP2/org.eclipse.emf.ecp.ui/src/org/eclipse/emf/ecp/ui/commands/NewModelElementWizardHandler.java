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
package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.dialogs.ModelElementSelectionTreeDialog;
import org.eclipse.emf.ecp.ui.util.ActionHelper;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Hodaie This is the handler for "Add new model element" context menu command. The command is only shown on
 *         LeafSections. The handler initializes and shows the AddNewModelElementWizard
 * @author Eugen Neufeld
 */
// TODO: Revise
public class NewModelElementWizardHandler extends AbstractHandler
{

  /**
   * String to pass the eclass which containments shall be shown in the new element wizard.
   */
  public static final String COMMAND_ECLASS_PARAM = "org.eclipse.emf.ecp.navigator.eClassParameter";

  /**
   * . ({@inheritDoc})
   */
  public Object execute(final ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;

    ECPProject ecpProject = (ECPProject)ssel.getFirstElement();

    Set<EPackage> ePackages = new HashSet<EPackage>();

    for (Object o : Registry.INSTANCE.values())
    {
      if (o instanceof EPackage)
      {
        ePackages.add((EPackage)o);
      }
    }

    ModelElementSelectionTreeDialog dialog = new ModelElementSelectionTreeDialog(HandlerUtil.getActiveShell(event),
        ePackages, ecpProject.getUnsupportedEPackages(), ecpProject.getFilteredPackages(),
        ecpProject.getFilteredEClasses());
    dialog.setAllowMultiple(false);
    int result = dialog.open();
    if (result == Dialog.OK)
    {
      EObject newMEInstance = null;
      Object[] dialogSelection = dialog.getResult();
      for (Object object : dialogSelection)
      {
        if (object instanceof EClass)
        {
          EClass eClasse = (EClass)object;
          // 1.create ME
          EPackage ePackage = eClasse.getEPackage();
          newMEInstance = ePackage.getEFactoryInstance().create(eClasse);

          ecpProject.getElements().add(newMEInstance);

          // 3.open the newly created ME
          ActionHelper.openModelElement(newMEInstance, ActionHelper.class.getName(), ecpProject);

        }
      }
    }
    return null;
  }

}
