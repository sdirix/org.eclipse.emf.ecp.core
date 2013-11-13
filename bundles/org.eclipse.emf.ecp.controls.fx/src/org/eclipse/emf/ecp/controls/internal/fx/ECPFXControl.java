package org.eclipse.emf.ecp.controls.internal.fx;

import javafx.scene.Node;

import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;

public abstract class ECPFXControl extends ECPAbstractControl {

	public abstract Node render();
	
	@Override
	@Deprecated
	public boolean showLabel() {
		return true;
	}

}
