package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;

import org.eclipse.emf.ecp.view.model.Composite;

public interface CustomSWTRenderer {

	Map<Class<? extends Composite>, SWTRenderer<?>> getCustomRenderers();
}
