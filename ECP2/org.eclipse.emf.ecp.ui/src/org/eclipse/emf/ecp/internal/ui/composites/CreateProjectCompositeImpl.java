/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emf.ecp.internal.ui.composites;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.internal.ui.model.ProvidersLabelProvider;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.common.CreateProjectComposite;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.List;

/**
 * This class generates a composite on top of a parent composite containing all ui elements necessary to create an
 * ecpproject.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CreateProjectCompositeImpl implements CreateProjectComposite {

	/**
	 * Constructor for the Project composite.
	 * 
	 * @param providers list of valid providers
	 */
	public CreateProjectCompositeImpl(List<ECPProvider> providers) {
		this.providers = providers;
	}

	private CreateProjectChangeListener listener;

	private final List<ECPProvider> providers;

	private ECPProvider provider;

	private String projectName;

	private StackLayout providerStackLayout;

	private Composite providerStack;

	/** {@inheritDoc} **/
	public Composite createUI(Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));
		if (providers.size() > 1) {
			final Label label = new Label(composite, SWT.NONE);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			label.setText(Messages.UICreateProject_ProjectProvider + ":");//$NON-NLS-1$

			ComboViewer providersViewer = new ComboViewer(composite, SWT.NONE | SWT.READ_ONLY);
			Combo combo = providersViewer.getCombo();
			GridData gdCombo = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
			gdCombo.minimumWidth = 150;
			combo.setLayoutData(gdCombo);

			providersViewer.setContentProvider(new ArrayContentProvider());
			providersViewer.setLabelProvider(new ProvidersLabelProvider());
			providersViewer.setSorter(new ViewerSorter());
			providersViewer.setInput(providers);

			if (providers.size() > 1) {
				providersViewer.setSelection(new StructuredSelection(providers.get(0)));
			}

			providersViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					provider = (ECPProvider) selection.getFirstElement();
					updateUI();
					if (listener != null) {
						listener.providerChanged(provider);
					}
				}

				private void updateUI() {
					UIProvider uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(provider);
					Control newProjectUI = uiProvider.createNewProjectUI(providerStack);

					if (newProjectUI != null) {
						providerStackLayout.topControl = newProjectUI;
					} else {
						if (providerStackLayout.topControl != null) {
							providerStackLayout.topControl = null;
						}
					}
					providerStack.layout();
				}
			});

		} else if (providers.size() == 1) {
			provider = providers.get(0);
		}

		Label labelName = new Label(composite, SWT.NONE);
		labelName.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		labelName.setText(Messages.UICreateProject_ProjectName + ":"); //$//$NON-NLS-1$

		final Text textProjectName = new Text(composite, SWT.BORDER | SWT.SINGLE);
		textProjectName.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));
		textProjectName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				projectName = textProjectName.getText();
				if (projectName.equals("")) //$NON-NLS-1$
				{
					projectName = null;
				}
				if (listener != null) {
					listener.projectNameChanged(projectName);
				}
			}
		});
		providerStackLayout = new StackLayout();
		providerStack = new Composite(composite, SWT.NONE);
		providerStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		providerStack.setLayout(providerStackLayout);
		return composite;
	}

	/** {@inheritDoc} **/
	public ECPProvider getProvider() {
		return provider;
	}

	/** {@inheritDoc} **/
	public String getProjectName() {
		return projectName;
	}

	/** {@inheritDoc} **/
	public void setListener(CreateProjectChangeListener listener) {
		this.listener = listener;
	}

	/** {@inheritDoc} **/
	public void dispose() {
	}

}
