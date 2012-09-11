/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.commands.ECPCommand;

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * @author Eugen Neufeld
 */
public class DateTimeWidget extends ECPAttributeWidget
{
  private ImageHyperlink dateDeleteButton;

  private DateTime dateWidget;

  private DateTime timeWidget;

  private Composite parentComposite;

  private EObject modelElement;

  private EStructuralFeature feature;

  /**
   * @param dbc
   */
  public DateTimeWidget(EMFDataBindingContext dbc, EObject modelElement, EStructuralFeature feature)
  {
    super(dbc);
    this.modelElement = modelElement;
    this.feature = feature;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.FormToolkit,
   * org.eclipse.swt.widgets.Composite, int)
   */
  @Override
  public Control createWidget(FormToolkit toolkit, Composite composite, int style)
  {
    int numColumns = ((GridLayout)composite.getLayout()).numColumns;
    GridLayoutFactory.fillDefaults().numColumns(numColumns + 2).spacing(2, 0).applyTo(composite);
    createDateAndTimeWidget(composite);
    // has to be set manually as child elements of a composite aren't disabled automatically
    parentComposite = composite;
    return parentComposite;
  }

  private void createDateAndTimeWidget(Composite composite)
  {

    dateWidget = new DateTime(composite, SWT.DATE);
    dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    timeWidget = new DateTime(composite, SWT.TIME | SWT.SHORT);
    timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    dateDeleteButton = new ImageHyperlink(composite, SWT.TOP);
    dateDeleteButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));

    dateDeleteButton.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseUp(MouseEvent e)
      {
        new ECPCommand(modelElement)
        {

          @Override
          protected void doRun()
          {
            modelElement.eSet(feature, null);
          }
        }.run(true);
      }
    });
  }

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#bindValue(org.eclipse.core.databinding.observable.value
   * .IObservableValue)
   */
  @Override
  public void bindValue(IObservableValue modelValue, final ControlDecoration controlDecoration)
  {
    IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
    IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
    IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
    getDbc().bindValue(target, modelValue);

  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#setEditable(boolean)
   */
  @Override
  public void setEditable(boolean isEditable)
  {
    dateDeleteButton.setEnabled(isEditable);
    dateWidget.setEnabled(isEditable);
    timeWidget.setEnabled(isEditable);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#getControl()
   */
  @Override
  public Control getControl()
  {
    return parentComposite;
  }

}
