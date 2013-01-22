/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.mecontrols.AbstractControl;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.ecp.internal.editor.widgets.ECPWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.LinkWidget;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
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
 * @author Eugen Neufeld
 */
public class ReferenceControl extends AbstractControl {

	private Label labelWidgetImage;

	private ControlDecoration controlDecoration;

	private Control control;

	private ModelElementChangeListener modelElementChangeListener;

	private ECPWidget widget;

	private ComposedAdapterFactory composedAdapterFactory;

	protected AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<? extends EStructuralFeature> getEStructuralFeatureType() {
		return EReference.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#isMulti()
	 */
	@Override
	protected boolean isMulti() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#createControl(org.eclipse.swt.widgets.Composite,
	 * int)
	 */
	@Override
	protected Control createControl(Composite parent, final int style) {
		final Composite composite = getToolkit().createComposite(parent, style);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(2, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

		labelWidgetImage = getToolkit().createLabel(composite, "    ");
		labelWidgetImage.setBackground(parent.getBackground());
		final Composite linkComposite = getToolkit().createComposite(composite, SWT.NONE);
		linkComposite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(linkComposite);
		createWidgetControl(linkComposite, style);

		modelElementChangeListener = new ModelElementChangeListener(getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				if (notification.getFeature() == getStructuralFeature()) {
					createWidgetControl(linkComposite, style);
				}
			}
		};

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		for (Action action : initActions()) {
			createButtonForAction(action, composite);
		}

		return composite;
	}

	private void createWidgetControl(final Composite composite, final int style) {
		if (control != null) {
			control.dispose();
			controlDecoration.dispose();
		}
		final EObject opposite = (EObject) getModelElement().eGet(getStructuralFeature());
		if (opposite != null) {
			widget = getWidget(opposite);
			control = widget.createWidget(getToolkit(), composite, style);
			widget.setEditable(isEditable());
		} else {
			control = getToolkit().createLabel(composite, "(Not Set)");
			control.setBackground(composite.getBackground());
			control.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		}

		controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
		controlDecoration.setDescriptionText("Invalid input");
		controlDecoration.setShowHover(true);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();

		// IObservableValue model = EMFEditObservables.observeValue(getContext().getEditingDomain(), getModelElement(),
		// getStructuralFeature());
		// widget.bindValue(model, controlDecoration);

		composite.layout();
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

	/**
	 * @return
	 */
	private ECPWidget getWidget(EObject opposite) {
		return new LinkWidget(getModelElement(), opposite, (EReference) getStructuralFeature(), getContext());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getPriority()
	 */
	@Override
	protected int getPriority() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (control != null) {
			control.dispose();
			controlDecoration.dispose();
		}
		labelWidgetImage.dispose();
		modelElementChangeListener.remove();
		if (widget != null) {
			widget.dispose();
		}
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
	}

}
