/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.CompositeUiProvider;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Eugen Neufeld
 */
public abstract class ECPWizard<T extends CompositeUiProvider> extends Wizard
{
  private T uiProvider;

  public void setUIProvider(T uiProvider)
  {
    this.uiProvider = uiProvider;
  }

  protected T getUIProvider()
  {
    return uiProvider;
  }
}
