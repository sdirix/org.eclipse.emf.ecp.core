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
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.internal.ui.PreferenceHelper;
import org.eclipse.emf.ecp.internal.ui.util.ECPFileDialogHelper;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.internal.client.importexport.ExportImportControllerExecutor;
import org.eclipse.emf.emfstore.internal.client.importexport.ExportImportControllerFactory;
import org.eclipse.emf.emfstore.internal.client.importexport.impl.ExportImportDataUnits;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

/**
 * A helper class that can be used to import projects.
 *
 * @author Eugen Neufeld, David Soto Setzke
 *
 */
public final class ImportProjectHelper {

	private static final String FILE_EXTENSION = ExportImportDataUnits.ProjectSpace.getExtension();

	/**
	 * These filter names are used to filter which files are displayed.
	 */
	public static final String[] FILTER_NAMES = { "Model Files (*" + FILE_EXTENSION + ")" }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * These filter extensions are used to filter which files are displayed.
	 */
	public static final String[] FILTER_EXTS = { "*" + FILE_EXTENSION }; //$NON-NLS-1$

	private static final String EXPORT_MODEL_PATH = "org.eclipse.emf.ecp.exportProjectModelPath"; //$NON-NLS-1$

	private static final String ECP_UI_PLUGIN_ID = "org.eclipse.emf.ecp.ui"; //$NON-NLS-1$

	private static final String FILE_DIALOG_HELPER_CLASS = "org.eclipse.emf.ecp.internal.ui.util.ECPFileDialogHelperImpl"; //$NON-NLS-1$

	private ImportProjectHelper() {
	}

	/**
	 * Imports a project.
	 *
	 * @param shell The active shell
	 */
	public static void importProject(Shell shell) {
		final File file = getFile(shell);
		if (file == null) {
			return;
		}

		try {
			new ExportImportControllerExecutor(file, new NullProgressMonitor())
				.execute(ExportImportControllerFactory.Import.getImportProjectSpaceController());
			PreferenceHelper.setPreference(EXPORT_MODEL_PATH, file.getParent());

			final List<ESLocalProject> localProjects = ESWorkspaceProvider.INSTANCE.getWorkspace().getLocalProjects();
			final ESLocalProject localProject = localProjects.get(localProjects.size() - 1);
			final ECPProperties properties = ECPUtil.createProperties();
			properties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, localProject.getLocalProjectId().getId());
			final InputDialog id = new InputDialog(shell, "Project Name", //$NON-NLS-1$
				"Enter the name for the imported project.", localProject.getProjectName(), new IInputValidator() { //$NON-NLS-1$

					@Override
					public String isValid(String newText) {
						return ECPUtil.getECPProjectManager().getProject(newText) == null ? null : String.format(
							"A project with the name %s already exists.", newText); //$NON-NLS-1$
					}
				});
			final int result = id.open();
			if (Window.CANCEL == result) {
				return;
			}
			final String projectName = id.getValue();
			ECPUtil.getECPProjectManager().createProject(
				ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME), projectName, properties);

		} catch (final IOException ex) {
			Activator.log(ex);
		} catch (final ECPProjectWithNameExistsException ex) {
			Activator.log(ex);
		}
	}

	private static File getFile(Shell shell) {
		try {
			final Class<ECPFileDialogHelper> clazz = loadClass(ECP_UI_PLUGIN_ID,
				FILE_DIALOG_HELPER_CLASS);
			final ECPFileDialogHelper fileDialogHelper = clazz.getConstructor().newInstance();
			final String fileName = fileDialogHelper.getPathForImport(shell);
			if (fileName != null) {
				return new File(fileName);
			}
		} catch (final ClassNotFoundException ex) {
			Activator.log(ex);
		} catch (final InstantiationException ex) {
			Activator.log(ex);
		} catch (final IllegalAccessException ex) {
			Activator.log(ex);
		} catch (final IllegalArgumentException ex) {
			Activator.log(ex);
		} catch (final InvocationTargetException ex) {
			Activator.log(ex);
		} catch (final NoSuchMethodException ex) {
			Activator.log(ex);
		} catch (final SecurityException ex) {
			Activator.log(ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + " cannot be loaded because bundle " + bundleName //$NON-NLS-1$
				+ " cannot be resolved"); //$NON-NLS-1$
		}
		return (Class<T>) bundle.loadClass(clazz);
	}

}
