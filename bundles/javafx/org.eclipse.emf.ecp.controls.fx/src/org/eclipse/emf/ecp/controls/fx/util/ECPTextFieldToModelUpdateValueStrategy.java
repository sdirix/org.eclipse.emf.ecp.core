package org.eclipse.emf.ecp.controls.fx.util;

import org.eclipse.emf.databinding.EMFUpdateValueStrategy;

public class ECPTextFieldToModelUpdateValueStrategy extends
		EMFUpdateValueStrategy {

	public ECPTextFieldToModelUpdateValueStrategy() {
	}

	@Override
	public Object convert(Object value) {
		if (value == null || "".equals(value))
			return null;
		return super.convert(value);
	}
}
