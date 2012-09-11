/**
 * 
 */
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen Neufeld
 */
public interface CompositeUiProvider
{
  Composite createUI(Composite parent);
}
