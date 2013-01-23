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
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.editor.controls.reference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.controls.AbstractMultiControl;
import org.eclipse.emf.ecp.editor.controls.ECPWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.LinkWidget;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;

/**
 * This is an implementation for a control allowing to set multiple references.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ReferenceMultiControl extends AbstractMultiControl {

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;
	private Action[] actions;

	/**
	 * Constructor for the {@link ReferenceMultiControl}.
	 */
	public ReferenceMultiControl() {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.controls.attribute.multi.AbstractMultiControl#getToolbarActions()
	 */
	@Override
	protected Action[] getToolbarActions() {
		if (actions == null) {
			Action addRef = new AddReferenceAction(getContext().getModelElement(), (EReference) getStructuralFeature(),
				getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider);
			Action newRef = new NewReferenceAction(getContext().getModelElement(), (EReference) getStructuralFeature(),
				getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider);
			actions = new Action[] { addRef, newRef };
		}
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.controls.attribute.multi.AbstractMultiControl#getWidget()
	 */
	@Override
	protected ECPWidget createWidget() {
		return new LinkWidget(getContext().getModelElement(), (EReference) getStructuralFeature(), getContext(),
			getItemPropertyDescriptor());
	}

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

	@Override
	public void dispose() {
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
		super.dispose();
	}

	@Override
	protected int getPriority() {
		return 1;
	}
}
