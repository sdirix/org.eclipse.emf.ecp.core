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

import org.eclipse.emf.ecp.ui.util.Messages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractFilteredSelectionComposite<T extends StructuredViewer> implements ICompositeProvider {

	private T viewer;

	private Object[] selection;

	public AbstractFilteredSelectionComposite() {
		super();
	}

	public Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(composite);

		Label filterLabel = new Label(composite, SWT.LEFT);
		filterLabel.setText(Messages.AbstractModelElementHelper_FilterLabel);
		final Text filterInput = new Text(composite, SWT.SEARCH);
		filterInput.setMessage(Messages.AbstractModelElementHelper_FilterText);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(filterInput);

		viewer = createViewer(composite);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).minSize(0, 150).span(2, 1)
			.applyTo(viewer.getControl());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				selection = ((IStructuredSelection) viewer.getSelection()).toArray();
			}
		});
		viewer.addFilter(getFilter());

		filterInput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = filterInput.getText();
				getFilter().setSearchTerm(text);
				expandViewer();
				if (text != null && text.length() == 0) {
					collapsViewer();
				}
				viewer.refresh();
			}
		});

		return composite;
	}

	/**
	 * 
	 */
	protected void expandViewer() {

	}

	protected void collapsViewer() {
	}

	/**
	 * @return the viewer
	 */
	public T getViewer() {
		return viewer;
	}

	/**
	 * @return the selection
	 */
	public Object[] getSelection() {
		return selection;
	}

	protected abstract T createViewer(Composite composite);

	/**
	 * @return
	 */
	protected abstract ECPViewerFilter getFilter();
}
