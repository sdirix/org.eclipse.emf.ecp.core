/**
 * 
 */
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

/**
 * @author Eugen Neufeld
 */
public class TreeViewerFactory
{

  public static TreeViewer createTreeViewer(Composite parent, ILabelProvider labelProvider,
      ITreeContentProvider contentProvider, Object input, ILabelDecorator labelDecorator)
  {
    TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    createTreeViewer(labelProvider, contentProvider, input, labelDecorator, viewer);
    return viewer;
  }

  public static TreeViewer createCheckedTreeViewer(Composite parent, ILabelProvider labelProvider,
      ITreeContentProvider contentProvider, Object input, ILabelDecorator labelDecorator)
  {
    final ContainerCheckedTreeViewer viewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
        | SWT.V_SCROLL);
    createTreeViewer(labelProvider, contentProvider, input, labelDecorator, viewer);
    return viewer;
  }

  /**
   * @param labelProvider
   * @param contentProvider
   * @param input
   * @param labelDecorator
   * @param viewer
   */
  private static void createTreeViewer(ILabelProvider labelProvider, ITreeContentProvider contentProvider,
      Object input, ILabelDecorator labelDecorator, TreeViewer viewer)
  {
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(labelProvider);
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(input);

    if (labelDecorator != null)
    {
      if (!(labelProvider instanceof DecoratingLabelProvider))
      {
        viewer.setLabelProvider(new DecoratingLabelProvider(labelProvider, labelDecorator));
      }
    }
  }
}
