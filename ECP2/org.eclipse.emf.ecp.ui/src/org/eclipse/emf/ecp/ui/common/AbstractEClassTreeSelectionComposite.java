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
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.model.MEClassLabelProvider;
import org.eclipse.emf.ecp.ui.model.ModelClassFilter;
import org.eclipse.emf.ecp.ui.model.ModelTreeContentProvider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import java.util.Collection;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class AbstractEClassTreeSelectionComposite extends AbstractFilteredSelectionComposite<TreeViewer> {

	private final ModelClassFilter filter = new ModelClassFilter();
	private final Collection<EPackage> ePackages;

	private final Collection<EPackage> unsupportedEPackages;

	private final Collection<EPackage> filteredEPackages;

	private final Collection<EClass> filteredEClasses;

	public AbstractEClassTreeSelectionComposite(Collection<EPackage> ePackages,
		Collection<EPackage> unsupportedEPackages, Collection<EPackage> filteredEPackages,
		Collection<EClass> filteredEClasses) {
		super();
		this.ePackages = ePackages;
		this.unsupportedEPackages = unsupportedEPackages;
		this.filteredEPackages = filteredEPackages;
		this.filteredEClasses = filteredEClasses;
	}

	/**
	 * @param project
	 */
	public AbstractEClassTreeSelectionComposite(ECPProject project) {
		this(ECPUtil.getAllRegisteredEPackages(), project.getUnsupportedEPackages(), project.getVisiblePackages(),
			project.getVisibleEClasses());
	}

	private ILabelProvider getLabelProvider() {
		return new MEClassLabelProvider();
	}

	private ITreeContentProvider getContentProvider() {
		return new ModelTreeContentProvider(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
	}

	private Object getInput() {
		// give an empty object, otherwise it does not initialize
		return new Object();
	}

	@Override
	protected ECPViewerFilter getFilter() {
		return filter;
	}

	protected abstract boolean isCheckedTree();

	@Override
	protected TreeViewer createViewer(Composite composite) {
		TreeViewer viewer = null;
		if (isCheckedTree()) {
			viewer = createCheckedTreeViewer(composite);
		} else {
			viewer = createTreeViewer(composite);
		}
		return viewer;
	}

	private TreeViewer createTreeViewer(Composite composite) {
		return TreeViewerFactory.createTreeViewer(composite, getLabelProvider(), getContentProvider(), getInput(),
			null, true);
	}

	private TreeViewer createCheckedTreeViewer(Composite composite) {
		return TreeViewerFactory.createCheckedTreeViewer(composite, getLabelProvider(), getContentProvider(),
			getInput(), null, true);
	}

	@Override
	protected void expandViewer() {
		getViewer().expandAll();
	}

	@Override
	protected void collapsViewer() {
		getViewer().collapseAll();
	}
}
