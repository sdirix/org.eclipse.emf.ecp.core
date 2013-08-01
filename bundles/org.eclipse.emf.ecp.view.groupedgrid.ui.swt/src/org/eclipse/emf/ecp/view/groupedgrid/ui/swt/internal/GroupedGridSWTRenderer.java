/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.groupedgrid.ui.swt.internal;

import java.util.List;

import org.eclipse.emf.ecp.edit.groupedgrid.model.Group;
import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid;
import org.eclipse.emf.ecp.edit.groupedgrid.model.Row;
import org.eclipse.emf.ecp.edit.groupedgrid.model.Span;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen Neufeld
 * 
 */
public class GroupedGridSWTRenderer extends AbstractSWTRenderer<GroupedGrid> {

	public static final GroupedGridSWTRenderer INSTANCE = new GroupedGridSWTRenderer();

	@Override
	public Control renderSWT(Node<GroupedGrid> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite parent = getParentFromInitData(initData);
		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());

		node.addRenderingResultDelegator(withSWT(columnComposite));

		final int maxNumColumns = calculateColumns(node.getRenderable());

		GridLayoutFactory.fillDefaults().numColumns(maxNumColumns).equalWidth(true)
			.applyTo(columnComposite);

		final List<Node<?>> children = node.getChildren();
		int currentControl = 0;

		for (final Group group : node.getRenderable().getGroups()) {
			// Label
			final Composite labelComposite = new Composite(columnComposite, SWT.NONE);
			labelComposite.setBackground(parent.getBackground());
			GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(labelComposite);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).span(maxNumColumns, 1)
				.applyTo(labelComposite);
			final Label l = new Label(labelComposite, SWT.NONE);
			l.setText(group.getName());
			l.setBackground(parent.getBackground());
			GridDataFactory.fillDefaults().grab(false, false).align(SWT.BEGINNING, SWT.BEGINNING)
				.applyTo(l);
			final Label seperator = new Label(labelComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(seperator);
			// Content
			for (final Row row : group.getRows()) {
				int spanned = 0;
				for (final org.eclipse.emf.ecp.view.model.Composite child : row.getChildren()) {
					final Node<? extends Renderable> childNode = children.get(currentControl++);

					final int hSpan = getHSpanOfComposite(child);
					final Control childRender = SWTRenderers.INSTANCE.render(columnComposite,
						childNode, adapterFactoryItemDelegator);

					// TOOD; when does this case apply?
					if (childRender == null) {
						continue;
					}

					childRender.setBackground(parent.getBackground());
					GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).indent(0, 0)
						.span(hSpan, 1).applyTo(childRender);
					GridDataFactory.createFrom((GridData) childRender.getLayoutData()).indent(10, 0)
						.applyTo(childRender);
					spanned += hSpan;
					if (childNode.isLeaf()
						&& org.eclipse.emf.ecp.view.model.Control.class.isInstance(childNode.getRenderable())) {
						final org.eclipse.emf.ecp.view.model.Control control = (org.eclipse.emf.ecp.view.model.Control) childNode
							.getRenderable();
						if (control.getLabelAlignment() == Alignment.LEFT) {
							spanned++;
						}
					}

				}
				final int spanDif = maxNumColumns - spanned;
				if (spanDif != 0) {
					final Label filler = new Label(columnComposite, SWT.NONE);
					filler.setBackground(parent.getBackground());
					GridDataFactory.fillDefaults()
						.span(spanDif, 1).applyTo(filler);
				}
			}
		}
		return columnComposite;
	}

	/**
	 * @param renderable
	 * @return
	 */
	private int calculateColumns(GroupedGrid renderable) {
		int maxColumns = 0;
		for (final Group group : renderable.getGroups()) {
			for (final Row row : group.getRows()) {
				int columns = 0;
				for (final org.eclipse.emf.ecp.view.model.Composite composite : row.getChildren()) {
					columns += getHSpanOfComposite(composite) + getExtraColumnForLabel(composite);
				}
				if (columns > maxColumns) {
					maxColumns = columns;
				}
			}
		}
		return maxColumns;
	}

	/**
	 * @param composite
	 * @return
	 */
	private int getHSpanOfComposite(org.eclipse.emf.ecp.view.model.Composite composite) {
		for (final Attachment attachment : composite.getAttachments()) {
			if (Span.class.isInstance(attachment)) {
				final Span span = (Span) attachment;
				return span.getHorizontalSpan();
			}
		}
		return 1;
	}

	private int getExtraColumnForLabel(org.eclipse.emf.ecp.view.model.Composite child) {
		if (org.eclipse.emf.ecp.view.model.Control.class.isInstance(child)) {
			final org.eclipse.emf.ecp.view.model.Control control = (org.eclipse.emf.ecp.view.model.Control) child;
			return control.getLabelAlignment() == Alignment.LEFT ? 1 : 0;
		}
		return 0;

	}
}
