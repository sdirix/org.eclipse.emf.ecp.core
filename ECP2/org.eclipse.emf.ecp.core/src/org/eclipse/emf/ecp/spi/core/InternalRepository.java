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
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore.StorableElement;
import org.eclipse.emf.ecp.spi.core.util.InternalRegistryElement;

/**
 * @author Eike Stepper
 */
public interface InternalRepository extends ECPRepository, StorableElement, InternalRegistryElement
{
  public InternalProvider getProvider();

  public Object getProviderSpecificData();

  public void setProviderSpecificData(Object data);

  public void notifyObjectsChanged(Object[] objects);
}
