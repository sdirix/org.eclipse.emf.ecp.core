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

import org.eclipse.net4j.util.ObjectUtil;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ProvidersContentProvider;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;
import org.eclipse.emf.ecp.ui.util.Messages;

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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugen Neufeld
 */
public class AddRepositoryComposite implements ICompositeProvider {

	public interface AddRepositoryChangeListener {
		public void repositoryNameChanged(String repositoryName);

		public void repositoryLabelChanged(String repositoryLabel);

		public void repositoryDescriptionChanged(String repositoryDescription);

		public void repositoryProviderChanged(ECPProvider provider);
	}

	public AddRepositoryComposite() {

	}

	public AddRepositoryComposite(ECPProvider provider) {
		this.provider = provider;
	}

	private Composite providerStack;

	private StackLayout providerStackLayout;

	private Text repositoryLabelText;

	private Text repositoryDescriptionText;

	private Text repositoryNameText;

	private AddRepositoryChangeListener listener;

	private ECPProvider provider;

	private String repositoryName;

	private String repositoryLabel;

	private String repositoryDescription;

	public Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));

		if (provider == null) {
			Label label = new Label(composite, SWT.NONE);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			label.setText(Messages.AddRepositoryComposite_RepositoryProvider);

			ComboViewer providersViewer = new ComboViewer(composite, SWT.NONE);
			Combo combo = providersViewer.getCombo();
			GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_combo.widthHint = 150;
			combo.setLayoutData(gd_combo);
			providersViewer.setContentProvider(new ProvidersContentProvider(true));
			providersViewer.setLabelProvider(new ProvidersLabelProvider());
			providersViewer.setSorter(new ViewerSorter());
			providersViewer.setInput(ECPProviderRegistry.INSTANCE);
			providersViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					provider = (ECPProvider) selection.getFirstElement();
					if (provider != null) {
						showProviderUI(provider);
						if (listener != null) {
							listener.repositoryProviderChanged(provider);
						}
					}
				}
			});

			for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
				if (provider.canAddRepositories()) {
					providersViewer.setSelection(new StructuredSelection(provider));
					break;
				}
			}
		}

		GridData gd_providerStack = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_providerStack.heightHint = 136;
		providerStackLayout = new StackLayout();
		providerStack = new Composite(composite, SWT.NONE);
		providerStack.setLayout(providerStackLayout);
		providerStack.setLayoutData(gd_providerStack);
		{
			Label repositoryNameLabel = new Label(composite, SWT.NONE);
			repositoryNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			repositoryNameLabel.setText(Messages.AddRepositoryComposite_RepositoryName);

			repositoryNameText = new Text(composite, SWT.BORDER);
			// repositoryNameText.setText(repositoryName);
			repositoryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			repositoryNameText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					boolean labelUnchanged = ObjectUtil.equals(repositoryName, repositoryLabel);
					repositoryName = repositoryNameText.getText();
					if (labelUnchanged) {
						repositoryLabelText.setText(repositoryName);
					}
					if (listener != null) {
						listener.repositoryNameChanged(repositoryName);
					}
				}
			});
		}
		{
			Label repositoryLabelLabel = new Label(composite, SWT.NONE);
			repositoryLabelLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			repositoryLabelLabel.setText(Messages.AddRepositoryComposite_RepositoryLabel);

			repositoryLabelText = new Text(composite, SWT.BORDER);
			// repositoryLabelText.setText(repositoryLabel);
			repositoryLabelText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			repositoryLabelText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					repositoryLabel = repositoryLabelText.getText();
					if (listener != null) {
						listener.repositoryLabelChanged(repositoryLabel);
					}
				}
			});
		}
		{
			Label repositoryDescriptionLabel = new Label(composite, SWT.NONE);
			repositoryDescriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			repositoryDescriptionLabel.setText(Messages.AddRepositoryComposite_RepositoryDescription);

			repositoryDescriptionText = new Text(composite, SWT.BORDER);
			// repositoryDescriptionText.setText(repositoryDescription);
			GridData gd_repositoryDescriptionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_repositoryDescriptionText.heightHint = 36;
			repositoryDescriptionText.setLayoutData(gd_repositoryDescriptionText);
			repositoryDescriptionText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					repositoryDescription = repositoryDescriptionText.getText();
					if (listener != null) {
						listener.repositoryDescriptionChanged(repositoryDescription);
					}
				}
			});
		}

		if (provider != null) {
			showProviderUI(provider);
		}

		return composite;
	}

	private Map<String, Control> providerControls = new HashMap<String, Control>();

	private Map<String, ECPProperties> providerProperties = new HashMap<String, ECPProperties>();

	protected void showProviderUI(ECPProvider provider) {
		if (providerStack == null) {
			return;
		}
		String name = provider.getName();
		Control control = providerControls.get(name);
		if (control == null) {
			ECPProperties properties = ECPUtil.createProperties();

			UIProvider uiProvider = UIProviderRegistry.INSTANCE.getUIProvider(name);
			control = uiProvider.createAddRepositoryUI(providerStack, properties, repositoryNameText,
				repositoryLabelText, repositoryDescriptionText);
			providerControls.put(name, control);
			providerProperties.put(name, properties);
		}

		providerStackLayout.topControl = control;
		providerStack.layout();
	}

	/**
	 * @return
	 */
	public ECPProvider getProvider() {
		return provider;
	}

	/**
	 * @return
	 */
	public String getRepositoryName() {
		return repositoryName;
	}

	/**
	 * @return
	 */
	public String getRepositoryDescription() {
		return repositoryDescription;
	}

	/**
	 * @return
	 */
	public ECPProperties getProperties() {
		if (provider == null) {
			return null;
		}

		return providerProperties.get(provider.getName());
	}

	/**
	 * @return
	 */
	public String getRepositoryLabel() {
		return repositoryLabel;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(AddRepositoryChangeListener listener) {
		this.listener = listener;
	}
}
