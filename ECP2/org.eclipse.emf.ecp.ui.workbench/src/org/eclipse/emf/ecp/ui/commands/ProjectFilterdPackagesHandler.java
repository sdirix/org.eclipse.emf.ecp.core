/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.CheckedSelectModelClassComposite;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.FilterModelElementWizard;
import org.eclipse.emf.ecp.wizards.WizardUICallback;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 *  This Handler uses the {@link HandlerHelper#filterProjectPackages(ECPProject, org.eclipse.emf.ecp.ui.common.AbstractUICallback)} method
 * to filter a project.
 * @author Eugen Neufeld
 */
public class ProjectFilterdPackagesHandler extends AbstractHandler
{

	/**
	 * {@inheritDoc}
	 */
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    IStructuredSelection ssel = (IStructuredSelection)selection;
    
    HandlerHelper.filterProjectPackages((ECPProject)ssel.getFirstElement(),new WizardUICallback<CheckedSelectModelClassComposite>(HandlerUtil.getActiveShell(event), new FilterModelElementWizard()));
    
    
    return null;
  }
}
