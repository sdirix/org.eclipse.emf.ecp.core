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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

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
    HandlerHelper.newModelElementHandlerHelper(ssel, HandlerUtil.getActiveShell(event));
    
    return null;
  }
}
