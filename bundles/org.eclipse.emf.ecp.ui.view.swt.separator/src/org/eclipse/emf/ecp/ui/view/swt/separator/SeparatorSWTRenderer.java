package org.eclipse.emf.ecp.ui.view.swt.separator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.separator.Separator;

public class SeparatorSWTRenderer implements CustomSWTRenderer {

	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer<?>> renderers;
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer<?>>() {
			{
				put(Separator.class, new SWTSeparatorRenderer());
			}
		};
		return renderers;
	}

}
