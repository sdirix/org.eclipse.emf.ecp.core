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
package org.eclipse.emf.ecp.ui.actions;

import org.eclipse.emf.ecp.core.util.ECPCloseable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public abstract class CloseableAction extends AbstractAction
{
  public CloseableAction()
  {
  }

  @Override
  protected void run(IAction action, IStructuredSelection selection)
  {
    for (Iterator<?> it = selection.iterator(); it.hasNext();)
    {
      Object element = it.next();
      if (element instanceof ECPCloseable)
      {
        ECPCloseable closeable = (ECPCloseable)element;
        runCloseable(closeable);
      }
    }
  }

  protected abstract void runCloseable(ECPCloseable closeable);

  /**
   * @author Eike Stepper
   */
  public static final class Close extends CloseableAction
  {
    @Override
    protected void runCloseable(ECPCloseable closeable)
    {
      closeable.close();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Open extends CloseableAction
  {
    @Override
    protected void runCloseable(ECPCloseable closeable)
    {
      closeable.open();
    }
  }
}
