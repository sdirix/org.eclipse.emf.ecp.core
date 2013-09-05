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
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPAbstractCustomControlSWT extends
	ECPAbstractCustomControl {
	public final static int VALIDATION_ERROR_IMAGE = 0;
	public final static int ADD_IMAGE = 1;
	public final static int DELETE_IMAGE = 2;
	public final static int HELP_IMAGE = 3;

	/**
	 * @param editableFeatures
	 * @param referencedFeatures
	 */
	public ECPAbstractCustomControlSWT(
		Set<ECPCustomControlFeature> editableFeatures,
		Set<ECPCustomControlFeature> referencedFeatures) {
		super(editableFeatures, referencedFeatures);
	}

	private final SWTCustomControlHelper swtHelper = new SWTCustomControlHelper();
	private Label validationLabel;
	private Composite composite;

	protected final void createValidationLabel(Composite parent) {
		validationLabel = new Label(parent, SWT.NONE);
		validationLabel.setBackground(parent.getBackground());
		// set the size of the label to the size of the image
		GridDataFactory.fillDefaults().hint(16, 17)
			.align(SWT.BEGINNING, SWT.CENTER).applyTo(validationLabel);

	}

	protected final void showError(String title, String message) {
		showMessageDialog(MessageDialog.ERROR, title, message);
	}

	protected final void showInfo(String title, String message) {
		showMessageDialog(MessageDialog.INFORMATION, title, message);
	}

	// Not yet API
	@SuppressWarnings("restriction")
	private void showMessageDialog(int type, String title, String message) {
		final MessageDialog dialog = new MessageDialog(composite.getShell(), title,
			null, message, type,
			new String[] { JFaceResources
				.getString(IDialogLabelKeys.OK_LABEL_KEY) }, 0);
		new ECPDialogExecutor(dialog) {

			@Override
			public void handleResult(int codeResult) {
				// Nothing to do
			}
		}.execute();
	}

	/**
	 * Adds the controls of this {@link CustomControl} to the given composite.
	 * 
	 * @param composite The composite on which this custom control shall add its controls.
	 * @return a list of {@link RenderingResultRow}s. The RenderingResultsRows are in order with the added controls.
	 */
	protected abstract List<RenderingResultRow<Control>> createControls(Composite composite);

	public final void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR
			|| diagnostic.getSeverity() == Diagnostic.WARNING) {
			final Image image = getImage(VALIDATION_ERROR_IMAGE);
			Diagnostic reason = diagnostic;
			if (diagnostic.getChildren() != null
				&& diagnostic.getChildren().size() != 0) {
				reason = diagnostic.getChildren().get(0);
			}
			if (validationLabel != null) {
				validationLabel.setImage(image);
				validationLabel.setToolTipText(reason.getMessage());
			}
			final List<?> data = diagnostic.getData();

			handleCreatedControls(diagnostic);

			handleContentValidation(diagnostic.getSeverity(),
				(EStructuralFeature) (data.size() > 1 ? data.get(1) : null));
		} else {
			resetValidation();
		}
	}

	/**
	 * @param diagnostic
	 */
	private void resetControlValidation() {
		final Set<EStructuralFeature> keySet = controlMap.keySet();

		for (final EStructuralFeature eStructuralFeature : keySet) {
			final ECPControl ecpControl = controlMap.get(eStructuralFeature);
			ecpControl.resetValidation();
		}
	}

	/**
	 * @param diagnostic
	 */
	private void handleCreatedControls(Diagnostic diagnostic) {
		if (diagnostic.getData().size() < 1) {
			return;
		}
		if (!(diagnostic.getData().get(1) instanceof EStructuralFeature)) {
			return;
		}
		final EStructuralFeature feature = (EStructuralFeature) diagnostic.getData().get(1);
		final ECPControl ecpControl = controlMap.get(feature);
		if (ecpControl == null) {
			return;
		}
		ecpControl.handleValidation(diagnostic);
	}

	protected abstract void handleContentValidation(int severity,
		EStructuralFeature feature);

	public final void resetValidation() {
		resetControlValidation();
		if (validationLabel != null) {
			validationLabel.setImage(null);
		}
		resetContentValidation();
	}

	protected abstract void resetContentValidation();

	protected final SWTCustomControlHelper getSWTHelper() {
		return swtHelper;
	}

	private final Image getImage(int imageType) {
		switch (imageType) {
		case VALIDATION_ERROR_IMAGE:
			return Activator.getImage("icons/validation_error.png");
		case HELP_IMAGE:
			return Activator.getImage("icons/help.png");
		case ADD_IMAGE:
			return Activator.getImage("icons/add.png");
		case DELETE_IMAGE:
			return Activator.getImage("icons/delete.png");
		default:
			return null;
		}
	}

	public Composite createControl(ECPCustomControlFeature feature, Composite parent) {
		return getControl(SWTControl.class, feature).createControl(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void dispose() {
		if (composite != null) {
			composite.dispose();
			composite = null;
		}
		if (validationLabel != null) {
			validationLabel.dispose();
			validationLabel = null;
		}
		super.dispose();
	}

	public final class SWTCustomControlHelper {

		public final Image getImage(int imageType) {
			return ECPAbstractCustomControlSWT.this.getImage(imageType);
		}
	}

}
