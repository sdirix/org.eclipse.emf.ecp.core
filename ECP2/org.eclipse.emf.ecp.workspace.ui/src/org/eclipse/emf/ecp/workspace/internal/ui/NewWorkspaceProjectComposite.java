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
package org.eclipse.emf.ecp.workspace.internal.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.wizards.SelectModelElementWizard;
import org.eclipse.emf.ecp.ui.common.CompositeFactory;
import org.eclipse.emf.ecp.ui.common.CompositeStateObserver;
import org.eclipse.emf.ecp.ui.common.SelectionComposite;
import org.eclipse.emf.ecp.workspace.internal.core.WorkspaceProvider;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import java.io.IOException;
import java.util.HashSet;

/**
 * The NewWorkspaceProjectComposite allows a user to provide the information needed for the
 * creation of a WorkspaceProject.
 * 
 * @author Tobias Verhoeven
 */
public class NewWorkspaceProjectComposite extends Composite {

	private CompositeStateObserver compositeStateObserver;
	private ECPProperties properties;
	private boolean complete;

	private Text newFileText;
	private Text rootClassText;
	private Text importFileText;
	private Button createButton;
	private Button importButton;
	private StackLayout providerStackLayout;
	private Composite providerStackComposite;
	private Composite newProjectComposite;
	private Composite importProjectComposite;
	private Label seperator;

	private Resource resource;
	private EClass eClass;

	/**
	 * Instantiates a new new workspace project composite.
	 * 
	 * @param parent a widget which will be the parent of the new instance (cannot be null)
	 * @param observer the observer
	 * @param projectProperties the project properties
	 */
	public NewWorkspaceProjectComposite(Composite parent, CompositeStateObserver observer,
		ECPProperties projectProperties) {
		super(parent, SWT.NONE);
		properties = projectProperties;
		compositeStateObserver = observer;
		setLayout(new GridLayout(2, false));
		createStackComposite(parent);
		notifyObserver();
	}

	private void createStackComposite(Composite parent) {

		providerStackLayout = new StackLayout();

		createButton = new Button(this, SWT.RADIO);
		createButton.setText("Create New Project");
		importButton = new Button(this, SWT.RADIO);
		importButton.setText("Import XMI");

		providerStackComposite = new Composite(this, SWT.NONE);
		providerStackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		providerStackComposite.setLayout(providerStackLayout);

		createNewWorkspaceProjectComposite(providerStackComposite);
		createImportWorkspaceProjectComposite(providerStackComposite);

		providerStackLayout.topControl = newProjectComposite;

		createButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				providerStackLayout.topControl = newProjectComposite;
				checkStatusChanged();
				providerStackComposite.layout();
			}
		});

		importButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				providerStackLayout.topControl = importProjectComposite;
				checkStatusChanged();
				providerStackComposite.layout();
			}
		});

		createButton.setSelection(true);
	}

	private void createImportWorkspaceProjectComposite(final Composite composite) {

		importProjectComposite = new Composite(composite, SWT.NONE);
		importProjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		importProjectComposite.setLayout(new GridLayout(3, false));

		final Label selectFileLabel = new Label(importProjectComposite, SWT.NONE);
		selectFileLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		selectFileLabel.setText("Select File:");//$NON-NLS-1$

		importFileText = new Text(importProjectComposite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		importFileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkStatusChanged();
			}
		});
		importFileText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button importFileButton = new Button(importProjectComposite, SWT.SINGLE);
		importFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.OPEN);
				fileDialog.setText("Open");
				String path = fileDialog.open();
				if (path != null) {
					importFileText.setText(path);
				}
			}
		});
		importFileButton.setText("Choose");
		importFileButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));
	}

	/**
	 * @param composite
	 */
	private void createNewWorkspaceProjectComposite(final Composite composite) {

		newProjectComposite = new Composite(composite, SWT.NONE);
		newProjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		newProjectComposite.setLayout(new GridLayout(3, false));

		final Label filenameLabel = new Label(newProjectComposite, SWT.NONE);
		filenameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		filenameLabel.setText("Filename:");//$NON-NLS-1$

		newFileText = new Text(newProjectComposite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		newFileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkStatusChanged();
			}
		});
		newFileText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button newFileButton = new Button(newProjectComposite, SWT.SINGLE);
		newFileButton.setText("Choose");
		newFileButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));

		final Label rootClassLabel = new Label(newProjectComposite, SWT.NONE);
		rootClassLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		rootClassLabel.setText("Root Class:");//$NON-NLS-1$

		rootClassText = new Text(newProjectComposite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		rootClassText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkStatusChanged();
			}
		});
		rootClassText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		Button rootClassButton = new Button(newProjectComposite, SWT.SINGLE);
		rootClassButton.setText("Choose");
		rootClassButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));

		newFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.SAVE);
				fileDialog.setText("Open");
				String path = fileDialog.open();
				if (path != null) {

					ResourceSet resourceSet = new ResourceSetImpl();
					resource = resourceSet.createResource(URI.createFileURI(path));

					fillResource();
					newFileText.setText(path);
				}
			}
		});

		rootClassButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				eClass = openClassSelectionDialog(composite);
				fillResource();
				if (eClass != null) {
					rootClassText.setText(eClass.getEPackage().getNsURI() + "/" + eClass.getName());
				}
			}
		});
	}

	private EClass openClassSelectionDialog(Composite composite) {

		SelectionComposite helper = CompositeFactory.getSelectModelClassComposite(new HashSet<EPackage>(),
			ECPUtil.getAllRegisteredEPackages(), new HashSet<EClass>());

		SelectModelElementWizard wizard = new SelectModelElementWizard("Choose Root Class", "Choose Root Class",
			"Choose Root Class", "Select the class that will be used as the root of the your new project.");
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

	private void notifyObserver() {
		if (compositeStateObserver != null) {
			compositeStateObserver.compositeChangedState(this, complete, properties);
		}
	}

	private void checkStatusChanged() {

		properties.addProperty(WorkspaceProvider.PROP_ROOT_URI, createButton.getSelection() ? newFileText.getText()
			: importFileText.getText());

		boolean pendingStatus = createButton.getSelection() && newFileTextStatus() && rootClassTextStatus()
			|| importButton.getSelection() && importFileTextStatus();

		if (pendingStatus != complete) {
			complete = pendingStatus;
			notifyObserver();
		}
	}

	private boolean newFileTextStatus() {
		return nonEmptyString(newFileText.getText());
	}

	private boolean importFileTextStatus() {
		return nonEmptyString(importFileText.getText());
	}

	private boolean rootClassTextStatus() {
		return nonEmptyString(rootClassText.getText());
	}

	private boolean nonEmptyString(String string) {
		return string != null && string.length() > 0 && string.trim().length() == string.length();
	}

	private void fillResource() {
		if (resource != null && eClass != null) {
			EObject root = EcoreUtil.create(eClass);

			resource.getContents().clear();

			resource.getContents().add(root);
			try {
				resource.save(null);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}
}
