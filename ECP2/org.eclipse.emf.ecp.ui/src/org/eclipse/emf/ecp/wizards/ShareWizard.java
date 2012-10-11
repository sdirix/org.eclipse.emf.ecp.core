/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.util.Messages;
import org.eclipse.emf.ecp.wizards.page.AddRepositoryPage;
import org.eclipse.emf.ecp.wizards.page.SelectOrCreateRepositoryPage;
import org.eclipse.emf.ecp.wizards.page.SelectRepositoryPage;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Eugen Neufeld
 */
public class ShareWizard extends Wizard
{

  private ECPRepository selectedRepository = null;

  private ECPProvider provider;

  // private AddRepositoryPage addPage;

  private AddRepositoryComposite repositoryComposite;

  /**
   * @return the provider
   */
  public ECPProvider getProvider()
  {
    return provider;
  }

  /**
   * @return the selectedRepository
   */
  public ECPRepository getSelectedRepository()
  {
    return selectedRepository;
  }

  /**
   * @param selectedRepository
   *          the selectedRepository to set
   */
  public void setSelectedRepository(ECPRepository selectedRepository)
  {
    this.selectedRepository = selectedRepository;
  }

  /**
   * . ({@inheritDoc})
   */
  @Override
  public void addPages()
  {

    SelectOrCreateRepositoryPage userChoicePage = new SelectOrCreateRepositoryPage("UserChoice"); //$NON-NLS-1$
    SelectRepositoryPage selectPage = new SelectRepositoryPage("SelectRepository"); //$NON-NLS-1$
    repositoryComposite = new AddRepositoryComposite(provider);
    AddRepositoryPage addPage = new AddRepositoryPage("AddRepository", repositoryComposite); //$NON-NLS-1$
    addPage(userChoicePage);
    addPage(selectPage);
    addPage(addPage);
  }

  /**
   * . ({@inheritDoc})
   */
  @Override
  public boolean canFinish()
  {

    return selectedRepository != null || repositoryComposite.getProperties() != null
        && repositoryComposite.getRepositoryName() != null;

  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish()
  {
    if (selectedRepository == null)
    {
      selectedRepository = ECPRepositoryManager.INSTANCE.addRepository(provider, repositoryComposite
          .getRepositoryName(),
          repositoryComposite.getRepositoryLabel() == null ? "" : repositoryComposite.getRepositoryLabel(), //$NON-NLS-1$
          repositoryComposite.getRepositoryDescription() == null ? "" : repositoryComposite.getRepositoryDescription(), //$NON-NLS-1$
          repositoryComposite.getProperties());
    }
    return true;
  }

  /**
   * @param provider
   *          - {@link ECPProvider} to filter the known {@link ECPRepository}s by
   */
  public void init(ECPProvider provider)
  {
    this.provider = provider;
    setWindowTitle(Messages.ShareWizard_Title_Share);
  }

}
