/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.template.controls.swt;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecp.edit.internal.swt.controls.AbstractTextControl;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * Control for selecting a color.
 *
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class TemplateColorControl extends AbstractTextControl {

	private final Map<String, Color> colors = new LinkedHashMap<String, Color>();

	@Override
	protected void fillControlComposite(final Composite composite) {
		final Composite main = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(main);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(main);
		super.fillControlComposite(main);

		final Label color = new Label(main, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(color);
		color.setText("Select Color");
		color.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				final ColorDialog cd = new ColorDialog(composite.getShell());
				cd.setText("Select Color");
				cd.setRGB(colors.get(getDomainModelReference().getIterator().next()).getRGB());
				final RGB rgb = cd.open();
				if (rgb == null) {
					return;
				}

				final String red = Integer.toHexString(0x100 | rgb.red).substring(1);
				final String green = Integer.toHexString(0x100 | rgb.green).substring(1);
				final String blue = Integer.toHexString(0x100 | rgb.blue).substring(1);
				final String result = red + green + blue;
				getDomainModelReference().getIterator().next().set(result);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Do nothing
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Do nothing
			}
		});
		final ISWTObservableValue observeBackground = SWTObservables.observeBackground(color);
		getDataBindingContext().bindValue(observeBackground, getModelValue(), new UpdateValueStrategy() {
			// targetToModel
			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.core.databinding.UpdateValueStrategy#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object value) {
				if (value == null) {
					return ""; //$NON-NLS-1$
				}
				final Color color = (Color) value;
				final String red = Integer.toHexString(0x100 | color.getRed()).substring(1);
				final String green = Integer.toHexString(0x100 | color.getGreen()).substring(1);
				final String blue = Integer.toHexString(0x100 | color.getBlue()).substring(1);
				return red + green + blue;
			}

		}, new UpdateValueStrategy() {
			// ModelToTarget
			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.core.databinding.UpdateValueStrategy#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object value) {
				if (value == null) {
					return null;
				}
				final String colorHex = (String) value;
				if (!colors.containsKey(value)) {
					final String redString = colorHex.substring(0, 2);
					final String greenString = colorHex.substring(2, 4);
					final String blueString = colorHex.substring(4, 6);
					final int red = Integer.parseInt(redString, 16);
					final int green = Integer.parseInt(greenString, 16);
					final int blue = Integer.parseInt(blueString, 16);
					colors.put(colorHex, new Color(Display.getDefault(), red, green, blue));
				}
				return colors.get(colorHex);
			}
		});
		color.setToolTipText("Press to select the color");
	}

	@Override
	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_view_template_control_color"; //$NON-NLS-1$
	}

	@Override
	protected String getUnsetLabelText() {
		return "No Color set";
	}

	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset Color";
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.AbstractTextControl#dispose()
	 */
	@Override
	public void dispose() {
		for (final Color color : colors.values()) {
			color.dispose();
		}
		super.dispose();
	}

}
