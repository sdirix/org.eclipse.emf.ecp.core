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
 * 
 * @author Shterev
 * @author Eugen Neufeld
 */
public class AnnotationPositionDescriptor extends AbstractAttributeDescriptor<String>
{

  // private String defaultValue = "left";
  // /**
  // * {@inheritDoc}
  // */
  // public String getValue(IItemPropertyDescriptor propertyDescriptor, EObject modelElement) {
  // EAnnotation priority = ((EStructuralFeature) propertyDescriptor.getFeature(modelElement))
  // .getEAnnotation("org.eclipse.emf.ecp.editor");
  // if (priority == null || priority.getDetails() == null || priority.getDetails().get("position") == null) {
  // return defaultValue;
  // }
  // String s = priority.getDetails().get("position");
  // return s;
  // }
  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getAnnotationName()
   */
  @Override
  protected String getAnnotationName()
  {
    return "position";
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getDefaultValue()
   */
  @Override
  protected String getDefaultValue()
  {
    return "left";
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getTypedValue(java.lang.String)
   */
  @Override
  protected String getTypedValue(String value)
  {
    return value;
  }

}
