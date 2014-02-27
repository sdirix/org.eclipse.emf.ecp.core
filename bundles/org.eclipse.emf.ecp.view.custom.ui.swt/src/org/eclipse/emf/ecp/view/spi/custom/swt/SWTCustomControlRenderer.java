/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.custom.swt;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTValidationHelper;
import org.eclipse.emf.ecp.internal.edit.EditMessages;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomControl;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;

public class SWTCustomControlRenderer extends AbstractSWTRenderer<VCustomControl> {

	private ECPAbstractCustomControlSWT swtCustomControl;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#preInit()
	 */
	@Override
	protected void preInit() {
		super.preInit();
		final VCustomControl customControl = getVElement();
		swtCustomControl = loadObject(customControl.getBundleName(), customControl.getClassName());
		if (swtCustomControl == null) {
			// TODO
			throw new IllegalStateException(String.format("The  %1$s/%2$s cannot be loaded!",
				customControl.getBundleName(), customControl.getClassName()));
		}
		swtCustomControl.init(getVElement(), getViewModelContext());
	}

	private static ECPAbstractCustomControlSWT loadObject(String bundleName, String clazz) {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			new ClassNotFoundException(clazz + EditMessages.CONTROLFACTROY_CANNOT_BE_LOADED
				+ bundleName
				+ EditMessages.CONTROLFACTORY_CANNOT_BE_RESOLVED);
			return null;
		}
		try {
			final Class<?> loadClass = bundle.loadClass(clazz);
			if (!ECPAbstractCustomControlSWT.class.isAssignableFrom(loadClass)) {
				return null;
			}
			return ECPAbstractCustomControlSWT.class.cast(loadClass.newInstance());
		} catch (final ClassNotFoundException ex) {
			return null;
		} catch (final InstantiationException ex) {
			return null;
		} catch (final IllegalAccessException ex) {
			return null;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		swtCustomControl.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription(org.eclipse.emf.ecp.view.spi.model.VElement)
	 */
	@Override
	public GridDescription getGridDescription() {
		return swtCustomControl.getGridDescription();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(org.eclipse.emf.ecp.view.spi.layout.grid.GridCell,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control renderControl(GridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		return swtCustomControl.renderControl(cell, parent);
	}

	@Override
	protected void applyReadOnly() {
		swtCustomControl.applyReadOnly(getControls());
	}

	@Override
	protected void applyEnable() {
		swtCustomControl.applyEnable(getControls());
	}

	@Override
	protected void applyVisible() {
		for (int i = 0; i < getControls().length; i++) {
			if (getControls()[i] == null) {
				continue;
			}
			final Object layoutData = getControls()[i].getLayoutData();
			if (GridData.class.isInstance(layoutData)) {
				final GridData gridData = (GridData) layoutData;
				if (gridData != null) {
					gridData.exclude = false;
				}
			}
			getControls()[i].setVisible(getVElement().isVisible());
		}
	}

	/**
	 * Allows implementers to display the validation state of the control.
	 * The default implementation does nothing.
	 */
	@Override
	protected void applyValidation() {
		Label validationIcon = null;
		switch (getControls().length) {
		case 3:
			validationIcon = Label.class.cast(getControls()[1]);
			break;
		default:
			break;
		}

		if (validationIcon != null && !validationIcon.isDisposed()) {
			validationIcon.setImage(getValidationIcon(getVElement().getDiagnostic().getHighestSeverity()));
			validationIcon.setToolTipText(getVElement().getDiagnostic().getMessage());
		}
		swtCustomControl.applyValidation();

	}

	private Image getValidationIcon(int severity) {
		return SWTValidationHelper.INSTANCE.getValidationIcon(severity);
	}

	private Color getValidationBackgroundColor(int severity) {
		return SWTValidationHelper.INSTANCE.getValidationBackgroundColor(severity);
	}
}
