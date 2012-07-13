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
public class AnnotationPriorityDescriptor extends AbstractAttributeDescriptor<Double>
{
  //
  // private double defaultValue = 100.0;
  //
  // /**
  // * {@inheritDoc}
  // */
  // public Double getValue(IItemPropertyDescriptor propertyDescriptor, EObject modelElement) {
  // EAnnotation priority = ((EStructuralFeature) propertyDescriptor.getFeature(modelElement))
  // .getEAnnotation("org.eclipse.emf.ecp.editor");
  // if (priority == null || priority.getDetails() == null || priority.getDetails().get("priority") == null) {
  // return defaultValue;
  // }
  // String s = priority.getDetails().get("priority");
  // return Double.parseDouble(s);
  // }
  @Override
  protected String getAnnotationName()
  {
    return "priority";
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getDefaultValue()
   */
  @Override
  protected Double getDefaultValue()
  {
    return 100d;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.descriptor.AbstractAttributeDescriptor#getTypedValue(java.lang.String)
   */
  @Override
  protected Double getTypedValue(String value)
  {
    return Double.parseDouble(value);
  }
}
