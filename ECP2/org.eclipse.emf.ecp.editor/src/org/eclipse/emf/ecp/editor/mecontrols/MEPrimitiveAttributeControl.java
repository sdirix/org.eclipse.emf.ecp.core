/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Max Hohenegger (Bug 377561)
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A control used for primitive types that may be represented by a textual representation.
 * 
 * @author helming
 * @author emueller
 * @param <T>
 *          the actual primitive type
 */
public abstract class MEPrimitiveAttributeControl<T> extends AbstractMEControl implements IValidatableControl
{

  public static final Map<Class<?>, Class<?>> primitives = new HashMap<Class<?>, Class<?>>();

  static
  {
    primitives.put(Boolean.class, boolean.class);
    primitives.put(Byte.class, byte.class);
    primitives.put(Short.class, short.class);
    primitives.put(Character.class, char.class);
    primitives.put(Integer.class, int.class);
    primitives.put(Long.class, long.class);
    primitives.put(Float.class, float.class);
    primitives.put(Double.class, double.class);
  }

  // private Text text;

  protected boolean doVerify;

  private EAttribute attribute;

  private Composite composite;

  private Label labelWidgetImage; // Label for diagnostic image

  private ControlDecoration controlDecoration;

  private EMFDataBindingContext dbc;

  /**
   * Returns the priority by which the control should be rendered. The priority determines which control will be used to
   * render a specific type since multiple controls may be registered to render the same type.
   * 
   * @return an integer value representing the priority by which the control gets rendered
   */
  protected abstract int getPriority();

  /**
   * Returns a default value if the SWT Text control should be empty.
   * 
   * @return a default value
   */
  protected abstract T getDefaultValue();

  /**
   * Called when the SWT Text control loses its focus. This is useful for cases where {@link #validateString(String)}
   * returns true but the value entered by the user is invalid. For instance, this might be the case when the user
   * enters a number that would cause a overflow. In such cases the number should be set to max value that can
   * represented by the type in question.
   * 
   * @param text
   *          the string value currently entered in the SWT Text control
   */
  protected abstract void postValidate(String text);

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#canRender(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
   *      org.eclipse.emf.ecore.EObject)
   */
  @Override
  public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement)
  {
    Object feature = itemPropertyDescriptor.getFeature(modelElement);

    if (feature instanceof EAttribute
        && (((EAttribute)feature).getEType().getInstanceClass().equals(primitives.get(getClassType())) || ((EAttribute)feature)
            .getEType().getInstanceClass().equals(getClassType())) && !((EAttribute)feature).isMany())
    {

      return getPriority();
    }

    return AbstractMEControl.DO_NOT_RENDER;
  }

  @SuppressWarnings("unchecked")
  protected Class<T> getClassType()
  {
    Class<?> clazz = getClass();

    while (!(clazz.getGenericSuperclass() instanceof ParameterizedType))
    {
      clazz = clazz.getSuperclass();
    }

    Type[] actualTypeArguments = ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments();
    return (Class<T>)actualTypeArguments[0];
  }

  @Override
  protected Control createControl(Composite parent, int style)
  {
    // TODO: activate verification once again
    doVerify = false;
    Object feature = getItemPropertyDescriptor().getFeature(getModelElement());
    this.attribute = (EAttribute)feature;

    composite = getToolkit().createComposite(parent, style);
    composite.setBackgroundMode(SWT.INHERIT_FORCE);
    GridLayoutFactory.fillDefaults().numColumns(2).spacing(2, 0).applyTo(composite);
    GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

    labelWidgetImage = getToolkit().createLabel(composite, "    ");
    labelWidgetImage.setBackground(parent.getBackground());

    Control control = createAttributeControl(composite, style);
    control.setEnabled(isEditable());

    controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
    controlDecoration.setDescriptionText("Invalid input");
    controlDecoration.setShowHover(true);
    FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
        FieldDecorationRegistry.DEC_ERROR);
    controlDecoration.setImage(fieldDecoration.getImage());

    IObservableValue model = EMFEditObservables.observeValue(getEditingDomain(), getModelElement(), attribute);
    dbc = new EMFDataBindingContext();
    IObservableValue observeText = getObservableValue();
    dbc.bindValue(observeText, model, getTargetToModelStrategy(), getModelToTargetStrategy());

    addVerifyListener();

    addFocusListener(dbc);

    return composite;
  }

  /**
   * @return
   */
  protected abstract Control createAttributeControl(Composite composite, int style);

  /**
   * 
   */
  protected abstract void addVerifyListener();

  protected abstract void addFocusListener(final EMFDataBindingContext dbc);

  protected abstract IObservableValue getObservableValue();

  /**
   * Returns the strategy that is used to convert the targetValue to the model value. This is the default imlpementation
   * where every targetvalue is valid.
   * 
   * @return the target to model update value strategy
   */
  protected UpdateValueStrategy getTargetToModelStrategy()
  {
    UpdateValueStrategy strategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
    strategy.setAfterGetValidator(new IValidator()
    {

      public IStatus validate(Object value)
      {
        getControlDecoration().hide();
        return ValidationStatus.ok();
      }

    });
    return strategy;
  }

  /**
   * Returns the strategy that is used to convert the model value to the targetValue. This is the default
   * implementation.
   * 
   * @return the model to string update value strategy
   */
  protected UpdateValueStrategy getModelToTargetStrategy()
  {
    return null;
  }

  /**
   * {@inheritDoc}
   **/
  public void handleValidation(Diagnostic diagnostic)
  {
    if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING)
    {
      Image image = org.eclipse.emf.ecp.editor.Activator.getImageDescriptor("icons/validation_error.png").createImage();
      this.labelWidgetImage.setImage(image);
      this.labelWidgetImage.setToolTipText(diagnostic.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   **/
  public void resetValidation()
  {
    this.labelWidgetImage.setImage(null);
    this.labelWidgetImage.setToolTipText("");
  }

  /**
   * @return the controlDecoration
   */
  public ControlDecoration getControlDecoration()
  {
    return controlDecoration;
  }

  /**
   * @return the attribute
   */
  public EAttribute getAttribute()
  {
    return attribute;
  }

}
