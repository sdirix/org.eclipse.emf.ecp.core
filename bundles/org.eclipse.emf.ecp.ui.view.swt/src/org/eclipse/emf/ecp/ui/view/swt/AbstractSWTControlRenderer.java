package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.view.model.Control;

public abstract class AbstractSWTControlRenderer<C extends Control> extends AbstractSWTRenderer<C> {
	
	protected abstract SWTControl getControl();
	
}
