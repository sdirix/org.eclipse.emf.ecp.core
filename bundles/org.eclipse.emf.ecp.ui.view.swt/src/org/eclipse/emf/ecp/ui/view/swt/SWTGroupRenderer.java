package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Renders a SWT group.
 * 
 */
public class SWTGroupRenderer extends AbstractSWTRenderer<Group> {
	public static final SWTGroupRenderer INSTANCE = new SWTGroupRenderer();

	@Override
	public Control renderSWT(Node<Group> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);
		final Group modelGroup = node.getRenderable();
		final org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(parent, SWT.TITLE);
		if (modelGroup.getName() != null) {
			group.setText(modelGroup.getName());
		}

		GridLayoutFactory.fillDefaults()
			.numColumns(2)
			.equalWidth(false)
			.applyTo(group);

		node.addRenderingResultDelegator(withSWT(group));

		for (final Node<? extends Renderable> child : node.getChildren()) {// org.eclipse.emf.ecp.view.model.Composite
																			// modelComposite :
																			// modelGroup.getComposites()) {

			Control control;
			try {
				control = SWTRenderers.INSTANCE.render(group, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}

			if (!child.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1)
					.applyTo(control);
			}
		}

		return group;
	}
}
