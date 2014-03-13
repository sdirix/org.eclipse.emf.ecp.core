/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.view.internal.core.swt.Activator;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTControlSWTRenderer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Renderer for {@link org.eclipse.swt.widgets.Control Controls}.
 * This renderer will be removed with the next major release.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
@Deprecated
public class ECPLegacyControlSWTRenderer extends SimpleControlSWTControlSWTRenderer {

	private SWTControl control;

	@Override
	protected Control createSWTControl(Composite parent, Setting setting) {
		final ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();

		if (controlFactory == null) {
			Activator.getDefault().ungetECPControlFactory();
			return null;
		}

		control = controlFactory.createControl(SWTControl.class,
			getVElement().getDomainModelReference());

		Activator.getDefault().ungetECPControlFactory();
		if (control == null) {
			return null;
		}
		control.init(getViewModelContext(), getVElement());

		return control.createControl(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.AbstractControlSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		if (control != null) {
			control.dispose();
		}
		super.dispose();
	}

	@Override
	protected Binding[] createBindings(Control control, Setting setting) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#getUnsetText()
	 */
	@Override
	protected String getUnsetText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isUnsettable() {
		return false;
	}

}
