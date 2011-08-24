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
package org.eclipse.emf.ecp.spi.core.util;

import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.spi.core.InternalProvider;

/**
 * @author Eike Stepper
 */
public class ModelContextWrapper<DELEGATE extends ECPModelContext> implements ECPModelContext
{
  private final DELEGATE delegate;

  public ModelContextWrapper(DELEGATE delegate)
  {
    this.delegate = delegate;
  }

  public final DELEGATE getDelegate()
  {
    return delegate;
  }

  public final InternalProvider getProvider()
  {
    return (InternalProvider)delegate.getProvider();
  }

  public final ECPModelContext getContext()
  {
    return delegate.getContext();
  }

  @Override
  public final boolean equals(Object obj)
  {
    return delegate.equals(obj);
  }

  @Override
  public final int hashCode()
  {
    return delegate.hashCode();
  }

  @Override
  public String toString()
  {
    return delegate.toString();
  }
}
