package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.model.MEClassLabelProvider;
import org.eclipse.emf.ecp.ui.model.ModelClassFilter;
import org.eclipse.emf.ecp.ui.model.ModelTreeContentProvider;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.Collection;

public abstract class AbstractModelElementHelper
{
  private final Collection<EPackage> ePackages;

  private final Collection<EPackage> unsupportedEPackages;

  private final Collection<EPackage> filteredEPackages;

  private final Collection<EClass> filteredEClasses;

  private TreeViewer treeViewer;

  public AbstractModelElementHelper(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
      Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses)
  {
    super();
    this.ePackages = ePackages;
    this.unsupportedEPackages = unsupportedEPackages;
    this.filteredEPackages = filteredEPackages;
    this.filteredEClasses = filteredEClasses;
  }

  /**
   * @param project
   */
  public AbstractModelElementHelper(ECPProject project)
  {
    this(ECPUtil.getAllRegisteredEPackages(), project.getUnsupportedEPackages(), project.getFilteredPackages(), project
        .getFilteredEClasses());
  }

  public Composite createUI(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NULL);

    GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(composite);

    Label filterLabel = new Label(composite, SWT.LEFT);
    filterLabel.setText("Search:");
    final Text filterInput = new Text(composite, SWT.SEARCH);
    filterInput.setMessage("Model Element class");
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(filterInput);

    final ModelClassFilter filter = new ModelClassFilter();
    filterInput.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        String text = filterInput.getText();
        filter.setSearchTerm(text);
        treeViewer.expandAll();
        if (text != null && text.length() == 0)
        {
          treeViewer.collapseAll();
        }
        treeViewer.refresh();
      }
    });

    if (isCheckedTree())
    {
      createCheckedTreeViewer(composite);
    }
    else
    {
      createTreeViewer(composite);
    }

    treeViewer.setComparator(new ViewerComparator());
    treeViewer.addFilter(filter);
    // give an empty object, otherwise it does not initialize

    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).minSize(0, 150).span(2, 1)
        .applyTo(treeViewer.getTree());
    return composite;
  }

  protected abstract boolean isCheckedTree();

  private void createTreeViewer(Composite composite)
  {
    treeViewer = TreeViewerFactory.createTreeViewer(composite, new MEClassLabelProvider(),
        new ModelTreeContentProvider(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses),
        new Object(), null);
  }

  private void createCheckedTreeViewer(Composite composite)
  {
    treeViewer = TreeViewerFactory.createCheckedTreeViewer(composite, new MEClassLabelProvider(),
        new ModelTreeContentProvider(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses),
        new Object(), null);
  }

  /**
   * @return the treeViewer
   */
  public TreeViewer getTreeViewer()
  {
    return treeViewer;
  }

}
