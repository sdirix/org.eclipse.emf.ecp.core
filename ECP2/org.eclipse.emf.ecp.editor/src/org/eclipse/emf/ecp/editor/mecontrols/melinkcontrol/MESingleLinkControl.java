/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.ModelElementChangeListener;
import org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI Control for the ME reference single links.
 * 
 * @author helming
 */
public class MESingleLinkControl extends AbstractMEControl {

	private int style;

	private MELinkControl meControl;

	private Label labelWidget;

	private ModelElementChangeListener modelElementChangeListener;

	private ComposedAdapterFactory composedAdapterFactory;

	protected AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	/**
	 * Standard Constructor.
	 */
	public MESingleLinkControl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<? extends EStructuralFeature> getEStructuralFeatureType() {
		return EReference.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(final Composite parent, int style) {
		final Composite composite = getToolkit().createComposite(parent, style);

		GridLayoutFactory.fillDefaults().spacing(0, 0).numColumns(3).equalWidth(false).applyTo(composite);

		this.style = style;
		final Composite linkArea = getToolkit().createComposite(composite);
		linkArea.setLayout(new FillLayout());
		updateLink(linkArea, composite);

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		for (Action action : initActions()) {
			createButtonForAction(action, composite);
		}

		modelElementChangeListener = new ModelElementChangeListener(getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				if (notification.getFeature() == getStructuralFeature()) {
					updateLink(linkArea, composite);
				}

			}
		};

		return composite;
	}

	/**
	 * Creates the actions for the control.
	 * 
	 * @return list of actions
	 */
	protected List<Action> initActions() {
		List<Action> result = new ArrayList<Action>();
		AddReferenceAction addAction = new AddReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider);
		result.add(addAction);
		ReferenceAction newAction = new NewReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider);
		result.add(newAction);
		return result;
	}

	/**
	 * Creates a button for an action.
	 * 
	 * @param action
	 *            the action
	 */
	private void createButtonForAction(final Action action, Composite composite) {
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

	private void updateLink(Composite linkArea, Composite composite) {
		if (meControl != null) {
			meControl.dispose();
		}
		if (labelWidget != null) {
			labelWidget.dispose();
		}

		EObject opposite = (EObject) getModelElement().eGet(getStructuralFeature());
		if (opposite != null) {
			MELinkControlFactory meLinkControlFactory = MELinkControlFactory.getInstance();
			meControl = meLinkControlFactory.createMELinkControl(getItemPropertyDescriptor(), opposite,
				getModelElement(), getContext());
			meControl.createControl(linkArea, style, getItemPropertyDescriptor(), opposite, getModelElement(),
				getToolkit(), getContext());
		} else {
			labelWidget = getToolkit().createLabel(linkArea, "(Not Set)");
			labelWidget.setBackground(composite.getBackground());
			labelWidget.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		}
		linkArea.layout(true);
		composite.layout(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		modelElementChangeListener.remove();
		if (meControl != null) {
			meControl.dispose();
		}
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return EObject.class;
	}

	@Override
	protected boolean isMulti() {
		return false;
	}
	// @Override
	// public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement)
	// {
	// Object feature = itemPropertyDescriptor.getFeature(modelElement);
	// if (feature instanceof EReference && !((EReference)feature).isMany()
	// && EObject.class.isAssignableFrom(((EReference)feature).getEType().getInstanceClass()))
	// {
	// return PRIORITY;
	// }
	// return AbstractMEControl.DO_NOT_RENDER;
	// }
}
