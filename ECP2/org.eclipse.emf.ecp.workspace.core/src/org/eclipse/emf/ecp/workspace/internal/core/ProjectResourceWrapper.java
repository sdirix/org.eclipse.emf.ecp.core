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
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.core.resources.IResource;

/**
 * @author Eike Stepper
 */
public class ProjectResourceWrapper extends ResourceWrapper<InternalProject>
{
  public ProjectResourceWrapper(ECPProject project, IResource resource)
  {
    super((InternalProject)project, resource);
  }

  @Override
  protected Object createChild(InternalProject project, IResource member)
  {
    return new ProjectResourceWrapper(project, member);
  }
}
