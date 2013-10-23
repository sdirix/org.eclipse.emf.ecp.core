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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.util.DoubleColumnRow;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPControlSWT;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.internal.swt.util.SingleColumnRow;
import org.eclipse.emf.ecp.edit.spi.ECPControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.ECPAbstractCustomControl;
import org.eclipse.emf.ecp.view.custom.ui.internal.swt.Activator;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Extend this class in order to provide an own implementation of an
 * {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl ECPCustomControl}.
 * 
 * @author Eugen Neufeld
 * 
 */
// Missing SPI or API definitions
@SuppressWarnings("restriction")
public abstract class ECPAbstractCustomControlSWT extends
	ECPAbstractCustomControl implements ECPControlSWT {
	/**
	 * Constant for an validation error image.
	 */
	public static final int VALIDATION_ERROR_IMAGE = 0;
	/**
	 * Constant for an add image.
	 */
	public static final int ADD_IMAGE = 1;
	/**
	 * Constant for an delete image.
	 */
	public static final int DELETE_IMAGE = 2;
	/**
	 * Constant for a help image.
	 */
	public static final int HELP_IMAGE = 3;

	/**
	 * Extend this class for an SWT implementation of the {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl
	 * ECPCustomControl}.
	 * 
	 * @param features the features which will be used in this
	 *            {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl
	 *            ECPCustomControl}
	 */
	public ECPAbstractCustomControlSWT(
		Set<VDomainModelReference> features) {
		super(features);
	}

	private final SWTCustomControlHelper swtHelper = new SWTCustomControlHelper();
	private Label validationLabel;
	private Shell shell;
	private List<RenderingResultRow<Control>> renderingResult;

	/**
	 * This will create a validation label which will show the validation result of the whole
	 * {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl
	 * ECPCustomControl}.
	 * 
	 * @param parent the {@link Composite} to position the validation label on
	 */
	protected final void createValidationLabel(Composite parent) {
		validationLabel = new Label(parent, SWT.NONE);
		validationLabel.setBackground(parent.getBackground());
		validationLabel.setImage(getImage(VALIDATION_ERROR_IMAGE));
		validationLabel.setVisible(false);
		// GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);
	}

	/**
	 * This allows to show an error dialog.
	 * 
	 * @param title the title of the dialog
	 * @param message the message to show in the dialog
	 */
	protected final void showError(String title, String message) {
		showMessageDialog(MessageDialog.ERROR, title, message);
	}

	/**
	 * This allows to show an info dialog.
	 * 
	 * @param title the title of the dialog
	 * @param message the message to show in the dialog
	 */
	protected final void showInfo(String title, String message) {
		showMessageDialog(MessageDialog.INFORMATION, title, message);
	}

	/**
	 * Sets the shell where message dialogs are displayed.
	 * 
	 * @param shell the shell
	 */
	public final void setMessageShell(Shell shell) {
		this.shell = shell;
	}

	private void showMessageDialog(int type, String title, String message) {
		final MessageDialog dialog = new MessageDialog(shell, title,
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
	 * This is called by the framework when this {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl
	 * ECPCustomControl} is about to be rendered.
	 * 
	 * @param composite The composite on which this custom control shall add its controls.
	 * @return a list of {@link RenderingResultRow}s. The RenderingResultsRows are in order with the added controls.
	 */
	public final List<RenderingResultRow<Control>> createControls(Composite composite) {
		renderingResult = createControl(composite);
		return renderingResult;
	}

	/**
	 * This is called when this {@link org.eclipse.emf.ecp.view.custom.model.ECPCustomControl
	 * ECPCustomControl} is about to be rendered.
	 * 
	 * @param composite The composite on which this custom control shall add its controls.
	 * @return a list of {@link RenderingResultRow}s. The RenderingResultsRows are in order with the added controls.
	 */
	protected abstract List<RenderingResultRow<Control>> createControl(Composite composite);

	/**
	 * Override this method in order to correctly set the custom control to editable or not editable.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ECPControl#setEditable(boolean)
	 */
	public void setEditable(boolean isEditable) {
		// Do nothing

		for (final RenderingResultRow<Control> row : renderingResult) {
			if (SingleColumnRow.class.isInstance(row)) {
				((SingleColumnRow) row).getControl().setEnabled(false);
			}
			else if (DoubleColumnRow.class.isInstance(row)) {
				((DoubleColumnRow) row).getLeftControl().setEnabled(false);
				((DoubleColumnRow) row).getRightControl().setEnabled(false);

			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ECPControl#handleValidation(org.eclipse.emf.common.util.Diagnostic)
	 */
	public final void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR
			|| diagnostic.getSeverity() == Diagnostic.WARNING) {

			Diagnostic reason = diagnostic;
			if (diagnostic.getChildren() != null
				&& diagnostic.getChildren().size() != 0) {
				reason = diagnostic.getChildren().get(0);
			}
			if (validationLabel != null) {
				// validationLabel.setImage(image);
				validationLabel.setVisible(true);
				validationLabel.setToolTipText(reason.getMessage());
			}
			final List<?> data = diagnostic.getData();

			handleCreatedControls(diagnostic);

			handleContentValidation(diagnostic.getSeverity(),
				(EStructuralFeature) (data.size() > 1 && EStructuralFeature.class.isInstance(data.get(1)) ? data.get(1)
					: null));
		} else {
			resetValidation();
		}
	}

	/**
	 * @param diagnostic
	 */
	private void handleCreatedControls(Diagnostic diagnostic) {
		if (diagnostic.getData().size() < 2) {
			return;
		}
		if (!(diagnostic.getData().get(1) instanceof EStructuralFeature)) {
			return;
		}
		final EStructuralFeature feature = (EStructuralFeature) diagnostic.getData().get(1);
		final ECPControl ecpControl = getRetrievedControl(feature);
		if (ecpControl == null) {
			return;
		}
		ecpControl.handleValidation(diagnostic);
	}

	/**
	 * This is called so that an error can be shown by the user.
	 * 
	 * @param severity the severity of the error
	 * @param feature the feature for which the error occurred
	 */
	protected abstract void handleContentValidation(int severity,
		EStructuralFeature feature);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ECPControl#resetValidation()
	 */
	public final void resetValidation() {
		resetControlValidation();
		if (validationLabel != null) {
			validationLabel.setVisible(false);
		}
		resetContentValidation();
	}

	/**
	 * This is called so that the user can be reset all validation errors.
	 */
	protected abstract void resetContentValidation();

	/**
	 * This is a helper method which provides an {@link SWTCustomControlHelper}. It allows to get an image based on the
	 * constants defined in {@link ECPAbstractCustomControlSWT}.
	 * 
	 * @return the {@link SWTCustomControlHelper} to use to retrieve images.
	 */
	protected final SWTCustomControlHelper getSWTHelper() {
		return swtHelper;
	}

	private Image getImage(int imageType) {
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

	/**
	 * Allows to create a framework control based on an {@link VDomainModelReference}.
	 * 
	 * @param domainModelReference the {@link VDomainModelReference} to create the control for
	 * @param parent the {@link Composite} to create the control on
	 * @return the rendered {@link Composite} of the created control
	 */
	protected final Composite createControl(VDomainModelReference domainModelReference, Composite parent) {
		final SWTControl control = getControl(SWTControl.class, domainModelReference);
		control.init(getModelElementContext(), domainModelReference);
		return control.createControl(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void dispose() {
		if (validationLabel != null) {
			validationLabel.dispose();
			validationLabel = null;
		}
		super.dispose();
	}

	/**
	 * Creates a binding for a {@link StructuredViewer} based on a {@link ECPCustomControlFeature} and the array of
	 * {@link IValueProperty IValueProperties} for labels.
	 * 
	 * @param customControlFeature the {@link ECPCustomControlFeature} to use
	 * @param viewer the {@link StructuredViewer} to bind
	 * @param labelProperties the array if {@link IValueProperty IValueProperties} to use for labels
	 */
	protected void createViewerBinding(VDomainModelReference customControlFeature, StructuredViewer viewer,
		IValueProperty[] labelProperties) {

		final IObservableList list = getObservableList(customControlFeature);
		ViewerSupport.bind(viewer, list, labelProperties);
	}

	/**
	 * The {@link SWTCustomControlHelper} allows the retrieval of SWT specific elements.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	public final class SWTCustomControlHelper {

		/**
		 * Allows to get an {@link Image} based on the constants defined in {@link ECPAbstractCustomControlSWT}.
		 * 
		 * @param imageType the image type to retrieve
		 * @return the retrieved Image or null if an unknown imageType was provided
		 */
		public Image getImage(int imageType) {
			return ECPAbstractCustomControlSWT.this.getImage(imageType);
		}
	}

}
