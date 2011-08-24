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

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.util.ECPDelegating;
import org.eclipse.emf.ecp.core.util.ECPModel;
import org.eclipse.emf.ecp.core.util.ECPModelContext;

/**
 * @author Eike Stepper
 */
public abstract class ModelWrapper<CONTEXT extends ECPModelContext, DELEGATE> implements ECPModel,
    ECPDelegating<DELEGATE>, Comparable<ModelWrapper<CONTEXT, DELEGATE>>
{
  private final CONTEXT context;

  private final DELEGATE delegate;

  public ModelWrapper(CONTEXT context, DELEGATE delegate)
  {
    this.context = context;
    this.delegate = delegate;
  }

  public final ECPProvider getProvider()
  {
    return getContext().getProvider();
  }

  public final CONTEXT getContext()
  {
    return context;
  }

  public DELEGATE getDelegate()
  {
    return delegate;
  }

  public int compareTo(ModelWrapper<CONTEXT, DELEGATE> o)
  {
    return getName().compareTo(o.getName());
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (context == null ? 0 : context.hashCode());
    result = prime * result + (delegate == null ? 0 : delegate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    ModelWrapper<?, ?> other = (ModelWrapper<?, ?>)obj;
    if (context == null)
    {
      if (other.context != null)
      {
        return false;
      }
    }
    else if (!context.equals(other.context))
    {
      return false;
    }

    if (delegate == null)
    {
      if (other.delegate != null)
      {
        return false;
      }
    }
    else if (!delegate.equals(other.delegate))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    return getName();
  }

  public abstract String getName();
}
