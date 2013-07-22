package org.eclipse.emf.ecp.ui.view.swt.renderer.example;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTControlRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomSWTControlRenderer implements CustomSWTRenderer {

	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends Renderable>, SWTRenderer<?>> renderers = new LinkedHashMap<Class<? extends Renderable>, SWTRenderer<?>>();
		renderers.put(Control.class, new CustomControlRenderer());
		return renderers;
	}

	class CustomControlRenderer extends SWTControlRenderer {
		@Override
		public org.eclipse.swt.widgets.Control renderSWT(Node<Control> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
			final org.eclipse.swt.widgets.Control render = super.renderSWT(node, adapterFactoryItemDelegator, initData);

			if (render == null) {
				return null;
			}

			render.setCursor(new Cursor(render.getDisplay(), SWT.ARROW));

			return render;
		}
	}
}
