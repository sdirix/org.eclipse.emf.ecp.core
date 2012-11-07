/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
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
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Eugen Neufeld
 * 
 */
public class CheckedSelectModelClassComposite extends AbstractEClassTreeSelectionComposite {
	private Object[] checked = null;

	private Object[] initialSelection = null;

	/**
	 * @param ePackages
	 * @param unsupportedEPackages
	 * @param filteredEPackages
	 * @param filteredEClasses
	 */
	public CheckedSelectModelClassComposite(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
		Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses) {
		super(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
	}

	@Override
	protected boolean isCheckedTree() {
		return true;
	}

	@Override
	public Composite createUI(Composite parent) {
		Composite composite = super.createUI(parent);
		Composite buttons = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(buttons);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(buttons);

		Button buttonAll = new Button(buttons, SWT.PUSH);
		buttonAll.setText(Messages.CheckedModelElementHelper_SelectAllLabel);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(buttonAll);
		buttonAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getViewer().setCheckedElements(
					((IStructuredContentProvider) getViewer().getContentProvider()).getElements(new Object()));
				setChecked();
			}
		});
		Button buttonNone = new Button(buttons, SWT.PUSH);
		buttonNone.setText(Messages.CheckedModelElementHelper_DeselectAllLabel);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(buttonNone);
		buttonNone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getViewer().setCheckedElements(new Object[0]);
				setChecked();
			}
		});
		getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setChecked();
			}
		});

		if (initialSelection != null) {
			getViewer().setCheckedElements(initialSelection);
		}
		return composite;
	}

	/**
	 * @return the treeViewer
	 */
	@Override
	public CheckboxTreeViewer getViewer() {
		return (CheckboxTreeViewer) super.getViewer();
	}

	/**
	   * 
	   */
	private void setChecked() {
		List<Object> objects = new ArrayList<Object>(Arrays.asList(getViewer().getCheckedElements()));
		objects.removeAll(Arrays.asList(getViewer().getGrayedElements()));
		checked = objects.toArray();
	}

	public Object[] getChecked() {

		return checked;

	}

	public void setInitialSelection(Object[] selection) {
		if (getViewer() != null) {
			getViewer().setCheckedElements(selection);
		} else {
			initialSelection = selection;
		}
	}
}
