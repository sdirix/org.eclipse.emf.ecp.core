/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.ide.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emfforms.ide.internal.builder.ViewModelBuilder;
import org.eclipse.emfforms.ide.internal.builder.ViewModelNature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for Class {@link ViewModelBuilder}
 */
public class ViewModelBuilder_PTest {

	@Before
	public void deleteProjects() throws Exception {
		final IProgressMonitor monitor = new NullProgressMonitor();
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (final IProject project : root.getProjects()) {
			project.delete(true, monitor);
		}

	}

	@Test
	public void validProject() throws Exception {
		// initial state
		final String projectName = "ValidModel"; //$NON-NLS-1$
		final IProgressMonitor monitor = new NullProgressMonitor();
		final IProject project = createAndPopulateProject(projectName, monitor);
		IMarker[] markers = findMarkersOnResource(project);
		// No build yet => no markers
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);

		// trigger builder by adding nature to the project and auto-build is on
		setAutoBuild(true);
		ViewModelNature.toggleNature(project);
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

		// final state
		markers = findMarkersOnResource(project);
		// valid Files => No marker
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);
	}

	@Test
	public void notAViewModelProject() throws Exception {
		final String projectName = "NotAViewModel"; //$NON-NLS-1$
		final IProgressMonitor monitor = new NullProgressMonitor();
		final IProject project = createAndPopulateProject(projectName, monitor);
		IMarker[] markers = findMarkersOnResource(project);
		// No build yet => no markers
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);

		// trigger builder by adding nature to the project and auto-build is on
		setAutoBuild(true);
		ViewModelNature.toggleNature(project);
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

		// final state
		markers = findMarkersOnResource(project);
		// no view files (wrong XMI, not XML file, etc.) => Mark them with an error.
		Assert.assertEquals(2, markers.length);
	}

	@Test
	public void validationErrors() throws Exception {
		final String projectName = "ValidationErrors";//$NON-NLS-1$
		final IProgressMonitor monitor = new NullProgressMonitor();
		final IProject project = createAndPopulateProject(projectName, monitor);
		IMarker[] markers = findMarkersOnResource(project);
		// No build yet => no markers
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);

		// trigger builder by adding nature to the project and auto-build is on
		setAutoBuild(true);
		ViewModelNature.toggleNature(project);
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

		// final state
		markers = findMarkersOnResource(project);

		// 4 errors:
		// 2 unresolved DMR and one missing DMR as ECP pure validation errros
		// an annotation with a missing key as a simple EMF error
		for (final IMarker marker : markers) {
			System.err.println(marker);
		}
		Assert.assertEquals(4, markers.length);
	}

	@Test
	public void noAutoBuild() throws Exception {
		final String projectName = "ValidationErrors"; //$NON-NLS-1$
		final IProgressMonitor monitor = new NullProgressMonitor();
		final IProject project = createAndPopulateProject(projectName, monitor);
		IMarker[] markers = findMarkersOnResource(project);
		// No build yet => no markers
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);

		// trigger builder by adding nature to the project and auto-build is on
		setAutoBuild(false);
		ViewModelNature.toggleNature(project);
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

		// final state
		markers = findMarkersOnResource(project);
		// valid Files => No marker
		Assert.assertArrayEquals(Collections.<IMarker> emptyList().toArray(), markers);
	}

	private static void setAutoBuild(boolean autoBuild) throws CoreException {
		final IWorkspaceDescription description = ResourcesPlugin.getWorkspace().getDescription();
		description.setAutoBuilding(autoBuild);
		ResourcesPlugin.getWorkspace().setDescription(description);
	}

	private IMarker[] findMarkersOnResource(IResource resource) {
		IMarker[] problems = null;
		final int depth = IResource.DEPTH_INFINITE;
		try {
			problems = resource.findMarkers(IMarker.PROBLEM, true, depth);
		} catch (final CoreException e) {
			// something went wrong
		}
		return problems;
	}

	private static IProject createProject(String name, IProgressMonitor monitor) throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(name);
		project.create(monitor);
		project.open(monitor);
		return project;
	}

	private static IFile importFileIntoProject(IProject project, File file, IProgressMonitor monitor)
		throws CoreException, IOException {
		final IFile targetResource = project.getFile(new Path(file.getName()));
		final InputStream contentStream = new FileInputStream(file);
		targetResource.create(contentStream, false, monitor);
		contentStream.close();
		return targetResource;

	}

	private static IProject createAndPopulateProject(String projectName, IProgressMonitor monitor)
		throws CoreException, IOException {
		final IProject project = createProject(projectName, monitor);
		// copy content of the resources equivalent folder
		final String folderName = String.format("/resources/%s/", projectName);//$NON-NLS-1$
		final String resourceFolderPath = new File(".").getAbsolutePath() + folderName;//$NON-NLS-1$
		final File resourceFolder = new File(resourceFolderPath);
		for (final File file : resourceFolder.listFiles()) {
			importFileIntoProject(project, file, monitor);
		}
		return project;
	}
}
