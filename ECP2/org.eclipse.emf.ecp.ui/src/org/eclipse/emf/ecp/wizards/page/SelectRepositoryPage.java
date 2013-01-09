/**
 * 
 */
package org.eclipse.emf.ecp.wizards.page;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.ui.model.RepositoriesContentProvider;
import org.eclipse.emf.ecp.ui.model.RepositoriesLabelProvider;
import org.eclipse.emf.ecp.wizards.ShareWizard;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
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

  /**
   * @param pageName
   */
  public SelectRepositoryPage(String pageName)
  {
    super(pageName);
    setTitle(Messages.SelectRepositoryPage_PageTitle_SelectRepository);
    setDescription(Messages.SelectRepositoryPage_PageDescription_SelectRepository);
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
        ((ShareWizard)getWizard()).getProvider());
    TableViewer viewer = new TableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(contentProvider);
    viewer.setLabelProvider(new RepositoriesLabelProvider(contentProvider));
    viewer.setSorter(new ViewerSorter());
    viewer.setInput(ECPRepositoryManager.INSTANCE);
    viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent event)
      {
        ECPRepository ecpRepository = (ECPRepository)((IStructuredSelection)event.getSelection()).getFirstElement();
        ((ShareWizard)getWizard()).setSelectedRepository(ecpRepository);
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
