/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.core.swt;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen
 * 
 */
public abstract class SimpleControlSWTRenderer extends AbstractControlSWTRenderer<VControl> {
	/**
	 * Default constructor.
	 */
	public SimpleControlSWTRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	SimpleControlSWTRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription()
	 */
	@Override
	public final GridDescription getGridDescription() {
		return GridDescriptionFactory.INSTANCE.createSimpleGrid(1,
			getVElement().getLabelAlignment() == LabelAlignment.NONE ? 2 : 3);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(int, org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	protected final Control renderControl(GridCell gridCell, Composite parent)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		int controlIndex = gridCell.getColumn();
		if (getVElement().getLabelAlignment() == LabelAlignment.NONE) {
			controlIndex++;
		}
		switch (controlIndex) {
		case 0:
			return createLabel(parent);
		case 1:
			return createValidationIcon(parent);
		case 2:
			if (isUnsettable()) {
				return createUnsettableControl(parent);
			}
			return createControl(parent);
		default:
			return null;
		}
	}

	/**
	 * Returns true if the control is unsettable.
	 * 
	 * @return true if unsettable, false otherwise
	 */
	protected boolean isUnsettable() {
		return getVElement().getDomainModelReference().getEStructuralFeatureIterator().next().isUnsettable();
	}

	private Control createUnsettableControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(composite);
		final Composite controlComposite = new Composite(composite, SWT.NONE);
		controlComposite.setBackground(parent.getBackground());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(controlComposite);
		final StackLayout sl = new StackLayout();
		controlComposite.setLayout(sl);
		final Control baseControl = createControl(controlComposite);
		final Control createUnsetLabel = createUnsetLabel(controlComposite);
		final Button unsetButton = new Button(composite, SWT.PUSH);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, false).applyTo(unsetButton);
		unsetButton.addSelectionListener(new SelectionAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				final Setting setting = getVElement().getDomainModelReference().getIterator().next();
				Object value = null;
				if (!setting.isSet()) {
					sl.topControl = baseControl;
					unsetButton.setText("Unset");
					value = setting
						.getEStructuralFeature().getDefaultValue();
				}
				else {
					sl.topControl = createUnsetLabel;
					unsetButton.setText("Set");
					value = SetCommand.UNSET_VALUE;
				}
				final EditingDomain editingDomain = getEditingDomain(setting);
				editingDomain.getCommandStack().execute(
					SetCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), value));
				composite.layout();
			}
		});

		if (getVElement().getDomainModelReference().getIterator().next().isSet()) {
			sl.topControl = baseControl;
			unsetButton.setText("Unset");
		}
		else {
			sl.topControl = createUnsetLabel;
			unsetButton.setText("Set");
		}

		return composite;
	}

	private Control createUnsetLabel(Composite parent) {
		final Label unsetLabel = new Label(parent, SWT.NONE);
		unsetLabel.setBackground(parent.getBackground());
		unsetLabel.setText(getUnsetText());
		return unsetLabel;
	}

	protected abstract String getUnsetText();

	/**
	 * Set the provided validation color as the background for the provided control.
	 * 
	 * @param control the control to set the color on
	 * @param validationColor the validation color to set
	 */
	protected void setValidationColor(Control control, Color validationColor) {
		control.setBackground(validationColor);
	}

	@Override
	protected final void applyValidation() {

		Label validationIcon;
		Control editControl;
		switch (getControls().length) {
		case 2:
			validationIcon = Label.class.cast(getControls()[0]);
			editControl = getControls()[1];
			break;
		case 3:
			validationIcon = Label.class.cast(getControls()[1]);
			editControl = getControls()[2];
			break;
		default: // TODO log error ;
			return;
		}
		// triggered due to another validation rule before this control is rendered
		if (validationIcon == null || editControl == null) {
			return;
		}
		// validation rule triggered after the control was disposed
		if (validationIcon.isDisposed()) {
			return;
		}
		// no diagnostic set
		if (getVElement().getDiagnostic() == null) {
			return;
		}
		validationIcon.setImage(getValidationIcon(getVElement().getDiagnostic().getHighestSeverity()));
		validationIcon.setToolTipText(getVElement().getDiagnostic().getMessage());

		setValidationColor(editControl, getValidationBackgroundColor(getVElement().getDiagnostic()
			.getHighestSeverity()));
	}

	/**
	 * Creates the control itself.
	 * 
	 * @param parent the {@link Composite} to render onto
	 * @return the rendered control
	 */
	protected abstract Control createControl(Composite parent);
}
