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

import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPProperties.Listener;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Eike Stepper
 */
public class Properties extends Registry<Map.Entry<String, String>, Listener> implements ECPProperties
{
  public Properties()
  {
    activate();
  }

  public Properties(ObjectInput in) throws IOException
  {
    activate();
    int count = in.readInt();
    for (int i = 0; i < count; i++)
    {
      String key = in.readUTF();
      String value = in.readUTF();
      addProperty(key, value);
    }

  }

  public void write(ObjectOutput out) throws IOException
  {
    Entry<String, String>[] entries = getElements();
    out.writeInt(entries.length);
    for (Entry<String, String> entry : entries)
    {
      out.writeUTF(entry.getKey());
      out.writeUTF(entry.getValue());
    }
  }

  public final void addProperty(String key, String value)
  {
    Map.Entry<String, String> property = new Property(key, value);
    doChangeElements(null, Collections.singleton(property));
  }

  public final void removeProperty(String key)
  {
    doChangeElements(Collections.singleton(key), null);
  }

  public String getValue(String name)
  {
    Entry<String, String> element = getElement(name);
    return element == null ? null : element.getValue();
  }

  public String[] getKeys()
  {
    return getElementNames();
  }

  public Map.Entry<String, String>[] getProperties()
  {
    return getElements();
  }

  public boolean hasProperties()
  {
    return hasElements();
  }

  public ECPProperties copy()
  {
    ECPProperties copy = ECPUtil.createProperties();
    for (Entry<String, String> property : getElements())
    {
      copy.addProperty(property.getKey(), property.getValue());
    }

    return copy;
  }

  @Override
  protected String getElementName(Map.Entry<String, String> element)
  {
    return element.getKey();
  }

  @Override
  protected void notifyListener(Listener listener, Map.Entry<String, String>[] oldElements,
      Map.Entry<String, String>[] newElements) throws Exception
  {
    listener.propertiesChanged(oldElements, newElements);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Map.Entry<String, String>[] createElementArray(int size)
  {
    return new Map.Entry[size];
  }

  @Override
  protected Listener[] createListenerArray(int size)
  {
    return new Listener[size];
  }

  /**
   * @author Eike Stepper
   */
  public static final class Property implements Map.Entry<String, String>
  {
    private final String key;

    private final String value;

    public Property(String key, String value)
    {
      this.key = key;
      this.value = value;
    }

    public String getKey()
    {
      return key;
    }

    public String getValue()
    {
      return value;
    }

    public String setValue(String value)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
      return key + " --> " + value;
    }
  }
}
