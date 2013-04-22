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
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EObjectObservableMap;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.CellEditorFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPCellEditor;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private EClass clazz;
	private Button unsetButton = null;

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
	protected Binding bindValue() {
		return null;
	}

	@Override
	public Composite createControl(Composite parent) {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		clazz = ((EReference) getStructuralFeature()).getEReferenceType();

		Label label = new Label(parent, SWT.NONE);
		label.setText(getItemPropertyDescriptor().getDisplayName(null));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(label);

		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);

		// delegate to super class
		createContentControl(composite);

		return parent;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#fillControlComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void fillControlComposite(Composite parent) {
		Composite parentComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(parentComposite);
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(2).applyTo(parentComposite);

		Composite buttonComposite = new Composite(parentComposite, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).grab(true, false).applyTo(buttonComposite);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(true).applyTo(buttonComposite);

		createAddRowButton(clazz, buttonComposite);
		createRemoveRowButton(clazz, buttonComposite);

		if (!isEmbedded() && getStructuralFeature().isUnsettable()) {
			unsetButton = new Button(buttonComposite, SWT.PUSH);
			unsetButton.setToolTipText(getUnsetButtonTooltip());
			unsetButton.setImage(Activator.getImage("icons/delete.png")); //$NON-NLS-1$
		}

		Composite composite = new Composite(parentComposite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, 200).span(2, 1)
			.applyTo(composite);

		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
			| SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tableViewer.getTable());
		tableViewer.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_swt_table");
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer,
			new FocusCellOwnerDrawHighlighter(tableViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
					|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
					|| event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR
					|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TableViewerEditor.create(tableViewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
			| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
			| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// create a content provider
		ObservableListContentProvider cp = new ObservableListContentProvider();

		EObject tempInstance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		ECPTableViewerComparator comparator = new ECPTableViewerComparator();
		tableViewer.setComparator(comparator);
		int columnNumber = 0;
		for (final EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				tempInstance, feature);
			if (itemPropertyDescriptor == null) {
				// if we can't render because no edit information is available, do nothing
				continue;
			}

			final CellEditor cellEditor = CellEditorFactory.INSTANCE.getCellEditor(itemPropertyDescriptor,
				tempInstance, tableViewer.getTable());
			// create a new column
			final TableViewerColumn column = new TableViewerColumn(tableViewer, cellEditor.getStyle());

			if (ECPCellEditor.class.isInstance(cellEditor)) {
				column.getColumn().setData("width", ((ECPCellEditor) cellEditor).getColumnWidthWeight());
			} else {
				column.getColumn().setData("width", 100);
			}

			// determine the attribute that should be observed
			IObservableMap map = null;
			if (feature.isMany()) {
				map = new EObjectObservableMap(cp.getKnownElements(), feature);
			} else {
				map = EMFProperties.value(feature).observeDetail(cp.getKnownElements());
			}
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

			column.getColumn().addSelectionListener(getSelectionAdapter(comparator, column.getColumn(), columnNumber));
			// set the column title & set the size
			column.getColumn().setText(itemPropertyDescriptor.getDisplayName(null));
			column.getColumn().setToolTipText(itemPropertyDescriptor.getDescription(null));
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(false);
			// remove if no editing needed
			EditingSupport observableSupport = new EditingSupport(tableViewer) {
				private EditingState editingState;

				private final ColumnViewerEditorActivationListenerHelper activationListener = new ColumnViewerEditorActivationListenerHelper();

				/**
				 * Default implementation always returns <code>true</code>.
				 * 
				 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
				 */
				@Override
				protected boolean canEdit(Object element) {
					// return false here otherwise
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

			columnNumber++;
		}
		tableViewer.setContentProvider(cp);
		IObservableList list = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		tableViewer.setInput(list);
		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		// IMPORTANT:
		// - the minimumWidth and (non)resizable settings of the ColumnWeightData are not supported properly
		// - the layout stops resizing columns that have been resized manually by the user (this could be considered a
		// feature though)
		for (TableColumn col : tableViewer.getTable().getColumns()) {
			layout.setColumnData(col, new ColumnWeightData((Integer) col.getData("width"), 50));
			// layout.setColumnData(col, new ColumnPixelData((Integer) col.getData("width")));
		}
	}

	@Override
	protected Button getCustomUnsetButton() {
		return unsetButton;
	}

	private SelectionAdapter getSelectionAdapter(final ECPTableViewerComparator comparator, final TableColumn column,
		final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
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

				final List<EObject> deletionList = new ArrayList<EObject>();
				Iterator<?> iterator = selection.iterator();

				while (iterator.hasNext()) {
					deletionList.add((EObject) iterator.next());
				}

				MessageDialog dialog = new MessageDialog(tableViewer.getTable().getShell(), "Delete?", null,
					"Are you sure you want to delete the selected Elements?", MessageDialog.CONFIRM, new String[] {
						JFaceResources.getString(IDialogLabelKeys.YES_LABEL_KEY),
						JFaceResources.getString(IDialogLabelKeys.NO_LABEL_KEY) }, 0);

				new ECPDialogExecutor(dialog) {

					@Override
					public void handleResult(int codeResult) {
						if (codeResult == IDialogConstants.CANCEL_ID) {
							return;
						}

						EObject modelElement = getModelElementContext().getModelElement();
						EditingDomain editingDomain = getModelElementContext().getEditingDomain();
						editingDomain.getCommandStack().execute(
							RemoveCommand.create(editingDomain, modelElement, getStructuralFeature(), deletionList));
					}
				}.execute();
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

	private void addRow(EClass clazz) {
		EObject modelElement = getModelElementContext().getModelElement();
		EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);

		EditingDomain editingDomain = getModelElementContext().getEditingDomain();
		if (tableViewer.getSelection() == null) {
			editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, modelElement, getStructuralFeature(), instance));
		} else {
			editingDomain.getCommandStack().execute(
				AddCommand.create(editingDomain, modelElement, getStructuralFeature(), instance, tableViewer.getTable()
					.getSelectionIndex()));
		}

	}

	private class ECPTableViewerComparator extends ViewerComparator {
		private int propertyIndex;
		private static final int DESCENDING = 1;
		private int direction = DESCENDING;

		public ECPTableViewerComparator() {
			propertyIndex = 0;
			direction = DESCENDING;
		}

		public int getDirection() {
			return direction == 1 ? SWT.DOWN : SWT.UP;
		}

		public void setColumn(int column) {
			if (column == propertyIndex) {
				// Same column as last sort; toggle the direction
				direction = 1 - direction;
			} else {
				// New column; do an ascending sort
				propertyIndex = column;
				direction = DESCENDING;
			}
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			int rc = 0;
			EObject object1 = (EObject) e1;
			EObject object2 = (EObject) e2;
			EStructuralFeature feat1 = object1.eClass().getEAllStructuralFeatures().get(propertyIndex);
			EStructuralFeature feat2 = object2.eClass().getEAllStructuralFeatures().get(propertyIndex);

			Object value1 = object1.eGet(feat1);
			Object value2 = object2.eGet(feat2);

			if (value1 == null) {
				rc = 1;
			} else if (value2 == null) {
				rc = -1;
			} else {
				rc = value1.toString().compareTo(value2.toString());
			}
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getHelpText()
	 */
	@Override
	protected String getHelpText() {
		return "This is a table control";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		return "Not set. Click to set!";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset";
	}
}
