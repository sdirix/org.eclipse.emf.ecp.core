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
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.util.ModelContextWrapper;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author Eike Stepper
 */
public class RepositoryWrapper extends ModelContextWrapper<InternalRepository> implements InternalRepository
{
  public RepositoryWrapper(InternalRepository delegate)
  {
    super(delegate);
  }

  public boolean canDelete()
  {
    return getDelegate().canDelete();
  }

  public boolean isDisposed()
  {
    return getDelegate().isDisposed();
  }

  public ECPProperties getProperties()
  {
    return getDelegate().getProperties();
  }

  public String getName()
  {
    return getDelegate().getName();
  }

  public void delete()
  {
    getDelegate().delete();
  }

  public void dispose()
  {
    getDelegate().dispose();
  }

  public void addDisposeListener(DisposeListener listener)
  {
    getDelegate().addDisposeListener(listener);
  }

  public String getDefaultCheckoutName()
  {
    return getDelegate().getDefaultCheckoutName();
  }

  public ECPRepository getRepository()
  {
    return getDelegate().getRepository();
  }

  public ECPProject checkout(String projectName, ECPProperties projectProperties)
  {
    return getDelegate().checkout(projectName, projectProperties);
  }

  public void removeDisposeListener(DisposeListener listener)
  {
    getDelegate().removeDisposeListener(listener);
  }

  public void setLabel(String label)
  {
    getDelegate().setLabel(label);
  }

  public void setDescription(String description)
  {
    getDelegate().setDescription(description);
  }

  public Object getProviderSpecificData()
  {
    return getDelegate().getProviderSpecificData();
  }

  public void setProviderSpecificData(Object data)
  {
    getDelegate().setProviderSpecificData(data);
  }

  public void notifyObjectsChanged(Object[] objects)
  {
    getDelegate().notifyObjectsChanged(objects);
  }

  public String getLabel()
  {
    return getDelegate().getLabel();
  }

  public String getDescription()
  {
    return getDelegate().getDescription();
  }

  public ECPProject[] getOpenProjects()
  {
    return getDelegate().getOpenProjects();
  }

  public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
  {
    return getDelegate().getAdapter(adapter);
  }

  public int compareTo(ECPElement o)
  {
    return getDelegate().compareTo(o);
  }

  public boolean isStorable()
  {
    return getDelegate().isStorable();
  }

  public void write(ObjectOutput out) throws IOException
  {
    getDelegate().write(out);
  }
}
