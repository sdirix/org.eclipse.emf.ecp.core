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
import org.eclipse.emf.ecp.core.util.ECPPropertiesAware;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore.StorableElement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author Eike Stepper
 */
public abstract class PropertiesElement extends Element implements StorableElement, ECPPropertiesAware
{
  private final ECPProperties properties;

  public PropertiesElement(String name, ECPProperties properties)
  {
    super(name);
    this.properties = properties;
  }

  public PropertiesElement(ObjectInput in) throws IOException
  {
    super(in.readUTF());
    properties = new Properties(in);
  }

  public void write(ObjectOutput out) throws IOException
  {
    out.writeUTF(getName());
    ((Properties)properties).write(out);
  }

  public final ECPProperties getProperties()
  {
    return properties;
  }
}
