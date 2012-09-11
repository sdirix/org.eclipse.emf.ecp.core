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
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.BooleanWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget;

/**
 * This is the standard Control to edit boolean values.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public class MEBoolControl extends MEAttributeControl
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
   */
  @Override
  protected Class<?> getClassType()
  {
    return Boolean.class;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
   * EMFDataBindingContext)
   */
  @Override
  protected ECPAttributeWidget getAttributeWidget(EMFDataBindingContext dbc)
  {
    return new BooleanWidget(dbc);
  }

}
