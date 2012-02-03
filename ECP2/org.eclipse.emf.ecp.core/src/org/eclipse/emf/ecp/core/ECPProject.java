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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPPropertiesAware;
import org.eclipse.emf.ecp.core.util.ECPRepositoryAware;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

import org.eclipse.core.runtime.IAdaptable;

/**
 * @author Eike Stepper
 */
public interface ECPProject extends ECPElement, ECPModelContext, ECPRepositoryAware, ECPPropertiesAware, ECPCloseable,
    ECPDeletable, IEditingDomainProvider, IAdaptable
{
  public static final String TYPE = "Project";

  /**
   * @return the metamodel context of this project
   */
  public ECPMetamodelContext getMetamodelContext();

  /**
   * @param newMEInstance
   */
  public void addToRoot(EObject newMEInstance);

  /**
   * Returns <code>true</code> if this project is shared with a {@link ECPRepository repository}, <code>false</code>
   * otherwise. Same as calling <code>getRepository() != null</code>.
   */
  public boolean isShared();

  public void share(ECPRepository repository);

  public ECPRepository unshare();
}
