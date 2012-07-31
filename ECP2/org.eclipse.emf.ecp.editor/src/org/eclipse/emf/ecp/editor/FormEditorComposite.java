/**
 * 
 */
package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.editor.descriptor.AnnotationHiddenDescriptor;
import org.eclipse.emf.ecp.editor.descriptor.AnnotationPositionDescriptor;
import org.eclipse.emf.ecp.editor.descriptor.AnnotationPriorityDescriptor;
import org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl;
import org.eclipse.emf.ecp.editor.mecontrols.IValidatableControl;
import org.eclipse.emf.ecp.editor.mecontrols.METextControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eugen Neufeld
 */
public class FormEditorComposite
{

  public FormEditorComposite(EObject modelElement, EditorModelelementContext modelElementContext, Composite parent,
      FormToolkit toolkit)
  {
    this.modelElement = modelElement;
    this.modelElementContext = modelElementContext;
    this.parent = parent;
    this.toolkit = toolkit;
  }

  private final Composite parent;

  private final FormToolkit toolkit;

  private final EObject modelElement;

  private final EditorModelelementContext modelElementContext;

  private Map<EStructuralFeature, AbstractMEControl> meControls = new HashMap<EStructuralFeature, AbstractMEControl>();

  private Map<AbstractMEControl, Diagnostic> valdiatedControls = new HashMap<AbstractMEControl, Diagnostic>();

  private List<IItemPropertyDescriptor> leftColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

  private List<IItemPropertyDescriptor> rightColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

  private List<IItemPropertyDescriptor> bottomAttributes = new ArrayList<IItemPropertyDescriptor>();

  private Composite leftColumnComposite;

  private Composite rightColumnComposite;

  private Composite bottomComposite;

  public Composite createUI()
  {
    Composite topComposite = toolkit.createComposite(parent);
    topComposite.setLayout(new GridLayout());
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topComposite);

    sortAndOrderAttributes();
    if (!rightColumnAttributes.isEmpty())
    {
      SashForm topSash = new SashForm(topComposite, SWT.HORIZONTAL);
      GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topSash);
      toolkit.adapt(topSash, true, true);
      topSash.setSashWidth(4);
      leftColumnComposite = toolkit.createComposite(topSash, SWT.NONE);
      rightColumnComposite = toolkit.createComposite(topSash, SWT.NONE);
      GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(5, 2, 5, 5)
          .applyTo(rightColumnComposite);
      GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(rightColumnComposite);
      int[] topWeights = { 50, 50 };
      topSash.setWeights(topWeights);
    }
    else
    {
      leftColumnComposite = toolkit.createComposite(topComposite, SWT.NONE);
    }

    GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(2, 5, 5, 5)
        .applyTo(leftColumnComposite);
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(leftColumnComposite);

    bottomComposite = toolkit.createComposite(topComposite);
    GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(0, 0, 0, 0)
        .applyTo(bottomComposite);
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(bottomComposite);

    // Sort and order attributes
    // Create attributes
    createAttributes(toolkit, leftColumnComposite, leftColumnAttributes);
    if (!rightColumnAttributes.isEmpty())
    {
      createAttributes(toolkit, rightColumnComposite, rightColumnAttributes);
    }
    createAttributes(toolkit, bottomComposite, bottomAttributes);

    return topComposite;
  }

  /**
   * Filters attributes marked with "hidden=true" annotation
   * 
   * @param propertyDescriptors
   *          property descriptors to filter
   */
  private void filterHiddenAttributes(Collection<IItemPropertyDescriptor> propertyDescriptors)
  {
    Iterator<IItemPropertyDescriptor> iterator = propertyDescriptors.iterator();

    AnnotationHiddenDescriptor visibilityDescriptor = new AnnotationHiddenDescriptor();

    while (iterator.hasNext())
    {
      IItemPropertyDescriptor descriptor = iterator.next();

      if (visibilityDescriptor.getValue(descriptor, modelElement))
      {
        iterator.remove();
      }
    }
  }

  private void sortAndOrderAttributes()
  {

    AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
        new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

    List<IItemPropertyDescriptor> propertyDescriptors = adapterFactoryItemDelegator
        .getPropertyDescriptors(modelElement);
    if (propertyDescriptors != null)
    {
      filterHiddenAttributes(propertyDescriptors);
      AnnotationPositionDescriptor positionDescriptor = new AnnotationPositionDescriptor();
      for (IItemPropertyDescriptor itemPropertyDescriptor : propertyDescriptors)
      {
        String value = positionDescriptor.getValue(itemPropertyDescriptor, modelElement);
        if (value.equals("left"))
        {
          leftColumnAttributes.add(itemPropertyDescriptor);
        }
        else if (value.equals("right"))
        {
          rightColumnAttributes.add(itemPropertyDescriptor);
        }
        else if (value.equals("bottom"))
        {
          bottomAttributes.add(itemPropertyDescriptor);
        }
        else
        {
          leftColumnAttributes.add(itemPropertyDescriptor);
        }
      }

      final HashMap<IItemPropertyDescriptor, Double> priorityMap = new HashMap<IItemPropertyDescriptor, Double>();
      AnnotationPriorityDescriptor priorityDescriptor = new AnnotationPriorityDescriptor();
      for (IItemPropertyDescriptor itemPropertyDescriptor : propertyDescriptors)
      {
        priorityMap.put(itemPropertyDescriptor, priorityDescriptor.getValue(itemPropertyDescriptor, modelElement));
      }

      Comparator<IItemPropertyDescriptor> comparator = new Comparator<IItemPropertyDescriptor>()
      {
        public int compare(IItemPropertyDescriptor o1, IItemPropertyDescriptor o2)
        {
          return Double.compare(priorityMap.get(o1), priorityMap.get(o2));
        }
      };
      Collections.sort(leftColumnAttributes, comparator);
      Collections.sort(rightColumnAttributes, comparator);
      Collections.sort(bottomAttributes, comparator);

    }

  }

  private void createAttributes(FormToolkit toolkit, Composite column, List<IItemPropertyDescriptor> attributes)
  {
    Composite attributeComposite = toolkit.createComposite(column);
    GridLayoutFactory.fillDefaults().numColumns(2).applyTo(attributeComposite);
    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.BEGINNING).indent(10, 0)
        .applyTo(attributeComposite);

    // TODO add extensionpoint
    ControlFactory controlFactory = ControlFactory.getInstance();

    for (IItemPropertyDescriptor itemPropertyDescriptor : attributes)
    {
      AbstractMEControl meControl = controlFactory.createControl(itemPropertyDescriptor, modelElement,
          modelElementContext);
      if (meControl == null)
      {
        continue;
      }
      meControls.put((EStructuralFeature)itemPropertyDescriptor.getFeature(modelElement), meControl);
      Control control;
      if (meControl.getShowLabel())
      {
        Label label = toolkit.createLabel(attributeComposite, itemPropertyDescriptor.getDisplayName(modelElement));
        label.setData(modelElement);
        label.setToolTipText(itemPropertyDescriptor.getDescription(modelElement));
        control = meControl.createControl(attributeComposite, SWT.WRAP, itemPropertyDescriptor, modelElement,
            modelElementContext, toolkit);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(label);
        GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).indent(10, 0).applyTo(control);
        meControl.applyCustomLayoutData();
      }
      else
      {
        control = meControl.createControl(attributeComposite, SWT.WRAP, itemPropertyDescriptor, modelElement,
            modelElementContext, toolkit);
        control.setData(modelElement);
        control.setToolTipText(itemPropertyDescriptor.getDescription(modelElement));
        GridDataFactory.fillDefaults().span(2, 1).grab(true, true).align(SWT.FILL, SWT.BEGINNING).indent(10, 0)
            .applyTo(control);
      }
    }

  }

  /**
   * @return the modelElement
   */
  public EObject getModelElement()
  {
    return modelElement;
  }

  /**
   * @return the modelElementContext
   */
  public EditorModelelementContext getModelElementContext()
  {
    return modelElementContext;
  }

  /**
   * Dispose the ModelElement controls.
   */
  public void dispose()
  {
    for (AbstractMEControl control : meControls.values())
    {
      control.dispose();
    }
  }

  /**
   * Set focus to the controls.
   */
  public void setFocus()
  {
    // set keyboard focus on the first Text control
    for (AbstractMEControl meControl : meControls.values())
    {
      if (meControl instanceof METextControl)
      {
        ((METextControl)meControl).setFocus();
        return;
      }
    }
    leftColumnComposite.setFocus();
  }

  /**
   * Triggers live validation of the model attributes.
   **/
  public void updateLiveValidation()
  {
    Diagnostic diagnostic = Diagnostician.INSTANCE.validate(modelElement);
    List<AbstractMEControl> affectedControls = new ArrayList<AbstractMEControl>();

    for (Iterator<Diagnostic> i = diagnostic.getChildren().iterator(); i.hasNext();)
    {
      Diagnostic childDiagnostic = i.next();
      Object object = childDiagnostic.getData().get(0);
      if (object instanceof EObject)
      {
        EObject eObject = (EObject)object;
        if (eObject != modelElement)
        {
          continue;
        }
      }
      if (childDiagnostic.getData().size() < 2)
      {
        continue;
      }
      AbstractMEControl meControl = meControls.get(childDiagnostic.getData().get(1));
      affectedControls.add(meControl);
      if (meControl instanceof IValidatableControl)
      {
        if (valdiatedControls.containsKey(meControl))
        {
          if (childDiagnostic.getSeverity() != valdiatedControls.get(meControl).getSeverity())
          {
            ((IValidatableControl)meControl).handleValidation(childDiagnostic);
            valdiatedControls.put(meControl, childDiagnostic);
          }
        }
        else
        {
          ((IValidatableControl)meControl).handleValidation(childDiagnostic);
          valdiatedControls.put(meControl, childDiagnostic);
        }
      }
    }

    Map<AbstractMEControl, Diagnostic> temp = new HashMap<AbstractMEControl, Diagnostic>();
    temp.putAll(valdiatedControls);
    for (Map.Entry<AbstractMEControl, Diagnostic> entry : temp.entrySet())
    {
      AbstractMEControl meControl = entry.getKey();
      if (!affectedControls.contains(meControl))
      {
        valdiatedControls.remove(meControl);
        ((IValidatableControl)meControl).resetValidation();
      }
    }
  }
}
