/**
 * 
 */
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecore.EClass;

import java.util.Set;

/**
 * @author Jonas
 */
public interface ECPMetamodelContext
{

  /**
   * @return the classes which can be contained on the root level
   */
  Set<EClass> getAllRootEClasses();

  /**
   * @param eClass
   * @return if the class is non domain
   */
  boolean isNonDomainElement(EClass eClass);

  /**
   * @return if this context is guessed
   */
  boolean isGuessed();

}
