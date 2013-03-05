package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public interface ECPCellEditor {
	IValueProperty getValueProperty();
	void instantiate(IItemPropertyDescriptor descriptor);
	int getStyle();
}
