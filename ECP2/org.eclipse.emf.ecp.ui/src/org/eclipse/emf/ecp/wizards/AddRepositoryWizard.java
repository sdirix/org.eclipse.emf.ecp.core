/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.util.Messages;
import org.eclipse.emf.ecp.wizards.page.AddRepositoryPage;

/**
 * @author Eugen Neufeld
 */
public class AddRepositoryWizard extends ECPWizard<AddRepositoryComposite>
{

  /**
   * . ({@inheritDoc})
   */
  @Override
  public void addPages()
  {
    setWindowTitle(Messages.AddRepositoryWizard_Title_AddRepository);

    super.addPages();
    addPage(new AddRepositoryPage(Messages.AddRepositoryWizard_PageTitle_AddReposditory, getUIProvider()));
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish()
  {
    return true;
  }

}
