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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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

import java.util.Collection;
import java.util.Iterator;

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

  /**
   * @param ecpProject
   * @return list of Elements of this project
   */
  // TODO check whether not to use fillChildren
  public EList<EObject> getElements(ECPProject ecpProject);

  /**
   * filter packages that are not supported by this provider
   * 
   * @param ePackages
   *          packages to filter from
   * @return a {@link Collection} of {@link EPackage}s that are not supported
   */
  public Collection<EPackage> getUnsupportedEPackages(Collection<EPackage> ePackages, InternalRepository repository);

  /**
   * Return all {@link EObject}s that this provider supports for linking them to the modelElement and the provided
   * eReference.
   * 
   * @param ecpProject
   *          - the project the call is from
   * @param modelElement
   *          - {@link EObject} to add the {@link EReference} to
   * @param eReference
   *          - the {@link EReference} to add
   * @return {@link Iterator} of {@link EObject} that can be linked
   */
  public Iterator<EObject> getLinkElements(ECPProject ecpProject, EObject modelElement, EReference eReference);
}
