/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;

/**
 * @author Eugen Neufeld
 */
public class StringWidget extends AbstractTextWidget<String>
{

  /**
   * @param dbc
   */
  public StringWidget(EMFDataBindingContext dbc)
  {
    super(dbc);
  }

  @Override
  protected String convertStringToModel(String s)
  {
    return s;
  }

  @Override
  protected boolean validateString(String s)
  {
    return true;
  }

  @Override
  protected String convertModelToString(String t)
  {
    return t;
  }

  @Override
  protected String getDefaultValue()
  {
    return "";
  }

  /*
   * (non-Javadoc)
   * @see AbstractTextWidget#postValidate(java.lang.String)
   */
  @Override
  protected void postValidate(String text)
  {
    // do nothing

  }

}
