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
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.associationclasscontrol;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.Activator;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.editor.MESuggestedSelectionDialog;
import org.eclipse.emf.ecp.editor.OverlayImageDescriptor;
import org.eclipse.emf.ecp.editor.commands.ECPCommand;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class provides the icon and command to link to a existing object over an AssociationClassElement.
 * 
 * @author Michael Haeger
 */
public class AddAssociationClassAction extends Action
{

  private static final String DIALOG_MESSAGE = "Enter model element name prefix or pattern (e.g. *Trun?)";

  private EReference eReference;

  private EObject modelElement;

  private final EditorModelelementContext context;

  /**
   * The link command.
   * 
   * @author Michael Haeger
   * @author Eugen Neufeld
   */
  private final class AddAssociationClassCommand extends ECPCommand
  {

    public AddAssociationClassCommand(EObject eObject)
    {
      super(eObject);
    }

    @Override
    protected void doRun()
    {
      Iterator<EObject> allElements = context.getLinkElements(eReference);
      Set<EObject> elements = new HashSet<EObject>();
      while (allElements.hasNext())
      {
        elements.add(allElements.next());
      }
      MESuggestedSelectionDialog dlg = new MESuggestedSelectionDialog("Select Elements", DIALOG_MESSAGE, true,
          modelElement, eReference, elements);
      if (dlg.open() == Window.OK)
      {
        if (eReference.isMany())
        {
          for (Object result : dlg.getResult())
          {
            AssociationClassHelper.createAssociation(eReference, modelElement, (EObject)result,
                context.getMetaModelElementContext());
          }
        }
        else
        {
          AssociationClassHelper.createAssociation(eReference, modelElement, (EObject)dlg.getFirstResult(),
              context.getMetaModelElementContext());
        }
      }
    }
  }

  /**
   * Default constructor.
   * 
   * @param modelElement
   *          the object
   * @param eReference
   *          the reference to the AssociationClassElement
   * @param descriptor
   *          the descriptor used to generate display content
   * @param context
   *          model element context
   */
  public AddAssociationClassAction(EObject modelElement, EReference eReference, IItemPropertyDescriptor descriptor,
      EditorModelelementContext context)
  {
    this.modelElement = modelElement;
    this.eReference = eReference;
    this.context = context;
    Object obj = null;
    if (!eReference.getEReferenceType().isAbstract())
    {
      obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance().create(eReference.getEReferenceType());
    }
    Image image = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
        ComposedAdapterFactory.Descriptor.Registry.INSTANCE)).getImage(obj);
    ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/link_overlay.png");
    OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
        OverlayImageDescriptor.LOWER_RIGHT);
    setImageDescriptor(imageDescriptor);
    String attribute = descriptor.getDisplayName(eReference);
    // make singular attribute labels
    if (attribute.endsWith("ies"))
    {
      attribute = attribute.substring(0, attribute.length() - 3) + "y";
    }
    else if (attribute.endsWith("s"))
    {
      attribute = attribute.substring(0, attribute.length() - 1);
    }
    setToolTipText("Link " + attribute);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    new AddAssociationClassCommand(modelElement).run(true);
  }
}
