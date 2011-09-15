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
package org.eclipse.emf.ecp.core;

/**
 * @author Eike Stepper
 */
public interface ECPProviderRegistry
{
  public static final ECPProviderRegistry INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProviderRegistryImpl.INSTANCE;

  public ECPProvider getProvider(Object adaptable);

  public ECPProvider getProvider(String name);

  public ECPProvider[] getProviders();

  public boolean hasProviders();

  public void addProvider(ECPProvider provider);

  public void removeProvider(String name);

  public void addListener(Listener listener);

  public void removeListener(Listener listener);

  /**
   * @author Eike Stepper
   */
  public interface Listener
  {
    public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception;
  }
}
