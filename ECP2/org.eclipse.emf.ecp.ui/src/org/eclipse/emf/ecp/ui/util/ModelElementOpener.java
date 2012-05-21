/**
 * 
 */
package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;

/**
 * Modelelement opener offer the functionality to open a specific model element. Standard opener is the model element
 * editor. Example for specific opener are the diagrams.
 * 
 * @author helming
 */
public interface ModelElementOpener
{
  /**
   * Constant to return if the opener should not open the modelelement.
   */
  int DONOTOPEN = -1;

  /**
   * Checks whether the model element should be opened by this opener, depending on the priority. The model element will
   * be opened with the registered opener with the highest priority.
   * 
   * @param modelElement
   *          the model element to check
   * @return a priority indicating how well the opener can open the element
   */
  int canOpen(EObject modelElement);

  /**
   * The action to open the model element.
   * 
   * @param modelElement
   *          the model element to open
   * @param ecpProject
   *          the project this element belongs to
   */
  void openModelElement(EObject modelElement, ECPProject ecpProject);
}
