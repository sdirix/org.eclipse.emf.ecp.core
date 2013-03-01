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

import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.CellEditorFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

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

		final Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).hint(SWT.DEFAULT, 200)
			.applyTo(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
			| SWT.BORDER);

		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(contentProvider);
		tableViewer.getTable().setHeaderVisible(true);
		IObservableList list = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
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
			CellEditor cellEditor=CellEditorFactory.INSTANCE.getCellEditor(itemPropertyDescriptor, tempInstance, tableViewer.getTable());
			IValueProperty editorProperty=CellEditorProperties.control().value(WidgetProperties.text(SWT.FocusOut));
			if(ECPCellEditor.class.isInstance(cellEditor)){
				editorProperty=((ECPCellEditor)cellEditor).getValueProperty();
			}
			column.setEditingSupport(createEditingSupport(tableViewer, getDataBindingContext(), cellEditor, editorProperty,
				property));

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

		return composite;
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
			protected IObservableValue doCreateCellEditorObservable(CellEditor cellEditor) {
				return cellEditorProperty.observe(cellEditor);
			}

			protected IObservableValue doCreateElementObservable(Object element, ViewerCell cell) {
				return elementProperty.observe(element);
			}

			protected CellEditor getCellEditor(Object element) {
				return cellEditor;
			}

			protected Binding createBinding(IObservableValue target, IObservableValue model) {
				return getDataBindingContext().bindValue(target, model,
					new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);
			}
		};
	}
}
