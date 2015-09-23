/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Stefan Dirix - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.emf2web.ui.wizard;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo;
import org.eclipse.emf.ecp.emf2web.ui.messages.Messages;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A page for a single generated file which's main purpose is to select a location for the file.
 *
 * @author Stefan Dirix
 *
 */
public class SelectLocationPage extends WizardPage {
	private DataBindingContext bindingContext;

	private final GenerationInfo generationInfo;
	private Text locationText;
	private Text generatedText;
	private Button btnWrap;
	private ControlDecoration requiredLocationDecoration;

	/**
	 * Constructor.
	 *
	 * @param generationInfo The {@link GenerationInfo} for which this page is responsible.
	 */
	public SelectLocationPage(GenerationInfo generationInfo) {
		super("wizardPage"); //$NON-NLS-1$
		this.generationInfo = generationInfo;

		setTitle(Messages.getString("SelectLocationPage.PageTitle")); //$NON-NLS-1$
		setDescription(Messages.getString("SelectLocationPage.PageDescription") + generationInfo.getNameProposal()); //$NON-NLS-1$
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		final Label lblLocation = new Label(container, SWT.NONE);
		lblLocation.setText(Messages.getString("SelectLocationPage.LocationLabel")); //$NON-NLS-1$
		new Label(container, SWT.NONE);

		locationText = new Text(container, SWT.BORDER);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		requiredLocationDecoration = new ControlDecoration(locationText, SWT.LEFT | SWT.TOP);
		final FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
			.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		requiredLocationDecoration.setImage(fieldDecoration.getImage());
		requiredLocationDecoration.setDescriptionText(Messages.getString("SelectLocationPage.RequiredDecorationText")); //$NON-NLS-1$
		requiredLocationDecoration.hide();

		final Button browseButton = new Button(container, SWT.NONE);
		browseButton.addSelectionListener(new BrowseButtonSelectionListener());
		browseButton.setText(Messages.getString("SelectLocationPage.BrowseButtonLabel")); //$NON-NLS-1$

		if (generationInfo.getWrapper() != null) {
			final Group grpWrapper = new Group(container, SWT.NONE);
			grpWrapper.setLayout(new GridLayout(1, false));
			grpWrapper.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			grpWrapper.setText(Messages.getString("SelectLocationPage.OptionalSettingsGroupText")); //$NON-NLS-1$

			btnWrap = new Button(grpWrapper, SWT.CHECK);
			btnWrap.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			final String buttonText = Messages.getString("SelectLocationPage.WrapButtonText") //$NON-NLS-1$
				+ generationInfo.getWrapper().getName();
			btnWrap.setText(buttonText);
		}

		final Group grpPreview = new Group(container, SWT.NONE);
		grpPreview.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpPreview.setText(Messages.getString("SelectLocationPage.ContentGroupText")); //$NON-NLS-1$

		generatedText = new Text(grpPreview, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		bindingContext = initDataBindings();

		WizardPageSupport.create(this, bindingContext);
	}

	/**
	 * Selection Listener for the Browse button.
	 */
	private class BrowseButtonSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			final ResourceDialog resourceDialog = new ResourceDialog(e.display.getActiveShell(),
				Messages.getString("SelectLocationPage.SelectLocationDialogTitle"), //$NON-NLS-1$
				SWT.SINGLE | SWT.SAVE);

			final int result = resourceDialog.open();
			if (result == Window.OK) {
				generationInfo.setLocation(resourceDialog.getURIs().get(0));
				bindingContext.updateTargets();
				requiredLocationDecoration.hide();
			}
		}
	}

	/**
	 * Validates the given location.
	 */
	private class LocationValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			if (value == null) {
				requiredLocationDecoration.show();
				return ValidationStatus.error(Messages.getString("SelectLocationPage.LocationError")); //$NON-NLS-1$
			}
			requiredLocationDecoration.hide();
			return ValidationStatus.ok();
		}
	}

	/**
	 * Converts a manually entered string to an {@link URI}.
	 */
	private class StringToURIConverter implements IConverter {
		@Override
		public Object getToType() {
			return URI.class;
		}

		@Override
		public Object getFromType() {
			return String.class;
		}

		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null || "".equals(fromObject)) { //$NON-NLS-1$
				return null;
			}
			final String path = (String) fromObject;
			return URI.createFileURI(path);
		}
	}

	/**
	 * Creates and initializes the used data bindings.
	 *
	 * @return The initialized {@link DataBindingContext}.
	 */
	protected DataBindingContext initDataBindings() {
		final DataBindingContext bindingContext = new DataBindingContext();

		final IObservableValue observeTextLocationTextObserveWidget = WidgetProperties.text(SWT.Modify)
			.observe(locationText);
		final IObservableValue locationGenerationInfoObserveValue = PojoProperties.value("location") //$NON-NLS-1$
			.observe(generationInfo);
		bindingContext.bindValue(
			observeTextLocationTextObserveWidget, locationGenerationInfoObserveValue, new UpdateValueStrategy()
				.setConverter(new StringToURIConverter()).setAfterConvertValidator(new LocationValidator()),
			null);

		final IObservableValue observeTextGeneratedTextObserveWidget = WidgetProperties.text(SWT.Modify)
			.observe(generatedText);
		final IObservableValue generatedStringGenerationInfoObserveValue = PojoProperties.value("generatedString") //$NON-NLS-1$
			.observe(generationInfo);
		bindingContext.bindValue(observeTextGeneratedTextObserveWidget, generatedStringGenerationInfoObserveValue, null,
			null);

		if (generationInfo.getWrapper() != null) {
			final IObservableValue observeSelectionBtnWrapObserveWidget = WidgetProperties.selection().observe(btnWrap);
			final IObservableValue wrapGenerationInfoObserveValue = PojoProperties.value("wrap") //$NON-NLS-1$
				.observe(generationInfo);
			bindingContext.bindValue(observeSelectionBtnWrapObserveWidget, wrapGenerationInfoObserveValue, null, null);
		}

		return bindingContext;
	}
}
