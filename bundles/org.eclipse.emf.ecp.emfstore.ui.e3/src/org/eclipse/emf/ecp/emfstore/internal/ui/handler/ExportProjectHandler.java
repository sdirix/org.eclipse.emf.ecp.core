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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.e3.Activator;
import org.eclipse.emf.ecp.internal.ui.util.ECPFileDialogHelper;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.internal.client.importexport.ExportImportControllerExecutor;
import org.eclipse.emf.emfstore.internal.client.importexport.ExportImportControllerFactory;
import org.eclipse.emf.emfstore.internal.client.importexport.impl.ExportImportDataUnits;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

/**
 * An export project handler.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ExportProjectHandler extends AbstractHandler {

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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil
			.getActiveMenuSelection(event))
			.getFirstElement();
		final ESLocalProject localProject = EMFStoreProvider.INSTANCE.getProjectSpace(project);

		final String filePath = getFilePathByFileDialog(HandlerUtil.getActiveShell(event), project.getName());
		if (filePath == null) {
			return null;
		}
		try {
			new ExportImportControllerExecutor(new File(filePath), new NullProgressMonitor())
				.execute(ExportImportControllerFactory.Export
					.getExportProjectSpaceController(((ESLocalProjectImpl) localProject).toInternalAPI()));

		} catch (final IOException ex) {
			Activator.log(ex);
		}

		return null;
	}

	private String getFilePathByFileDialog(Shell shell, String modelElementName) {
		try {
			final Class<ECPFileDialogHelper> clazz = loadClass(ECP_UI_PLUGIN_ID,
				FILE_DIALOG_HELPER_CLASS);
			final ECPFileDialogHelper fileDialogHelper = clazz.getConstructor().newInstance();
			return fileDialogHelper.getPathForExport(shell, modelElementName);
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
	private <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + " cannot be loaded because bundle " + bundleName //$NON-NLS-1$
				+ " cannot be resolved"); //$NON-NLS-1$
		}
		return (Class<T>) bundle.loadClass(clazz);
	}

}
