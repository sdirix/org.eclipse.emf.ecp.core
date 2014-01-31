package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class StringCellEditor extends TextCellEditor implements ECPCellEditor {

	public StringCellEditor() {
	}

	public StringCellEditor(Composite parent) {
		super(parent);
	}

	public StringCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public IValueProperty getValueProperty() {
		return CellEditorProperties.control().value(WidgetProperties.text(SWT.FocusOut));
	}

	public void instantiate(EStructuralFeature feature, ViewModelContext viewModelContext) {
		// TODO Auto-generated method stub

	}

	public String getFormatedString(Object value) {
		if (value == null) {
			return ""; //$NON-NLS-1$
		}
		return String.valueOf(value);
	}

	public int getColumnWidthWeight() {
		return 100;
	}

	public UpdateValueStrategy getTargetToModelStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	public UpdateValueStrategy getModelToTargetStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEditable(boolean editable) {
		if (text != null) {
			text.setEditable(editable);
		}
	}

}
