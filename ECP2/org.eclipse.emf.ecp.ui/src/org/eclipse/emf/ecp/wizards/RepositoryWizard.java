/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Eugen Neufeld
 */
public class RepositoryWizard extends Wizard
{

  private ECPRepository selectedRepository = null;

  private ECPProvider provider;

  private AddRepositoryPage addPage;

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

    SelectOrCreateRepositoryPage userChoicePage = new SelectOrCreateRepositoryPage("UserChoice");
    SelectRepositoryPage selectPage = new SelectRepositoryPage("SelectRepository");
    addPage = new AddRepositoryPage("AddRepository", provider);
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

    return selectedRepository != null || addPage.getProperties() != null && addPage.getRepositoryName() != null;

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
      selectedRepository = ECPRepositoryManager.INSTANCE
          .addRepository(provider, addPage.getRepositoryName(),
              addPage.getRepositoryLabel() == null ? "" : addPage.getRepositoryLabel(),
              addPage.getRepositoryDescription() == null ? "" : addPage.getRepositoryDescription(),
              addPage.getProperties());
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
    setWindowTitle("ECPRepository Wizard");
  }

}
