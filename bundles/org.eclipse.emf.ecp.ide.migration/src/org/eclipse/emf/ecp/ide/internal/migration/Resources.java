/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ide.internal.migration;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Utility class for handling view model files.
 *
 */
public final class Resources {

	private static final String VIEW_EXT = "view"; //$NON-NLS-1$

	private Resources() {
		// private ctor
	}

	/**
	 * Find all view model files within the workspace.
	 *
	 * @param monitor a {@link SubMonitor} that allow to report progress
	 *
	 * @return a set of view model files
	 */
	public static Set<IFile> findAllViewFilesInWorkspace(SubMonitor monitor) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final SubMonitor subMonitor = SubMonitor.convert(monitor, root.getProjects().length);
		try {
			return collectViewFiles(root, subMonitor);
		} catch (final CoreException ex) {
			return new LinkedHashSet<IFile>();
		}

	}

	/**
	 * Collect all files recursively within the given container.
	 *
	 * @param container the container from which to collect all files from
	 * @param subMonitor a {@link SubMonitor} that allows to report progress
	 * @return a set of files for which the predicate evaluated to true
	 * @throws CoreException in case an error occurs while traversing the files
	 */
	public static Set<IFile> collectViewFiles(IContainer container,
		SubMonitor subMonitor) throws CoreException {

		final Set<IFile> files = new LinkedHashSet<IFile>();
		for (final IResource member : container.members()) {
			if (member instanceof IContainer) {
				files.addAll(collectViewFiles(IContainer.class.cast(member), subMonitor));
			} else if (member != null && member.getFileExtension() != null
				&& member.getFileExtension().equals(VIEW_EXT)) {
				files.add((IFile) member);
			}
		}

		if (container instanceof IProject) {
			subMonitor.worked(1);
		}

		return files;
	}
}
