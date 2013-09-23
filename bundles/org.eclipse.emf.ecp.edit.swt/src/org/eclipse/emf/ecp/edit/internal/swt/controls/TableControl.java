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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.AdapterFactory;
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
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
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
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The class describing a table control.
 * 
 * @author Eugen Neufeld
 * 
 */
// this class is not serialized
@SuppressWarnings("serial")
public class TableControl extends SWTControl {

	private TableViewer tableViewer;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private Button unsetButton;
	private EClass clazz;
	private TableControlConfiguration tableControlConfiguration;

	private final Map<EObject, Map<EStructuralFeature, Diagnostic>> featureErrorMap = new HashMap<EObject, Map<EStructuralFeature, Diagnostic>>();

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

	/**
	 * Constructor for a String control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 * @param tableControlConfiguration the {@link TableControlConfiguration} to use when creating the table
	 */
	public TableControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded, TableControlConfiguration tableControlConfiguration) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		this.tableControlConfiguration = tableControlConfiguration;
	}

	@Override
	protected Binding bindValue() {
		return null;
	}

	@Override
	public Composite createControl(final Composite parent) {

		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });

		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		clazz = ((EReference) getStructuralFeature()).getEReferenceType();

		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setBackground(parent.getBackground());

		final Composite titleComposite = new Composite(composite, SWT.NONE);
		titleComposite.setBackground(parent.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING)
			.applyTo(titleComposite);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(titleComposite);

		final Label label = new Label(titleComposite, SWT.NONE);
		label.setBackground(parent.getBackground());
		label.setText(getItemPropertyDescriptor().getDisplayName(null));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(label);

		// VALIDATION
		validationLabel = new Label(titleComposite, SWT.NONE);
		validationLabel.setBackground(parent.getBackground());
		// set the size of the label to the size of the image
		GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);

		boolean createButtons = true;
		if (tableControlConfiguration != null) {
			createButtons = !tableControlConfiguration.isAddRemoveDisabled();
		}
		if (createButtons) {
			// addButtons
			final Composite buttonComposite = new Composite(titleComposite, SWT.NONE);
			GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).grab(true, false).applyTo(buttonComposite);
			int numButtons = 2;

			createAddRowButton(clazz, buttonComposite);
			createRemoveRowButton(clazz, buttonComposite);
			if (!isEmbedded() && getStructuralFeature().isUnsettable()) {
				unsetButton = new Button(buttonComposite, SWT.PUSH);
				unsetButton.setToolTipText(getUnsetButtonTooltip());
				unsetButton.setImage(Activator.getImage("icons/delete.png")); //$NON-NLS-1$
				numButtons++;
			}
			GridLayoutFactory.fillDefaults().numColumns(numButtons).equalWidth(true).applyTo(buttonComposite);
		}
		final Composite controlComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL,
			SWT.FILL).applyTo(controlComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(controlComposite);

		// delegate to super class
		createContentControl(controlComposite);

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#fillControlComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void fillControlComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// composite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(1, 200).applyTo(composite);

		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, SWT.DEFAULT)
		// .applyTo(tableViewer.getTable());
		tableViewer.getTable().setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_table"); //$NON-NLS-1$
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		final TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer,
			new FocusCellOwnerDrawHighlighter(tableViewer));
		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
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
		final ObservableListContentProvider cp = new ObservableListContentProvider();

		final EObject tempInstance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		final ECPTableViewerComparator comparator = new ECPTableViewerComparator();
		tableViewer.setComparator(comparator);
		int columnNumber = 0;
		List<EStructuralFeature> structuralFeatures = clazz.getEAllStructuralFeatures();
		Map<EStructuralFeature, Boolean> readOnlyColumn = null;
		if (tableControlConfiguration != null) {
			structuralFeatures = new ArrayList<EStructuralFeature>();
			readOnlyColumn = new HashMap<EStructuralFeature, Boolean>();
			for (final TableColumnConfiguration tcc : tableControlConfiguration.getColumns()) {
				structuralFeatures.add(tcc.getColumnAttribute());
				readOnlyColumn.put(tcc.getColumnAttribute(), tcc.isReadOnly());
			}
		}

		for (final EStructuralFeature feature : structuralFeatures) {
			final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				tempInstance, feature);
			if (itemPropertyDescriptor == null) {
				// if we can't render because no edit information is available, do nothing
				continue;
			}

			final CellEditor cellEditor = CellEditorFactory.INSTANCE.getCellEditor(itemPropertyDescriptor,
				tempInstance, tableViewer.getTable(), getModelElementContext());
			// create a new column
			final TableViewerColumn column = new TableViewerColumn(tableViewer, cellEditor.getStyle());

			if (ECPCellEditor.class.isInstance(cellEditor)) {
				column.getColumn().setData("width", ((ECPCellEditor) cellEditor).getColumnWidthWeight()); //$NON-NLS-1$
			} else {
				column.getColumn().setData("width", 100); //$NON-NLS-1$
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
					final EObject element = (EObject) cell.getElement();
					final Object value = attributeMaps[0].get(element);

					if (ECPCellEditor.class.isInstance(cellEditor)) {
						final ECPCellEditor ecpCellEditor = (ECPCellEditor) cellEditor;
						final String text = ecpCellEditor.getFormatedString(value);
						cell.setText(text == null ? "" : text); //$NON-NLS-1$

					} else {

						cell.setText(value == null ? "" : value.toString()); //$NON-NLS-1$
						cell.getControl().setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_edit_cellEditor_string"); //$NON-NLS-1$
					}

					// if (featureErrorMap.containsKey(element)
					// && featureErrorMap.get(element).containsKey(attributeMaps[0].getValueType())) {
					// cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					// } else {
					// cell.setBackground(null);
					// }
				}
			});

			column.getColumn().addSelectionListener(getSelectionAdapter(comparator, column.getColumn(), columnNumber));
			// set the column title & set the size
			column.getColumn().setText(itemPropertyDescriptor.getDisplayName(null));
			column.getColumn().setToolTipText(itemPropertyDescriptor.getDescription(null));
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(false);
			boolean addEditingSupport = true;
			if (readOnlyColumn != null) {
				addEditingSupport = !readOnlyColumn.get(feature);
			}
			if (addEditingSupport) {
				// remove if no editing needed
				final EditingSupport observableSupport = new EditingSupport(tableViewer) {
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
						final IObservableValue target = doCreateCellEditorObservable(cellEditor);
						Assert.isNotNull(target, "doCreateCellEditorObservable(...) did not return an observable"); //$NON-NLS-1$

						final IObservableValue model = doCreateElementObservable(cell.getElement(), cell);
						Assert.isNotNull(model, "doCreateElementObservable(...) did not return an observable"); //$NON-NLS-1$

						final Binding binding = createBinding(target, model);

						Assert.isNotNull(binding, "createBinding(...) did not return a binding"); //$NON-NLS-1$

						editingState = new EditingState(binding, target, model);

						getViewer().getColumnViewerEditor().addEditorActivationListener(activationListener);
					}

					@Override
					protected CellEditor getCellEditor(Object element) {
						return cellEditor;
					}

					protected Binding createBinding(IObservableValue target, IObservableValue model) {
						if (ECPCellEditor.class.isInstance(cellEditor)) {
							return getDataBindingContext().bindValue(target, model,
								((ECPCellEditor) cellEditor).getTargetToModelStrategy(),
								((ECPCellEditor) cellEditor).getModelToTargetStrategy());
						}
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
					protected final void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
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
			columnNumber++;
		}
		tableViewer.setContentProvider(cp);
		final IObservableList list = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		tableViewer.setInput(list);

		// IMPORTANT:
		// - the minimumWidth and (non)resizable settings of the ColumnWeightData are not supported properly
		// - the layout stops resizing columns that have been resized manually by the user (this could be considered a
		// feature though)
		final TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		for (int i = 0; i < tableViewer.getTable().getColumns().length; i++) {
			final Integer storedValue = (Integer) tableViewer.getTable().getColumns()[i].getData("width"); //$NON-NLS-1$
			layout.setColumnData(tableViewer.getTable().getColumns()[i], new ColumnWeightData(storedValue == null ? 50
				: storedValue));
		}
	}

	@Override
	protected Button getCustomUnsetButton() {
		return unsetButton;
	}

	private SelectionAdapter getSelectionAdapter(final ECPTableViewerComparator comparator, final TableColumn column,
		final int index) {
		final SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				final int dir = comparator.getDirection();
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private Button removeButton;

	private void createRemoveRowButton(EClass clazz, final Composite buttonComposite) {
		removeButton = new Button(buttonComposite, SWT.None);
		final Image image = Activator.getImage("icons/delete.png"); //$NON-NLS-1$
		removeButton.setImage(image);
		removeButton.setToolTipText(ControlMessages.TableControl_RemoveSelected
			+ clazz.getInstanceClass().getSimpleName());
		removeButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

				if (selection == null || selection.getFirstElement() == null) {
					return;
				}

				final List<EObject> deletionList = new ArrayList<EObject>();
				final Iterator<?> iterator = selection.iterator();

				while (iterator.hasNext()) {
					deletionList.add((EObject) iterator.next());
				}

				deleteRowUserConfirmDialog(deletionList);

			}
		});
		final EObject modelElement = getModelElementContext().getModelElement();
		final List<?> containments = (List<?>) modelElement.eGet(getStructuralFeature());
		if (containments.size() <= getStructuralFeature().getLowerBound()) {
			removeButton.setEnabled(false);
		}
	}

	/**
	 * This method shows a user confirmation dialog when the user attempts to delete a row in the table.
	 * 
	 * @param deletionList the list of selected EObjects to delete
	 */
	protected void deleteRowUserConfirmDialog(final List<EObject> deletionList) {
		final MessageDialog dialog = new MessageDialog(tableViewer.getTable().getShell(),
			ControlMessages.TableControl_Delete, null,
			ControlMessages.TableControl_DeleteAreYouSure, MessageDialog.CONFIRM, new String[] {
				JFaceResources.getString(IDialogLabelKeys.YES_LABEL_KEY),
				JFaceResources.getString(IDialogLabelKeys.NO_LABEL_KEY) }, 0);

		final EObject modelElement = getModelElementContext().getModelElement();

		new ECPDialogExecutor(dialog) {

			@Override
			public void handleResult(int codeResult) {
				if (codeResult == IDialogConstants.CANCEL_ID) {
					return;
				}

				deleteRows(deletionList);

				final List<?> containments = (List<?>) modelElement.eGet(getStructuralFeature());
				if (containments.size() < getStructuralFeature().getUpperBound()) {
					addButton.setEnabled(true);
				}
				if (containments.size() <= getStructuralFeature().getLowerBound()) {
					removeButton.setEnabled(false);
				}
			}
		}.execute();
	}

	/**
	 * This is called by {@link #deleteRowUserConfirmDialog(List)} after the user confirmed to delete the selected
	 * elements.
	 * 
	 * @param deletionList the list of {@link EObject EObjects} to delete
	 */
	protected void deleteRows(List<EObject> deletionList) {
		final EditingDomain editingDomain = getModelElementContext().getEditingDomain();
		final EObject modelElement = getModelElementContext().getModelElement();
		editingDomain.getCommandStack().execute(
			RemoveCommand.create(editingDomain, modelElement, getStructuralFeature(), deletionList));
	}

	/**
	 * This method is called to add a new entry in the domain model and thus to create a new row in the table. The
	 * element to create is defined by the provided class.
	 * You can override this method but you have to call super nonetheless.
	 * 
	 * @param clazz the {@link EClass} defining the EObject to create
	 */
	protected void addRow(EClass clazz) {
		final EObject modelElement = getModelElementContext().getModelElement();
		final EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);

		final EditingDomain editingDomain = getModelElementContext().getEditingDomain();
		editingDomain.getCommandStack().execute(
			AddCommand.create(editingDomain, modelElement, getStructuralFeature(), instance));

	}

	private Button addButton;

	private void createAddRowButton(final EClass clazz, final Composite buttonComposite) {
		addButton = new Button(buttonComposite, SWT.None);
		final Image image = Activator.getImage("icons/add.png"); //$NON-NLS-1$
		addButton.setImage(image);
		addButton.setToolTipText(ControlMessages.TableControl_AddInstanceOf + clazz.getInstanceClass().getSimpleName());
		addButton.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				addRow(clazz);
				final EObject modelElement = getModelElementContext().getModelElement();
				final List<?> containments = (List<?>) modelElement.eGet(getStructuralFeature());
				if (getStructuralFeature().getUpperBound() != -1
					&& containments.size() >= getStructuralFeature().getUpperBound()) {
					addButton.setEnabled(false);
				}
				if (containments.size() > getStructuralFeature().getLowerBound()) {
					removeButton.setEnabled(true);
				}
			}
		});

		final EObject modelElement = getModelElementContext().getModelElement();
		final List<?> containments = (List<?>) modelElement.eGet(getStructuralFeature());
		if (getStructuralFeature().getUpperBound() != -1
			&& containments.size() >= getStructuralFeature().getUpperBound()) {
			addButton.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		composedAdapterFactory.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			final Image image = Activator.getImage(VALIDATION_ERROR_ICON);
			validationLabel.setImage(image);
			validationLabel.setToolTipText(diagnostic.getMessage());
			final EObject object = (EObject) diagnostic.getData().get(0);
			if (!featureErrorMap.containsKey(object)) {
				featureErrorMap.put(object, new HashMap<EStructuralFeature, Diagnostic>());
			}
			if (diagnostic.getData().size() > 1) {
				featureErrorMap.get(object).put((EStructuralFeature) diagnostic.getData().get(1), diagnostic);
			}
			tableViewer.update(object, null);
			// tableViewer.refresh();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetValidation() {
		featureErrorMap.clear();
		// tableViewer.refresh();
		if (validationLabel == null || validationLabel.isDisposed()) {
			return;
		}
		validationLabel.setImage(null);

	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean isEditable) {
		tableViewer.getTable().setEnabled(false);
	}

	/**
	 * The {@link ViewerComparator} for this table which allows 3 states for sort order:
	 * none, up and down.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	private class ECPTableViewerComparator extends ViewerComparator {
		private int propertyIndex;
		private static final int NONE = 0;
		private int direction = NONE;

		public ECPTableViewerComparator() {
			propertyIndex = 0;
			direction = NONE;
		}

		public int getDirection() {
			switch (direction) {
			case 0:
				return SWT.NONE;
			case 1:
				return SWT.UP;
			case 2:
				return SWT.DOWN;
			default:
				return SWT.NONE;
			}

		}

		public void setColumn(int column) {
			if (column == propertyIndex) {
				// Same column as last sort; toggle the direction
				direction = (direction + 1) % 3;
			} else {
				// New column; do an ascending sort
				propertyIndex = column;
				direction = 1;
			}
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (direction == 0) {
				return 0;
			}
			int rc = 0;
			final EObject object1 = (EObject) e1;
			final EObject object2 = (EObject) e2;
			final EStructuralFeature feat1 = object1.eClass().getEAllStructuralFeatures().get(propertyIndex);
			final EStructuralFeature feat2 = object2.eClass().getEAllStructuralFeatures().get(propertyIndex);

			final Object value1 = object1.eGet(feat1);
			final Object value2 = object2.eGet(feat2);

			if (value1 == null) {
				rc = 1;
			} else if (value2 == null) {
				rc = -1;
			} else {
				rc = value1.toString().compareTo(value2.toString());
			}
			// If descending order, flip the direction
			if (direction == 2) {
				rc = -rc;
			}
			return rc;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		return ControlMessages.TableControl_NotSetClickToSet;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return ControlMessages.TableControl_Unset;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getControlForTooltip()
	 */
	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { tableViewer.getControl() };
	}
}
