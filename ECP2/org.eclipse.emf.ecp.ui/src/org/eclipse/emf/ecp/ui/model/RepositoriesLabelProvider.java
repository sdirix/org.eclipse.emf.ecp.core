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
package org.eclipse.emf.ecp.ui.model;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.internal.ui.Activator;

import org.eclipse.swt.graphics.Image;

/**
 * @author Eike Stepper
 */
public class RepositoriesLabelProvider extends ECPLabelProvider
{
  private static final Image REPOSITORY = Activator.getImage("icons/repository.gif");

  public RepositoriesLabelProvider(ECPModelContextProvider modelContextProvider)
  {
    super(modelContextProvider);
  }

  @Override
  public String getText(Object element)
  {
    if (element instanceof ECPRepository)
    {
      ECPRepository repository = (ECPRepository)element;
      return repository.getLabel();
    }

    return super.getText(element);
  }

  @Override
  public Image getImage(Object element)
  {
    if (element instanceof ECPRepository)
    {
      return REPOSITORY;
    }

    return super.getImage(element);
  }
}
