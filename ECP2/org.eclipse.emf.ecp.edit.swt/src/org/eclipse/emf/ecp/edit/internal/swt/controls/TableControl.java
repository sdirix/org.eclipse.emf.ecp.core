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
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.CellEditorFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
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
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public TableControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
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
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		// ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
		// @Override
		// protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
		// return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
		// || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
		// || event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
		// || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
		// }
		// };
		//
		// TableViewerEditor.create(tableViewer, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
		// | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
		// | ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// create a content provider
		ObservableListContentProvider cp = new ObservableListContentProvider();

		EObject tempInstance = clazz.getEPackage().getEFactoryInstance().create(clazz);

		for (final EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				tempInstance, feature);
			final CellEditor cellEditor = CellEditorFactory.INSTANCE.getCellEditor(itemPropertyDescriptor,
				tempInstance, tableViewer.getTable());
			// create a new column
			TableViewerColumn column = new TableViewerColumn(tableViewer, cellEditor.getStyle());
			// determine the attribute that should be observed
			IObservableMap map = EMFProperties.value(feature).observeDetail(cp.getKnownElements());
			column.setLabelProvider(new ObservableMapCellLabelProvider(map) {
				@Override
				public void update(ViewerCell cell) {
					Object element = cell.getElement();
					Object value = attributeMaps[0].get(element);

					if (ECPCellEditor.class.isInstance(cellEditor)) {
						ECPCellEditor ecpCellEditor = (ECPCellEditor) cellEditor;
						String text = ecpCellEditor.getFormatedString(value);
						cell.setText(text == null ? "" : text);
					} else {
						cell.setText(value == null ? "" : value.toString()); //$NON-NLS-1$
					}
				}
			});

			// set the column title & set the size
			column.getColumn().setText(itemPropertyDescriptor.getDisplayName(null));
			column.getColumn().setToolTipText(itemPropertyDescriptor.getDescription(null));
			column.getColumn().setResizable(false);
			column.getColumn().setMoveable(false);

			EditingSupport observableSupport = new EditingSupport(tableViewer) {
				private EditingState editingState;

				private final ColumnViewerEditorActivationListenerHelper activationListener = new ColumnViewerEditorActivationListenerHelper();

				/**
				 * 
				 * Default implementation always returns <code>true</code>.
				 * 
				 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
				 */
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}

				/**
				 * Default implementation always returns <code>null</code> as this will be
				 * handled by the Binding.
				 * 
				 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
				 */
				@Override
				protected Object getValue(Object element) {
					// no op
					return null;
				}

				/**
				 * Default implementation does nothing as this will be handled by the
				 * Binding.
				 * 
				 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
				 */
				@Override
				protected void setValue(Object element, Object value) {
					// no op
				}

				/**
				 * Creates a {@link Binding} between the editor and the element to be
				 * edited. Invokes {@link #doCreateCellEditorObservable(CellEditor)},
				 * {@link #doCreateElementObservable(Object, ViewerCell)}, and then
				 * {@link #createBinding(IObservableValue, IObservableValue)}.
				 */
				@Override
				protected void initializeCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
					IObservableValue target = doCreateCellEditorObservable(cellEditor);
					Assert.isNotNull(target, "doCreateCellEditorObservable(...) did not return an observable"); //$NON-NLS-1$

					IObservableValue model = doCreateElementObservable(cell.getElement(), cell);
					Assert.isNotNull(model, "doCreateElementObservable(...) did not return an observable"); //$NON-NLS-1$

					Binding binding = createBinding(target, model);

					Assert.isNotNull(binding, "createBinding(...) did not return a binding"); //$NON-NLS-1$

					editingState = new EditingState(binding, target, model);

					getViewer().getColumnViewerEditor().addEditorActivationListener(activationListener);
				}

				@Override
				protected CellEditor getCellEditor(Object element) {
					return cellEditor;
				}

				protected Binding createBinding(IObservableValue target, IObservableValue model) {
					return getDataBindingContext().bindValue(target, model);
				}

				protected IObservableValue doCreateElementObservable(Object element, ViewerCell cell) {
					return EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
						(EObject) element, feature);
				}

				protected IObservableValue doCreateCellEditorObservable(CellEditor cellEditor) {
					if (ECPCellEditor.class.isInstance(cellEditor)) {
						return ((ECPCellEditor) cellEditor).getValueProperty().observe(cellEditor);
					}
					return SWTObservables.observeText(cellEditor.getControl(), SWT.FocusOut);
				}

				@Override
				final protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
					editingState.binding.updateTargetToModel();
				}

				class ColumnViewerEditorActivationListenerHelper extends ColumnViewerEditorActivationListener {

					@Override
					public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
						// do nothing
					}

					@Override
					public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
						editingState.dispose();
						editingState = null;

						getViewer().getColumnViewerEditor().removeEditorActivationListener(this);
					}

					@Override
					public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
						// do nothing
					}

					@Override
					public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
						// do nothing
					}
				}

				/**
				 * Maintains references to objects that only live for the length of the edit
				 * cycle.
				 */
				class EditingState {
					IObservableValue target;

					IObservableValue model;

					Binding binding;

					EditingState(Binding binding, IObservableValue target, IObservableValue model) {
						this.binding = binding;
						this.target = target;
						this.model = model;
					}

					void dispose() {
						binding.dispose();
						target.dispose();
						model.dispose();
					}
				}
			};
			column.setEditingSupport(observableSupport);

		}
		tableViewer.setContentProvider(cp);
		list = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(), getModelElementContext()
			.getModelElement(), getStructuralFeature());
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
		Image image = Activator.getImage("icons/delete.png"); //$NON-NLS-1$
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
		Image image = Activator.getImage("icons/add.png"); //$NON-NLS-1$
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addRow(EClass clazz) {
		EObject modelElement = getModelElementContext().getModelElement();
		EStructuralFeature structuralFeature = getStructuralFeature();
		EList list = (EList) modelElement.eGet(structuralFeature);

		EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		list.add(instance);
	}

}
