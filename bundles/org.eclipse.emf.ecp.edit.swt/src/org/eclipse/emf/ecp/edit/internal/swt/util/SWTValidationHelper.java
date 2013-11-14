/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 * Eugen Neufeld - VTViewTemplate implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.view.template.model.VTControlValidationTemplate;
import org.eclipse.emf.ecp.view.template.model.VTTemplateFactory;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplate;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Helper class for accessing icons and colours for validations.
 * 
 * @author jfaltermeier
 * 
 */
public final class SWTValidationHelper {

	/**
	 * The instance of the SWTValidationHelper.
	 */
	public static final SWTValidationHelper INSTANCE = new SWTValidationHelper();

	private final Map<String, Color> colorMap = new LinkedHashMap<String, Color>();
	private VTViewTemplate defaultTemplate;

	private SWTValidationHelper() {
		// singleton
	}

	/**
	 * Returns the background color for a control with the given validation severity.
	 * 
	 * @param severity severity the severity of the {@link Diagnostic}
	 * @return the color to be used as a background color
	 */
	public Color getValidationBackgroundColor(int severity) {
		final VTViewTemplate template = getTemplate();
		String colorHex = null;

		switch (severity) {
		case Diagnostic.OK:
			colorHex = template.getControlValidationConfiguration().getOkColorHEX();
			break;
		case Diagnostic.INFO:
			colorHex = template.getControlValidationConfiguration().getInfoColorHEX();
			break;
		case Diagnostic.WARNING:
			colorHex = template.getControlValidationConfiguration()
				.getWarningColorHEX();
			break;
		case Diagnostic.ERROR:
			colorHex = template.getControlValidationConfiguration().getErrorColorHEX();
			break;
		case Diagnostic.CANCEL:
			colorHex = template.getControlValidationConfiguration().getCancelColorHEX();
			break;
		default:
			throw new IllegalArgumentException(
				"The specified severity value " + severity + " is invalid. See Diagnostic class."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (colorHex == null) {
			return null;
		}
		if (!colorMap.containsKey(colorHex)) {
			colorMap.put(colorHex, getColor(colorHex));
		}
		return colorMap.get(colorHex);
	}

	/**
	 * Returns the validation icon matching the given severity.
	 * 
	 * @param severity the severity of the {@link Diagnostic}
	 * @return the icon to be displayed, or <code>null</code> when no icon is to be displayed
	 */
	public Image getValidationIcon(int severity) {
		final VTViewTemplate template = getTemplate();
		String imageUrl = null;

		switch (severity) {
		case Diagnostic.OK:
			imageUrl = template.getControlValidationConfiguration().getOkImageURL();
			break;
		case Diagnostic.INFO:
			imageUrl = template.getControlValidationConfiguration().getInfoImageURL();
			break;
		case Diagnostic.WARNING:
			imageUrl = template.getControlValidationConfiguration()
				.getWarningImageURL();
			break;
		case Diagnostic.ERROR:
			imageUrl = template.getControlValidationConfiguration().getErrorImageURL();
			break;
		case Diagnostic.CANCEL:
			imageUrl = template.getControlValidationConfiguration().getCancelImageURL();
			break;
		default:
			throw new IllegalArgumentException(
				"The specified severity value " + severity + " is invalid. See Diagnostic class."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (imageUrl == null) {
			return null;
		}
		try {
			return Activator.getImage(new
				URL(imageUrl));
		} catch (final MalformedURLException ex) {
			return null;
		}
	}

	private VTViewTemplate getTemplate() {
		final VTViewTemplateProvider vtViewTemplateProvider = Activator.getDefault().getVTViewTemplateProvider();
		if (vtViewTemplateProvider == null || vtViewTemplateProvider.getViewTemplate() == null) {
			if (defaultTemplate == null) {
				defaultTemplate = VTTemplateFactory.eINSTANCE.createViewTemplate();
				final VTControlValidationTemplate validationTemplate = VTTemplateFactory.eINSTANCE
					.createControlValidationTemplate();
				defaultTemplate.setControlValidationConfiguration(validationTemplate);
				validationTemplate.setErrorColorHEX("ff0000"); //$NON-NLS-1$
				validationTemplate.setWarningColorHEX("FFD800");//$NON-NLS-1$
				validationTemplate.setErrorImageURL(Activator.getDefault().getBundle()
					.getResource("icons/validation_error.png").toExternalForm()); //$NON-NLS-1$				
			}
			return defaultTemplate;
		}
		return vtViewTemplateProvider.getViewTemplate();
	}

	/**
	 * @param colorHex
	 * @return
	 */
	private Color getColor(String colorHex) {
		final String redString = colorHex.substring(0, 2);
		final String greenString = colorHex.substring(2, 4);
		final String blueString = colorHex.substring(4, 6);
		final int red = Integer.parseInt(redString, 16);
		final int green = Integer.parseInt(greenString, 16);
		final int blue = Integer.parseInt(blueString, 16);
		return new Color(Display.getDefault(), red, green, blue);
	}

}
