package org.eclipse.emf.ecp.edit.internal.swt.table;

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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCellEditor extends CellEditor implements ECPCellEditor {

	private DateTime dateWidget;
	private DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

	// private DateTime timeWidget;

	public DateCellEditor() {
		// TODO Auto-generated constructor stub
	}

	public DateCellEditor(Composite parent) {
		super(parent);
	}

	public DateCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public IValueProperty getValueProperty() {
		return new WidgetValueProperty() {

			public Object getValueType() {
				return Date.class;
			}

			@Override
			protected Object doGetValue(Object source) {
				return DateCellEditor.this.doGetValue();
			}

			@Override
			protected void doSetValue(Object source, Object value) {
				DateCellEditor.this.doSetValue(value);
			}

			@Override
			public IObservableValue observe(Object source) {
				if (source instanceof DateCellEditor) {
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
		// Composite composite = new Composite(parent, SWT.NONE);
		dateWidget = new DateTime(parent, SWT.DATE);
		dateWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_edit_swt_cellEditor_date");
		// timeWidget = new DateTime(composite, SWT.TIME);
		// timeWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_edit_swt_cellEditor_time");

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
				DateCellEditor.this.focusLost();
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
		selectedCalendarDate.set(Calendar.HOUR_OF_DAY, dateWidget.getHours());
		selectedCalendarDate.set(Calendar.MINUTE, dateWidget.getMinutes());
		selectedCalendarDate.set(Calendar.SECOND, dateWidget.getSeconds());
		return selectedCalendarDate.getTime();
	}

	@Override
	protected void doSetFocus() {
		dateWidget.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) value);
		dateWidget.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		dateWidget.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
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
		Date cal = (Date) value;
		if (value == null) {
			return "";
		}
		return dateFormat.format(cal);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor#getColumnWidthWeight()
	 */
	public int getColumnWidthWeight() {
		return 75;
	}
}
