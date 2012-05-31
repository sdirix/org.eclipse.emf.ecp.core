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
package org.eclipse.emf.ecp.ui.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ModelContentProvider;
import org.eclipse.emf.ecp.ui.model.ModelLabelProvider;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public class ModelExplorerView extends TreeView
{
  /**
   * @author Jonas
   * @author Eugen Neufeld
   */
  public class DoubleClickListener implements IDoubleClickListener
  {

    /**
     * Opens an EObject using the ActionHelper
     */
    public void doubleClick(DoubleClickEvent event)
    {
      if (event.getSelection() instanceof IStructuredSelection)
      {
        IStructuredSelection structuredSelection = (IStructuredSelection)event.getSelection();
        Object firstElement = structuredSelection.getFirstElement();
        if (firstElement instanceof EObject)
        {
          ECPModelContext context = ECPUtil.getModelContext(contentProvider, structuredSelection.toArray());
          ActionHelper.openModelElement((EObject)firstElement, "modelexplorer", (ECPProject)context);
        }
      }

    }
  }

  public static final String ID = "org.eclipse.emf.ecp.ui.ModelExplorerView";

  private ModelContentProvider contentProvider = new ModelContentProvider();

  public ModelExplorerView()
  {
    super(ID);
  }

  @Override
  protected void configureViewer(final TreeViewer viewer)
  {
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new ModelLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPProjectManager.INSTANCE);
    viewer.addDoubleClickListener(new DoubleClickListener());

    final MyEditingDomainViewerDropAdapter dropAdapter = new MyEditingDomainViewerDropAdapter(viewer);
    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
    viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
    viewer.addDropSupport(dndOperations, transfers, dropAdapter);
    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent event)
      {
        Object[] elements = getSelection().toArray();
        if (elements == null || elements.length == 0)
        {
          return;
        }
        EditingDomain domain = null;
        ECPProject project = null;
        if (elements[0] instanceof ECPProject)
        {
          ECPModelContext context = ECPUtil.getModelContext(contentProvider, elements);
          if (context != null && context instanceof ECPProject)
          {
            project = (ECPProject)context;
          }
        }
        else
        {
          ECPModelContextProvider contextProvider = (ECPModelContextProvider)viewer.getContentProvider();
          project = (ECPProject)ECPUtil.getModelContext(contextProvider, elements[0]);
        }
        domain = project.getEditingDomain();
        dropAdapter.setEditingDomain(domain);
      }
    });
  }

  @Override
  protected void fillContextMenu(IMenuManager manager)
  {
    Object[] elements = getSelection().toArray();

    ECPModelContext context = ECPUtil.getModelContext(contentProvider, elements);
    if (context != null)
    {
      UIProvider provider = UIProviderRegistry.INSTANCE.getUIProvider(context);
      if (provider != null)
      {
        provider.fillContextMenu(manager, context, elements);
      }
    }

    super.fillContextMenu(manager);
  }

  /**
   * @author Eugen Neufeld
   */
  private class MyEditingDomainViewerDropAdapter extends EditingDomainViewerDropAdapter
  {
    /*
     * @param viewer
     */
    public MyEditingDomainViewerDropAdapter(Viewer viewer)
    {
      super(null, viewer);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dragOver(DropTargetEvent event)
    {
      Object target = extractDropTarget(event.item);
      if (target instanceof ECPProject)
      {

        source = getDragSource(event);

        ECPProject project = (ECPProject)target;
        if (project.getElements().contains(source.iterator().next()))
        {
          event.detail = DND.DROP_NONE;
        }
        else
        {
          event.feedback = DND.FEEDBACK_SELECT | getAutoFeedback();
        }
      }
      else
      {
        super.dragOver(event);
      }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void drop(DropTargetEvent event)
    {
      Object target = extractDropTarget(event.item);

      if (target instanceof ECPProject)
      {
        ECPProject project = (ECPProject)target;

        source = getDragSource(event);

        project.getElements().add((EObject)source.iterator().next());
      }
      else
      {
        super.drop(event);
      }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dropAccept(DropTargetEvent event)
    {
      Object target = extractDropTarget(event.item);
      if (target instanceof ECPProject)
      {
        event.feedback = DND.FEEDBACK_SELECT | getAutoFeedback();

      }
      else
      {
        super.dropAccept(event);
      }
    }

    void setEditingDomain(EditingDomain editingDomain)
    {
      domain = editingDomain;

    }

  }
}
