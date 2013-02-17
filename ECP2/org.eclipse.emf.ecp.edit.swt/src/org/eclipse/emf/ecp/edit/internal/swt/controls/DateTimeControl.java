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

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
/**
 * This class defines a DateTimeControl which is used for displaying {@link EStructuralFeature}s which have a date
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public class DateTimeControl extends SingleControl {

	private DateTime dateWidget;

	private DateTime timeWidget;

	private Composite parentComposite;

	private StackLayout sl;

	private Label unsetLabel;
	/**
	 * Constructor for a dateTime control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public DateTimeControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	@Override
	protected void fillInnerComposite(Composite composite) {
		parentComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(parentComposite);
		sl = new StackLayout();
		parentComposite.setLayout(sl);
		final Composite dateTimeComposite = new Composite(parentComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(4).applyTo(dateTimeComposite);

		createDateAndTimeWidget(dateTimeComposite);

		unsetLabel = new Label(parentComposite, SWT.NONE);
		//TODO language
		unsetLabel.setText("No date set! Click to set date.");//$NON-NLS-1$
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);
		unsetLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
				sl.topControl = dateTimeComposite;
				parentComposite.layout(true);
			}

			public void mouseDown(MouseEvent e) {
				//nothing to do
			}

			public void mouseDoubleClick(MouseEvent e) {
				//nothing to do
			}
		});
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			sl.topControl = dateTimeComposite;
		} else {
			sl.topControl = unsetLabel;
		}
		parentComposite.layout();
	}

	/**
	 * This method creates the date widget, the time widget and the delete button.
	 * 
	 * @param composite the parent {@link Composite}
	 */
	private void createDateAndTimeWidget(Composite composite) {
		int numColumns = 3;
		if (isEmbedded()) {
			numColumns = 2;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(0, 0).equalWidth(false).applyTo(composite);
		
		dateWidget = new DateTime(composite, SWT.DATE);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		timeWidget = new DateTime(composite, SWT.TIME | SWT.SHORT);
		timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		if(!isEmbedded()){
			Button unsetdate=new Button(composite, SWT.PUSH);
			unsetdate.setToolTipText("UnsetDate");
			unsetdate.setImage(Activator.getDefault().getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
			unsetdate.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					
					getModelElementContext().getEditingDomain().getCommandStack().execute(
						new RemoveCommand(getModelElementContext().getEditingDomain(), getModelElementContext().getModelElement(),
							getStructuralFeature(), getModelElementContext().getModelElement().eGet(getStructuralFeature())));

					sl.topControl = unsetLabel;
					parentComposite.layout();
				}
				
			});
		}
	}
	
	@Override
	public void setEditable(boolean isEditable) {
		dateWidget.setEnabled(isEditable);
		timeWidget.setEnabled(isEditable);
	}

	@Override
	public void bindValue() {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
		IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
		getDataBindingContext().bindValue(target, getModelValue());
	}

}
