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
package org.eclipse.emf.ecp.ide.view.internal.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecp.ide.view.service.IDEViewModelRegistry;
import org.eclipse.emf.ecp.ide.view.service.ViewModelEditorCallback;
import org.eclipse.emf.ecp.internal.ide.util.EcoreHelper;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * An implementation of the {@link IDEViewModelRegistry}.
 * 
 * @author Eugen Neufeld
 * @author Alexandra Buzila
 */
public class IDEViewModelRegistryImpl implements IDEViewModelRegistry {

	/**
	 * This caches an instance of the model package. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	private static VViewPackage viewPackage = VViewPackage.eINSTANCE;

	/**
	 * This caches an instance of the model factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	private static VViewFactory viewFactory = viewPackage.getViewFactory();

	private final Map<VView, ViewModelEditorCallback> viewModelViewModelEditorMapping = new LinkedHashMap<VView, ViewModelEditorCallback>();

	private final Map<String, Set<VView>> ecoreViewMapping = new LinkedHashMap<String, Set<VView>>();

	private final Map<VView, String> viewModelviewModelFileMapping = new LinkedHashMap<VView, String>();

	private final Map<String, IResourceChangeListener> resourceChangeListeners = new LinkedHashMap<String, IResourceChangeListener>();

	@Override
	public void register(String ecorePath, VView viewModel) {
		if (!ecoreViewMapping.containsKey(ecorePath) || ecoreViewMapping.get(ecorePath).isEmpty()) {
			if (ecoreViewMapping.get(ecorePath) == null) {
				ecoreViewMapping.put(ecorePath, new LinkedHashSet<VView>());
			}
			if (!resourceChangeListeners.containsKey(ecorePath)) {
				addECoreChangeListener(ecorePath);
			}
		}
		ecoreViewMapping.get(ecorePath).add(viewModel);
	}

	private void addECoreChangeListener(final String ecorePath) {
		final IResourceChangeListener listener = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				IResourceDelta delta = event.getDelta();
				if (delta != null) {
					while (delta.getAffectedChildren().length != 0) {
						delta = delta.getAffectedChildren()[0];
					}
					for (final VView view : ecoreViewMapping.get(ecorePath)) {
						final String ecorePath = view.getEcorePath();
						if (ecorePath == null) {
							return;
						}
						if (ecorePath.contains(delta.getResource().getFullPath().toString())) {
							final ViewModelEditorCallback viewModelEditorCallback = viewModelViewModelEditorMapping
								.get(view);
							if (viewModelEditorCallback == null) {
								continue;
							}
							viewModelEditorCallback.signalEcoreOutOfSync();
						}
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
		resourceChangeListeners.put(ecorePath, listener);
	}

	@Override
	public void unregister(String registeredEcorePath, VView viewModel) {
		if (ecoreViewMapping.containsKey(registeredEcorePath)) {
			ecoreViewMapping.get(registeredEcorePath).remove(viewModel);
			if (ecoreViewMapping.get(registeredEcorePath).size() == 0)
			{
				final IResourceChangeListener listener = resourceChangeListeners.get(registeredEcorePath);
				if (listener != null) {
					ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
					resourceChangeListeners.remove(registeredEcorePath);
				}
			}
		}
	}

	@Override
	public void registerViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor) throws IOException {

		viewModelViewModelEditorMapping.put(viewModel, viewModelEditor);
		final String ecorePath = viewModel.getEcorePath();
		EcoreHelper.registerEcore(ecorePath);

	}

	@Override
	public void unregisterViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor) {
		viewModelViewModelEditorMapping.remove(viewModel);
		final String ecorePath = viewModel.getEcorePath();
		if (ecorePath != null) {
			EcoreHelper.unregisterEcore(ecorePath);
		}
	}

	@Override
	public void registerViewModel(VView view, String path) {
		if (!viewModelviewModelFileMapping.containsKey(view)) {
			viewModelviewModelFileMapping.put(view, path);
		}
	}

	@Override
	public VView createViewModel(IFile modelFile, EClass selectedEClass, IFile selectedEcore)
		throws IOException {

		AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] { adapterFactory,
			new ReflectiveItemProviderAdapterFactory() });
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory,
			new BasicCommandStack());

		// create resource for the view
		final URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
		final Resource resource = domain.createResource(fileURI.toString());

		// Add the initial model object to the contents.
		final VView view = (VView) createInitialModel();
		if (view == null) {
			return null;
		}
		resource.getContents().add(view);

		// Add the selected EClass as the VView's RootEClass
		//
		// get the EClass from the registry, to ensure it has the correct href
		final Resource r = selectedEClass.eResource();
		final EPackage ePackage = (EPackage) r.getContents().get(0);

		final Registry instance = org.eclipse.emf.ecore.EPackage.Registry.INSTANCE;
		EPackage ep = (EPackage) instance.get(ePackage.getNsURI());
		if (ep == null)
		{
			EcoreHelper.registerEcore(selectedEcore.getFullPath().toString());
			ep = (EPackage) instance.get(ePackage.getNsURI());
		}
		final EClass ec = (EClass) ep.getEClassifier(selectedEClass.getName());

		view.setRootEClass(ec);
		// Update the VView-EClass mapping
		view.setEcorePath(selectedEcore.getFullPath().toString());

		// Save the contents of the resource to the file system.
		final Map<Object, Object> options = new HashMap<Object, Object>();
		resource.save(options);

		return view;
	}

	/**
	 * Create a new model.
	 */
	private EObject createInitialModel() {

		final EObject rootObject = viewFactory.createView();
		return rootObject;
	}

}
