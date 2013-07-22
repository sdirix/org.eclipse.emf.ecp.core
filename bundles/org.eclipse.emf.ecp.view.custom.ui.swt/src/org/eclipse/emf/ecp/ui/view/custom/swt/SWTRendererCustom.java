package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.model.Renderable;


public class SWTRendererCustom implements CustomSWTRenderer {

	public SWTRendererCustom() {
		// TODO Auto-generated constructor stub
	}

	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends Renderable>, SWTRenderer<?>> map=new HashMap<Class<? extends Renderable>, SWTRenderer<?>>();
        map.put(CustomControl.class , new CustomControlSWTRenderer());
        return map;
	}

}
