package org.eclipse.emf.ecp.view.group.ui.swt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.group.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;

public class SWTRendererGroup implements CustomSWTRenderer {

	public SWTRendererGroup() {
		// TODO Auto-generated constructor stub
	}

	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends Renderable>, SWTRenderer<?>> map = new HashMap<Class<? extends Renderable>, SWTRenderer<?>>();
		map.put(Group.class, SWTGroupRenderer.INSTANCE);
		return map;
	}

}
