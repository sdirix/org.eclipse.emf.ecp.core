/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.ui.model.RepositoriesContentProvider;
import org.eclipse.emf.ecp.ui.model.RepositoriesLabelProvider;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class SelectRepositoryPage extends WizardPage
{

  private TreeViewer viewer;

  /**
   * @param pageName
   */
  protected SelectRepositoryPage(String pageName)
  {
    super(pageName);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    container.setLayout(new GridLayout(1, true));

    RepositoriesContentProvider contentProvider = new RepositoriesContentProvider(
        ((RepositoryWizard)getWizard()).getProvider());
    viewer = new TreeViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new RepositoriesLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPRepositoryManager.INSTANCE);
    viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent event)
      {
        ECPRepository ecpRepository = (ECPRepository)((IStructuredSelection)event.getSelection()).getFirstElement();
        ((RepositoryWizard)getWizard()).setSelectedRepository(ecpRepository);
        setPageComplete(true);
      }
    });

    setControl(container);
    setPageComplete(false);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
   */
  @Override
  public boolean canFlipToNextPage()
  {
    return false;
  }

}
