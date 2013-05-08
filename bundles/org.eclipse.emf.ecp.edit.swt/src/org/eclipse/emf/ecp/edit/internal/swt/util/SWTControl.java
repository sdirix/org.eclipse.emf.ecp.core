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

import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.AbstractControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * This class defines a SWTCOntrol which is an abstract class defining an {@link AbstractControl} for SWT.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class SWTControl extends AbstractControl {

	/**
	 * RAP theming variable to set.
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";

	protected static final String VALIDATION_ERROR_ICON = "icons/validation_error.png";//$NON-NLS-1$

	protected Label validationLabel;

	private IObservableValue modelValue;
	private Binding binding;

	private Composite controlComposite;
	private Composite parentComposite;
	private StackLayout sl;
	private Label unsetLabel;

	/**
	 * Constructor for a swt control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public SWTControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.edit.controls.AbstractControl#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		int numColumns = 2;
		if (isEmbedded()) {
			numColumns--;
		}
		if (getModelElementContext().isRunningAsWebApplication()) {
			numColumns++;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(10, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(composite);
		if (!isEmbedded()) {
			validationLabel = new Label(composite, SWT.NONE);
			// set the size of the label to the size of the image
			GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);
		}

		Composite innerComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(innerComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(innerComposite);
		createContentControl(innerComposite);
		setEditable(isEditable());

		binding = bindValue();

		// write initial values to model (if they differ from the default value of the model-element)
		if (!getStructuralFeature().isUnsettable()
			&& !getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			if (binding != null) {
				binding.updateTargetToModel();
			}
		}

		if (getModelElementContext().isRunningAsWebApplication()) {
			Button b = new Button(composite, SWT.PUSH);
			b.setImage(Activator.getImage("icons/help.png"));
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					MessageDialog dialog = new MessageDialog(parent.getShell(), "Help", null, getHelpText(),
						MessageDialog.INFORMATION, new String[] { JFaceResources
							.getString(IDialogLabelKeys.OK_LABEL_KEY) }, 0);
					new ECPDialogExecutor(dialog) {

						@Override
						public void handleResult(int codeResult) {

						}
					}.execute();
				}

			});
			// Control[] tabList = Arrays.copyOfRange(composite.getTabList(), 0, composite.getTabList().length - 1);
			Control[] tabList = new Control[composite.getTabList().length - 1];
			System.arraycopy(composite.getTabList(), 0, tabList, 0, tabList.length);
			composite.setTabList(tabList);
		} else {
			Control[] controls = getControlsForTooltip();
			if (controls != null) {
				for (Control control : controls) {
					control.setToolTipText(getHelpText());
				}
			}

		}
		return composite;
	}

	/**
	 * @return
	 */
	protected abstract Control[] getControlsForTooltip();

	/**
	 * Helper for creating the unset stacklayout and creating the control's composite.
	 * 
	 * @param composite
	 */
	protected void createContentControl(Composite composite) {
		parentComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(parentComposite);
		sl = new StackLayout();
		parentComposite.setLayout(sl);
		controlComposite = new Composite(parentComposite, SWT.NONE);
		// 1 column for control, 1 for default unset button
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(2, 0).applyTo(controlComposite);

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
				}
			}

			public void mouseDown(MouseEvent e) {
				// nothing to do
			}

			public void mouseDoubleClick(MouseEvent e) {
				// nothing to do
			}
		});

		fillControlComposite(controlComposite);

		if (!isEmbedded() && getStructuralFeature().isUnsettable()) {
			Button unsetButton = getCustomUnsetButton();
			if (unsetButton == null) {
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
		}

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
	 * Returns the {@link DataBindingContext} set in the constructor.
	 * 
	 * @return the {@link DataBindingContext}
	 */
	protected DataBindingContext getDataBindingContext() {
		return getModelElementContext().getDataBindingContext();
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
		IObservableValue model = EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		return model;
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
		Button selectButton = new Button(composite, SWT.PUSH);
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
}
