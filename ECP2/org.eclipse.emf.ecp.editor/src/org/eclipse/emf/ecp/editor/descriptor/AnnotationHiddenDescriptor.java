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
package org.eclipse.emf.ecp.editor.descriptor;


/**
 * A {@link IAttributeDescriptor} using the annotation in the genmodel.
 */
public class AnnotationHiddenDescriptor extends AbstractAttributeDescriptor<Boolean>
{

  // private final Boolean defaultValue = new Boolean(false);

  // /**
  // * Returns true, if feature associated with given.
  // * {@link IItemPropertyDescriptor} marked with hidden=true annotation
  // *
  // * @see
  // org.eclipse.emf.ecp.editor.IAttributeDescriptor#getValue(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
  // * org.eclipse.emf.ecore.EObject)
  // * @param propertyDescriptor
  // * the {@link IItemPropertyDescriptor}
  // * @param modelElement
  // * the {@link EObject}
  // * @return isHidden as boolean
  // */
  // public Boolean getValue(IItemPropertyDescriptor propertyDescriptor,
  // EObject modelElement) {
  // EAnnotation priority = ((EStructuralFeature) propertyDescriptor.getFeature(modelElement))
  // .getEAnnotation("org.eclipse.emf.ecp.editor");
  // if (priority == null || priority.getDetails() == null || priority.getDetails().get("hidden") == null) {
  // return defaultValue;
  // }
  // return Boolean.parseBoolean(priority.getDetails().get("hidden"));
  // }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getAnnotationName()
   */
  @Override
  protected String getAnnotationName()
  {
    return "hidden";
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getDefaultValue()
   */
  @Override
  protected Boolean getDefaultValue()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getTypedValue(java.lang.String)
   */
  @Override
  protected Boolean getTypedValue(String value)
  {
    return Boolean.parseBoolean(value);
  }
}
