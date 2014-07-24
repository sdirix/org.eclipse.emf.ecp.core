/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 */
package org.eclipse.emf.ecp.view.spi.compoundcontrol.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecp.view.spi.compoundcontrol.model.VCompoundControl;
import org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * {@link ContainerSWTRenderer} for the {@link VCompoundControl} view model. Basically a group renderer.
 *
 * @author jfaltermeier
 *
 */
public class CompoundControlSWTRenderer extends ContainerSWTRenderer<VCompoundControl> {
	private static final String COMPOUND_CONTROL = "org_eclipse_emf_ecp_ui_compound_control"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */
	public CompoundControlSWTRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 *
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	protected CompoundControlSWTRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getCustomVariant()
	 */
	@Override
	protected String getCustomVariant() {
		return COMPOUND_CONTROL;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getChildren()
	 */

	@SuppressWarnings("restriction")
	@Override
	protected Collection<VContainedElement> getChildren() {
		final List<VContainedElement> children = new ArrayList<VContainedElement>();
		children.addAll(getVElement().getControls());
		return children;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.ContainerSWTRenderer#getComposite(org.eclipse.swt.widgets.Composite)
	 */
	@SuppressWarnings("restriction")
	@Override
	protected Composite getComposite(Composite parent) {
		final Group group = new Group(parent, SWT.TITLE);
		if (getVElement().getName() != null) {
			group.setText(getVElement().getName());
		}
		return group;
	}

}
