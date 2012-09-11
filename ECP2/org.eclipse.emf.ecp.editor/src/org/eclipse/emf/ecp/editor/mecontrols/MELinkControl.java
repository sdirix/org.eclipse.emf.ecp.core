/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.ModelElementChangeListener;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.LinkWidget;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen Neufeld
 */
public class MELinkControl extends AbstractMEControl
{

  private Label labelWidgetImage;

  private ControlDecoration controlDecoration;

  private Control control;

  private ModelElementChangeListener modelElementChangeListener;

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getEStructuralFeatureType()
   */
  @Override
  protected Class<? extends EStructuralFeature> getEStructuralFeatureType()
  {
    return EReference.class;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
   */
  @Override
  protected Class<?> getClassType()
  {
    return EObject.class;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#isMulti()
   */
  @Override
  protected boolean isMulti()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#createControl(org.eclipse.swt.widgets.Composite, int)
   */
  @Override
  protected Control createControl(Composite parent, final int style)
  {
    final Composite composite = getToolkit().createComposite(parent, style);
    composite.setBackgroundMode(SWT.INHERIT_FORCE);
    GridLayoutFactory.fillDefaults().numColumns(2).spacing(2, 0).applyTo(composite);
    GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

    labelWidgetImage = getToolkit().createLabel(composite, "    ");
    labelWidgetImage.setBackground(parent.getBackground());
    createWidgetControl(composite, style);

    modelElementChangeListener = new ModelElementChangeListener(getModelElement())
    {

      @Override
      public void onChange(Notification notification)
      {
        if (notification.getFeature() == getStructuralFeature())
        {
          createWidgetControl(composite, style);
        }
      }
    };

    controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
    controlDecoration.setDescriptionText("Invalid input");
    controlDecoration.setShowHover(true);
    FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
        FieldDecorationRegistry.DEC_ERROR);
    controlDecoration.setImage(fieldDecoration.getImage());
    controlDecoration.hide();

    return composite;
  }

  private void createWidgetControl(final Composite composite, final int style)
  {
    if (control != null)
    {
      control.dispose();
    }
    final EObject opposite = (EObject)getModelElement().eGet(getStructuralFeature());
    if (opposite != null)
    {
      ECPWidget widget = getWidget(opposite);
      control = widget.createWidget(getToolkit(), composite, style);
      widget.setEditable(isEditable());
    }
    else
    {
      control = getToolkit().createLabel(composite, "(Not Set)");
      control.setBackground(composite.getBackground());
      control.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
    }
    composite.layout();
  }

  /**
   * @return
   */
  private ECPWidget getWidget(EObject opposite)
  {

    return new LinkWidget(getModelElement(), opposite, (EReference)getStructuralFeature(), getContext());
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getPriority()
   */
  @Override
  protected int getPriority()
  {
    // TODO Auto-generated method stub
    return 2;
  }

}
