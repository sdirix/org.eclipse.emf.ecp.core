/**
 * 
 */
package org.eclipse.emf.ecp.editor.descriptor;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * @author Eugen Neufeld
 */
public abstract class AbstractAttributeDescriptor<T>
{
  /**
   * @param propertyDescriptor
   *          the property descriptor
   * @param modelElement
   *          the model element
   * @return Returns the property from a given propertyDescriptor as an A value.
   */

  public T getValue(IItemPropertyDescriptor propertyDescriptor, EObject modelElement)
  {
    EAnnotation priority = ((EStructuralFeature)propertyDescriptor.getFeature(modelElement))
        .getEAnnotation("org.eclipse.emf.ecp.editor");
    if (priority == null || priority.getDetails() == null || priority.getDetails().get(getAnnotationName()) == null)
    {
      return getDefaultValue();
    }
    String s = priority.getDetails().get(getAnnotationName());
    return getTypedValue(s);
  }

  protected abstract String getAnnotationName();

  protected abstract T getDefaultValue();

  protected abstract T getTypedValue(String value);
}
