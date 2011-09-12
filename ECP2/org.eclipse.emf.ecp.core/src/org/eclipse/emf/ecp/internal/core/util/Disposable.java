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
package org.eclipse.emf.ecp.internal.core.util;

import org.eclipse.emf.ecp.core.util.ECPDisposable;
import org.eclipse.emf.ecp.internal.core.Activator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class Disposable implements ECPDisposable
{
  private final List<DisposeListener> listeners = new ArrayList<DisposeListener>();

  private final ECPDisposable delegate;

  private boolean disposed;

  public Disposable(ECPDisposable delegate)
  {
    this.delegate = delegate;
  }

  public final synchronized boolean isDisposed()
  {
    return disposed;
  }

  public final void dispose()
  {
    DisposeListener[] array = null;

    synchronized (this)
    {
      if (!disposed)
      {
        doDispose();
        disposed = true;
        array = listeners.toArray(new DisposeListener[listeners.size()]);
      }
    }

    if (array != null)
    {
      for (int i = 0; i < array.length; i++)
      {
        DisposeListener listener = array[i];

        try
        {
          listener.disposed(delegate);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }
  }

  public final synchronized void addDisposeListener(DisposeListener listener)
  {
    listeners.add(listener);
  }

  public final synchronized void removeDisposeListener(DisposeListener listener)
  {
    listeners.remove(listener);
  }

  protected void doDispose()
  {
  }
}
