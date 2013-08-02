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
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
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

	public final Composite createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		final int numColumns = 1;

		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(10, 0)
			.applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false)
			.align(SWT.FILL, SWT.BEGINNING).applyTo(composite);

		final Composite innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setBackground(parent.getBackground());
		GridDataFactory.fillDefaults().grab(true, false)
			.align(SWT.FILL, SWT.BEGINNING).applyTo(innerComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(innerComposite);
		createContentControl(innerComposite);

		return composite;
	}

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

	protected abstract void createContentControl(Composite composite);

	public final void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR
			|| diagnostic.getSeverity() == Diagnostic.WARNING) {
			final Image image = getImage(VALIDATION_ERROR_IMAGE);
			validationLabel.setImage(image);
			Diagnostic reason = diagnostic;
			if (diagnostic.getChildren() != null
				&& diagnostic.getChildren().size() != 0) {
				reason = diagnostic.getChildren().get(0);
			}
			validationLabel.setToolTipText(reason.getMessage());
			final List<?> data = diagnostic.getData();
			handleContentValidation(diagnostic.getSeverity(),
				(EStructuralFeature) (data.size() > 1 ? data.get(1) : null));
		} else {
			resetValidation();
		}
	}

	protected abstract void handleContentValidation(int severity,
		EStructuralFeature feature);

	public final void resetValidation() {
		validationLabel.setImage(null);
		resetContentValidation();
	}

	protected abstract void resetContentValidation();

	public final void setEditable(boolean isEditable) {
		composite.setEnabled(isEditable);
	}

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

	public final class SWTCustomControlHelper {

		public final Image getImage(int imageType) {
			return ECPAbstractCustomControlSWT.this.getImage(imageType);
		}
	}

}
