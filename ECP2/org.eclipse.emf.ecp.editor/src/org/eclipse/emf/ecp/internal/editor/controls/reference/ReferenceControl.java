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

package org.eclipse.emf.ecp.internal.editor.controls.reference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.controls.AbstractSingleControl;
import org.eclipse.emf.ecp.editor.controls.ECPWidget;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.ecp.internal.editor.widgets.LinkWidget;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation for a single reference control.
 * 
 * @author Eugen Neufeld
 */
public class ReferenceControl extends AbstractSingleControl {

	private ModelElementChangeListener modelElementChangeListener;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ImageHyperlink deleteLink;

	private MEHyperLinkDeleteAdapter linkAdapter;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<EReference> getEStructuralFeatureType() {
		return EReference.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getSupportedClassType() {
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#getPriority()
	 */
	@Override
	protected int getPriority() {
		return 1;
	}

	@Override
	public void dispose() {
		super.dispose();
		modelElementChangeListener.remove();
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractSingleControl#getNumberOfAddtionalElements()
	 */
	@Override
	protected int getNumberOfAddtionalElements() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.controls.AbstractSingleControl#createControlActions(org.eclipse.swt.widgets.Composite
	 * )
	 */
	@Override
	protected void createControlActions(Composite composite) {
		createDeleteAction(composite);

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		for (Action action : initActions()) {
			createButtonForAction(action, composite);
		}
	}

	/**
	 * Creates a delete action as a link.
	 * 
	 * @param composite the composite to create the action on
	 */
	protected void createDeleteAction(Composite composite) {
		deleteLink = getToolkit().createImageHyperlink(composite, SWT.NONE);
		Image deleteImage = null;

		deleteImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);

		deleteLink.setImage(deleteImage);

		linkAdapter = new MEHyperLinkDeleteAdapter(getContext().getModelElement(), (EReference) getStructuralFeature(),
			(EObject) getContext().getModelElement().eGet(getStructuralFeature()), getContext());
		deleteLink.addMouseListener(linkAdapter);
	}

	/**
	 * Creates the actions for the control.
	 * 
	 * @return list of actions
	 */
	private List<Action> initActions() {
		List<Action> result = new ArrayList<Action>();
		AddReferenceAction addAction = new AddReferenceAction(getContext().getModelElement(),
			(EReference) getStructuralFeature(), getItemPropertyDescriptor(), getContext(), getShell(),
			adapterFactoryLabelProvider);
		result.add(addAction);
		ReferenceAction newAction = new NewReferenceAction(getContext().getModelElement(),
			(EReference) getStructuralFeature(), getItemPropertyDescriptor(), getContext(), getShell(),
			adapterFactoryLabelProvider);
		result.add(newAction);
		return result;
	}

	/**
	 * Creates a button for an action.
	 * 
	 * @param action the action to create the button from
	 * @param composite the composite to create the button on
	 */
	protected void createButtonForAction(final Action action, Composite composite) {
		Button selectButton = getToolkit().createButton(composite, "", SWT.PUSH);
		selectButton.setImage(action.getImageDescriptor().createImage());
		selectButton.setToolTipText(action.getToolTipText());
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractSingleControl#getWidget()
	 */
	@Override
	protected ECPWidget getWidget() {
		return new LinkWidget(getContext().getModelElement(), (EReference) getStructuralFeature(), getContext(),
			getItemPropertyDescriptor());
	}
}
