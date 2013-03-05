package org.eclipse.emf.ecp.edit.internal.swt.table;

import java.text.MessageFormat;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

public class XmlDateCellEditor extends CellEditor implements ECPCellEditor {

	private DateTime dateWidget;

	public XmlDateCellEditor() {
		// TODO Auto-generated constructor stub
	}

	public XmlDateCellEditor(Composite parent) {
		super(parent);
	}

	public XmlDateCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public IValueProperty getValueProperty() {
		return CellEditorProperties.control().value(WidgetProperties.selection());
	}

	public void instantiate(IItemPropertyDescriptor descriptor) {

	}

	@Override
	protected Control createControl(Composite parent) {
		dateWidget = new DateTime(parent, SWT.DATE|SWT.DROP_DOWN);
		dateWidget.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		return dateWidget;
	}

	@Override
	protected Object doGetValue() {
		return new XMLCalendar(dateWidget.getYear()+"-"+dateWidget.getMonth()+"-"+dateWidget.getDay(), XMLCalendar.DATE);
	}

	@Override
	protected void doSetFocus() {
		dateWidget.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		XMLGregorianCalendar cal=(XMLGregorianCalendar)value;
		dateWidget.setDate(cal.getYear(), cal.getMonth(), cal.getDay());
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		super.keyReleaseOccured(keyEvent);
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		}
	}
	
	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate() {
		// must set the selection before getting value

		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			MessageFormat.format(getErrorMessage(),
					new Object[] { newValue });
		}

		fireApplyEditorValue();
		deactivate();
	}
}
