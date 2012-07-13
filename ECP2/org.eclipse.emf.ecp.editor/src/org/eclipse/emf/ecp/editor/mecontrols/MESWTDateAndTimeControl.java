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
import org.eclipse.emf.ecp.editor.ECPCommand;

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import java.util.Date;

/**
 * An SWT-based date widget with an additional time field, which are both controlled by spinners.
 * 
 * @author Hunnilee
 */
public class MESWTDateAndTimeControl extends MEPrimitiveAttributeControl<Date>// AbstractMEControl implements
// IValidatableControl
{

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
  protected Date getDefaultValue()
  {
    return new Date();
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
    dateComposite = new Composite(composite, SWT.NONE);
    GridLayoutFactory.fillDefaults().numColumns(3).spacing(2, 0).applyTo(dateComposite);
    GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(dateComposite);

    createDateAndTimeWidget();
    return dateComposite;
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
  protected IObservableValue getObservableValue()
  {
    IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
    IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
    return new DateAndTimeObservableValue(dateObserver, timeObserver);
    // return null;
  }

  // private static final int PRIORITY = 3;
  //
  // private EAttribute attribute;
  //
  private ImageHyperlink dateDeleteButton;

  //
  private Composite dateComposite;

  //
  private DateTime dateWidget;

  private DateTime timeWidget;

  //
  // private Label labelWidgetImage; // Label for diagnostic image
  //
  // @Override
  // public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement)
  // {
  // return PRIORITY;
  // }
  //
  // @Override
  // protected Control createControl(Composite parent, int style)
  // {
  // attribute = (EAttribute)getItemPropertyDescriptor().getFeature(getModelElement());
  // dateComposite = getToolkit().createComposite(parent);
  // dateComposite.setBackgroundMode(SWT.INHERIT_FORCE);
  // GridLayoutFactory.fillDefaults().numColumns(4).spacing(2, 0).applyTo(dateComposite);
  // GridDataFactory.fillDefaults().grab(true, false).applyTo(dateComposite);
  //
  // createDateAndTimeWidget();
  //
  // IObservableValue model = EMFEditObservables.observeValue(getEditingDomain(), getModelElement(), attribute);
  // EMFDataBindingContext dbc = new EMFDataBindingContext();
  // IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
  // IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
  // dbc.bindValue(new DateAndTimeObservableValue(dateObserver, timeObserver), model, null, null);
  //
  // return dateComposite;
  // }
  //
  private void createDateAndTimeWidget()
  {

    dateWidget = new DateTime(dateComposite, SWT.DATE);
    dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    timeWidget = new DateTime(dateComposite, SWT.TIME | SWT.SHORT);
    timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    dateDeleteButton = new ImageHyperlink(dateComposite, SWT.TOP);
    dateDeleteButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));

    dateDeleteButton.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseUp(MouseEvent e)
      {
        new ECPCommand(getModelElement())
        {

          @Override
          protected void doRun()
          {
            getModelElement().eSet(getAttribute(), null);
          }
        }.run(true);
      }
    });
  }
  //
  // /**
  // * . {@inheritDoc}
  // */
  // public void handleValidation(Diagnostic diagnostic)
  // {
  // if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING)
  // {
  // Image image = org.eclipse.emf.ecp.editor.Activator.getImageDescriptor("icons/validation_error.png").createImage();
  // labelWidgetImage.setImage(image);
  // labelWidgetImage.setToolTipText(diagnostic.getMessage());
  // }
  // }
  //
  // /**
  // * . {@inheritDoc}
  // */
  // public void resetValidation()
  // {
  // labelWidgetImage.setImage(null);
  // labelWidgetImage.setToolTipText("");
  //
  // }

}
