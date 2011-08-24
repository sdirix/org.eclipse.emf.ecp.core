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
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.util.ModelContextWrapper;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author Eike Stepper
 */
public class ProjectWrapper extends ModelContextWrapper<InternalProject> implements InternalProject
{
  public ProjectWrapper(InternalProject delegate)
  {
    super(delegate);
  }

  public boolean canDelete()
  {
    return getDelegate().canDelete();
  }

  public boolean isOpen()
  {
    return getDelegate().isOpen();
  }

  public ECPProperties getProperties()
  {
    return getDelegate().getProperties();
  }

  public String getName()
  {
    return getDelegate().getName();
  }

  public void open()
  {
    getDelegate().open();
  }

  public void delete()
  {
    getDelegate().delete();
  }

  public void close()
  {
    getDelegate().close();
  }

  public ECPProject getProject()
  {
    return getDelegate().getProject();
  }

  public EditingDomain getEditingDomain()
  {
    return getDelegate().getEditingDomain();
  }

  public InternalRepository getRepository()
  {
    return getDelegate().getRepository();
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

  public void undispose(InternalRepository repository)
  {
    getDelegate().undispose(repository);
  }

  public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType)
  {
    return getDelegate().getAdapter(adapterType);
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
