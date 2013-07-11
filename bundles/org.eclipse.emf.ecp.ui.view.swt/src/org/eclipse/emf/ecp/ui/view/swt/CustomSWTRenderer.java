package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;

import org.eclipse.emf.ecp.view.model.Renderable;

public interface CustomSWTRenderer {

	Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers();
}
