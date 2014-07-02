/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.group.swt.internal.collapsable;

import org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

/**
 * A Group renderer, which is collapsable.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class CollapsableGroupSWTRenderer extends ContainerSWTRenderer<VGroup> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#renderControl(org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control renderControl(SWTGridCell gridCell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final ExpandBar bar = new ExpandBar(parent, SWT.V_SCROLL);
		bar.setBackground(parent.getBackground());

		// First item
		final Composite composite = new Composite(bar, SWT.NONE);
		composite.setLayout(new FillLayout());
		final ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		String text = getVElement().getName();
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		item0.setText(text);
		super.renderControl(gridCell, composite);
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		bar.addExpandListener(new ExpandListener() {

			@Override
			public void itemCollapsed(ExpandEvent e) {
				getVElement().setCollapsed(true);
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				getVElement().setCollapsed(false);
			}

		});
		item0.setExpanded(getVElement().isCollapsed());
		return bar;
	}

}
