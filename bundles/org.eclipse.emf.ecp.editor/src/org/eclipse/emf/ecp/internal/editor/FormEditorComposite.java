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

package org.eclipse.emf.ecp.internal.editor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.edit.ECPAbstractControl;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.util.ECPModelElementChangeListener;
import org.eclipse.emf.ecp.editor.IEditorCompositeProvider;
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

	private ECPModelElementChangeListener modelElementChangeListener;

	/**
	 * Default Constructor.
	 * 
	 * @param modelElementContext the {@link ECPControlContext}
	 */
	public FormEditorComposite(ECPControlContext modelElementContext) {
		this.modelElementContext = modelElementContext;
		addModelElementListener();
	}

	/**
	 * 
	 */
	private void addModelElementListener() {
		modelElementChangeListener = new ECPModelElementChangeListener(modelElementContext.getModelElement()) {

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

	private final ECPControlContext modelElementContext;

	private Map<EStructuralFeature, ECPAbstractControl> meControls = new LinkedHashMap<EStructuralFeature, ECPAbstractControl>();

	private Map<ECPAbstractControl, Diagnostic> valdiatedControls = new HashMap<ECPAbstractControl, Diagnostic>();

	private List<IItemPropertyDescriptor> leftColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

	private List<IItemPropertyDescriptor> rightColumnAttributes = new ArrayList<IItemPropertyDescriptor>();

	private List<IItemPropertyDescriptor> bottomAttributes = new ArrayList<IItemPropertyDescriptor>();

	private Composite leftColumnComposite;

	private Composite rightColumnComposite;

	private Composite bottomComposite;
	private ComposedAdapterFactory composedAdapterFactory;

	/** {@inheritDoc} */
	public Composite createUI(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setBackground(parent.getBackground());
		topComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		topComposite.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topComposite);

		sortAndOrderAttributes();
		if (!rightColumnAttributes.isEmpty()) {
			SashForm topSash = new SashForm(topComposite, SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(topSash);
			// toolkit.adapt(topSash, true, true);
			topSash.setSashWidth(4);
			leftColumnComposite = new Composite(topSash, SWT.NONE);
			rightColumnComposite = new Composite(topSash, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(5, 2, 5, 5)
				.applyTo(rightColumnComposite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(rightColumnComposite);
			int[] topWeights = { 50, 50 };
			topSash.setWeights(topWeights);
		} else {
			leftColumnComposite = new Composite(topComposite, SWT.NONE);
		}

		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(2, 5, 5, 5)
			.applyTo(leftColumnComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(leftColumnComposite);

		bottomComposite = new Composite(topComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(0, 0, 0, 0)
			.applyTo(bottomComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(bottomComposite);

		// Sort and order attributes
		// Create attributes
		createAttributes(leftColumnComposite, leftColumnAttributes);
		if (!rightColumnAttributes.isEmpty()) {
			createAttributes(rightColumnComposite, rightColumnAttributes);
		}
		createAttributes(bottomComposite, bottomAttributes);

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

	private void createAttributes(Composite column, List<IItemPropertyDescriptor> attributes) {
		Composite attributeComposite = new Composite(column, SWT.NONE);
		attributeComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(attributeComposite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.BEGINNING).indent(10, 0)
			.applyTo(attributeComposite);

		ECPControlFactory controlFactory = ECPControlFactory.INSTANCE;
		for (IItemPropertyDescriptor itemPropertyDescriptor : attributes) {

			SWTControl control = controlFactory.createControl(SWTControl.class, itemPropertyDescriptor,
				modelElementContext);
			if (control == null) {
				continue;
			}
			int numColumnSpan = 2;
			if (control.showLabel()) {
				Label label = new Label(attributeComposite, SWT.NONE);
				// label.setBackground(attributeComposite.getBackground());
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
		for (ECPAbstractControl control : meControls.values()) {
			control.dispose();
		}
		meControls.clear();
		modelElementChangeListener.remove();
		composedAdapterFactory.dispose();
	}

	/** {@inheritDoc} */
	public void updateLiveValidation() {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(modelElementContext.getModelElement());
		List<ECPAbstractControl> affectedControls = new ArrayList<ECPAbstractControl>();

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
			ECPAbstractControl meControl = meControls.get(childDiagnostic.getData().get(1));
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

		Map<ECPAbstractControl, Diagnostic> temp = new HashMap<ECPAbstractControl, Diagnostic>();
		temp.putAll(valdiatedControls);
		for (Map.Entry<ECPAbstractControl, Diagnostic> entry : temp.entrySet()) {
			ECPAbstractControl meControl = entry.getKey();
			if (!affectedControls.contains(meControl)) {
				valdiatedControls.remove(meControl);
				meControl.resetValidation();
			}
		}
	}

	/** {@inheritDoc} */
	public void focus() {
		leftColumnComposite.setFocus();
	}
}
