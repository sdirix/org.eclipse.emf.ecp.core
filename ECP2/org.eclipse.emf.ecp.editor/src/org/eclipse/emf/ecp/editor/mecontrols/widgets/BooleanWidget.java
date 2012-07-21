/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 */
public class BooleanWidget extends ECPWidget
{

  /**
   * @param dbc
   */
  public BooleanWidget(EMFDataBindingContext dbc)
  {
    super(dbc);
  }

  Button check;

  @Override
  public Control createWidget(final FormToolkit toolkit, final Composite composite, final int style)
  {
    check = toolkit.createButton(composite, "", SWT.CHECK);
    return check;
  }

  @Override
  public void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration)
  {
    IObservableValue targetValue = SWTObservables.observeSelection(check);
    getDbc().bindValue(targetValue, modelValue);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget#setEditable(boolean)
   */
  @Override
  public void setEditable(boolean isEditable)
  {
    check.setEnabled(isEditable);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget#getControl()
   */
  @Override
  public Control getControl()
  {
    return check;
  }
}
