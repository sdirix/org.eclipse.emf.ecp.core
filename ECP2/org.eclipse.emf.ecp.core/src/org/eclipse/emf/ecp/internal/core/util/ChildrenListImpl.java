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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class ChildrenListImpl extends BasicEList<Object> implements InternalChildrenList
{
  private static final long serialVersionUID = 1L;

  private final Object parent;

  public ChildrenListImpl(Object parent)
  {
    this.parent = parent;
  }

  public final Object getParent()
  {
    return parent;
  }

  public synchronized boolean hasChildren()
  {
    return !isEmpty();
  }

  public synchronized Object[] getChildren()
  {
    return toArray(new Object[size()]);
  }

  public synchronized Object getChild(int index)
  {
    return get(index);
  }

  public synchronized final void addChild(Object child)
  {
    add(child);
    childrenAdded();
  }

  public synchronized final <T> void addChildren(T... children)
  {
    for (int i = 0; i < children.length; i++)
    {
      Object child = children[i];
      add(child);
    }

    childrenAdded();
  }

  public synchronized final <T> void addChildren(Collection<T> children)
  {
    addAll(children);
    childrenAdded();
  }

  public boolean isLazy()
  {
    return false;
  }

  public boolean isComplete()
  {
    return true;
  }

  protected void childrenAdded()
  {
    // Can be overridden in subclasses
  }
}
