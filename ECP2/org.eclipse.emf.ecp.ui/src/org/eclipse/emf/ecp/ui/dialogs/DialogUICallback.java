/**
 * 
 */
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.common.CompositeUiProvider;

/**
 * @author Eugen Neufeld
 */
public class DialogUICallback<T extends CompositeUiProvider> extends AbstractUICallback
{

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#setCompositeUIProvider(org.eclipse.emf.ecp.ui.common.
   * CompositeUiProvider)
   */
  @Override
  public void setCompositeUIProvider(CompositeUiProvider uiProvider)
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#open()
   */
  @Override
  public int open()
  {
    return CANCEL;
  }

}
