package org.eclipse.emf.ecp.ui.view.swt.renderer.example;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTControlRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomSWTControlRenderer implements CustomSWTRenderer {

	public Map<Class<? extends Composite>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends Composite>, SWTRenderer<?>> renderers = new LinkedHashMap<Class<? extends Composite>, SWTRenderer<?>>();
		renderers.put(Control.class, new CustomControlRenderer());
		return renderers;
	}

	class CustomControlRenderer extends SWTControlRenderer {
		public SWTRendererNode render(Control control, ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
			final SWTRendererNode render = super.render(control, controlContext, adapterFactoryItemDelegator);

			if (render == null) {
				return null;
			}

			render.getRenderedResult().setCursor(new Cursor(render.getRenderedResult().getDisplay(), SWT.ARROW));

			return render;
		}
	}
}
