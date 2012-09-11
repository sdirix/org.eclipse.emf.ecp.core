/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.CheckedModelElementHelper;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class FilterModelElementWizard extends ECPWizard<CheckedModelElementHelper>
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
    WizardPage page = new WizardPage("Filter")
    {

      public void createControl(Composite parent)
      {
        Composite composite = getUIProvider().createUI(parent);

        setPageComplete(true);
        setControl(composite);
      }
    };
    addPage(page);
    page.setTitle("Select EPacakges and EClasses");
    page.setDescription("Select the pacakges that should be suggested when creating new model elements.");
  }

}
