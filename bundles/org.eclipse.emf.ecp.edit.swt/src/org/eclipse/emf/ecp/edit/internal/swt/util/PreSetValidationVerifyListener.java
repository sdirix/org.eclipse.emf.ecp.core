/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emfforms.spi.common.validation.PreSetValidationService;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Utility class for setting up a {@link VerifyListener}
 * that performs pre-set validation.
 *
 */
public final class PreSetValidationVerifyListener {

	private static PreSetValidationService preSetValidationService;

	private PreSetValidationVerifyListener() {
		init();
	}

	private void init() {
		if (preSetValidationService == null) {
			final BundleContext bundleContext = FrameworkUtil
				.getBundle(getClass())
				.getBundleContext();

			final ServiceReference<PreSetValidationService> serviceReference = bundleContext
				.getServiceReference(PreSetValidationService.class);

			preSetValidationService = serviceReference != null
				? bundleContext.getService(serviceReference) : null;
		}
	}

	/**
	 * Create an instance that sets up necessary services to perform
	 * pre-set validation.
	 *
	 * @return an instance of this class with the necessary services initialized
	 */
	public static PreSetValidationVerifyListener create() {
		return new PreSetValidationVerifyListener();
	}

	/**
	 * Attach the {@link VerifyListener} to the given {@link Text} widget.
	 * Performs pre-set validation for the given {@link EStructuralFeature}
	 *
	 * @param text the text widget the created verify listener should be attached to
	 * @param feature the feature to be validated
	 */
	public void attachTo(Text text, final EStructuralFeature feature) {
		final VerifyListener verifyListener = new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				final String currentText = Text.class.cast(e.widget).getText();
				final String changedText = currentText.substring(0, e.start) + e.text
					+ currentText.substring(e.end);

				final Diagnostic diag = preSetValidationService.validate(feature, changedText);

				if (diag.getSeverity() == Diagnostic.OK) {
					return;
				}

				e.doit = false;
			}
		};

		if (preSetValidationService != null) {
			text.addVerifyListener(verifyListener);
		}
	}
}
