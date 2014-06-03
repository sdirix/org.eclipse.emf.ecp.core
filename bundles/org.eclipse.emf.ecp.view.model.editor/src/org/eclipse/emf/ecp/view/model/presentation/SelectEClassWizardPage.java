/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.presentation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Alexandra
 * 
 */
public class SelectEClassWizardPage extends WizardPage {
	private Composite container;
	private Button generateViewModelChkBox;
	private IFile selectedEcore;

	/**
	 * @return the selectedEcore
	 */
	public IFile getSelectedEcore() {
		return selectedEcore;
	}

	/**
	 * @param selectedEcore the selectedEcore to set
	 */
	public void setSelectedEcore(IFile selectedEcore) {
		this.selectedEcore = selectedEcore;
	}

	private EClass selectedEClass;
	private TreeViewer treeViewer;

	/**
	 * @param selectedEClass the selectedEClass to set
	 */
	public void setSelectedEClass(EClass selectedEClass) {
		this.selectedEClass = selectedEClass;
	}

	public SelectEClassWizardPage() {
		super("Select EClass"); //$NON-NLS-1$
		setTitle("Select EClass"); //$NON-NLS-1$
		setDescription("Select an EClass for the current View Model."); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (treeViewer != null && selectedEcore != null) {
			treeViewer.setInput(selectedEcore);
			treeViewer.expandToLevel(2);
		}
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 5;
		container.setLayout(layout);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);
		layout.numColumns = 1;

		final Label label1 = new Label(container, SWT.NONE);
		label1.setText("Select an EClass:"); //$NON-NLS-1$

		if (selectedEcore != null) {

			final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new ReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
			final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
			treeViewer = new TreeViewer(container, SWT.H_SCROLL | SWT.V_SCROLL);
			treeViewer.setContentProvider(getContentProvider(adapterFactory));
			treeViewer.setLabelProvider(labelProvider);
			treeViewer.setInput(selectedEcore);
			treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (((IStructuredSelection) event.getSelection()).getFirstElement() instanceof EClass) {
						setSelectedEClass((EClass) ((IStructuredSelection) event.getSelection()).getFirstElement());
					} else {
						setSelectedEClass(null);
					}
					setPageComplete(getSelectedEClass() != null);
				}
			});
			treeViewer.expandToLevel(2);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(SWT.DEFAULT, 200)
				.applyTo(treeViewer.getControl());
			container.layout(true);
			container.pack();

		}

		generateViewModelChkBox = new Button(container, SWT.CHECK);
		generateViewModelChkBox.setText("Fill view model with default layout"); //$NON-NLS-1$
		generateViewModelChkBox.setSelection(true);

		setControl(container);
		setPageComplete(false);
	}

	private ITreeContentProvider getContentProvider(final AdapterFactory adapterFactory) {
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}

			@Override
			public boolean hasChildren(Object element) {

				if (EPackage.class.isInstance(element)) {
					return true;
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}

			private final ResourceSetImpl resourceSet = new ResourceSetImpl();

			@Override
			public Object[] getChildren(Object parentElement) {
				if (EPackage.class.isInstance(parentElement)) {
					final Set<Object> children = new LinkedHashSet<Object>();
					children.addAll(EPackage.class.cast(parentElement).getESubpackages());
					for (final EObject obj : EPackage.class.cast(parentElement).getEClassifiers()) {
						if (EClass.class.isInstance(obj)) {
							children.add(obj);
						}
					}
					return children.toArray();
				}
				if (IFile.class.isInstance(parentElement))
				{
					final String path = ((IFile) parentElement).getFullPath().toString();
					final URI uri = URI.createPlatformResourceURI(path, true);
					parentElement = resourceSet.getResource(uri, true);

					final ITreeItemContentProvider treeItemContentProvider =
						(ITreeItemContentProvider) adapterFactory.adapt(parentElement, ITreeItemContentProvider.class);
					return (treeItemContentProvider != null ?
						treeItemContentProvider.getChildren(parentElement) :
						Collections.EMPTY_LIST).toArray();
				}
				return null;
			}
		};
	}

	public boolean isGenerateViewModelOptionSelected() {
		return generateViewModelChkBox.getSelection();
	}

	public EClass getSelectedEClass() {
		return selectedEClass;
	}

}
