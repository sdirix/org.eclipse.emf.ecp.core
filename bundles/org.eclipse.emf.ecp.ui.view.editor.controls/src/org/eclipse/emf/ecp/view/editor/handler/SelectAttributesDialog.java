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
package org.eclipse.emf.ecp.view.editor.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog for selecting the attributes for which controls should be generated.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SelectAttributesDialog extends Dialog {

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryLabelProvider labelProvider;
	private final VView view;
	private final Set<EStructuralFeature> selectedFeatures = new LinkedHashSet<EStructuralFeature>();
	private final EClass rootClass;

	/**
	 * Constructor.
	 * 
	 * @param view for identifying the attributes which are not referenced yet
	 * @param rootClass the rootClass of the view
	 * @param parentShell the shell for creating the dialog
	 */
	public SelectAttributesDialog(VView view, EClass rootClass, Shell parentShell) {
		super(parentShell);
		this.view = view;
		this.rootClass = rootClass;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		((GridLayout) composite.getLayout()).numColumns = 2;

		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });

		labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		final Button bUnreferenced = new Button(composite, SWT.CHECK);
		bUnreferenced.setText("Show only unreferenced Attributes?"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).span(2, 1)
			.applyTo(bUnreferenced);

		final Label labelAttributes = new Label(composite, SWT.NONE);
		labelAttributes.setText("Select Attributes"); //$NON-NLS-1$

		final CheckboxTableViewer tvAttributes = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		tvAttributes.setLabelProvider(labelProvider);
		tvAttributes.setContentProvider(ArrayContentProvider.getInstance());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).hint(SWT.DEFAULT, 200)
			.applyTo(tvAttributes.getControl());

		tvAttributes.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				final EStructuralFeature object = (EStructuralFeature) event.getElement();
				if (event.getChecked()) {
					selectedFeatures.add(object);
				} else {
					selectedFeatures.remove(object);
				}

			}
		});

		List<EStructuralFeature> attributes = null;
		if (!bUnreferenced.getSelection()) {
			attributes = rootClass.getEAllStructuralFeatures();
		} else {
			attributes = getUnreferencedSegmentAttributes(rootClass);
		}
		tvAttributes.setInput(attributes);

		bUnreferenced.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				List<EStructuralFeature> attributes = null;
				if (!bUnreferenced.getSelection()) {
					attributes = rootClass.getEAllStructuralFeatures();
				} else {
					attributes = getUnreferencedSegmentAttributes(rootClass);
				}
				tvAttributes.setInput(attributes);
			}

		});

		final Composite compositeButtons = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compositeButtons);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).span(2, 1)
			.applyTo(compositeButtons);

		final Button bSelectAll = new Button(compositeButtons, SWT.PUSH);
		bSelectAll.setText("Select All"); //$NON-NLS-1$
		bSelectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				final List<EStructuralFeature> segments = (List<EStructuralFeature>) tvAttributes.getInput();
				tvAttributes.setAllChecked(true);
				selectedFeatures.addAll(segments);
			}

		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(bSelectAll);
		final Button bDeSelectAll = new Button(compositeButtons, SWT.PUSH);
		bDeSelectAll.setText("Deselect All"); //$NON-NLS-1$
		bDeSelectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				final List<EStructuralFeature> segments = (List<EStructuralFeature>) tvAttributes.getInput();
				tvAttributes.setAllChecked(false);
				selectedFeatures.removeAll(segments);
			}

		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(bDeSelectAll);

		return composite;
	}

	@Override
	public boolean close() {
		labelProvider.dispose();
		composedAdapterFactory.dispose();
		return super.close();
	}

	private List<EStructuralFeature> getUnreferencedSegmentAttributes(EClass eClass) {
		final List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		final List<EStructuralFeature> allStructuralFeatures = new ArrayList<EStructuralFeature>(
			eClass.getEAllStructuralFeatures());

		final TreeIterator<EObject> eAllContents = view.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject eObject = eAllContents.next();
			if (org.eclipse.emf.ecp.view.spi.model.VControl.class.isInstance(eObject)) {
				final org.eclipse.emf.ecp.view.spi.model.VControl control = (org.eclipse.emf.ecp.view.spi.model.VControl) eObject;
				final VDomainModelReference domainModelReference = control.getDomainModelReference();
				if (domainModelReference == null) {
					continue;
				}
				final Iterator<EStructuralFeature> structuralFeatureIterator = domainModelReference
					.getEStructuralFeatureIterator();
				while (structuralFeatureIterator.hasNext()) {
					final EStructuralFeature feature = structuralFeatureIterator.next();
					if (feature != null && feature.getEContainingClass().equals(eClass)) {
						result.add(feature);
					}
				}
			}

		}

		allStructuralFeatures.removeAll(result);
		return allStructuralFeatures;
	}

	/** @return the set of features selected in the dialog, for which controls should be generated. */
	public Set<EStructuralFeature> getSelectedFeatures() {
		return selectedFeatures;
	}

	/** @return the rootEClass the dialog is displaying the attributes for. */
	public EClass getRootClass() {
		return rootClass;
	}

}
