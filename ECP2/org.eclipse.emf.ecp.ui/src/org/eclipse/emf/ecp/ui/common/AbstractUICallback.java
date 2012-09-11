/**
 * 
 */
package org.eclipse.emf.ecp.ui.common;

/**
 * @author Eugen Neufeld
 */
public abstract class AbstractUICallback
{

  public abstract void setCompositeUIProvider(CompositeUiProvider uiProvider);

  /**
   * return code when the callback was closed successfully
   */
  public static final int OK = 0;

  /**
   * return code when the callback was canceled
   */
  public static final int CANCEL = 1;

  /**
   * the abstract method, which return
   * 
   * @return {@link #OK} when successful else {@link #CANCEL}
   */
  public abstract int open();
}
