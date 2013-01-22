/**
 * 
 */
package org.eclipse.emf.ecp.internal.editor.widgets;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;
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

	public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);
}
