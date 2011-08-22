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

import org.eclipse.emf.ecp.core.ECPProject;

import org.eclipse.core.expressions.PropertyTester;

/**
 * @author Eike Stepper
 */
public class ECPProjectPropertyTester extends PropertyTester
{
  public static final String NAME = "name";

  public static final String REPOSITORY_NAME = "repositoryName";

  public static final String REPOSITORY_DESCRIPTION = "repositoryDescription";

  public static final String PROVIDER_NAME = "providerName";

  public ECPProjectPropertyTester()
  {
  }

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    ECPProject project = (ECPProject)receiver;
    Object value = getValue(project, property);
    return ObjectUtil.equals(value, expectedValue);
  }

  public static Object getValue(ECPProject project, String property)
  {
    if (NAME.equals(property))
    {
      return project.getName();
    }

    if (REPOSITORY_NAME.equals(property))
    {
      return project.getRepository().getName();
    }

    if (REPOSITORY_DESCRIPTION.equals(property))
    {
      return project.getRepository().getDescription();
    }

    if (PROVIDER_NAME.equals(property))
    {
      return project.getRepository().getProvider().getName();
    }

    return null;
  }
}
