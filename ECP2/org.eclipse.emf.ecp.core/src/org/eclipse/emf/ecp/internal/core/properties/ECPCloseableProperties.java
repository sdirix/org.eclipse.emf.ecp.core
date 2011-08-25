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

import org.eclipse.net4j.util.properties.DefaultPropertyTester;
import org.eclipse.net4j.util.properties.IProperties;
import org.eclipse.net4j.util.properties.Properties;
import org.eclipse.net4j.util.properties.Property;

import org.eclipse.emf.ecp.core.util.ECPCloseable;

/**
 * @author Eike Stepper
 */
public class ECPCloseableProperties extends Properties<ECPCloseable>
{
  public static final IProperties<ECPCloseable> INSTANCE = new ECPCloseableProperties();

  public ECPCloseableProperties()
  {
    super(ECPCloseable.class);

    add(new Property<ECPCloseable>("open")
    {
      @Override
      protected Object eval(ECPCloseable closeable)
      {
        return closeable.isOpen();
      }
    });

    add(new Property<ECPCloseable>("closed")
    {
      @Override
      protected Object eval(ECPCloseable closeable)
      {
        return !closeable.isOpen();
      }
    });
  }

  public static void main(String[] args)
  {
    new Tester().dumpContributionMarkup();
  }

  /**
   * @author Eike Stepper
   */
  public static final class Tester extends DefaultPropertyTester<ECPCloseable>
  {
    public Tester()
    {
      super(INSTANCE);
    }
  }
}
