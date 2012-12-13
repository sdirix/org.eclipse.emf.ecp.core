/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPWidget {

	/**
   * 
   */
	public ECPWidget() {
		super();
	}

	public abstract Control createWidget(final FormToolkit toolkit, final Composite composite, final int style);

	public abstract void setEditable(boolean isEditable);

	/**
	 * @return the Control
	 */
	public abstract Control getControl();

	public abstract void dispose();
}
