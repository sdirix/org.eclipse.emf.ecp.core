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

package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.ModelElementChangeListener;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.MEHyperLinkAdapter;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.MEHyperLinkDeleteAdapter;
import org.eclipse.emf.ecp.internal.editor.labelprovider.ShortLabelProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
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

	private ILabelProvider labelProvider;

	private ILabelProviderListener labelProviderListener;

	private ImageHyperlink imageHyperlink;

	private EObject modelElement;

	private EObject linkModelElement;

	private EReference eReference;

	private EditModelElementContext context;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ShortLabelProvider shortLabelProvider;

	private ModelElementChangeListener modelElementChangeListener;

	private ImageHyperlink deleteLink;

	private MEHyperLinkDeleteAdapter linkAdapter;

	private IDecoratorManager decoratorManager;

	private IHyperlinkListener listener;

	/**
	 * @param dbc
	 */
	public LinkWidget(EObject modelElement, EObject linkModelElement, EReference eReference,
		EditModelElementContext context) {

		this.modelElement = modelElement;
		this.linkModelElement = linkModelElement;
		this.eReference = eReference;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.
	 * FormToolkit
	 * , org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public Control createWidget(FormToolkit toolkit, Composite composite, int style) {
		linkComposite = toolkit.createComposite(composite);
		linkComposite.setLayout(new GridLayout(3, false));

		createHyperlink(toolkit, composite);
		createDeleteAction(toolkit);
		return linkComposite;
	}

	private void createDeleteAction(FormToolkit toolkit) {
		deleteLink = toolkit.createImageHyperlink(linkComposite, SWT.NONE);
		Image deleteImage = null;

		deleteImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);

		deleteLink.setImage(deleteImage);

		linkAdapter = new MEHyperLinkDeleteAdapter(modelElement, eReference, linkModelElement, context);
		deleteLink.addMouseListener(linkAdapter);
	}

	private void createHyperlink(FormToolkit toolkit, final Composite parent) {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		shortLabelProvider = new ShortLabelProvider(composedAdapterFactory);
		decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();

		labelProvider = new DecoratingLabelProvider(adapterFactoryLabelProvider, decoratorManager.getLabelDecorator());
		labelProviderListener = new ILabelProviderListener() {
			public void labelProviderChanged(LabelProviderChangedEvent event) {
				imageHyperlink.setImage(labelProvider.getImage(linkModelElement));
			}
		};
		labelProvider.addListener(labelProviderListener);
		modelElementChangeListener = new ModelElementChangeListener(linkModelElement) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						if (hyperlink != null && !hyperlink.isDisposed()) {

							String text = shortLabelProvider.getText(linkModelElement);
							hyperlink.setText(text);
							hyperlink.setToolTipText(text);
							linkComposite.layout(true);
							parent.getParent().layout(true);
						}
					}

				});

			}
		};

		Image image = labelProvider.getImage(linkModelElement);
		imageHyperlink = toolkit.createImageHyperlink(linkComposite, SWT.NONE);
		imageHyperlink.setImage(image);
		imageHyperlink.setData(linkModelElement.eClass());
		// TODO: Reactivate
		// ModelElementClassTooltip.enableFor(imageHyperlink);
		hyperlink = toolkit.createHyperlink(linkComposite, shortLabelProvider.getText(linkModelElement), SWT.NONE);
		hyperlink.setToolTipText(shortLabelProvider.getText(linkModelElement));
		listener = new MEHyperLinkAdapter(linkModelElement, modelElement, eReference.getName(), context);
		hyperlink.addHyperlinkListener(listener);
		imageHyperlink.addHyperlinkListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		// a hyperlink is never editable
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#getControl()
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
		labelProvider.removeListener(labelProviderListener);
		labelProvider.dispose();
		modelElementChangeListener.remove();
		deleteLink.dispose();
		hyperlink.removeHyperlinkListener(listener);
		hyperlink.dispose();
		decoratorManager.dispose();
	}
}
