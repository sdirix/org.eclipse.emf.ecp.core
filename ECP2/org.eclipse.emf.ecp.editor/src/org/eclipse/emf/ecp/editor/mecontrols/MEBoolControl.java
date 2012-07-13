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

import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This is the standard Control to edit boolean values.
 * 
 * @author helming
 */
public class MEBoolControl extends MEPrimitiveAttributeControl<Boolean>
{

  private Button check;

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#getPriority()
   */
  @Override
  protected int getPriority()
  {
    return 1;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#getDefaultValue()
   */
  @Override
  protected Boolean getDefaultValue()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#postValidate(java.lang.String)
   */
  @Override
  protected void postValidate(String text)
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#createAttributeControl(org.eclipse.swt.widgets
   * .Composite, int)
   */
  @Override
  protected Control createAttributeControl(Composite composite, int style)
  {
    check = getToolkit().createButton(composite, "", SWT.CHECK);
    return check;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#addVerifyListener()
   */
  @Override
  protected void addVerifyListener()
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#addFocusListener(org.eclipse.emf.databinding.
   * EMFDataBindingContext)
   */
  @Override
  protected void addFocusListener(EMFDataBindingContext dbc)
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#getObservableValue()
   */
  @Override
  protected ISWTObservableValue getObservableValue()
  {
    return SWTObservables.observeSelection(check);
  }

}
