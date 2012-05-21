/**
 * 
 */
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.ui.util.MEClassLabelProvider;
import org.eclipse.emf.ecp.wizards.ModelClassFilter;
import org.eclipse.emf.ecp.wizards.ModelTreeContentProvider;

import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import java.util.Collection;

/**
 * @author Eugen Neufeld
 */
public class ModelElementSelectionTreeDialog extends ElementTreeSelectionDialog
{
  private ModelClassFilter filter;

  public ModelElementSelectionTreeDialog(Shell shell, Collection<EPackage> ePackages,
      Collection<EPackage> unsupportedEPackages, Collection<EPackage> projectFilteredEPackages,
      Collection<EClass> projectFilteredEClasses)
  {
    super(shell, new MEClassLabelProvider(), new ModelTreeContentProvider(ePackages, unsupportedEPackages,
        projectFilteredEPackages, projectFilteredEClasses));
    filter = new ModelClassFilter();
    setInput(new Object());
    setMessage("Select the packages that should be suggested when creating new elements");
    setTitle("Select packages");
    addFilter(filter);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    createMessageArea(composite).setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    Label filterLabel = new Label(composite, SWT.LEFT);
    filterLabel.setText("Search:");
    final Text filterInput = new Text(composite, SWT.SEARCH);
    filterInput.setMessage("Model Element class");
    filterInput.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

    filterInput.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        String text = filterInput.getText();
        filter.setSearchTerm(text);
        getTreeViewer().expandAll();
        if (text != null && text.length() == 0)
        {
          getTreeViewer().collapseAll();
        }
        getTreeViewer().refresh();
      }
    });
    createTreeViewer(composite).getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    getTreeViewer().setSorter(new ViewerSorter());

    return composite;
  }
}
