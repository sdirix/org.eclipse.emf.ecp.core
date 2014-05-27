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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	 * @see org.eclipse.emf.common.ui.wizard.ExampleInstallerWizard# loadFromExtensionPoints()
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
			selectedEClass = selectEClassPage
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
		result &= createViewModelFile() && updateViewModelPluginFilePath() && renamePlugin();

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
		final IProject p = projectDescriptors.get(0).getProject();

		try {
			// rename project description (in .project file)
			updateProjectDescriptionFile(p);
			final IFile manifestFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(p.getFolder("META-INF").getFullPath().append("MANIFEST.MF")); //$NON-NLS-1$ //$NON-NLS-2$
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

	/**
	 * Changes the name of the project in the project description file (.project)
	 */
	private void updateProjectDescriptionFile(IProject p) {
		final IPath filePath = p.getFullPath().append(".project"); //$NON-NLS-1$
		final IFile descriptionFile = ResourcesPlugin.getWorkspace().getRoot()
			.getFile(filePath);
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			final InputStream in = descriptionFile.getContents();
			final Document doc = dBuilder.parse(in);

			final NodeList nList = doc.getElementsByTagName("projectDescription"); //$NON-NLS-1$
			final Element eElement = (Element) nList.item(0);
			eElement.getElementsByTagName("name") //$NON-NLS-1$
				.item(0).getChildNodes().item(0).setNodeValue(p.getName());

			// write the content into xml file
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);

			final String xmlContent = result.getWriter().toString();
			final FileWriter out = new FileWriter(descriptionFile.getRawLocation().makeAbsolute().toFile());
			out.write(xmlContent);
			out.flush();
			out.close();

			ResourcesPlugin.getWorkspace().getRoot().getProject(p.getName())
				.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (final ParserConfigurationException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		} catch (final CoreException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		} catch (final SAXException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		} catch (final IOException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		} catch (final TransformerConfigurationException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		} catch (final TransformerException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		}

	}

	private boolean createViewModelFile() {
		final IProject p = projectDescriptors.get(0).getProject();
		final IPath path = p.getFolder("viewmodels").getFullPath().append(selectedEClass.getName() + ".view"); //$NON-NLS-1$ //$NON-NLS-2$
		viewModelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		final boolean generateViewModelControls = selectEClassPage
			.isGenerateViewModelOptionSelected();
		try {
			final IDEViewModelRegistry registry = getViewModelRegistry();
			if (registry != null) {
				final VView view = registry.createViewModel(viewModelFile, selectedEClass, selectedEcore);
				if (generateViewModelControls) {
					ControlGenerator.generateAllControls(view);
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
	 * Update the name of the view model file in the plugin.xml file
	 */
	private boolean updateViewModelPluginFilePath() {
		// get plugin.xml file
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
