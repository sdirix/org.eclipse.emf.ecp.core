/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;

/**
 * @author Eugen Neufeld
 */
public class DoubleWidget extends AbstractTextWidget<Double>
{
  /**
   * @param dbc
   */
  public DoubleWidget(EMFDataBindingContext dbc)
  {
    super(dbc);
  }

  @Override
  protected Double convertStringToModel(String s)
  {
    return Double.parseDouble(s);
  }

  @Override
  protected boolean validateString(String s)
  {
    /*
     * Do not perform any validation here, since a double can be represented with characters which include 'E', 'f' or
     * 'd'. Furthermore if values become to be, 'Infinity' is also a valid value.
     */
    return true;
  }

  @Override
  protected String convertModelToString(Double t)
  {
    return Double.toString(t);
  }

  @Override
  protected void postValidate(String text)
  {
    try
    {
      setUnvalidatedString(Double.toString(Double.parseDouble(text)));
    }
    catch (NumberFormatException e)
    {
      setUnvalidatedString(Double.toString(getDefaultValue()));
    }
  }

  @Override
  protected Double getDefaultValue()
  {
    return 0.0;
  }
}
