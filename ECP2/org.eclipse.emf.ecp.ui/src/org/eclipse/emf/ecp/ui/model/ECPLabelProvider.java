/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.model;

import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.TreeContentProvider.ErrorElement;
import org.eclipse.emf.ecp.ui.model.TreeContentProvider.SlowElement;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eike Stepper
 */
public class ECPLabelProvider extends LabelProvider implements ECPModelContextProvider
{
  private final ECPModelContextProvider modelContextProvider;

  public ECPLabelProvider(ECPModelContextProvider modelContextProvider)
  {
    this.modelContextProvider = modelContextProvider;
  }

  @Override
  public String getText(Object element)
  {
    UIProvider uiProvider = getUIProvider(element);
    if (uiProvider != null)
    {
      String text = uiProvider.getText(element);
      if (text != null)
      {
        return text;
      }
    }

    return super.getText(element);
  }

  @Override
  public Image getImage(Object element)
  {
    if (element instanceof SlowElement)
    {
      return Activator.getImage("icons/pending.gif");
    }

    if (element instanceof ErrorElement)
    {
      return Activator.getImage("icons/error.gif");
    }

    UIProvider uiProvider = getUIProvider(element);
    if (uiProvider != null)
    {
      Image image = uiProvider.getImage(element);
      if (image != null)
      {
        return image;
      }
    }

    return super.getImage(element);
  }

  public UIProvider getUIProvider(Object element)
  {
    UIProvider uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(element);
    if (uiProvider == null)
    {
      ECPModelContext modelContext = getModelContext(element);
      if (modelContext != null)
      {
        uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(modelContext);
      }
    }

    return uiProvider;
  }

  public ECPModelContext getModelContext(Object element)
  {
    if (modelContextProvider != null)
    {
      return modelContextProvider.getModelContext(element);
    }

    return null;
  }

  /**
   * @deprecated Call {@link #fireEvent(LabelProviderChangedEvent)}
   */
  @Deprecated
  @Override
  protected void fireLabelProviderChanged(LabelProviderChangedEvent event)
  {
    super.fireLabelProviderChanged(event);
  }

  protected final void fireEvent(final LabelProviderChangedEvent event)
  {
    Display display = Display.getCurrent();
    if (display == null)
    {
      Display.getDefault().asyncExec(new Runnable()
      {
        public void run()
        {
          fireLabelProviderChanged(event);
        }
      });
    }
    else
    {
      fireLabelProviderChanged(event);
    }
  }
}
