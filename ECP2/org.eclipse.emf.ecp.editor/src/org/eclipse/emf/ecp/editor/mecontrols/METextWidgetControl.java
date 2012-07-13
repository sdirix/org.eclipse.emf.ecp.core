/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.databinding.EMFDataBindingContext;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eugen Neufeld
 */
public abstract class METextWidgetControl<T> extends MEPrimitiveAttributeControl<T>
{
  private Text text;

  /**
   * Sets the content of the SWT text control to the given string without calling {@link #validateString(String)}.
   * 
   * @param string
   *          the content of the SWT Text control
   */
  protected void setUnvalidatedString(String string)
  {
    boolean oldDoVerify = doVerify;
    doVerify = false;
    text.setText(string);
    doVerify = oldDoVerify;
  }

  /**
   * Validates the string entered in the text field. This method is executed whenever the user modifies the text
   * contained in the SWT Text control.
   * 
   * @param text
   *          the text to be validated
   * @return true if <code>text</code> is a valid model value
   */
  protected abstract boolean validateString(String value);

  /**
   * 
   */
  @Override
  protected void addVerifyListener()
  {
    text.addVerifyListener(new VerifyListener()
    {

      public void verifyText(VerifyEvent e)
      {
        if (doVerify)
        {

          final String oldText = text.getText();
          String newText = oldText.substring(0, e.start) + e.text + oldText.substring(e.end);
          if (!validateString(newText))
          {
            e.doit = false;
            return;
          }
        }
      }
    });
  }

  /**
   * Converts the string value to the actual model value.
   * 
   * @param s
   *          the string value to be converted
   * @return the model value of type <code>T</code>
   */
  protected abstract T convertStringToModel(String s);

  /**
   * Converts the actual model value to its textual representation.
   * 
   * @param modelValue
   *          the model value which needs to be converted to its textual representation
   * @return the string representation of the converted model value
   */
  protected abstract String convertModelToString(T modelValue);

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#addFocusListener()
   */
  @Override
  protected void addFocusListener(final EMFDataBindingContext dbc)
  {
    text.addFocusListener(new FocusListener()
    {

      public void focusLost(FocusEvent e)
      {
        if (text.getText().equals(""))
        {
          setUnvalidatedString(convertModelToString(getDefaultValue()));
        }
        else
        {
          postValidate(text.getText());
        }

        dbc.updateModels();
        dbc.updateTargets();
      }

      public void focusGained(FocusEvent e)
      {
        // do nothing
      }
    });
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#getObservableValue()
   */
  @Override
  protected ISWTObservableValue getObservableValue()
  {
    return SWTObservables.observeText(text, SWT.FocusOut);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyCustomLayoutData()
  {
    GridDataFactory.fillDefaults().grab(true, false).hint(250, 16).align(SWT.FILL, SWT.TOP).applyTo(text);
  }

  /**
   * This sets the keyboard focus in Text control.
   **/
  public void setFocus()
  {
    text.setFocus();
  }

  /**
   * Returns the strategy that is used to convert the string to the model value.
   * 
   * @return the target to model update value strategy
   */
  @Override
  protected UpdateValueStrategy getTargetToModelStrategy()
  {
    // Define a validator to check that only numbers are entered
    IValidator validator = new IValidator()
    {
      public IStatus validate(Object value)
      {
        boolean valid = validateString(text.getText());

        if (valid)
        {
          getControlDecoration().hide();
          return ValidationStatus.ok();
        }

        getControlDecoration().show();
        return ValidationStatus.error("Not a double.");
      }
    };
    UpdateValueStrategy strategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
    strategy.setAfterGetValidator(validator);
    strategy.setConverter(new IConverter()
    {

      public Object getToType()
      {
        return getClassType();
      }

      public Object getFromType()
      {
        return String.class;
      }

      public Object convert(Object fromObject)
      {
        T convertedValue = convertStringToModel((String)fromObject);
        return convertedValue;
      }
    });

    return strategy;
  }

  @Override
  protected UpdateValueStrategy getModelToTargetStrategy()
  {
    UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
    updateValueStrategy.setConverter(new IConverter()
    {

      public Object getToType()
      {
        return String.class;
      }

      public Object getFromType()
      {
        return getClassType();
      }

      public Object convert(Object fromObject)
      {
        @SuppressWarnings("unchecked")
        T val = (T)fromObject;
        return convertModelToString(val);
      }
    });
    return updateValueStrategy;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.MEPrimitiveAttributeControl#getControl()
   */
  @Override
  protected Control createAttributeControl(Composite composite, int style)
  {
    text = getToolkit().createText(composite, new String(), style | SWT.SINGLE | SWT.BORDER);
    text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    // text.setEditable(isEditable());

    return text;
  }
}
