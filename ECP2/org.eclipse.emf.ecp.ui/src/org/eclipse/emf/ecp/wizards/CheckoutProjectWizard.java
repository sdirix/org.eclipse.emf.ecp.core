/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.common.CheckoutProjectComposite;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public class CheckoutProjectWizard extends ECPWizard<CheckoutProjectComposite>
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

  @Override
  public void addPages()
  {
    WizardPage wp = new WizardPage("Checkout") //$NON-NLS-1$
    {

      public void createControl(Composite parent)
      {
        Composite composite = getUIProvider().createUI(parent);

        setPageComplete(true);
        setControl(composite);
      }
    };
    addPage(wp);

    wp.setTitle(Messages.CheckoutProjectWizard_PageTitle_CheckoutProject);
    wp.setImageDescriptor(Activator.getImageDescriptor("icons/checkout_project_wiz.png")); //$NON-NLS-1$

    ECPCheckoutSource checkoutSource = getUIProvider().getCheckoutSource();

    ECPRepository repository = checkoutSource.getRepository();
    if (checkoutSource == repository)
    {
      wp.setMessage(Messages.CheckoutProjectWizard_PageMessage_CheckoutRepositrory + repository.getLabel() + "."); //$NON-NLS-2$ //$NON-NLS-1$
    }
    else
    {
      wp.setMessage(Messages.CheckoutProjectWizard_PageMessage_CheckoutProject + getUIProvider().getUiProvider().getText(checkoutSource) + Messages.CheckoutProjectWizard_PageMessage_CheckoutFrom
          + repository.getLabel() + "."); //$NON-NLS-1$
    }
    setWindowTitle(Messages.CheckoutProjectWizard_Title_Checkout);
  }
}
