/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * @author Eugen Neufeld
 */
public abstract class ECPAttributeWidget extends ECPWidget
{
  private final EMFDataBindingContext dbc;

  public ECPAttributeWidget(EMFDataBindingContext dbc)
  {
    this.dbc = dbc;
  }

  public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);

  /**
   * @return the dbc
   */
  protected EMFDataBindingContext getDbc()
  {
    return dbc;
  }
}
