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
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.actions.AddReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.NewReferenceAction;
import org.eclipse.emf.ecp.edit.util.ECPModelElementChangeListener;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import java.net.URL;

/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a reference.
 * 
 * @author Eugen Neufeld
 * 
 */
public class LinkControl extends SingleControl {

	/**
	 * Constructor for a eenum control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public LinkControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	private Composite linkComposite;

	private Link hyperlink;

	private Label imageHyperlink;

	protected ComposedAdapterFactory composedAdapterFactory;

	// private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	// private ShortLabelProvider shortLabelProvider;

	private ECPModelElementChangeListener modelElementChangeListener;

	private Label unsetLabel;

	private StackLayout stackLayout;

	private Composite mainComposite;

	private Button[] buttons;

	protected AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	@Override
	protected void fillControlComposite(Composite composite) {
		int numColumns = 1 + getNumButtons();
		if (isEmbedded()) {
			numColumns = 1;
		}
		if (!isEmbedded() && getStructuralFeature().isUnsettable()) {
			numColumns++;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(0, 0).equalWidth(false).applyTo(composite);

		mainComposite = new Composite(composite, SWT.NONE);
		mainComposite.setBackground(composite.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(mainComposite);

		stackLayout = new StackLayout();
		mainComposite.setLayout(stackLayout);

		unsetLabel = new Label(mainComposite, SWT.NONE);
		// TODO language
		unsetLabel.setText("(Not Set)");//$NON-NLS-1$
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);

		linkComposite = new Composite(mainComposite, SWT.NONE);
		linkComposite.setLayout(new GridLayout(2, false));
		linkComposite.setBackground(composite.getBackground());

		createHyperlink();
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(linkComposite);
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			stackLayout.topControl = linkComposite;
		} else {
			stackLayout.topControl = unsetLabel;
		}
		if (!isEmbedded()) {
			buttons = createButtons(composite);
		}
	}

	protected int getNumButtons() {
		return 3;
	}

	protected Button[] createButtons(Composite composite) {
		Button[] buttons = new Button[3];
		buttons[0] = createButtonForAction(new DeleteReferenceAction(getModelElementContext(),
			getItemPropertyDescriptor(), getStructuralFeature()), composite);
		buttons[1] = createButtonForAction(new AddReferenceAction(getModelElementContext(),
			getItemPropertyDescriptor(), getStructuralFeature()), composite);
		buttons[2] = createButtonForAction(new NewReferenceAction(getModelElementContext(),
			getItemPropertyDescriptor(), getStructuralFeature()), composite);
		return buttons;
	}

	private void createHyperlink() {
		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		;
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
		// adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		// shortLabelProvider = new ShortLabelProvider(composedAdapterFactory);

		imageHyperlink = new Label(linkComposite, SWT.NONE);
		imageHyperlink.setBackground(linkComposite.getBackground());

		hyperlink = new Link(linkComposite, SWT.NONE);
		hyperlink.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_reference");
		hyperlink.setBackground(linkComposite.getBackground());
		hyperlink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				linkClicked((EObject) getModelValue().getValue());
			}

		});
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(hyperlink);

	}

	/**
	 * This code is called whenever the link of the link widget is clicked. You can overwrite this to change the
	 * behavior.
	 * 
	 * @param value the EObject that is linked
	 */
	protected void linkClicked(EObject value) {
		getModelElementContext().openInNewContext(value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean isEditable) {
		if (!isEmbedded()) {
			for (Button button : buttons) {
				button.setVisible(isEditable);
			}
		}
		mainComposite.getParent().layout();
	}

	@Override
	public Binding bindValue() {

		IObservableValue value = SWTObservables.observeText(hyperlink);

		Binding binding = getDataBindingContext().bindValue(value, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				updateChangeListener((EObject) value);
				return "<a>" + getLinkText(value) + "</a>";
			}
		});
		IObservableValue tooltipValue = SWTObservables.observeTooltipText(hyperlink);
		getDataBindingContext().bindValue(tooltipValue, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				return getLinkText(value);
			}
		});

		IObservableValue imageValue = SWTObservables.observeImage(imageHyperlink);
		getDataBindingContext().bindValue(imageValue, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				return getImage(value);
			}
		});

		return null;
	}

	protected Object getImage(Object value) {
		return Activator.getImage((URL) adapterFactoryItemDelegator.getImage(value));
	}

	protected Object getLinkText(Object value) {
		String linkName = adapterFactoryItemDelegator.getText(value);
		return linkName == null ? "" : linkName;
	}

	private void updateChangeListener(final EObject value) {
		if (modelElementChangeListener != null) {
			if (modelElementChangeListener.getTarget().equals(value)) {
				return;
			}
			modelElementChangeListener.remove();
			modelElementChangeListener = null;
		}
		if (value == null) {
			if (stackLayout.topControl != unsetLabel) {
				stackLayout.topControl = unsetLabel;
				mainComposite.layout();
			}

		} else {
			if (stackLayout.topControl != linkComposite) {
				stackLayout.topControl = linkComposite;
				mainComposite.layout();
			}

			modelElementChangeListener = new ECPModelElementChangeListener(value) {

				@Override
				public void onChange(Notification notification) {
					Display.getDefault().syncExec(new Runnable() {

						public void run() {
							getDataBindingContext().updateTargets();
							linkComposite.layout();

						}

					});

				}
			};

		}

	}

	@Override
	public void dispose() {
		// adapterFactoryItemDelegator.dispose();
		composedAdapterFactory.dispose();
		// shortLabelProvider.dispose();
		if (modelElementChangeListener != null) {
			modelElementChangeListener.remove();
		}
		hyperlink.dispose();
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		// TODO language
		return "No link set! Click to set link."; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset Link";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getControlForTooltip()
	 */
	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { hyperlink, imageHyperlink };
	}
}
