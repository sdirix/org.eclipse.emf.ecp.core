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
package org.eclipse.emf.ecp.internal.core.properties;

import org.eclipse.net4j.util.ObjectUtil;

import org.eclipse.emf.ecp.core.util.ECPCloseable;

import org.eclipse.core.expressions.PropertyTester;

/**
 * @author Eike Stepper
 */
public class ECPCloseablePropertyTester extends PropertyTester
{
  public static final String OPEN = "open";

  public static final String CLOSED = "closed";

  public ECPCloseablePropertyTester()
  {
  }

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    ECPCloseable closeable = (ECPCloseable)receiver;
    Object value = getValue(closeable, property);
    return ObjectUtil.equals(value, expectedValue);
  }

  public static Object getValue(ECPCloseable closeable, String property)
  {
    if (OPEN.equals(property))
    {
      return closeable.isOpen();
    }

    if (CLOSED.equals(property))
    {
      return !closeable.isOpen();
    }

    return null;
  }
}
