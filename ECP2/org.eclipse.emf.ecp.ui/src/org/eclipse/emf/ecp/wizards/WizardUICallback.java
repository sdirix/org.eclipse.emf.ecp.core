/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.common.ICompositeProvider;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eugen Neufeld
 */
public class WizardUICallback<T extends ICompositeProvider> extends AbstractUICallback
{

  private final Shell shell;

  private final ECPWizard<T> wizard;

  public WizardUICallback(Shell shell, ECPWizard<T> wizard)
  {
    this.shell = shell;
    this.wizard = wizard;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#open()
   */
  @Override
  public int open()
  {
    WizardDialog wd = new WizardDialog(shell, wizard);

    int result = wd.open();
    if (result == WizardDialog.OK)
    {
      return OK;
    }
    return CANCEL;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#setCompositeUIProvider(org.eclipse.emf.ecp.ui.common.
   * ICompositeProvider)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setCompositeUIProvider(ICompositeProvider uiProvider)
  {
    wizard.setUIProvider((T)uiProvider);
  }

}
