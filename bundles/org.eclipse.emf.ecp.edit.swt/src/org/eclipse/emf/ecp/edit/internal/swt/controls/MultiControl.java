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
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.AbstractControl;
import org.eclipse.emf.ecp.edit.ControlDescription;
import org.eclipse.emf.ecp.edit.ControlFactory;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.actions.ECPSWTAction;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPObservableValue;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.ecp.edit.util.StaticApplicableTester;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This control provides the necessary common functionality to create a multicontrol that are needed for
 * {@link EStructuralFeature}s that have multiple values.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class MultiControl extends SWTControl {

	private static final String ICONS_ARROW_DOWN_PNG = "icons/arrow_down.png";//$NON-NLS-1$
	private static final String ICONS_ARROW_UP_PNG = "icons/arrow_up.png";//$NON-NLS-1$

	private IObservableList model;
	private IListChangeListener changeListener;
	// list of controls
	private List<WidgetWrapper> widgetWrappers = new ArrayList<WidgetWrapper>();
	private List<ECPSWTAction> toolBarActions = new ArrayList<ECPSWTAction>();
	private Composite mainComposite;
	private Composite sectionComposite;
	private Label validationLabel;
	private ControlDescription controlDescription;
	private Class<?> supportedClassType;
	private ECPSWTAction[] actions;

	/**
	 * Constructor for a multi control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public MultiControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		findControlDescription(itemPropertyDescriptor, modelElementContext.getModelElement());
		actions = instantiateActions();
	}

	/**
	 * This returns the array of actions to display in the multi control.
	 * 
	 * @return the array of action to add
	 */
	protected abstract ECPSWTAction[] instantiateActions();

	private void findControlDescription(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		int bestPriority = -1;
		for (ControlDescription description : ControlFactory.INSTANCE.getControlDescriptors()) {
			for (ECPApplicableTester tester : description.getTester()) {
				if (StaticApplicableTester.class.isInstance(tester)) {
					StaticApplicableTester test = (StaticApplicableTester) tester;
					int priority = getTesterPriority(test, itemPropertyDescriptor, eObject);
					if (bestPriority < priority) {
						bestPriority = priority;
						controlDescription = description;
						supportedClassType = test.getSupportedClassType();
					}
				} else {
					continue;
				}
			}
		}
	}

	/**
	 * Checks the priority of a tester.
	 * 
	 * @param tester the {@link StaticApplicableTester} to test
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param eObject the {@link EObject} to use
	 * @return the priority
	 */
	protected abstract int getTesterPriority(StaticApplicableTester tester,
		IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.edit.controls.AbstractControl#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite createControl(Composite parent) {
		Composite superComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(10, 0).equalWidth(false).applyTo(superComposite);

		// VALIDATION
		validationLabel = new Label(superComposite, SWT.NONE);
		// set the size of the label to the size of the image
		GridDataFactory.fillDefaults().hint(16, 17).applyTo(validationLabel);

		mainComposite = new Composite(superComposite, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(mainComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(mainComposite);

		// TOOLBAR
		createSectionToolbar();
		// SEPERATOR
		Label seperator = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).span(3, 1).applyTo(seperator);

		model = EMFEditObservables.observeList(getModelElementContext().getEditingDomain(), getModelElementContext()
			.getModelElement(), getStructuralFeature());

		ScrolledComposite scrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, 150)
			.minSize(SWT.DEFAULT, 150).span(3, 1).applyTo(scrolledComposite);

		sectionComposite = new Composite(scrolledComposite, SWT.NONE);
		sectionComposite.setBackground(parent.getBackground());
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).spacing(0, 5).applyTo(sectionComposite);

		changeListener = new IListChangeListener() {

			public void handleListChange(ListChangeEvent event) {
				ListDiff diff = event.diff;
				diff.accept(new ListDiffVisitor() {

					@Override
					public void handleRemove(int index, Object element) {
						updateIndicesAfterRemove(index);
						getDataBindingContext().updateTargets();
					}

					@Override
					public void handleAdd(int index, Object element) {
						addControl();
						sectionComposite.layout();
						getDataBindingContext().updateTargets();
					}

					@Override
					public void handleMove(int oldIndex, int newIndex, Object element) {
						getDataBindingContext().updateTargets();
					}

					@Override
					public void handleReplace(int index, Object oldElement, Object newElement) {
						// do nothing
					}
				});
			}
		};
		model.addListChangeListener(changeListener);
		for (int i = 0; i < model.size(); i++) {
			addControl();
		}
		refreshSection();
		scrolledComposite.setContent(sectionComposite);
		scrolledComposite.setExpandHorizontal(true);

		return superComposite;
	}

	private void isFull() {
		int upperBound = getStructuralFeature().getUpperBound();
		boolean full = model.size() >= upperBound && upperBound != -1;
		for (Action action : toolBarActions) {
			action.setEnabled(!full);
		}
	}

	private void addControl() {

		ECPObservableValue modelValue = new ECPObservableValue(model, widgetWrappers.size(), supportedClassType);
		WidgetWrapper h = new WidgetWrapper(modelValue);

		h.createControl(sectionComposite, SWT.NONE);
		widgetWrappers.add(h);
	}

	/**
	 * Returns the {@link SWTControl}.
	 * 
	 * @return the created {@link SWTControl}
	 */
	@SuppressWarnings({ "unchecked" })
	private SWTControl getSingleInstance() {
		try {
			Constructor<? extends AbstractControl<Composite>> widgetConstructor = (Constructor<? extends AbstractControl<Composite>>) controlDescription
				.getControlClass().getConstructor(boolean.class, IItemPropertyDescriptor.class,
					EStructuralFeature.class, ECPControlContext.class, boolean.class);
			return (SWTControl) widgetConstructor.newInstance(false, getItemPropertyDescriptor(),
				getStructuralFeature(), getModelElementContext(), true);
		} catch (IllegalArgumentException ex) {
			Activator.logException(ex);
		} catch (InstantiationException ex) {
			Activator.logException(ex);
		} catch (IllegalAccessException ex) {
			Activator.logException(ex);
		} catch (InvocationTargetException ex) {
			Activator.logException(ex);
		} catch (SecurityException ex) {
			Activator.logException(ex);
		} catch (NoSuchMethodException ex) {
			Activator.logException(ex);
		}
		return null;
	}

	private void refreshSection() {
		int widgetSize = 150;
		if (widgetWrappers.size() > 0) {
			widgetSize = widgetWrappers.size()
				* (widgetWrappers.get(0).composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		}
		sectionComposite.setSize(sectionComposite.getSize().x, widgetSize < 150 ? 150 : widgetSize);
		sectionComposite.layout();
		isFull();
	}

	private void createSectionToolbar() {
		ToolBarManager toolBarManager = new ToolBarManager(SWT.RIGHT);
		ToolBar toolbar = toolBarManager.createControl(mainComposite);
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.END, SWT.BEGINNING).applyTo(toolbar);
		final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (!handCursor.isDisposed()) {
					handCursor.dispose();
				}
			}
		});
		for (ECPSWTAction action : actions) {
			action.setEnabled(isEditable());
			toolBarActions.add(action);
			toolBarManager.add(action);

		}
		toolBarManager.update(true);
	}

	/**
	 * This class is the common wrapper for multi controls. It adds a remove, move up and move down button.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	private final class WidgetWrapper {

		private final ECPObservableValue modelValue;

		private Composite composite;

		public WidgetWrapper(ECPObservableValue modelValue) {
			this.modelValue = modelValue;
		}

		private WidgetWrapper getThis() {
			return this;
		}

		void createControl(Composite parent, int style) {
			composite = new Composite(parent, style);
			composite.setBackgroundMode(SWT.INHERIT_FORCE);
			GridLayoutFactory.fillDefaults().numColumns(5).spacing(2, 0).applyTo(composite);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

			SWTControl widget = getSingleInstance();
			widget.setObservableValue(modelValue);
			widget.createControl(composite);

			createDeleteButton(composite);
			if (getStructuralFeature().isOrdered()) {
				createUpDownButtons(composite);
			}

		}

		/**
		 * Initializes the delete button.
		 */
		private void createDeleteButton(Composite composite) {
			Button delB = new Button(composite, SWT.PUSH);
			delB.setImage(Activator.getImage("icons/delete.png"));
			delB.addSelectionListener(new SelectionAdapter() {

				/*
				 * (non-Javadoc)
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent e) {

					getModelElementContext()
						.getEditingDomain()
						.getCommandStack()
						.execute(
							RemoveCommand.create(getModelElementContext().getEditingDomain(), getModelElementContext()
								.getModelElement(), getStructuralFeature(), modelValue.getValue()));
				}

			});
		}

		/**
		 * Initializes the up/down buttons.
		 */
		private void createUpDownButtons(Composite composite) {
			Image up = Activator.getImage(ICONS_ARROW_UP_PNG);
			Image down = Activator.getImage(ICONS_ARROW_DOWN_PNG);

			Button upB = new Button(composite, SWT.PUSH);
			upB.setImage(up);
			upB.addSelectionListener(new SelectionAdapter() {
				/*
				 * (non-Javadoc)
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (getThis().getModelValue().getIndex() == 0) {
						return;
					}
					int currentIndex = getThis().getModelValue().getIndex();

					getModelElementContext()
						.getEditingDomain()
						.getCommandStack()
						.execute(
							new MoveCommand(getModelElementContext().getEditingDomain(), getModelElementContext()
								.getModelElement(), getStructuralFeature(), currentIndex, currentIndex - 1));
				}
			});
			Button downB = new Button(composite, SWT.PUSH);
			downB.setImage(down);
			downB.addSelectionListener(new SelectionAdapter() {
				/*
				 * (non-Javadoc)
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (getThis().getModelValue().getIndex() + 1 == model.size()) {
						return;
					}
					int currentIndex = getThis().getModelValue().getIndex();

					getModelElementContext()
						.getEditingDomain()
						.getCommandStack()
						.execute(
							new MoveCommand(getModelElementContext().getEditingDomain(), getModelElementContext()
								.getModelElement(), getStructuralFeature(), currentIndex, currentIndex + 1));
				}
			});
		}

		/**
		 * @return the modelValue
		 */
		ECPObservableValue getModelValue() {
			return modelValue;
		}

		public void dispose() {
			composite.dispose();
			modelValue.dispose();
		}
	}

	private void updateIndicesAfterRemove(int indexRemoved) {
		WidgetWrapper wrapper = widgetWrappers.remove(widgetWrappers.size() - 1);
		wrapper.composite.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleValidation(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			Image image = Activator.getImage(VALIDATION_ERROR_ICON);
			validationLabel.setImage(image);
			validationLabel.setToolTipText(diagnostic.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetValidation() {
		if (validationLabel == null || validationLabel.isDisposed()) {
			return;
		}
		validationLabel.setImage(null);
	}

	@Override
	public void dispose() {

		model.removeListChangeListener(changeListener);
		model.dispose();

		for (WidgetWrapper helper : widgetWrappers) {
			helper.dispose();
		}
		mainComposite.dispose();
	}

	@Override
	public void bindValue() {
		// bind is widget specific
	}

	@Override
	public void setEditable(boolean isEditable) {
		for (ECPSWTAction action : actions) {
			action.setEnabled(isEditable);
		}
	}

}
