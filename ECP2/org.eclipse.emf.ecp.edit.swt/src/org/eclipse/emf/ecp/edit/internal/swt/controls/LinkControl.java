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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.actions.AddReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.NewReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.provider.ShortLabelProvider;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPObservableValue;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a reference.
 * 
 * @author Eugen Neufeld
 * 
 */
public class LinkControl extends SingleControl {
	private static final String EDITOR_ID = "org.eclipse.emf.ecp.editor"; //$NON-NLS-1$

	/**
	 * Constructor for a eenum control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public LinkControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	private Composite linkComposite;

	private Link hyperlink;

	private Label imageHyperlink;

	private EObject linkModelElement;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ShortLabelProvider shortLabelProvider;

	private ModelElementChangeListener modelElementChangeListener;

	private Label unsetLabel;

	private StackLayout stackLayout;

	private Composite mainComposite;

	private ModelElementChangeListener linkedModelElementChangeListener;

	private Button setNullButton;

	private Button addButton;

	private Button newButton;

	@Override
	protected void fillInnerComposite(Composite composite) {
		int numColumns = 4;
		if (isEmbedded()) {
			numColumns = 1;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(0, 0).equalWidth(false).applyTo(composite);

		mainComposite = new Composite(composite, SWT.NONE);
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

		createHyperlink();
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(linkComposite);
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			stackLayout.topControl = linkComposite;
		} else {
			stackLayout.topControl = unsetLabel;
		}
		if (!isEmbedded()) {
			setNullButton = createButtonForAction(new DeleteReferenceAction(getModelElementContext(),
				getItemPropertyDescriptor(), getStructuralFeature()), composite);
			addButton = createButtonForAction(new AddReferenceAction(getModelElementContext(),
				getItemPropertyDescriptor(), getStructuralFeature()), composite);
			newButton = createButtonForAction(new NewReferenceAction(getModelElementContext(),
				getItemPropertyDescriptor(), getStructuralFeature()), composite);
		}
	}

	private void createHyperlink() {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		shortLabelProvider = new ShortLabelProvider(composedAdapterFactory);
		modelElementChangeListener = new ModelElementChangeListener(getModelElementContext().getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						if (mainComposite.isDisposed()) {
							return;
						}
						if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
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

		imageHyperlink = new Label(linkComposite, SWT.NONE);

		// TODO: Reactivate
		// ModelElementClassTooltip.enableFor(imageHyperlink);
		hyperlink = new Link(linkComposite, SWT.NONE);
		String text = shortLabelProvider.getText(linkModelElement);
		hyperlink.setText("<a>" + text + "</a>");//$NON-NLS-1$ //$NON-NLS-2$
		hyperlink.setToolTipText(text);
		hyperlink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				getModelElementContext().openEditor(linkModelElement, EDITOR_ID);
			}

		});
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(hyperlink);

	}

	private void updateValues() {
		if (linkModelElement != null) {
			Image image = shortLabelProvider.getImage(linkModelElement);
			imageHyperlink.setImage(image);
			imageHyperlink.setData(linkModelElement.eClass());
			String text = shortLabelProvider.getText(linkModelElement);
			hyperlink.setText("<a>" + text + "</a>");//$NON-NLS-1$ //$NON-NLS-2$
			hyperlink.setToolTipText(text);
			hyperlink.update();
			// imageHyperlink.layout(true);

			// if (listener != null) {
			// hyperlink.removeHyperlinkListener(listener);
			// imageHyperlink.removeHyperlinkListener(listener);
			// listener = null;
			// }
			// if (listener == null) {
			// listener = new MEHyperLinkAdapter(linkModelElement, getModelElementContext());
			// hyperlink.addHyperlinkListener(listener);
			// imageHyperlink.addHyperlinkListener(listener);
			// }
		}
	}

	private void setLinkChangeListener() {

		if (linkedModelElementChangeListener != null) {
			linkedModelElementChangeListener.remove();
		}
		if (linkModelElement != null) {
			linkedModelElementChangeListener = new ModelElementChangeListener(linkModelElement) {

				@Override
				public void onChange(Notification notification) {
					Display.getDefault().syncExec(new Runnable() {

						public void run() {
							updateValues();

						}

					});

				}
			};
		}
	}

	private void setLinkModelElement() {
		if (!getStructuralFeature().isMany()) {
			linkModelElement = (EObject) getModelElementContext().getModelElement().eGet(getStructuralFeature());
		}
		updateValues();
	}

	@Override
	public void setEditable(boolean isEditable) {
		if (!isEmbedded()) {
			setNullButton.setVisible(isEditable);
			addButton.setVisible(isEditable);
			newButton.setVisible(isEditable);
		}
		mainComposite.getParent().layout();
	}

	@Override
	public void bindValue() {
		if (ECPObservableValue.class.isInstance(getModelValue())) {
			linkModelElement = (EObject) ((ECPObservableValue) getModelValue()).getValue();
		}
		setLinkModelElement();
		setLinkChangeListener();
	}

	@Override
	public void dispose() {
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
		shortLabelProvider.dispose();
		modelElementChangeListener.remove();
		if (linkedModelElementChangeListener != null) {
			linkedModelElementChangeListener.remove();
		}
		linkModelElement = null;
		hyperlink.dispose();
		super.dispose();
	}
}
