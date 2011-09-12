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
package org.eclipse.emf.ecp.core.util;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface ECPProperties
{
  public void addProperty(String key, String value);

  public void removeProperty(String key);

  public String getValue(String name);

  public String[] getKeys();

  public Map.Entry<String, String>[] getProperties();

  public boolean hasProperties();

  public ECPProperties copy();

  public void addListener(Listener listener);

  public void removeListener(Listener listener);

  /**
   * @author Eike Stepper
   */
  public interface Listener
  {
    public void propertiesChanged(Map.Entry<String, String>[] oldProperties, Map.Entry<String, String>[] newProperties);
  }
}
