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

import org.eclipse.emf.ecp.core.util.ECPProperties;

/**
 * @author Eike Stepper
 */
public interface ECPRepositoryManager
{
  public static final ECPRepositoryManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPRepositoryManagerImpl.INSTANCE;

  public ECPRepository getRepository(Object adaptable);

  public ECPRepository getRepository(String name);

  public ECPRepository[] getRepositories();

  public boolean hasRepositories();

  public ECPRepository addRepository(ECPProvider provider, String name, String label, String description,
      ECPProperties properties);

  public void addListener(Listener listener);

  public void removeListener(Listener listener);

  /**
   * @author Eike Stepper
   */
  public interface Listener
  {
    public void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception;

    public void objectsChanged(ECPRepository repository, Object[] objects) throws Exception;
  }
}
