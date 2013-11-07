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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * This class defines a SWTCOntrol which is an abstract class defining an {@link ECPAbstractControl} for SWT.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class SWTControl extends ECPAbstractControl implements ECPControlSWT {

	/**
	 * RAP theming variable to set.
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";//$NON-NLS-1$
	/**
	 * The icon for a validation error.
	 */
	protected static final String VALIDATION_ERROR_ICON = "icons/validation_error.png";//$NON-NLS-1$

	/**
	 * The label for the validation icon.
	 */
	protected Label validationLabel;

	private IObservableValue modelValue;
	private Binding binding;

	private Composite controlComposite;
	private Composite parentComposite;
	private StackLayout sl;
	private Label unsetLabel;

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.ECPControlSWT#createControls(org.eclipse.swt.widgets.Composite)
	 */
	public List<RenderingResultRow<Control>> createControls(final Composite parent) {
		final IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor();
		if (itemPropertyDescriptor == null) {
			return null;
		}
		return Collections.singletonList(SWTRenderingHelper.INSTANCE.getResultRowFactory().createRenderingResultRow(
			createControl(parent)));
	}

	/**
	 * This method is called to render the control on a parent.
	 * 
	 * @param parent the {@link Composite} which is the parent
	 * @return the created {@link Composite}
	 */
	public Composite createControl(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		int numColumns = 2;
		if (isEmbedded()) {
			numColumns--;
		}
		// TODO needed .spacing(10, 0) ?
		GridLayoutFactory.fillDefaults().numColumns(numColumns).applyTo(composite);

		createValidationIcon(composite);
		createDataControl(composite);

		setHelpTooltips();

		// init
		setEditable(isEditable());

		binding = bindValue();

		// write initial values to model (if they differ from the default value of the model-element)
		if (!getStructuralFeature().isUnsettable()
			&& !getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			if (binding != null) {
				binding.updateTargetToModel();
			}
		}

		return composite;
	}

	private void createValidationIcon(Composite composite) {
		if (!isEmbedded()) {
			validationLabel = new Label(composite, SWT.NONE);
			validationLabel.setBackground(composite.getBackground());
			// set the size of the label to the size of the image
			GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);
		}
	}

	private void createDataControl(Composite composite) {
		final Composite innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setBackground(composite.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(innerComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(innerComposite);
		createContentControl(innerComposite);
	}

	private void setHelpTooltips() {
		final Control[] controls = getControlsForTooltip();
		if (controls != null) {
			for (final Control control : controls) {
				control.setToolTipText(getHelpText());
			}
		}
	}

	/**
	 * Method for retrieving all controls which should have the help text as their tooltip.
	 * 
	 * @return the array of the controls to set a tooltip to
	 */
	protected abstract Control[] getControlsForTooltip();

	/**
	 * Helper for creating the unset stacklayout and creating the control's composite.
	 * 
	 * @param composite the parent {@link Composite} to create the control onto
	 */
	protected void createContentControl(Composite composite) {
		parentComposite = new Composite(composite, SWT.NONE);
		parentComposite.setBackground(composite.getBackground());
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(parentComposite);
		sl = new StackLayout();
		parentComposite.setLayout(sl);
		controlComposite = new Composite(parentComposite, SWT.NONE);
		controlComposite.setBackground(parentComposite.getBackground());

		// 1 column for control, 1 for default unset button
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(2, 0)
			.applyTo(controlComposite);

		unsetLabel = new Label(parentComposite, SWT.NONE);
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);
		unsetLabel.setText(getUnsetLabelText());
		unsetLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
				sl.topControl = controlComposite;
				parentComposite.layout(true);
				if (binding != null) {
					binding.updateTargetToModel();
				} else {
					final Object currentUnsetValue = getModelElementContext().getModelElement().eGet(
						getStructuralFeature());
					getModelElementContext().getModelElement().eSet(getStructuralFeature(), currentUnsetValue);
				}
			}

			public void mouseDown(MouseEvent e) {
				// nothing to do
			}

			public void mouseDoubleClick(MouseEvent e) {
				// nothing to do
			}
		});
		int numControls = 1;
		fillControlComposite(controlComposite);

		if (!isEmbedded() && getStructuralFeature().isUnsettable()) {
			Button unsetButton = getCustomUnsetButton();
			if (unsetButton == null) {
				numControls++;
				unsetButton = new Button(controlComposite, SWT.PUSH);
				unsetButton.setToolTipText(getUnsetButtonTooltip());
				unsetButton.setImage(Activator.getImage("icons/delete.png")); //$NON-NLS-1$
			}
			unsetButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					getModelElementContext()
						.getEditingDomain()
						.getCommandStack()
						.execute(
							new SetCommand(getModelElementContext().getEditingDomain(), getModelElementContext()
								.getModelElement(), getStructuralFeature(), SetCommand.UNSET_VALUE));

					showUnsetLabel();
				}
			});
			unsetButton.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_unset"); //$NON-NLS-1$
		}
		if (numControls != 1) {
			// 1 column for control, 1 for default unset button
			((GridLayout) controlComposite.getLayout()).numColumns = numControls;
		}

		// INFO margin needed for controlDecorator
		// .extendedMargins(10, 0, 0, 0)

		if (!getStructuralFeature().isUnsettable()
			|| getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			sl.topControl = controlComposite;
		} else {
			sl.topControl = unsetLabel;
		}

		parentComposite.layout();
	}

	/**
	 * 
	 */
	protected void showUnsetLabel() {
		sl.topControl = unsetLabel;
		parentComposite.layout();
	}

	/**
	 * This method must be overridden by concrete classes. Here the widget displaying the data is added to the
	 * composite.
	 * 
	 * @param controlComposite the {@link Composite} to add the widget to
	 */
	protected abstract void fillControlComposite(Composite controlComposite);

	/**
	 * The default unset button will be displayed to the right of the control's composite. Concrete classes may override
	 * this method to include an own unset button in their composite rather than using the default positioning.
	 * 
	 * @return The custom unset button of the concrete class
	 */
	protected Button getCustomUnsetButton() {
		return null;
	}

	/**
	 * The model value used for databinding. It is either the set one or the calculated.
	 * 
	 * @return the {@link IObservableValue}
	 */
	protected IObservableValue getModelValue() {
		if (modelValue != null) {
			return modelValue;
		}
		modelValue = EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		return modelValue;
	}

	/**
	 * Allows the user to set the {@link IObservableValue} to use in the control during databinding.
	 * 
	 * @param modelValue the set {@link IObservableValue}
	 */
	public void setObservableValue(IObservableValue modelValue) {
		this.modelValue = modelValue;
	}

	/**
	 * A helper method which creates a button for an action on a composite.
	 * 
	 * @param action the action to create a button for
	 * @param composite the composite to create the button onto
	 * @return the created button
	 */
	protected Button createButtonForAction(final Action action, final Composite composite) {
		final Button selectButton = new Button(composite, SWT.PUSH);
		selectButton.setImage(action.getImageDescriptor().createImage());
		selectButton.setToolTipText(action.getToolTipText());
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
				composite.layout();
			}

		});
		return selectButton;
	}

	/**
	 * Triggers the control to perform the databinding.
	 * 
	 * @return The {@link Binding}
	 */
	protected abstract Binding bindValue();

	/**
	 * Returns the help information.
	 * 
	 * @return The help text
	 */
	protected String getHelpText() {
		return getItemPropertyDescriptor().getDescription(null);
	}

	/**
	 * Returns the validation icon matching the given severity.
	 * 
	 * @param severity the severity of the {@link Diagnostic}
	 * @return the icon to be displayed, or <code>null</code> when no icon is to be displayed
	 */
	protected Image getValidationIcon(int severity) {
		switch (severity) {
		case Diagnostic.OK:
			return null;
		case Diagnostic.INFO:
			return null;
		case Diagnostic.WARNING:
			return Activator.getImage(VALIDATION_ERROR_ICON);
		case Diagnostic.ERROR:
			return Activator.getImage(VALIDATION_ERROR_ICON);
		case Diagnostic.CANCEL:
			return Activator.getImage(VALIDATION_ERROR_ICON);
		default:
			throw new IllegalArgumentException(
				"The specified severity value " + severity + " is invalid. See Diagnostic class."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Returns the background color for a control with the given validation severity.
	 * 
	 * @param severity severity the severity of the {@link Diagnostic}
	 * @return the color to be used as a background color
	 */
	protected Color getValidationBackgroundColor(int severity) {
		switch (severity) {
		case Diagnostic.OK:
			return getSystemColor(SWT.COLOR_WHITE);
		case Diagnostic.INFO:
			return getSystemColor(SWT.COLOR_YELLOW);
		case Diagnostic.WARNING:
			return getSystemColor(SWT.COLOR_YELLOW);
		case Diagnostic.ERROR:
			return getSystemColor(SWT.COLOR_RED);
		case Diagnostic.CANCEL:
			return getSystemColor(SWT.COLOR_DARK_RED);
		default:
			throw new IllegalArgumentException(
				"The specified severity value " + severity + " is invalid. See Diagnostic class."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Returns the string for the unset label.
	 * 
	 * @return The unset label text
	 */
	protected abstract String getUnsetLabelText();

	/**
	 * Returns the string for the unset button tooltip.
	 * 
	 * @return The unset button tooltip
	 */
	protected abstract String getUnsetButtonTooltip();

	/**
	 * Method for retrieving a {@link Color} based on the predefined SWT id.
	 * 
	 * @param color the SWT id of the color
	 * @return the Color or black if the id was incorrect
	 */
	protected final Color getSystemColor(int color) {
		if (parentComposite == null) {
			return Display.getDefault().getSystemColor(color);
		}
		return parentComposite.getShell().getDisplay().getSystemColor(color);
	}

	private Setting getSetting(VDomainModelReference domainModelReference) {
		final Iterator<Setting> iterator = domainModelReference.getIterator();
		int count = 0;
		Setting lastSetting = null;
		while (iterator.hasNext()) {
			count++;
			if (count == 2) {
				throw new IllegalArgumentException(
					"The passed VDomainModelReference resolves to more then one setting.");
			}
			lastSetting = iterator.next();
		}
		if (count == 0) {
			throw new IllegalArgumentException("The passed VDomainModelReference resolves to no setting.");
		}
		return lastSetting;
	}

	/**
	 * Return the {@link EStructuralFeature} of this control.
	 * 
	 * @return the {@link EStructuralFeature}
	 */
	@Deprecated
	protected EStructuralFeature getStructuralFeature() {
		final Iterator<EStructuralFeature> iterator = getDomainModelReference().getEStructuralFeatureIterator();
		int count = 0;
		EStructuralFeature lastFeature = null;
		while (iterator.hasNext()) {
			count++;
			// if (count == 2) {
			// throw new IllegalArgumentException(
			// "The passed VDomainModelReference resolves to more then one EStructuralFeature.");
			// }
			// lastFeature=iterator.next();
			if (lastFeature == null) {
				lastFeature = iterator.next();
			} else {
				iterator.next();
			}
		}
		if (count == 0) {
			throw new IllegalArgumentException("The passed VDomainModelReference resolves to no EStructuralFeature.");
		}

		return lastFeature;
	}

	/**
	 * Whether this control should be editable.
	 * 
	 * @return true if the {@link IItemPropertyDescriptor#canSetProperty(Object)} returns true, false otherwise
	 */
	@Deprecated
	protected boolean isEditable() {
		return getItemPropertyDescriptor().canSetProperty(getSetting(getDomainModelReference()).getEObject());
	}

	/**
	 * Convenience Method for retrieving an {@link IItemPropertyDescriptor}.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 */
	@Deprecated
	protected IItemPropertyDescriptor getItemPropertyDescriptor() {
		return getItemPropertyDescriptor(getSetting(getDomainModelReference()));
	}

	@Override
	public void dispose() {
		validationLabel = null;
		if (modelValue != null) {
			modelValue.dispose();
			modelValue = null;
		}
		if (binding != null) {
			binding.dispose();
			binding = null;
		}
		if (controlComposite != null) {
			controlComposite.dispose();
			controlComposite = null;
		}
		if (parentComposite != null) {
			parentComposite.dispose();
			parentComposite = null;
		}
		sl = null;
		if (unsetLabel != null) {
			unsetLabel.dispose();
			unsetLabel = null;
		}
		super.dispose();
	}
}
