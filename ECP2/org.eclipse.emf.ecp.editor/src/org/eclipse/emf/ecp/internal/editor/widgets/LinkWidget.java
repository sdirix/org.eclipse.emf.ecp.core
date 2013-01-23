/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
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

package org.eclipse.emf.ecp.internal.editor.widgets;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.controls.ECPObservableValue;
import org.eclipse.emf.ecp.editor.controls.ECPWidget;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.ecp.internal.editor.controls.reference.MEHyperLinkAdapter;
import org.eclipse.emf.ecp.internal.editor.labelprovider.ShortLabelProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * @author Eugen Neufeld
 */
public class LinkWidget extends ECPWidget {

	private Composite linkComposite;

	private Hyperlink hyperlink;

	private ImageHyperlink imageHyperlink;

	private EObject modelElement;

	private EObject linkModelElement;

	private EReference eReference;

	private EditModelElementContext context;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ShortLabelProvider shortLabelProvider;

	private ModelElementChangeListener modelElementChangeListener;

	private IHyperlinkListener listener;

	private Label unsetLabel;

	private StackLayout stackLayout;

	private Composite mainComposite;

	private ModelElementChangeListener modelElementChangeListener2;

	private ECPObservableValue observableValue;

	/**
	 * @param dbc
	 */
	public LinkWidget(EObject modelElement, EReference eReference, EditModelElementContext context,
		IItemPropertyDescriptor propertyDescriptor) {

		this.modelElement = modelElement;
		this.eReference = eReference;
		this.context = context;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.
	 * FormToolkit
	 * , org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public Control createWidget(FormToolkit toolkit, Composite composite, int style) {
		mainComposite = toolkit.createComposite(composite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(mainComposite);

		stackLayout = new StackLayout();
		mainComposite.setLayout(stackLayout);

		unsetLabel = toolkit.createLabel(mainComposite, "Not set!");
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(toolkit.getColors().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);

		linkComposite = toolkit.createComposite(mainComposite);
		linkComposite.setLayout(new GridLayout(2, false));

		createHyperlink(toolkit, mainComposite);

		if (modelElement.eIsSet(eReference)) {
			stackLayout.topControl = linkComposite;
		} else {
			stackLayout.topControl = unsetLabel;
		}
		mainComposite.layout();

		return mainComposite;
	}

	private void createHyperlink(FormToolkit toolkit, final Composite parent) {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		shortLabelProvider = new ShortLabelProvider(composedAdapterFactory);
		// decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();

		// labelProvider = new DecoratingLabelProvider(adapterFactoryLabelProvider,
		// decoratorManager.getLabelDecorator());
		// labelProviderListener = new ILabelProviderListener() {
		// public void labelProviderChanged(LabelProviderChangedEvent event) {
		// imageHyperlink.setImage(labelProvider.getImage(linkModelElement));
		// }
		// };
		// labelProvider.addListener(labelProviderListener);
		modelElementChangeListener = new ModelElementChangeListener(modelElement) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						if (modelElement.eIsSet(eReference)) {
							stackLayout.topControl = linkComposite;
							setLinkModelElement();
							setLinkChangeListener();
						} else {
							stackLayout.topControl = unsetLabel;
							linkModelElement = null;
						}
						mainComposite.layout();
					}

				});

			}
		};

		imageHyperlink = toolkit.createImageHyperlink(linkComposite, SWT.NONE);

		// TODO: Reactivate
		// ModelElementClassTooltip.enableFor(imageHyperlink);
		hyperlink = toolkit.createHyperlink(linkComposite, shortLabelProvider.getText(linkModelElement), SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(hyperlink);

	}

	private void updateValues() {
		if (linkModelElement != null) {
			Image image = shortLabelProvider.getImage(linkModelElement);
			imageHyperlink.setImage(image);
			imageHyperlink.setData(linkModelElement.eClass());
			String text = shortLabelProvider.getText(linkModelElement);
			hyperlink.setText(text);
			hyperlink.setToolTipText(text);
			hyperlink.update();
			imageHyperlink.layout(true);

			if (listener != null) {
				hyperlink.removeHyperlinkListener(listener);
				imageHyperlink.removeHyperlinkListener(listener);
				listener = null;
			}
			if (listener == null) {
				listener = new MEHyperLinkAdapter(linkModelElement, modelElement, eReference.getName(), context);
				hyperlink.addHyperlinkListener(listener);
				imageHyperlink.addHyperlinkListener(listener);
			}
		}
	}

	private void setLinkChangeListener() {

		if (modelElementChangeListener2 != null) {
			modelElementChangeListener2.remove();
		}
		if (linkModelElement != null) {
			modelElementChangeListener2 = new ModelElementChangeListener(linkModelElement) {

				@Override
				public void onChange(Notification notification) {
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							updateValues();

						}

					});

				}
			};
		}
	}

	private void setLinkModelElement() {
		if (eReference.isMany()) {
			linkModelElement = ((EList<EObject>) modelElement.eGet(eReference)).get(observableValue.getIndex());
		} else {
			linkModelElement = (EObject) modelElement.eGet(eReference);
		}
		updateValues();
	}

	@Override
	public void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration) {
		if (ECPObservableValue.class.isInstance(modelValue)) {
			observableValue = (ECPObservableValue) modelValue;
		}
		setLinkModelElement();
		setLinkChangeListener();
		// IObservableValue targetValue = SWTObservables.observeText(hyperlink);
		// context.getDataBindingContext().bindValue(targetValue, modelValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		// a hyperlink is never editable
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return linkComposite;
	}

	@Override
	public void dispose() {
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
		shortLabelProvider.dispose();
		modelElementChangeListener.remove();
		if (modelElementChangeListener2 != null) {
			modelElementChangeListener2.remove();
		}
		hyperlink.dispose();
	}
}
