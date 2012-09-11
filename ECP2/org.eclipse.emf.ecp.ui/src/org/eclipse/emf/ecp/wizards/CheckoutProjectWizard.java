/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.common.CheckoutProjectComposite;

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
    WizardPage wp = new WizardPage("Checkout")
    {

      public void createControl(Composite parent)
      {
        Composite composite = getUIProvider().createUI(parent);

        setPageComplete(true);
        setControl(composite);
      }
    };
    addPage(wp);

    wp.setTitle("Checkout");
    wp.setImageDescriptor(Activator.getImageDescriptor("icons/checkout_project_wiz.png"));

    ECPCheckoutSource checkoutSource = getUIProvider().getCheckoutSource();

    ECPRepository repository = checkoutSource.getRepository();
    if (checkoutSource == repository)
    {
      wp.setMessage("Checkout " + repository.getLabel() + ".");
    }
    else
    {
      wp.setMessage("Checkout " + getUIProvider().getUiProvider().getText(checkoutSource) + " from "
          + repository.getLabel() + ".");
    }
    setWindowTitle("Checkout");
  }
}
