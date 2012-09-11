/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
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
    setWindowTitle("ECPRepository Wizard");

    super.addPages();
    addPage(new AddRepositoryPage("AddRepository", getUIProvider()));
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
