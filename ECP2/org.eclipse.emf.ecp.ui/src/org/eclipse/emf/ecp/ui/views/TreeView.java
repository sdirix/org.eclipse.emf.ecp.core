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

import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.actions.RefreshViewerAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public abstract class TreeView extends ViewPart implements ISelectionProvider, ISetSelectionTarget
{
  public static final String GLOBAL_ADDITIONS = "global_additions";

  private final String id;

  private TreeViewer viewer;

  private DrillDownAdapter drillDownAdapter;

  private Action refreshAction;

  public TreeView(String id)
  {
    this.id = id;
  }

  public final String getID()
  {
    return id;
  }

  public final TreeViewer getViewer()
  {
    return viewer;
  }

  public final Action getRefreshAction()
  {
    return refreshAction;
  }

  @Override
  public void init(IViewSite site) throws PartInitException
  {
    super.init(site);
    site.setSelectionProvider(this);
  }

  @Override
  public final void createPartControl(Composite parent)
  {
    try
    {
      viewer = createViewer(parent);
      if (viewer == null)
      {
        throw new IllegalStateException("Viewer has not been created");
      }

      refreshAction = new RefreshViewerAction(viewer);
      drillDownAdapter = new DrillDownAdapter(viewer);

      hookContextMenu();
      hookDoubleClickAction();
      contributeToActionBars();
    }
    catch (RuntimeException ex)
    {
      Activator.log(ex);
      throw ex;
    }
    catch (Error ex)
    {
      Activator.log(ex);
      throw ex;
    }
  }

  @Override
  public void setFocus()
  {
    if (viewer != null)
    {
      viewer.getControl().setFocus();
    }
  }

  public IStructuredSelection getSelection()
  {
    if (viewer != null)
    {
      return (IStructuredSelection)viewer.getSelection();
    }

    return StructuredSelection.EMPTY;
  }

  public void setSelection(ISelection selection)
  {
    if (viewer != null)
    {
      viewer.setSelection(selection);
    }
  }

  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    if (viewer != null)
    {
      viewer.addSelectionChangedListener(listener);
    }
  }

  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    if (viewer != null)
    {
      viewer.removeSelectionChangedListener(listener);
    }
  }

  public void selectReveal(ISelection selection)
  {
    if (viewer != null)
    {
      viewer.setSelection(selection, true);
    }
  }

  protected void showMessage(String message)
  {
    MessageDialog.openInformation(viewer.getControl().getShell(), getTitle(), message);
  }

  protected ILabelDecorator createLabelDecorator()
  {
    return PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator();
  }

  protected TreeViewer createViewer(Composite parent)
  {
    TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    configureViewer(viewer);

    ILabelDecorator labelDecorator = createLabelDecorator();
    if (labelDecorator != null)
    {
      IBaseLabelProvider labelProvider = viewer.getLabelProvider();
      if (labelProvider instanceof ILabelProvider && !(labelProvider instanceof DecoratingLabelProvider))
      {
        viewer.setLabelProvider(new DecoratingLabelProvider((ILabelProvider)labelProvider, labelDecorator));
      }
    }

    return viewer;
  }

  protected abstract void configureViewer(TreeViewer viewer);

  protected void fillLocalPullDown(IMenuManager manager)
  {
    manager.add(new Separator());
    manager.add(refreshAction);
  }

  protected void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(refreshAction);
    // manager.add(new Separator());
    // drillDownAdapter.addNavigationActions(manager);
  }

  protected void fillContextMenu(IMenuManager manager)
  {
    manager.add(new Separator(GLOBAL_ADDITIONS));
    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    manager.add(new Separator());
  }

  protected void doubleClicked(DoubleClickEvent event)
  {
  }

  private void contributeToActionBars()
  {
    IActionBars bars = getViewSite().getActionBars();
    // fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  private void hookDoubleClickAction()
  {
    viewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        TreeView.this.doubleClicked(event);
      }
    });
  }

  private void hookContextMenu()
  {
    MenuManager manager = new MenuManager("#PopupMenu");
    manager.setRemoveAllWhenShown(true);
    manager.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        TreeView.this.fillContextMenu(manager);
      }
    });

    Control control = viewer.getControl();

    Menu menu = manager.createContextMenu(control);
    control.setMenu(menu);
    getSite().registerContextMenu(getID(), manager, viewer);
  }
}
