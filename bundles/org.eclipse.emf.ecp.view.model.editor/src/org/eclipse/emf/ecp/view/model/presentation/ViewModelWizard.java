/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.model.presentation;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecp.ide.view.service.IDEViewModelRegistry;
import org.eclipse.emf.ecp.view.editor.handler.ControlGenerator;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.osgi.framework.ServiceReference;

/**
 * This is a simple wizard for creating a new model file. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ViewModelWizard extends Wizard implements INewWizard {

	private static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.model.presentation"; //$NON-NLS-1$
	private IFile selectedEcore;

	/**
	 * @param selectedEcore the selectedEcore to set
	 */
	public void setSelectedEcore(IFile selectedEcore) {
		this.selectedEcore = selectedEcore;
	}

	private EClass selectedEClass;

	/**
	 * The supported extensions for created files. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS = Collections
		.unmodifiableList(Arrays.asList(ViewEditorPlugin.INSTANCE
			.getString("_UI_ViewEditorFilenameExtensions").split( //$NON-NLS-1$
				"\\s*,\\s*"))); //$NON-NLS-1$

	/**
	 * A formatted list of supported file extensions, suitable for display. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS = ViewEditorPlugin.INSTANCE
		.getString("_UI_ViewEditorFilenameExtensions").replaceAll( //$NON-NLS-1$
			"\\s*,\\s*", ", "); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * This caches an instance of the model package. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VViewPackage viewPackage = VViewPackage.eINSTANCE;

	/**
	 * This caches an instance of the model factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VViewFactory viewFactory = viewPackage.getViewFactory();

	/**
	 * This is the file creation page. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected ViewModelWizardNewFileCreationPage newFileCreationPage;

	private IncludeViewModelProviderXmiFileExtensionPage contributeToFileExtensionPage;

	/**
	 * Remember the selection during initialization for populating the default
	 * container. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * Remember the workbench during initialization. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IWorkbench workbench;

	/**
	 * Caches the names of the types that can be created as the root object.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected List<String> initialObjectNames;

	private SelectEcorePage selectEcorePage;
	private SelectEClassWizardPage selectEClassPage;

	/**
	 * This just records the information. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(ViewEditorPlugin.INSTANCE.getString("_UI_Wizard_label")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE
			.getImageDescriptor(ViewEditorPlugin.INSTANCE
				.getImage("full/wizban/NewView"))); //$NON-NLS-1$
	}

	/**
	 * Returns the names of the types that can be created as the root object.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<String> getInitialObjectNames() {
		if (initialObjectNames == null) {
			initialObjectNames = new ArrayList<String>();
			for (final EClassifier eClassifier : viewPackage.getEClassifiers()) {
				if (eClassifier instanceof EClass) {
					final EClass eClass = (EClass) eClassifier;
					if (!eClass.isAbstract()) {
						initialObjectNames.add(eClass.getName());
					}
				}
			}
			Collections.sort(initialObjectNames,
				CommonPlugin.INSTANCE.getComparator());
		}
		return initialObjectNames;
	}

	/**
	 * The framework calls this to create the contents of the wizard. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addPages() {

		selectEcorePage = new SelectEcorePage(PLUGIN_ID);
		addPage(selectEcorePage);

		newFileCreationPage = getNewFileCreationPage();
		addPage(newFileCreationPage);

		contributeToFileExtensionPage = new IncludeViewModelProviderXmiFileExtensionPage(PLUGIN_ID);
		addPage(contributeToFileExtensionPage);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getStartingPage()
	 */
	@Override
	public IWizardPage getStartingPage() {
		if (selectedEcore == null)
		{
			return selectEcorePage;
		}

		selectEClassPage = new SelectEClassWizardPage();
		selectEClassPage.setSelectedEcore(selectedEcore);
		selectEClassPage.setPageComplete(true);
		addPage(selectEClassPage);
		return selectEClassPage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page == selectEcorePage) {
			selectedEClass = null;
			selectedEcore = selectEcorePage.getSelectedEcore();
			if (selectedEcore != null) {
				if (selectEClassPage == null) {
					selectEClassPage = new SelectEClassWizardPage();
					selectEClassPage.setPageComplete(true);
					addPage(selectEClassPage);
				}
				selectEClassPage.setSelectedEcore(selectedEcore);
				return selectEClassPage;
			}
			return null;
		}
		if (page == selectEClassPage) {
			selectedEClass = selectEClassPage.getSelectedEClass();
			if (selectedEClass != null) {
				newFileCreationPage.setSelectedEClassName(selectedEClass.getName());
				return newFileCreationPage;
			}
		}
		if (page == newFileCreationPage) {
			return contributeToFileExtensionPage;
		}
		return null;
	}

	/**
	 * @return
	 */
	private ViewModelWizardNewFileCreationPage getNewFileCreationPage() {
		// Create page, set title and the initial model file name.
		newFileCreationPage = new ViewModelWizardNewFileCreationPage("Whatever", selection); //$NON-NLS-1$
		newFileCreationPage.setTitle(ViewEditorPlugin.INSTANCE
			.getString("_UI_ViewModelWizard_label")); //$NON-NLS-1$
		newFileCreationPage.setDescription(ViewEditorPlugin.INSTANCE
			.getString("_UI_ViewModelWizard_description")); //$NON-NLS-1$
		newFileCreationPage.setFileName(ViewEditorPlugin.INSTANCE
			.getString("_UI_ViewEditorFilenameDefaultBase") //$NON-NLS-1$
			+ "." //$NON-NLS-1$
			+ FILE_EXTENSIONS.get(0));

		return newFileCreationPage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {

		if (page == selectEClassPage) {
			return selectEcorePage;
		}
		else if (page == newFileCreationPage) {
			return selectEClassPage;
		}

		return null;

	}

	/**
	 * Get the file from the page. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IFile getModelFile() {
		return newFileCreationPage.getModelFile();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		if (selectEClassPage != null) {
			selectedEClass = selectEClassPage.getSelectedEClass();
			return selectedEClass != null
				&& (newFileCreationPage.isPageComplete() || contributeToFileExtensionPage.isPageComplete());
		}
		return false;
	}

	/**
	 * Do the work after everything is specified. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			final IFile modelFile = getModelFile();

			// Do the work within an operation.
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor progressMonitor) {
					try {

						final boolean generateViewModelControls = selectEClassPage
							.isGenerateViewModelOptionSelected();

						final IDEViewModelRegistry registry = getViewModelRegistry();
						if (registry == null) {
							ViewEditorPlugin.INSTANCE.log(new Status(IStatus.ERROR, PLUGIN_ID,
								"No View Model Registry", null)); //$NON-NLS-1$
							return;
						}
						// create view
						final VView view = registry.createViewModel(modelFile, selectedEClass, selectedEcore);
						// generate controls
						if (generateViewModelControls) {
							ControlGenerator.generateAllControls(view);
						}

						// contribute to ..provider.xmi.file extension point
						if (contributeToFileExtensionPage.isContributeToExtensionOptionSelected()) {
							addContribution(modelFile);
						}

						// Open the view
						final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
						page.openEditor(new FileEditorInput(modelFile),
							workbench.getEditorRegistry().getDefaultEditor(modelFile.getFullPath().toString()).getId());

					} catch (final Exception exception) {
						ViewEditorPlugin.INSTANCE.log(exception);
					} finally {
						progressMonitor.done();
					}
				}
			};

			getContainer().run(false, false, operation);

			// Select the new file resource in the current view.
			final IWorkbenchWindow workbenchWindow = workbench
				.getActiveWorkbenchWindow();
			final IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(
					modelFile);
				getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						((ISetSelectionTarget) activePart)
							.selectReveal(targetSelection);
					}
				});
			}
			return true;
		} catch (final Exception exception) {
			ViewEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}

	/**
	 * Return the {@link IDEViewModelRegistry}.
	 * 
	 * @return the {@link IDEViewModelRegistry}
	 */
	public static IDEViewModelRegistry getViewModelRegistry() {

		final ServiceReference<IDEViewModelRegistry> serviceReference = ViewEditorPlugin.getPlugin().getBundle()
			.getBundleContext()
			.getServiceReference(IDEViewModelRegistry.class);
		if (serviceReference == null) {
			return null;
		}
		return ViewEditorPlugin.getPlugin().getBundle().getBundleContext().getService(serviceReference);
	}

	/**
	 * @param workbench the workbench to set
	 */
	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	/**
	 * @param modelFile
	 */
	protected void addContribution(IFile modelFile) {

		final IProject project = modelFile.getProject();
		final IFile pluginFile = project.getFile("plugin.xml"); //$NON-NLS-1$
		try {
			final BufferedReader in = new BufferedReader(new InputStreamReader(pluginFile.getContents()));
			final String extension = "org.eclipse.emf.ecp.view.model.provider.xmi.file"; //$NON-NLS-1$
			String line = null;
			final StringBuffer contents = new StringBuffer();
			boolean extensionAdded = false;
			while ((line = in.readLine()) != null) {
				if (line.contains(extension)) {
					final int end = line.indexOf("/>"); //$NON-NLS-1$
					if (end != -1) {
						final String filePathAttribute = "<file filePath=\"" //$NON-NLS-1$
							+ modelFile.getProjectRelativePath().toString() + "\"/>"; //$NON-NLS-1$

						line = line.substring(0, end)
							+ ">\n" + filePathAttribute + "\n</extension>\n" + line.substring(end + 2, line.length()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					else {
						final String filePathAttribute = "<file filePath=\"" //$NON-NLS-1$
							+ modelFile.getProjectRelativePath().toString() + "\"/>"; //$NON-NLS-1$
						line = line.concat("\n" + filePathAttribute + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					extensionAdded = true;
				}
				if (line.contains("</plugin>") && !extensionAdded) { //$NON-NLS-1$
					final int end = line.indexOf("</plugin>"); //$NON-NLS-1$
					line = line.substring(0, end)
						+ "\n<extension  point=\"org.eclipse.emf.ecp.view.model.provider.xmi.file\">\n<file filePath=\"" //$NON-NLS-1$
						+ modelFile.getProjectRelativePath().toString() + "\"/>\n</extension>\n" + line.substring(end); //$NON-NLS-1$
				}

				contents.append(line + "\n"); //$NON-NLS-1$
			}
			in.close();

			final FileWriter out = new FileWriter(pluginFile.getRawLocation().makeAbsolute().toFile());
			out.write(String.valueOf(contents));
			out.flush();
			out.close();

			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (final CoreException e) {
			ViewEditorPlugin.INSTANCE.log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
		} catch (final IOException e) {
			ViewEditorPlugin.INSTANCE.log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
		}
	}
}
