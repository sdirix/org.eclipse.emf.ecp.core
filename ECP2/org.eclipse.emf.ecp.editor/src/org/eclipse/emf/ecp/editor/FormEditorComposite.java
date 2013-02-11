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

package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.edit.AbstractControl;
import org.eclipse.emf.ecp.edit.ControlFactory;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.ecp.internal.editor.descriptor.AnnotationHiddenDescriptor;
import org.eclipse.emf.ecp.internal.editor.descriptor.AnnotationPositionDescriptor;
import org.eclipse.emf.ecp.internal.editor.descriptor.AnnotationPriorityDescriptor;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eugen Neufeld
 */
public class FormEditorComposite implements IEditorCompositeProvider {

	private ModelElementChangeListener modelElementChangeListener;

	/**
	 * Constructor to initialize the {@link FormToolkit} on its own.
	 * 
	 * @param modelElementContext the {@link EditModelElementContext}
	 * @param shell the shell used for callbacks
	 */
	public FormEditorComposite(EditModelElementContext modelElementContext, Shell shell) {
		this(modelElementContext, new FormToolkit(shell.getDisplay()));
	}

	/**
	 * Constructor where the {@link FormToolkit} is provided.
	 * 
	 * @param modelElementContext the {@link EditModelElementContext}
	 * @param toolkit the {@link FormToolkit}
	 */
	public FormEditorComposite(EditModelElementContext modelElementContext, FormToolkit toolkit) {
		this.modelElementContext = modelElementContext;
		this.toolkit = toolkit;
		addModelElementListener();
	}

	/**
	 * 
	 */
	private void addModelElementListener() {
		modelElementChangeListener = new ModelElementChangeListener(modelElementContext.getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						updateLiveValidation();
					}
				});
			}
		};
	}

	private final FormToolkit toolkit;

	private final EditModelElementContext modelElementContext;

	private Map<EStructuralFeature, AbstractControl> meControls = new LinkedHashMap<EStructuralFeature, AbstractControl>();

	private Map<AbstractControl, Diagnostic> valdiatedControls = new HashMap<AbstractControl, Diagnostic>();

	private List<IItemPropertyDescriptor> leftColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

	private List<IItemPropertyDescriptor> rightColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

	private List<IItemPropertyDescriptor> bottomAttributes = new ArrayList<IItemPropertyDescriptor>();

	private Composite leftColumnComposite;

	private Composite rightColumnComposite;

	private Composite bottomComposite;
	private ComposedAdapterFactory composedAdapterFactory;

	/** {@inheritDoc} */
	public Composite createUI(Composite parent) {
		Composite topComposite = toolkit.createComposite(parent);
		topComposite.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topComposite);

		sortAndOrderAttributes();
		if (!rightColumnAttributes.isEmpty()) {
			SashForm topSash = new SashForm(topComposite, SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topSash);
			toolkit.adapt(topSash, true, true);
			topSash.setSashWidth(4);
			leftColumnComposite = toolkit.createComposite(topSash, SWT.NONE);
			rightColumnComposite = toolkit.createComposite(topSash, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(5, 2, 5, 5)
				.applyTo(rightColumnComposite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(rightColumnComposite);
			int[] topWeights = { 50, 50 };
			topSash.setWeights(topWeights);
		} else {
			leftColumnComposite = toolkit.createComposite(topComposite, SWT.NONE);
		}

		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(2, 5, 5, 5)
			.applyTo(leftColumnComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(leftColumnComposite);

		bottomComposite = toolkit.createComposite(topComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(0, 0, 0, 0)
			.applyTo(bottomComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(bottomComposite);

		// Sort and order attributes
		// Create attributes
		createAttributes(toolkit, leftColumnComposite, leftColumnAttributes);
		if (!rightColumnAttributes.isEmpty()) {
			createAttributes(toolkit, rightColumnComposite, rightColumnAttributes);
		}
		createAttributes(toolkit, bottomComposite, bottomAttributes);

		updateLiveValidation();
		return topComposite;
	}

	/**
	 * Filters attributes marked with "hidden=true" annotation.
	 * 
	 * @param propertyDescriptors
	 *            property descriptors to filter
	 */
	private void filterHiddenAttributes(Collection<IItemPropertyDescriptor> propertyDescriptors) {
		Iterator<IItemPropertyDescriptor> iterator = propertyDescriptors.iterator();

		AnnotationHiddenDescriptor visibilityDescriptor = new AnnotationHiddenDescriptor();

		while (iterator.hasNext()) {
			IItemPropertyDescriptor descriptor = iterator.next();

			if (visibilityDescriptor.getValue(descriptor, modelElementContext.getModelElement())) {
				iterator.remove();
			}
		}
	}

	private void sortAndOrderAttributes() {

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		List<IItemPropertyDescriptor> propertyDescriptors = adapterFactoryItemDelegator
			.getPropertyDescriptors(modelElementContext.getModelElement());
		if (propertyDescriptors != null) {
			filterHiddenAttributes(propertyDescriptors);
			AnnotationPositionDescriptor positionDescriptor = new AnnotationPositionDescriptor();
			for (IItemPropertyDescriptor itemPropertyDescriptor : propertyDescriptors) {
				String value = positionDescriptor.getValue(itemPropertyDescriptor,
					modelElementContext.getModelElement());
				if (value.equals("left")) {
					leftColumnAttributes.add(itemPropertyDescriptor);
				} else if (value.equals("right")) {
					rightColumnAttributes.add(itemPropertyDescriptor);
				} else if (value.equals("bottom")) {
					bottomAttributes.add(itemPropertyDescriptor);
				} else {
					leftColumnAttributes.add(itemPropertyDescriptor);
				}
			}

			final HashMap<IItemPropertyDescriptor, Double> priorityMap = new HashMap<IItemPropertyDescriptor, Double>();
			AnnotationPriorityDescriptor priorityDescriptor = new AnnotationPriorityDescriptor();
			for (IItemPropertyDescriptor itemPropertyDescriptor : propertyDescriptors) {
				priorityMap.put(itemPropertyDescriptor,
					priorityDescriptor.getValue(itemPropertyDescriptor, modelElementContext.getModelElement()));
			}

			Comparator<IItemPropertyDescriptor> comparator = new Comparator<IItemPropertyDescriptor>() {
				public int compare(IItemPropertyDescriptor o1, IItemPropertyDescriptor o2) {
					return Double.compare(priorityMap.get(o1), priorityMap.get(o2));
				}
			};
			Collections.sort(leftColumnAttributes, comparator);
			Collections.sort(rightColumnAttributes, comparator);
			Collections.sort(bottomAttributes, comparator);

		}

	}

	private void createAttributes(FormToolkit toolkit, Composite column, List<IItemPropertyDescriptor> attributes) {
		Composite attributeComposite = toolkit.createComposite(column);
		attributeComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(attributeComposite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.BEGINNING).indent(10, 0)
			.applyTo(attributeComposite);

		ControlFactory controlFactory = ControlFactory.INSTANCE;
		for (IItemPropertyDescriptor itemPropertyDescriptor : attributes) {

			AbstractControl<Composite> control = controlFactory.createControl(attributeComposite,
				itemPropertyDescriptor, modelElementContext);
			if (control == null) {
				continue;
			}
			int numColumnSpan = 2;
			if (control.showLabel()) {
				Label label = new Label(attributeComposite, SWT.NONE);
				label.setBackground(attributeComposite.getBackground());
				label.setText(itemPropertyDescriptor.getDisplayName(modelElementContext.getModelElement()));
				label.setToolTipText(itemPropertyDescriptor.getDescription(modelElementContext.getModelElement()));
				GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
				numColumnSpan--;
			}
			Composite composite = control.createControl(attributeComposite);

			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(numColumnSpan, 1)
				.applyTo(composite);
			meControls.put(
				(EStructuralFeature) itemPropertyDescriptor.getFeature(modelElementContext.getModelElement()), control);

		}
	}

	/** {@inheritDoc} */
	public void dispose() {
		for (AbstractControl control : meControls.values()) {
			control.dispose();
		}
		meControls.clear();
		modelElementChangeListener.remove();
		composedAdapterFactory.dispose();
	}

	/** {@inheritDoc} */
	public void updateLiveValidation() {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(modelElementContext.getModelElement());
		List<AbstractControl> affectedControls = new ArrayList<AbstractControl>();

		for (Iterator<Diagnostic> i = diagnostic.getChildren().iterator(); i.hasNext();) {
			Diagnostic childDiagnostic = i.next();
			Object object = childDiagnostic.getData().get(0);
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
				if (eObject != modelElementContext.getModelElement()) {
					continue;
				}
			}
			if (childDiagnostic.getData().size() < 2) {
				continue;
			}
			AbstractControl meControl = meControls.get(childDiagnostic.getData().get(1));
			if (meControl == null) {
				continue;
			}
			affectedControls.add(meControl);
			if (valdiatedControls.containsKey(meControl)) {
				if (childDiagnostic.getSeverity() != valdiatedControls.get(meControl).getSeverity()) {
					meControl.handleValidation(childDiagnostic);
					valdiatedControls.put(meControl, childDiagnostic);
				}
			} else {
				meControl.handleValidation(childDiagnostic);
				valdiatedControls.put(meControl, childDiagnostic);
			}

		}

		Map<AbstractControl, Diagnostic> temp = new HashMap<AbstractControl, Diagnostic>();
		temp.putAll(valdiatedControls);
		for (Map.Entry<AbstractControl, Diagnostic> entry : temp.entrySet()) {
			AbstractControl meControl = entry.getKey();
			if (!affectedControls.contains(meControl)) {
				valdiatedControls.remove(meControl);
				meControl.resetValidation();
			}
		}
	}

	/** {@inheritDoc} */
	public void focus() {
		// set keyboard focus on the first Text control
		// for (AbstractControl meControl : meControls.values()) {
		// if (meControl instanceof METextControl) {
		// ((METextControl) meControl).setFocus();
		// return;
		// }
		// }
		leftColumnComposite.setFocus();
	}

}
