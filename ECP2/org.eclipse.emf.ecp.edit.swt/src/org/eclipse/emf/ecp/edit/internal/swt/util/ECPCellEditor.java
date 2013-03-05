package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.property.value.IValueProperty;

public interface ECPCellEditor {
	IValueProperty getValueProperty();

	void instantiate(IItemPropertyDescriptor descriptor);

	int getStyle();

	/**
	 * @param value
	 * @return
	 */
	String getFormatedString(Object value);
}
