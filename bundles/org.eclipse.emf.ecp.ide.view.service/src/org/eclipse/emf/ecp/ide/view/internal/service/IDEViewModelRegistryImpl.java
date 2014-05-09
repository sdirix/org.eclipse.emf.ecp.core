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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecp.ide.view.service.Activator;
import org.eclipse.emf.ecp.ide.view.service.IDEViewModelRegistry;
import org.eclipse.emf.ecp.ide.view.service.ViewModelEditorCallback;
import org.eclipse.emf.ecp.internal.ide.util.EcoreHelper;
import org.eclipse.emf.ecp.view.ideconfig.model.IDEConfig;
import org.eclipse.emf.ecp.view.ideconfig.model.IdeconfigFactory;
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

	@Override
	public void register(String ecorePath, VView viewModel) {
		if (!ecoreViewMapping.containsKey(ecorePath)) {
			ecoreViewMapping.put(ecorePath, new LinkedHashSet<VView>());
			addECoreChangeListener(ecorePath);
		}
		ecoreViewMapping.get(ecorePath).add(viewModel);
	}

	private void addECoreChangeListener(final String ecorePath) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
			new IResourceChangeListener() {

				@Override
				public void resourceChanged(IResourceChangeEvent event) {
					IResourceDelta delta = event.getDelta();
					if (delta != null) {
						while (delta.getAffectedChildren().length != 0) {
							delta = delta.getAffectedChildren()[0];
						}
						for (final VView view : ecoreViewMapping.get(ecorePath)) {
							final String ecorePath = getEcorePath(view);
							if (ecorePath.contains(delta.getResource().getFullPath().toString())) {
								final ViewModelEditorCallback viewModelEditorCallback = viewModelViewModelEditorMapping
									.get(view);
								if (viewModelEditorCallback == null) {
									continue;
								}
								// viewModelEditorCallback.reloadViewModel();
								viewModelEditorCallback.signalEcoreOutOfSync();
							}
						}
					}
				}
			});
	}

	@Override
	public void unregister(String registeredEcorePath, VView viewModel) {
		if (ecoreViewMapping.containsKey(registeredEcorePath)) {
			ecoreViewMapping.get(registeredEcorePath).remove(viewModel);
		}
	}

	@Override
	public void registerViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor) throws IOException {

		viewModelViewModelEditorMapping.put(viewModel, viewModelEditor);
		final String ecorePath = getEcorePath(viewModel);
		EcoreHelper.registerEcore(ecorePath);

	}

	private void persistSelectedEcore(String ecorePath, String viewModelPath) {

		final ResourceSet resourceSet = new ResourceSetImpl();
		final int nameStart = viewModelPath.lastIndexOf("/") + 1; //$NON-NLS-1$
		final int nameEnd = viewModelPath.lastIndexOf("."); //$NON-NLS-1$
		final String newModelPath = viewModelPath.substring(0, nameStart)
			+ "." + viewModelPath.substring(nameStart, nameEnd) + ".ideconfig"; //$NON-NLS-1$ //$NON-NLS-2$
		final Resource resource = resourceSet.createResource(URI.createURI(newModelPath, true));

		final IDEConfig config = IdeconfigFactory.eINSTANCE.createIDEConfig();
		config.setEcorePath(ecorePath);

		resource.getContents().add(config);
		try {
			resource.save(null);
		} catch (final IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	private IDEConfig getIDEConfig(VView view) {
		// load the config
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("ideconfig", new XMIResourceFactoryImpl()); //$NON-NLS-1$
		// TODO move code
		final ResourceSet resSet = new ResourceSetImpl();
		final URI uri = EcoreUtil.getURI(view);
		final String viewName = uri.lastSegment();
		final String viewModelPath = uri.toString();
		final int nameStart = viewModelPath.lastIndexOf(viewName);
		final int nameEnd = viewModelPath.lastIndexOf("."); //$NON-NLS-1$
		final String newModelPath = viewModelPath.substring(0, nameStart)
			+ "." + viewModelPath.substring(nameStart, nameEnd) + ".ideconfig"; //$NON-NLS-1$ //$NON-NLS-2$

		// Get the config
		final Resource resource = resSet.getResource(URI.createURI(newModelPath), true);
		final IDEConfig config = (IDEConfig) resource.getContents().get(0);
		return config;
	}

	@Override
	public String getEcorePath(VView view) {
		final IDEConfig config = getIDEConfig(view);
		if (config != null) {
			return config.getEcorePath();
		}
		return null;
	}

	@Override
	public void persistSelectedEcore(String ecorePath, VView viewModel) {
		final String viewModelPath = viewModelviewModelFileMapping.get(viewModel);
		if (viewModelPath != null) {
			persistSelectedEcore(ecorePath, viewModelPath);
		}
	}

	@Override
	public void unregisterViewModelEditor(VView viewModel, ViewModelEditorCallback viewModelEditor) {
		viewModelViewModelEditorMapping.remove(viewModel);
		final String ecorePath = getEcorePath(viewModel);
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

		// Save the contents of the resource to the file system.
		final Map<Object, Object> options = new HashMap<Object, Object>();
		resource.save(options);

		// Update the VView-EClass mapping
		persistSelectedEcore(selectedEcore.getFullPath().toString(), modelFile.getLocationURI().toURL()
			.toExternalForm());

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
