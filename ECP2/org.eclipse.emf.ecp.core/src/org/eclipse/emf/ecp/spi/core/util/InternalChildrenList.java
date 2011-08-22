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

import org.eclipse.emf.ecp.core.util.ECPChildrenList;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public interface InternalChildrenList extends ECPChildrenList
{
  public void addChild(Object child);

  public <T> void addChildren(T... children);

  public <T> void addChildren(Collection<T> children);
}
