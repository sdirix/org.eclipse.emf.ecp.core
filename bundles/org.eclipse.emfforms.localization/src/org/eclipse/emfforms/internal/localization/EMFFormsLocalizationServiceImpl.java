/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.localization;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.emfforms.spi.core.services.locale.EMFFormsLocaleProvider;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;
import org.eclipse.osgi.service.localization.BundleLocalization;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;

/**
 * Service Implementation for retrieving translated Strings.
 *
 * @author Eugen Neufeld
 *
 */
public class EMFFormsLocalizationServiceImpl implements EMFFormsLocalizationService {

	private EMFFormsLocaleProvider localeProvider;
	private BundleLocalization bundleLocalization;
	private LogService logService;

	/**
	 * Called by the framework to set the EMFFormsLocaleProvider.
	 *
	 * @param localeProvider The {@link EMFFormsLocaleProvider}
	 */
	protected void setEMFFormsLocaleProvider(EMFFormsLocaleProvider localeProvider) {
		this.localeProvider = localeProvider;
	}

	/**
	 * Called by the framework to unset the EMFFormsLocaleProvider.
	 *
	 * @param localeProvider The {@link EMFFormsLocaleProvider}
	 */
	protected void unsetEMFFormsLocaleProvider(EMFFormsLocaleProvider localeProvider) {
		this.localeProvider = null;
	}

	/**
	 * Called by the framework to set the BundleLocalization.
	 *
	 * @param bundleLocalization The {@link BundleLocalization}
	 */
	protected void setBundleLocalization(BundleLocalization bundleLocalization) {
		this.bundleLocalization = bundleLocalization;
	}

	/**
	 * Called by the framework to set the LogService.
	 *
	 * @param logService The {@link LogService}
	 */
	protected void setLogService(LogService logService) {
		this.logService = logService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService#getString(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public String getString(Class<?> clazz, String key) {
		return getString(FrameworkUtil.getBundle(clazz), key);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService#getString(org.osgi.framework.Bundle,
	 *      java.lang.String)
	 */
	@Override
	public String getString(Bundle bundle, String key) {
		return innerGetString(bundle, key);
	}

	private String innerGetString(Bundle bundle, String key) {
		return getString(bundle, getLocale(), key);
	}

	private String getLocale() {
		if (localeProvider == null) {
			return null;
		}
		final Locale result = localeProvider.getLocale();
		return result.getLanguage();
	}

	private String getString(Bundle bundle, String localeLanguage, String key) {
		final ResourceBundle resourceBundle = bundleLocalization.getLocalization(bundle, localeLanguage);
		if (resourceBundle == null) {
			logService
				.log(
					LogService.LOG_WARNING,
					String
						.format(
							"No ResourceBundle found for Language '%1$s' in Bundle %2$s with Version %3$s.", localeLanguage, bundle.getSymbolicName(), bundle.getVersion().toString())); //$NON-NLS-1$
			return key;
		}
		if (!resourceBundle.containsKey(key)) {
			logService
				.log(
					LogService.LOG_WARNING,
					String
						.format(
							"The ResourceBundle for Language '%1$s' in Bundle %2$s with Version %3$s doesn't contain the key '%4$s'.", localeLanguage, bundle.getSymbolicName(), bundle.getVersion().toString(), key)); //$NON-NLS-1$
			return key;
		}
		final String result = resourceBundle.getString(key);
		// try {
		// return new String(result.getBytes("ISO-8859-1"), "UTF-8");
		// } catch (final UnsupportedEncodingException ex) {
		// return key;
		// }
		return result;
	}

}
