/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.CheckedSelectModelClassComposite;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class FilterModelElementWizard extends ECPWizard<CheckedSelectModelClassComposite>
{
  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish()
  {
    return true;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages()
  {
    super.addPages();
    WizardPage page = new WizardPage(Messages.FilterModelElementWizard_Title_FilterProject)
    {

      public void createControl(Composite parent)
      {
        Composite composite = getUIProvider().createUI(parent);

        setPageComplete(true);
        setControl(composite);
      }
    };
    addPage(page);
    page.setTitle(Messages.FilterModelElementWizard_PageTitle_FilterProject);
    page.setDescription(Messages.FilterModelElementWizard_PageDescription_FilterProject);
  }

}
