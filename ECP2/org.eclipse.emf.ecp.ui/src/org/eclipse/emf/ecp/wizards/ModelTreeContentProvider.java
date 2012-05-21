/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.wizards;

//TODO: Revise
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPMetamodelContext;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hodaie ContentProvider for TreeViewer which is shown on ModelTreePage
 * @author Eugen Neufeld
 */
public class ModelTreeContentProvider extends AdapterFactoryContentProvider
{

  private Set<EPackage> packages = new HashSet<EPackage>();

  private HashSet<EClass> modelElementClasses;

  private Set<EPackage> rootPackages;

  private final ECPMetamodelContext metaContext;

  /**
   * Constructor.
   */
  public ModelTreeContentProvider(ECPMetamodelContext metamodelContext)
  {
    super(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
    metaContext = metamodelContext;
    Set<EClass> eClasses;
    eClasses = metaContext.getAllRootEClasses();
    modelElementClasses = new HashSet<EClass>();
    rootPackages = new HashSet<EPackage>();
    for (EClass eClass : eClasses)
    {
      if (!isNonDomainElement(eClass) && !eClass.isAbstract())
      {
        modelElementClasses.add(eClass);
      }
    }

    extractRootPackages(modelElementClasses);
  }

  public ModelTreeContentProvider(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
      Collection<EPackage> projectFilteredEPackages, Collection<EClass> projectFilteredEClasss)
  {
    super(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
    metaContext = null;
    modelElementClasses = new HashSet<EClass>();
    rootPackages = new HashSet<EPackage>();
    for (EPackage ePackage : ePackages)
    {
      if (unsupportedEPackages.contains(ePackage))
      {
        continue;
      }
      boolean addToPackages = projectFilteredEPackages.contains(ePackage);
      boolean packageAllowed = addToPackages;
      for (EClassifier classifier : ePackage.getEClassifiers())
      {
        if (classifier instanceof EClass && !((EClass)classifier).isAbstract())
        {
          EClass eClass = (EClass)classifier;
          addToPackages = addToPackages || projectFilteredEClasss.contains(eClass);
          if (packageAllowed || projectFilteredEClasss.contains(eClass))
          {
            modelElementClasses.add(eClass);

          }
        }
      }

      if (addToPackages)
      {
        rootPackages.add(ePackage);
        extractAllSuperPackages(ePackage);
      }
    }
  }

  private void extractRootPackages(Set<EClass> eClasses)
  {
    for (EClass eClass : eClasses)
    {
      EPackage ePackage = eClass.getEPackage();
      packages.add(ePackage);
      extractAllSuperPackages(ePackage);
    }
  }

  private void extractAllSuperPackages(EPackage ePackage)
  {
    EPackage eSuperPackage = ePackage.getESuperPackage();
    if (eSuperPackage == null)
    {
      rootPackages.add(ePackage);
      return;
    }
    if (packages.contains(eSuperPackage))
    {
      return;
    }
    packages.add(eSuperPackage);
    extractAllSuperPackages(eSuperPackage);
  }

  /**
   * {@inheritDoc} Return an array of sub-packages of Model package.
   */
  @Override
  public Object[] getElements(Object inputElement)
  {
    if (rootPackages.size() == 1)
    {
      return getChildren(rootPackages.iterator().next());
    }
    return rootPackages.toArray();

    // // return ModelUtil.getAllModelPackages().toArray();
  }

  /**
   * {@inheritDoc} Shows the children only when argument is an EPackage. Also doesn't show the Children that are.
   * abstract or not ModelElement.
   */
  @Override
  public Object[] getChildren(Object object)
  {
    // show the children only when argument is an EPackage.
    // Also remove the Children that are abstract or not ModelElement.
    if (object instanceof EPackage)
    {

      // remove classes that do not inherit ModelElement
      // or are abstract.
      Object[] children = super.getChildren(object);
      List<Object> ret = new ArrayList<Object>();
      for (int i = 0; i < children.length; i++)
      {
        Object child = children[i];
        if (child instanceof EPackage && packages.contains(child))
        {
          ret.add(child);
        }
        if (child instanceof EClass && modelElementClasses.contains(child))
        {
          ret.add(child);
        }
      }
      return ret.toArray();

    }

    // for Children that are EClass, show nothing
    // Otherwise the EAttributes of EClass would be shown in tree
    return null;

  }

  /**
   * Checks if the argument is a NonDomainElement.
   * 
   * @param object
   *          EClass to be checked.
   * @return
   */
  private boolean isNonDomainElement(EClass eClass)
  {
    return false;
  }

  /**
   * {@inheritDoc} If argument is an EClass return false. This is to prevent showing of the plus sign beside an. EClass
   * in TreeViewer
   */
  @Override
  public boolean hasChildren(Object object)
  {
    // to remove the plus signs that are shown
    // beside EClasses in the tree.
    if (object instanceof EClass)
    {
      return false;
    }

    Object[] children = getChildren(object);
    if (children != null)
    {
      return children.length > 0;
    }

    return false;

  }

}
