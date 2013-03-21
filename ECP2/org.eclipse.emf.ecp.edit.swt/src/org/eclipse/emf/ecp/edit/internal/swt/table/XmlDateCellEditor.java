package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import javax.xml.datatype.XMLGregorianCalendar;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class XmlDateCellEditor extends CellEditor implements ECPCellEditor {

	private DateTime dateWidget;
	private DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);

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
		return new WidgetValueProperty() {

			public Object getValueType() {
				return XMLGregorianCalendar.class;
			}

			@Override
			protected Object doGetValue(Object source) {
				return XmlDateCellEditor.this.doGetValue();
			}

			@Override
			protected void doSetValue(Object source, Object value) {
				XmlDateCellEditor.this.doSetValue(value);
			}

			@Override
			public IObservableValue observe(Object source) {
				if (source instanceof XmlDateCellEditor) {
					return observe(dateWidget);
				}
				return super.observe(source);
			}
		};
	}

	public void instantiate(IItemPropertyDescriptor descriptor) {

	}

	@Override
	protected Control createControl(Composite parent) {
		dateWidget = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN);
		dateWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_edit_swt_cellEditor_date");
		dateWidget.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		dateWidget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

		});

		dateWidget.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		dateWidget.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				XmlDateCellEditor.this.focusLost();
			}
		});
		return dateWidget;
	}

	@Override
	protected Object doGetValue() {
		Calendar selectedCalendarDate = Calendar.getInstance();
		selectedCalendarDate.set(Calendar.YEAR, dateWidget.getYear());
		selectedCalendarDate.set(Calendar.MONTH, dateWidget.getMonth());
		selectedCalendarDate.set(Calendar.DAY_OF_MONTH, dateWidget.getDay());
		return new XMLCalendar(selectedCalendarDate.getTime(), XMLCalendar.DATE);
	}

	@Override
	protected void doSetFocus() {
		dateWidget.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		XMLGregorianCalendar cal = (XMLGregorianCalendar) value;
		GregorianCalendar gregCal = cal.toGregorianCalendar();
		dateWidget.setDate(gregCal.get(Calendar.YEAR), gregCal.get(Calendar.MONTH), gregCal.get(Calendar.DAY_OF_MONTH));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	@Override
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
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
			MessageFormat.format(getErrorMessage(), new Object[] { newValue });
		}

		fireApplyEditorValue();
		deactivate();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getFormatedString(java.lang.Object)
	 */
	public String getFormatedString(Object value) {
		XMLGregorianCalendar cal = (XMLGregorianCalendar) value;
		if (value == null) {
			return "";
		}
		return dateFormat.format(cal.toGregorianCalendar().getTime());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getColumnWidthWeight()
	 */
	public int getColumnWidthWeight() {
		return 75;
	}
}
