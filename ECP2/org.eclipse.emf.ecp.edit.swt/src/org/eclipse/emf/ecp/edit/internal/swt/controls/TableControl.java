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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.CellEditorFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import java.util.Iterator;

/**
 * The class describing a table control.
 * 
 * @author Eugen Neufeld
 * 
 */
public class TableControl extends SWTControl {

	private TableViewer tableViewer;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private IObservableList list;

	/**
	 * Constructor for a String control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public TableControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	@Override
	protected void bindValue() {
		// Not necessary
	}

	@Override
	public Composite createControl(Composite parent) {

		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		EClass clazz = ((EReference) getStructuralFeature()).getEReferenceType();

		final Composite parentComposite = new Composite(parent, SWT.NONE);
		parentComposite.setLayout(new GridLayout(2, false));

		final Composite composite = new Composite(parentComposite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).hint(SWT.DEFAULT, 200)
			.applyTo(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
			| SWT.BORDER);

		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(contentProvider);
		tableViewer.getTable().setHeaderVisible(true);
		list = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(), getModelElementContext()
			.getModelElement(), getStructuralFeature());
		IObservableSet set = contentProvider.getKnownElements();

		EObject tempInstance = clazz.getEPackage().getEFactoryInstance().create(clazz);

		for (EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				tempInstance, feature);
			IValueProperty property = EMFEditProperties.value(getModelElementContext().getEditingDomain(), feature);

			TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEAD);
			column.getColumn().setText(itemPropertyDescriptor.getDisplayName(null));
			column.getColumn().setToolTipText(itemPropertyDescriptor.getDescription(null));
			column.getColumn().setResizable(false);
			column.getColumn().setMoveable(false);
			column.setLabelProvider(new ObservableMapCellLabelProvider(property.observeDetail(set)));
			CellEditor cellEditor = CellEditorFactory.INSTANCE.getCellEditor(itemPropertyDescriptor, tempInstance,
				tableViewer.getTable());
			IValueProperty editorProperty = CellEditorProperties.control().value(WidgetProperties.text(SWT.FocusOut));
			if (ECPCellEditor.class.isInstance(cellEditor)) {
				editorProperty = ((ECPCellEditor) cellEditor).getValueProperty();
			}
			column.setEditingSupport(createEditingSupport(tableViewer, getDataBindingContext(), cellEditor,
				editorProperty, property));

		}
		tableViewer.setInput(list);

		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		// IMPORTANT:
		// - the minimumWidth and (non)resizable settings of the ColumnWeightData are not supported properly
		// - the layout stops resizing columns that have been resized manually by the user (this could be considered a
		// feature though)
		for (TableColumn col : tableViewer.getTable().getColumns()) {
			layout.setColumnData(col, new ColumnWeightData(100));
		}

		final Composite buttonComposite = new Composite(parentComposite, SWT.NONE);
		buttonComposite.setLayout(new FillLayout(SWT.VERTICAL));

		createAddRowButton(clazz, buttonComposite);
		createRemoveRowButton(clazz, buttonComposite);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		buttonComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 1, 1));

		return parentComposite;
	}

	private void createRemoveRowButton(EClass clazz, final Composite buttonComposite) {
		Button removeButton = new Button(buttonComposite, SWT.None);
		Image image = Activator.getImageDescriptor("icons/delete.png").createImage(); //$NON-NLS-1$
		removeButton.setImage(image);
		removeButton.setToolTipText("Remove the selected " + clazz.getInstanceClass().getSimpleName());
		removeButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

				if (selection == null || selection.getFirstElement() == null) {
					return;
				}

				Iterator<?> iterator = selection.iterator();

				while (iterator.hasNext()) {
					list.remove(iterator.next());
				}
			}
		});
	}

	private void createAddRowButton(final EClass clazz, final Composite buttonComposite) {
		Button addButton = new Button(buttonComposite, SWT.None);
		Image image = Activator.getImageDescriptor("icons/add.png").createImage(); //$NON-NLS-1$
		addButton.setImage(image);
		addButton.setToolTipText("Add an instance of " + clazz.getInstanceClass().getSimpleName());
		addButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				addRow(clazz);
			}
		});
	}

	@Override
	public void dispose() {
		composedAdapterFactory.dispose();
	}

	@Override
	public void handleValidation(Diagnostic diagnostic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetValidation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditable(boolean isEditable) {
		tableViewer.getTable().setEnabled(false);
	}

	private EditingSupport createEditingSupport(ColumnViewer viewer, DataBindingContext dbc,
		final CellEditor cellEditor, final IValueProperty cellEditorProperty, final IValueProperty elementProperty) {
		return new ObservableValueEditingSupport(viewer, dbc) {
			@Override
			protected IObservableValue doCreateCellEditorObservable(CellEditor cellEditor) {
				return cellEditorProperty.observe(cellEditor);
			}

			@Override
			protected IObservableValue doCreateElementObservable(Object element, ViewerCell cell) {
				return elementProperty.observe(element);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return cellEditor;
			}

			@Override
			protected Binding createBinding(IObservableValue target, IObservableValue model) {
				return getDataBindingContext().bindValue(target, model,
					new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
			}
		};
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addRow(EClass clazz) {
		EObject modelElement = getModelElementContext().getModelElement();
		EStructuralFeature structuralFeature = getStructuralFeature();
		EList list = (EList) modelElement.eGet(structuralFeature);

		EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		list.add(instance);
	}
}
