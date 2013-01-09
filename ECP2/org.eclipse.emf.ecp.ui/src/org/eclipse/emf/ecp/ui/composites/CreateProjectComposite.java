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
package org.eclipse.emf.ecp.ui.composites;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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
public class CreateProjectComposite implements ICompositeProvider {

	/**
	 * Listener interface that will be notified if the projectName or the selected provider changes.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	public interface CreateProjectChangeListener {
		/**
		 * Callback method providing the new project name.
		 * 
		 * @param projectName the new project name
		 */
		void projectNameChanged(String projectName);

		/**
		 * Callback method providing the new provide.
		 * 
		 * @param provider the new selected provider.
		 */
		void providerChanged(ECPProvider provider);
	}

	/**
	 * Constructor for the Project composite.
	 * 
	 * @param providers list of valid providers
	 */
	public CreateProjectComposite(List<ECPProvider> providers) {
		this.providers = providers;
	}

	private CreateProjectChangeListener listener;

	private final List<ECPProvider> providers;

	private ECPProvider provider;

	private String projectName;

	/** {@inheritDoc} **/
	public Composite createUI(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));
		if (providers.size() > 1) {
			Label label = new Label(composite, SWT.NONE);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			label.setText(Messages.UICreateProject_ProjectProvider + ":");//$NON-NLS-1$

			ComboViewer providersViewer = new ComboViewer(composite, SWT.NONE);
			Combo combo = providersViewer.getCombo();
			GridData gdCombo = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
			gdCombo.minimumWidth = 150;
			combo.setLayoutData(gdCombo);
			providersViewer.setContentProvider(new ArrayContentProvider());
			providersViewer.setLabelProvider(new ProvidersLabelProvider());
			providersViewer.setSorter(new ViewerSorter());
			providersViewer.setInput(providers);
			providersViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					provider = (ECPProvider) selection.getFirstElement();
					if (listener != null) {
						listener.providerChanged(provider);
					}
				}
			});
			if (providers.size() > 1) {
				providersViewer.setSelection(new StructuredSelection(providers.get(0)));
			}
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

		return composite;
	}

	/**
	 * @return the provider
	 */
	public ECPProvider getProvider() {
		return provider;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(CreateProjectChangeListener listener) {
		this.listener = listener;
	}

	/** {@inheritDoc} **/
	public void dispose() {
	}
}
