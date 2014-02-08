package org.eclipse.emf.ecp.controls.internal.fx;

import java.text.DecimalFormat;
import java.util.Locale;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.fx.util.ECPNumericalFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.fx.util.NumericalHelper;
import org.eclipse.emf.ecp.view.spi.model.VControl;

public class ECPFXNumerical extends ECPFXText {

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(
			final VControl control) {
		return new EMFUpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value == null || "".equals(value))
					return null;
				final DecimalFormat format = NumericalHelper.setupFormat(
						Locale.getDefault(), getInstanceClass(control));
				return format.format(value);
			}
		};
	}

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new ECPNumericalFieldToModelUpdateValueStrategy(
				getInstanceClass(control));
	}

	private Class<?> getInstanceClass(VControl control) {
		return control.getDomainModelReference()
				.getEStructuralFeatureIterator().next().getEType()
				.getInstanceClass();
	}

}
