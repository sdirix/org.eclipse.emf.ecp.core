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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPMetamodelContext;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPProviderAware;
import org.eclipse.emf.ecp.spi.core.util.AdapterProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.ecp.spi.core.util.InternalRegistryElement;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * @author Eike Stepper
 */
public interface InternalProvider extends ECPProvider, ECPProviderAware, ECPModelContextProvider,
    InternalRegistryElement, AdapterProvider
{
  public static final ComposedAdapterFactory EMF_ADAPTER_FACTORY = new ComposedAdapterFactory(
      ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

  public AdapterProvider getUIProvider();

  public void setUIProvider(AdapterProvider uiProvider);

  public EditingDomain createEditingDomain(InternalProject project);

  public boolean isSlow(Object parent);

  public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList);

  public void handleLifecycle(ECPModelContext context, LifecycleEvent event);

  /**
   * @author Eike Stepper
   */
  public enum LifecycleEvent
  {
    CREATE, INIT, DISPOSE, REMOVE;
  }

  public void addRootElement(ECPProject project, EObject rootElement);

  /**
   * @param project
   *          the project to retrieve the meta context for
   * @return the meta model context
   */
  public ECPMetamodelContext getMetamodelContext(ECPProject project);
}
