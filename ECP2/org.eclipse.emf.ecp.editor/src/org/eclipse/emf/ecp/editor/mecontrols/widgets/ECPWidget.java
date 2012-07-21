/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 */
public abstract class ECPWidget
{
  private final EMFDataBindingContext dbc;

  public ECPWidget(EMFDataBindingContext dbc)
  {
    this.dbc = dbc;
  }

  public abstract Control createWidget(final FormToolkit toolkit, final Composite composite, final int style);

  public abstract void setEditable(boolean isEditable);

  public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);

  /**
   * @return the dbc
   */
  protected EMFDataBindingContext getDbc()
  {
    return dbc;
  }

  /**
   * @return the Control
   */
  public abstract Control getControl();
}
