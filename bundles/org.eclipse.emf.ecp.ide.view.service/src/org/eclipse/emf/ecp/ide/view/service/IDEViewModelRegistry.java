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
package org.eclipse.emf.ecp.ide.view.service;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.view.ideconfig.model.IDEConfig;
import org.eclipse.emf.ecp.view.spi.model.VView;

/**
 * The View Model registry handling the update of open view model editors.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface IDEViewModelRegistry {

	/**
	 * Register an ECore with a VView.
	 * 
	 * @param ecorePath the path to the ECore connected to the VView
	 * @param viewModel the VView
	 */
	void register(String ecorePath, VView viewModel);

	/**
	 * Unregister a VView from an ECore. eg when the root class changes.
	 * 
	 * @param registeredEcorePath the path to the ECore connected to the VView
	 * @param viewModel the VView
	 */
	void unregister(String registeredEcorePath, VView viewModel);

	/**
	 * Register a view model editor with a view.
	 * 
	 * @param viewModel the VView
	 * @param viewModelEditor the view model editor
	 * @throws IOException if a resource cannot be loaded
	 */
	void registerViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor) throws IOException;

	/**
	 * Unregister a view model editor, called when the view model editor closes.
	 * 
	 * @param viewModel the VView
	 * @param viewModelEditor the view model editor
	 */
	void unregisterViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor);

	/**
	 * Saves the path {@code ecorePath} of the Ecore associated with the {@linkplain VView} located at the path
	 * {@code viewModelPath} in an IDEConfig file.
	 * 
	 * @param ecorePath path to the Ecore associated with the {@code viewModel}
	 * @param viewModelPath the path to the VView
	 */
	void persistSelectedEcore(String ecorePath, String viewModelPath);

	/**
	 * Saves the path {@code ecorePath} of the Ecore associated with the {@code viewModel} in an {@link IDEConfig} file.
	 * 
	 * @param ecorePath path to the Ecore associated with the {@code viewModel}
	 * @param viewModel the VView
	 */
	void persistSelectedEcore(String ecorePath, VView viewModel);

	/**
	 * Register a view with its absolute path.
	 * 
	 * @param view the VView
	 * @param viewPath the path to the view file
	 */
	void registerViewModel(VView view, String viewPath);

	/**
	 * Creates a new view model file.
	 * 
	 * @param modelFile the file in which the view should be saved
	 * @param selectedEClass the <em>Root EClass</em> for the new {@link VView}
	 * @param selectedEcore the ecore containing the <em>selectedEClass</em>
	 * 
	 * @return the newly created {@link VView}
	 * 
	 * @throws IOException when something goes wrong while loading or saving the resource
	 * 
	 */
	VView createViewModel(IFile modelFile, EClass selectedEClass, IFile selectedEcore)
		throws IOException;

	/**
	 * @param view a VView
	 * @return the path to the Ecore file to which the rootEClass of the view belongs to.
	 */
	String getEcorePath(VView view);

}
