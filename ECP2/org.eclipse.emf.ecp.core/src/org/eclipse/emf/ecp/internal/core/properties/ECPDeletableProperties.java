/*
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

import org.eclipse.emf.ecp.core.util.ECPDeletable;

/**
 * @author Eugen Neufeld
 */
public class ECPDeletableProperties extends Properties<ECPDeletable>
{
  public static final IProperties<ECPDeletable> INSTANCE = new ECPDeletableProperties();

  public ECPDeletableProperties()
  {
    super(ECPDeletable.class);

    add(new Property<ECPDeletable>("canDelete")
    {
      @Override
      protected Object eval(ECPDeletable deletable)
      {
        return deletable.canDelete();
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
  public static final class Tester extends DefaultPropertyTester<ECPDeletable>
  {
    public static final String NAMESPACE = "org.eclipse.emf.ecp.core.deletable";

    public Tester()
    {
      super(NAMESPACE, INSTANCE);
    }
  }
}
