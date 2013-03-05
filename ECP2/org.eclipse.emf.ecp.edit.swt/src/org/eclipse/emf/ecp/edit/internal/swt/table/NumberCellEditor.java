package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class NumberCellEditor extends TextCellEditor implements ECPCellEditor{

	public NumberCellEditor(Composite parent) {
		super(parent,SWT.RIGHT);
	}

	public NumberCellEditor(Composite parent, int style) {
		super(parent, style|SWT.RIGHT);
	}

	public IValueProperty getValueProperty() {
		return CellEditorProperties.control().value(WidgetProperties.text(SWT.FocusOut));
	}

	public void instantiate(IItemPropertyDescriptor descriptor) {
		//do nothing
	}

}
