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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eike Stepper
 */
public abstract class StructuredContentProvider<INPUT> implements IStructuredContentProvider
{
  private Viewer viewer;

  private INPUT input;

  public StructuredContentProvider()
  {
  }

  public Viewer getViewer()
  {
    return viewer;
  }

  public INPUT getInput()
  {
    return input;
  }

  public final void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
    this.viewer = viewer;

    if (input != null)
    {
      disconnectInput(input);
    }

    @SuppressWarnings("unchecked")
    INPUT tmp = (INPUT)newInput;
    input = tmp;

    if (input != null)
    {
      connectInput(input);
    }
  }

  public void refreshViewer()
  {
    Display display = viewer.getControl().getDisplay();
    if (display.getSyncThread() != Thread.currentThread())
    {
      display.asyncExec(new Runnable()
      {
        public void run()
        {
          viewer.refresh();
        }
      });
    }
    else
    {
      viewer.refresh();
    }
  }

  public void dispose()
  {
    if (input != null)
    {
      disconnectInput(input);
    }
  }

  protected void connectInput(INPUT input)
  {
    // Can be overridden in subclasses
  }

  protected void disconnectInput(INPUT input)
  {
    // Can be overridden in subclasses
  }
}
