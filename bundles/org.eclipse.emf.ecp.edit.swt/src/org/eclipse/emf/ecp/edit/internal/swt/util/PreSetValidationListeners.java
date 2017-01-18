/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDiagnostic;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.spi.common.validation.PreSetValidationService;
import org.eclipse.emfforms.spi.common.validation.PreSetValidationServiceRunnable;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
public final class PreSetValidationListeners {

	/**
	 * Singleton instance.
	 */
	private static PreSetValidationListeners instance = new PreSetValidationListeners();
	private PreSetValidationService preSetValidationService;

	private PreSetValidationListeners() {
		init();
	}

	/**
	 * Returns the validation listeners factory.
	 *
	 * @return the factory that can be used to create and attach listeners
	 */
	public static PreSetValidationListeners create() {
		return instance;
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
	 * Attach a {@link VerifyListener} to the given {@link Text} widget.
	 * Performs pre-set validation for the given {@link EStructuralFeature}
	 *
	 * @param text the text widget the created verify listener should be attached to
	 * @param feature the feature to be validated
	 */
	public void verify(Text text, final EStructuralFeature feature) {
		verify(text, feature, null);
	}

	/**
	 * Attach a {@link VerifyListener} to the given {@link Text} widget.
	 * Performs pre-set validation for the given {@link EStructuralFeature} and reports any
	 * errors to the given {@link VElement}.
	 *
	 * @param text the text widget the created verify listener should be attached to
	 * @param feature the feature to be validated
	 * @param vElement the {@link VElement} an {@link Diagnostic} may be attached to
	 */
	public void verify(Text text, final EStructuralFeature feature, final VElement vElement) {
		if (preSetValidationService != null) {
			final VerifyListener verifyListener = new VerifyListener() {
				@Override
				public void verifyText(VerifyEvent e) {
					final String currentText = Text.class.cast(e.widget).getText();
					final String changedText = currentText.substring(0, e.start) + e.text
						+ currentText.substring(e.end);

					final Diagnostic diag = preSetValidationService.validateLoose(feature, changedText);

					if (vElement != null) {
						final Diagnostic strictDiag = preSetValidationService.validate(feature, changedText);
						final VDiagnostic vDiagnostic = VViewFactory.eINSTANCE.createDiagnostic();
						vDiagnostic.getDiagnostics().add(strictDiag);

						if (strictDiag.getSeverity() != Diagnostic.OK) {
							vElement.setDiagnostic(vDiagnostic);
						}
					}

					if (diag.getSeverity() == Diagnostic.OK) {
						return;
					}

					e.doit = false;
				}
			};

			text.addVerifyListener(verifyListener);
		}
	}

	/**
	 * Attach a {@link FocusListener} to the given {@link Text} widget.
	 * Performs pre-set validation for the given {@link EStructuralFeature} and
	 * executes the {@link Runnable} in case the content of the text widget is
	 * invalid.
	 *
	 * @param text the text widget the created verify listener should be attached to
	 * @param feature the feature to be validated
	 * @param focusLost code to be executed in case the text is invalid and focus has been lost
	 * @param focusGained code to be executed in case the focus has been gained
	 */
	public void focus(final Text text, final EStructuralFeature feature,
		final PreSetValidationServiceRunnable focusLost,
		final Runnable focusGained) {
		if (preSetValidationService != null) {
			text.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					focusLost.run(preSetValidationService);
				}

				@Override
				public void focusGained(FocusEvent e) {
					focusGained.run();
				}
			});
		}
	}
}
