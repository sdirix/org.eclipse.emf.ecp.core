package org.eclipse.emf.ecp.view.horizontal.fx;

import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.util.Callback;

import org.eclipse.emf.ecp.view.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.model.internal.fx.RendererFX;
import org.eclipse.emf.ecp.view.model.internal.fx.RendererFactory;

@SuppressWarnings("restriction")
// TODO no api
public class HorizontalLayoutPageRendererFX implements
		RendererFX<VHorizontalLayout> {

	@Override
	public Node render(final VHorizontalLayout renderable) {
		final Pagination pagination = new Pagination(renderable.getChildren()
				.size(), 0);
		pagination.setPageFactory(new Callback<Integer, Node>() {

			public Node call(Integer param) {
				return RendererFactory.INSTANCE.render(renderable.getChildren()
						.get(param));

			}
		});
		return pagination;
	}

}
