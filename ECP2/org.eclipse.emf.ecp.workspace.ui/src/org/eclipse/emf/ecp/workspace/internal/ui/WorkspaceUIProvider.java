/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.workspace.internal.ui;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.wizards.NewModelElementWizard;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;
import org.eclipse.emf.ecp.ui.common.CompositeFactory;
import org.eclipse.emf.ecp.ui.common.SelectionComposite;
import org.eclipse.emf.ecp.workspace.internal.core.ResourceWrapper;
import org.eclipse.emf.ecp.workspace.internal.core.WorkspaceProvider;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.actions.NewWizardMenu;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import java.util.HashSet;

/**
 * @author Eike Stepper
 */
public class WorkspaceUIProvider extends DefaultUIProvider {
	private static final ILabelProvider WORKBENCH_LABEL_PROVIDER = new WorkbenchLabelProvider();

	/**
	 * Default constructor of an UIProvider.
	 */
	public WorkspaceUIProvider() {
		super(WorkspaceProvider.NAME);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ResourceWrapper) {
			ResourceWrapper<?> wrapper = (ResourceWrapper<?>) element;
			return WORKBENCH_LABEL_PROVIDER.getText(wrapper.getDelegate());
		}

		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ResourceWrapper) {
			ResourceWrapper<?> wrapper = (ResourceWrapper<?>) element;
			return WORKBENCH_LABEL_PROVIDER.getImage(wrapper.getDelegate());
		}

		return super.getImage(element);
	}

	@Override
	public void fillContextMenu(IMenuManager manager, ECPModelContext context, Object[] elements) {
		if (elements.length == 1) {
			Object element = elements[0];
			if (element instanceof InternalProject) {
				InternalProject project = (InternalProject) element;
				element = WorkspaceProvider.getRootElement(project);
			}

			if (element instanceof IWorkspaceRoot) {
				NewProjectAction action = new NewProjectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				manager.add(action);
			} else if (element instanceof IProject || element instanceof IFolder) {
				NewWizardMenu menu = new NewWizardMenu(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				manager.add(menu);
			}
		}

		super.fillContextMenu(manager, context, elements);
	}

	@Override
	public Control createCheckoutUI(Composite parent, ECPCheckoutSource checkoutSource, ECPProperties projectProperties) {
		// Suppress default properties composite
		return null;
	}

	@Override
	public Control createNewProjectUI(Composite parent) {

		final StackLayout providerStackLayout = new StackLayout();
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		Button createButton = new Button(composite, SWT.RADIO);
		Button importButton = new Button(composite, SWT.RADIO);
		createButton.setText("Create New Project");
		importButton.setText("Import XMI");

		final Composite providerStack = new Composite(composite, SWT.NONE);
		providerStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		providerStack.setLayout(providerStackLayout);
		providerStackLayout.topControl = createNewWorkspaceProjectComposite(providerStack);

		final Composite newComposite = createNewWorkspaceProjectComposite(providerStack);
		final Composite importComposite = createImportWorkspaceProjectComposite(providerStack);

		createButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				providerStackLayout.topControl = newComposite;
				providerStack.layout();
			}
		});

		importButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				providerStackLayout.topControl = importComposite;
				providerStack.layout();
			}
		});

		createButton.setSelection(true);
		return composite;
	}

	private Composite createImportWorkspaceProjectComposite(final Composite composite) {

		final Composite newComposite = new Composite(composite, SWT.NONE);
		newComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		newComposite.setLayout(new GridLayout(3, false));

		final Label label = new Label(newComposite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		label.setText("Select File:");//$NON-NLS-1$

		final Text fileText = new Text(newComposite, SWT.BORDER | SWT.SINGLE);
		fileText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button fileButton = new Button(newComposite, SWT.SINGLE);
		fileButton.setText("Choose");
		fileButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));

		fileButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.OPEN);
				fileDialog.setText("Open");
				String path = fileDialog.open();
				if (path != null) {
					fileText.setText(path);
				}
			}
		});
		return newComposite;
	}

	/**
	 * @param composite
	 */
	private Composite createNewWorkspaceProjectComposite(final Composite composite) {

		final Composite newComposite = new Composite(composite, SWT.NONE);
		newComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		newComposite.setLayout(new GridLayout(3, false));

		final Label dirLabel = new Label(newComposite, SWT.NONE);
		dirLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		dirLabel.setText("Filename:");//$NON-NLS-1$

		final Text fileText = new Text(newComposite, SWT.BORDER | SWT.SINGLE);
		fileText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button fileButton = new Button(newComposite, SWT.SINGLE);
		fileButton.setText("Choose");
		fileButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));

		final Label classLabel = new Label(newComposite, SWT.NONE);
		classLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		classLabel.setText("Root Class:");//$NON-NLS-1$

		final Text classText = new Text(newComposite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		classText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button classButton = new Button(newComposite, SWT.SINGLE);
		classButton.setText("Choose");
		classButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));

		fileButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.SAVE);
				fileDialog.setText("Open");
				// fd.setFilterPath("C:/");
				// String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
				// fd.setFilterExtensions(filterExt);
				String path = fileDialog.open();
				if (path != null) {
					fileText.setText(path);
				}
			}
		});

		classButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				EClass result = openClassSelectionDialog(composite);

				if (result != null) {
					classText.setText(result.getEPackage().getNsURI() + "/" + result.getName());
				}
			}
		});

		return newComposite;
	}

	private EClass openClassSelectionDialog(Composite composite) {
		SelectionComposite helper = CompositeFactory.getSelectModelClassComposite(new HashSet<EPackage>(),
			ECPUtil.getAllRegisteredEPackages(), new HashSet<EClass>());

		NewModelElementWizard wizard = new NewModelElementWizard("Choose Root Class");
		wizard.setCompositeProvider(helper);

		WizardDialog wd = new WizardDialog(composite.getShell(), wizard);

		int wizardResult = wd.open();
		if (wizardResult == WizardDialog.OK) {
			Object[] selection = helper.getSelection();
			if (selection == null || selection.length == 0) {
				return null;
			}
			return (EClass) selection[0];
		}
		return null;
	}
}
