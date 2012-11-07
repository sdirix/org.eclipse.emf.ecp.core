/**
 * 
 */
package org.eclipse.emf.ecp.wizards.page;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite.AddRepositoryChangeListener;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class AddRepositoryPage extends WizardPage
{

  /**
   * @param pageName
   */
  public AddRepositoryPage(String pageName, AddRepositoryComposite addRepositoryComposite)
  {
    super(pageName);
    this.addRepositoryComposite = addRepositoryComposite;
  }

  private AddRepositoryComposite addRepositoryComposite;

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite parent)
  {
    setTitle(Messages.AddRepositoryPage_PageTitle_AddRepository);
    setImageDescriptor(Activator.getImageDescriptor("icons/checkout_project_wiz.png")); //$NON-NLS-1$
    setMessage(Messages.AddRepositoryPage_PageMessage_AddRepository);

    Composite composite = addRepositoryComposite.createUI(parent);
    addRepositoryComposite.setListener(new AddRepositoryChangeListener()
    {

      public void repositoryProviderChanged(ECPProvider provider)
      {
      }

      public void repositoryNameChanged(String repositoryName)
      {
        if (repositoryName != null)
        {
          setPageComplete(true);
        }
        else
        {
          setPageComplete(false);
        }
      }

      public void repositoryLabelChanged(String repositoryLabel)
      {
      }

      public void repositoryDescriptionChanged(String repositoryDescription)
      {
      }
    });

    setControl(composite);
    setPageComplete(false);
  }

}
