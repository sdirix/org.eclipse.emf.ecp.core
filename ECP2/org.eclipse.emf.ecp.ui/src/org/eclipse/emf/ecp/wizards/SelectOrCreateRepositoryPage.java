/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class SelectOrCreateRepositoryPage extends WizardPage
{

  /**
   * @param pageName
   */
  protected SelectOrCreateRepositoryPage(String pageName)
  {
    super(pageName);
    // TODO Auto-generated constructor stub
  }

  private boolean createNewRepository = true;

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    container.setLayout(new GridLayout(1, true));
    Button bCreateNewRepository = new Button(container, SWT.RADIO);
    bCreateNewRepository.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    bCreateNewRepository.setText("Create new repository");
    bCreateNewRepository.addSelectionListener(new SelectionListener()
    {

      public void widgetSelected(SelectionEvent e)
      {
        createNewRepository = true;
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        widgetSelected(e);
      }
    });

    Button bSelectRepository = new Button(container, SWT.RADIO);
    bSelectRepository.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    bSelectRepository.setText("Select existing repository");
    bSelectRepository.addSelectionListener(new SelectionListener()
    {

      public void widgetSelected(SelectionEvent e)
      {
        createNewRepository = false;
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        widgetSelected(e);
      }
    });

    bCreateNewRepository.setSelection(true);

    setControl(container);
    setPageComplete(true);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
   */
  @Override
  public boolean canFlipToNextPage()
  {
    return true;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
   */
  @Override
  public IWizardPage getNextPage()
  {
    if (createNewRepository)
    {
      return getWizard().getPage("AddRepository");
    }
    return getWizard().getPage("SelectRepository");

  }

}
