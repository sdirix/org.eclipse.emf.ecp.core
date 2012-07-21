/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.IntegerWidget;

/**
 * @author Eugen Neufeld
 */
public class MultiIntegerAttributeControl extends MultiMEAttributeControl
{

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getAttributeWidget(org.eclipse
   * .emf.databinding.EMFDataBindingContext)
   */
  @Override
  protected ECPWidget getAttributeWidget(EMFDataBindingContext dbc)
  {
    return new IntegerWidget(dbc);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
   */
  @Override
  protected Class<?> getClassType()
  {
    return Integer.class;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getDefaultValue()
   */
  @Override
  protected Object getDefaultValue()
  {
    return 0;
  }
}
