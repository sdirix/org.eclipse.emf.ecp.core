/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila- initial API and implementation
 ******************************************************************************/

package org.eclipse.emf.ecp.view.model.internal.project.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.ui.wizard.AbstractExampleInstallerWizard;
import org.eclipse.emf.common.ui.wizard.ExampleInstallerWizard;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecp.ide.view.service.IDEViewModelRegistry;
import org.eclipse.emf.ecp.view.editor.handler.ControlGenerator;
import org.eclipse.emf.ecp.view.model.presentation.SelectEClassWizardPage;
import org.eclipse.emf.ecp.view.model.presentation.SelectEcorePage;
import org.eclipse.emf.ecp.view.model.presentation.ViewEditorPlugin;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.ServiceReference;

/**
 * @author Alexandra Buzila
 * 
 */
public class NewPluginProjectWizard extends ExampleInstallerWizard {
	private static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.model.internal.project.installer"; //$NON-NLS-1$
	private SelectEClassWizardPage selectEClassPage;
	private EClass selectedEClass;

	private IFile selectedEcore;

	/**
	 * @param selectedEcore
	 *            the selectedEcore to set
	 */
	public void setSelectedEcore(IFile selectedEcore) {
		this.selectedEcore = selectedEcore;
	}

	private WizardNewProjectCreationPage firstPage;
	private SelectEcorePage selectEcorePage;
	private String projectName;
	private IFile viewModelFile;
	/**
	 * Remember the selection during initialization for populating the default
	 * container. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IStructuredSelection selection;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * @param workbench
	 *            the workbench to set
	 */
	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	@Override
	public String getWindowTitle() {
		return "New View Model Project"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.ui.wizard.ExampleInstallerWizard#
	 * loadFromExtensionPoints()
	 */
	@Override
	protected void loadFromExtensionPoints() {

		projectDescriptors = new ArrayList<ProjectDescriptor>();
		filesToOpen = new ArrayList<FileToOpen>();
		final String wizardID = "org.eclipse.emf.ecp.view.model.internal.project.installer.NewVMProjectWizard"; //$NON-NLS-1$

		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			CommonUIPlugin.INSTANCE.getSymbolicName(), "examples"); //$NON-NLS-1$
		final IConfigurationElement[] exampleElements = extensionPoint.getConfigurationElements();
		for (int i = 0; i < exampleElements.length; i++) {
			final IConfigurationElement exampleElement = exampleElements[i];
			if ("example".equals(exampleElement.getName()) //$NON-NLS-1$
				&& wizardID.equals(exampleElement.getAttribute("wizardID"))) { //$NON-NLS-1$
				final IConfigurationElement[] projectDescriptorElements = exampleElement
					.getChildren("projectDescriptor"); //$NON-NLS-1$
				for (int j = 0; j < projectDescriptorElements.length; j++) {

					final IConfigurationElement projectDescriptorElement = projectDescriptorElements[j];
					final String contentURI = projectDescriptorElement
						.getAttribute("contentURI"); //$NON-NLS-1$
					if (projectName != null && contentURI != null) {
						final AbstractExampleInstallerWizard.ProjectDescriptor projectDescriptor =
							new AbstractExampleInstallerWizard.ProjectDescriptor();
						projectDescriptor.setName(projectName);

						URI uri = URI.createURI(contentURI);
						if (uri.isRelative()) {
							uri = URI.createPlatformPluginURI(projectDescriptorElement.getContributor().getName()
								+ "/" + contentURI, true); //$NON-NLS-1$
						}
						projectDescriptor.setContentURI(uri);
						projectDescriptor.setDescription(projectDescriptorElement.getAttribute("description")); //$NON-NLS-1$
						projectDescriptors.add(projectDescriptor);
					}
				}

				if (!projectDescriptors.isEmpty()) {
					final IConfigurationElement[] openElements = exampleElement.getChildren("fileToOpen"); //$NON-NLS-1$
					for (int j = 0; j < openElements.length; j++) {
						final IConfigurationElement openElement = openElements[j];
						final String location = openElement.getAttribute("location"); //$NON-NLS-1$
						if (location != null) {
							final AbstractExampleInstallerWizard.FileToOpen fileToOpen = new AbstractExampleInstallerWizard.FileToOpen();
							fileToOpen.setLocation(location);
							fileToOpen.setEditorID(openElement.getAttribute("editorID")); //$NON-NLS-1$
							filesToOpen.add(fileToOpen);
						}
					}

					// Only one example per wizard
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.common.ui.wizard.AbstractExampleInstallerWizard#addPages
	 * ()
	 */
	@Override
	public void addPages() {
		firstPage = new WizardNewProjectCreationPage("basicNewProjectPage"); //$NON-NLS-1$
		firstPage.setTitle("Project"); //$NON-NLS-1$
		firstPage.setDescription("Create a new View Model Project."); //$NON-NLS-1$
		firstPage.setInitialProjectName(getInitialProjectName());
		addPage(firstPage);

		selectEClassPage = new SelectEClassWizardPage();
		selectEClassPage.setSelectedEcore(selectedEcore);
		addPage(selectEClassPage);
		selectEClassPage.setPageComplete(true);

	}

	private String getInitialProjectName() {

		projectName = null;
		if (selectedEcore != null) {
			final IProject project = ((IResource) selectedEcore).getProject();
			projectName = project.getName() + ".viewmodel"; //$NON-NLS-1$
			for (int i = 1; ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName).exists(); ++i) {
				projectName = project.getName() + ".viewmodel" + i; //$NON-NLS-1$
			}
		}
		return projectName;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.
	 * IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page == firstPage) {
			projectName = firstPage.getProjectName();
			if (selectedEcore == null) {
				selectEcorePage = new SelectEcorePage(PLUGIN_ID);
				addPage(selectEcorePage);
				return selectEcorePage;
			}
			selectEClassPage = new SelectEClassWizardPage();
			selectEClassPage.setSelectedEcore(selectedEcore);
			addPage(selectEClassPage);
			selectEClassPage.setPageComplete(true);
			return selectEClassPage;
		}

		if (page == selectEcorePage) {
			selectedEcore = selectEcorePage.getSelectedEcore();
			selectEClassPage = new SelectEClassWizardPage();
			selectEClassPage.setSelectedEcore(selectedEcore);
			addPage(selectEClassPage);
			selectEClassPage.setPageComplete(true);
			return selectEClassPage;
		}
		return super.getNextPage(page);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if (page == selectEClassPage) {
			// deactivate Finish button
			selectedEClass = null;
		}
		return super.getPreviousPage(page);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		if (selectEClassPage != null) {
			selectedEClass = ((SelectEClassWizardPage) selectEClassPage)
				.getSelectedEClass();
			return selectedEClass != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.common.ui.wizard.AbstractExampleInstallerWizard#performFinish
	 * ()
	 */
	@Override
	public boolean performFinish() {
		// install the project template in the workspace
		boolean result = super.performFinish();
		// update the project and project files
		result &= createViewModelFile() && updateViewModelPluginFilePath() && renamePlugin(); //

		if (result) {
			final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(new FileEditorInput(viewModelFile),
					workbench.getEditorRegistry().getDefaultEditor(viewModelFile.getFullPath().toString()).getId());
			} catch (final PartInitException ex) {
				Activator.getDefault().getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
			}
		}
		return result;
	}

	private boolean renamePlugin() {

		// get manifest file
		//
		final IProject p = projectDescriptors.get(0).getProject();
		final IFile manifestFile = ResourcesPlugin.getWorkspace().getRoot()
			.getFile(p.getFolder("META-INF").getFullPath().append("MANIFEST.MF")); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			// change bundle-name and bundle-symbolicname
			final BufferedReader in = new BufferedReader(new InputStreamReader(manifestFile.getContents()));

			final String bundleNameQualifier = "Bundle-Name:"; //$NON-NLS-1$
			final String bundleSymbolicNameQualifier = "Bundle-SymbolicName:"; //$NON-NLS-1$
			String line = null;
			final StringBuffer contents = new StringBuffer();
			while ((line = in.readLine()) != null) {
				// Process each line and add output to Dest.txt file
				if (line.contains(bundleNameQualifier)) {
					final int startIndex = line.indexOf(bundleNameQualifier) + bundleNameQualifier.length();
					int endIndex = line.indexOf(";", startIndex); //$NON-NLS-1$
					if (endIndex == -1) {
						endIndex = line.length();
					}
					final String oldBundleName = line.substring(startIndex, endIndex);
					line = line.replace(oldBundleName, " " + p.getName()); //$NON-NLS-1$
				} else if (line.contains(bundleSymbolicNameQualifier)) {
					final int startIndex = line.indexOf(bundleSymbolicNameQualifier)
						+ bundleSymbolicNameQualifier.length();
					int endIndex = line.indexOf(";", startIndex); //$NON-NLS-1$
					if (endIndex == -1) {
						endIndex = line.length();
					}
					final String oldBundleSymbolicName = line.substring(startIndex, endIndex);
					line = line.replace(oldBundleSymbolicName, " " + p.getName()); //$NON-NLS-1$
				}

				contents.append(line + "\n"); //$NON-NLS-1$
			}
			in.close();

			final FileWriter out = new FileWriter(manifestFile.getRawLocation().makeAbsolute().toFile());
			out.write(String.valueOf(contents));
			out.flush();
			out.close();

			ResourcesPlugin.getWorkspace().getRoot().getProject(p.getName())
				.refreshLocal(IResource.DEPTH_INFINITE, null);

		} catch (final CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (final IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
		return true;
	}

	private boolean createViewModelFile() {
		final IProject p = projectDescriptors.get(0).getProject();
		final IPath path = p.getFolder("viewmodels").getFullPath().append(selectedEClass.getName() + ".view");
		final File f = new File(path.toString());
		try {
			f.createNewFile();
		} catch (final IOException ex1) {
			// TODO
		}
		viewModelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		final boolean generateViewModelControls = ((SelectEClassWizardPage) selectEClassPage)
			.isGenerateViewModelOptionSelected();
		try {
			final IDEViewModelRegistry registry = getViewModelRegistry();
			if (registry != null) {
				final VView view = registry.createViewModel(viewModelFile, selectedEClass, selectedEcore);
				if (generateViewModelControls) {
					generate(view);
				}

			}

		} catch (final IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			return false;
		}

		return true;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * @param view
	 */
	// FIXME
	private void generate(final VView view2) {
		// load resource
		// final String path = getViewModelRegistry().getEcorePath(view);
		final URI uri = EcoreUtil.getURI(view2);
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("*", new XMIResourceFactoryImpl()); //$NON-NLS-1$

		final ResourceSet resSet = new ResourceSetImpl();
		final Resource resource = resSet.getResource(uri, true);
		try {
			resource.load(null);
		} catch (final IOException ex) {
			// TODO Auto-generated catch block
		}
		// resolve the proxies
		int rsSize = resSet.getResources().size();
		EcoreUtil.resolveAll(resSet);
		while (rsSize != resSet.getResources().size()) {
			EcoreUtil.resolveAll(resSet);
			rsSize = resSet.getResources().size();
		}
		final VView view = (VView) resource.getContents().get(0);

		final EClass rootEClass = view.getRootEClass();
		final Set<EStructuralFeature> mySet = new
			LinkedHashSet<EStructuralFeature>(rootEClass.getEAllStructuralFeatures());
		ControlGenerator.addControls(rootEClass, view, rootEClass,
			mySet);
		// AdapterFactoryEditingDomain.getEditingDomainFor(view).getCommandStack()
		// .execute(new ChangeCommand(view) {
		//
		// @Override
		// protected void doExecute() {
		// ControlGenerator.addControls(rootEClass, view, rootEClass,
		// mySet);
		//
		// }
		// });
		// Save the contents of the resource to the file system.
		final Map<Object, Object> options = new HashMap<Object, Object>();
		try {
			resource.save(options);
		} catch (final IOException ex) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * Update the name of the view model file in the ... extension point
	 */
	private boolean updateViewModelPluginFilePath() {
		// get plugin.xml file
		//
		final IProject p = projectDescriptors.get(0).getProject();
		final IFile pluginFile = ResourcesPlugin.getWorkspace().getRoot()
			.getFile(p.getFile("plugin.xml").getFullPath()); //$NON-NLS-1$ 
		try {
			// change bundle-name and bundle-symbolicname
			final BufferedReader in = new BufferedReader(new InputStreamReader(pluginFile.getContents()));

			final String viewModelName = "$$viewmodel$$"; //$NON-NLS-1$
			String line = null;
			final StringBuffer contents = new StringBuffer();
			while ((line = in.readLine()) != null) {
				// Process each line and add output to Dest.txt file
				if (line.contains(viewModelName)) {
					line = line.replace(viewModelName, selectedEClass.getName() + ".view"); //$NON-NLS-1$
				}

				contents.append(line + "\n"); //$NON-NLS-1$
			}
			in.close();

			final FileWriter out = new FileWriter(pluginFile.getRawLocation().makeAbsolute().toFile());
			out.write(String.valueOf(contents));
			out.flush();
			out.close();

			ResourcesPlugin.getWorkspace().getRoot().getProject(p.getName())
				.refreshLocal(IResource.DEPTH_INFINITE, null);

		} catch (final CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (final IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
		return true;
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

}