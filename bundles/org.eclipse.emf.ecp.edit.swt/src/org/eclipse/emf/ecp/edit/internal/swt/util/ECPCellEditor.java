package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.property.value.IValueProperty;

public interface ECPCellEditor {
	/**
	 * RAP theming variable to set
	 */
	String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";

	IValueProperty getValueProperty();

	void instantiate(IItemPropertyDescriptor descriptor);

	int getStyle();

	/**
	 * @param value
	 * @return
	 */
	String getFormatedString(Object value);

	/**
	 * The returned value is used for layouting the table columns. The value is a relative column weight. A column
	 * containing text has a weight of 100. Please consider this when defining you weight. E.g if you return 200 your
	 * column will be twice the width of a text column.
	 * 
	 * @return the relative column width
	 */
	int getColumnWidthWeight();
}
