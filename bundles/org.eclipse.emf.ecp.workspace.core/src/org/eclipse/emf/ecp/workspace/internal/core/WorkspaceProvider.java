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

/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContainer;
import org.eclipse.emf.ecp.internal.core.util.ECPModelContextAdapter;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 * @author Tobias Verhoeven
 */
public class WorkspaceProvider extends DefaultProvider {

	/** The Provider Name. */
	public static final String NAME = "org.eclipse.emf.ecp.workspace.provider";

	/** Root URI Property Name. */
	public static final String PROP_ROOT_URI = "rootURI";

	/** The Workspace Provider Instance. */
	static WorkspaceProvider INSTANCE;

	/**
	 * Instantiates a new workspace provider.
	 */
	public WorkspaceProvider() {
		super(NAME);
		INSTANCE = this;
		// WORKSPACE.addResourceChangeListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public void handleLifecycle(ECPModelContainer context, LifecycleEvent event) {
		switch (event) {
		case INIT:
			handleInit(context);
			break;
		case DISPOSE:
			handelDispose(context);
			break;
		case CREATE:
			handleCreate(context);
			break;
		case REMOVE:
			handleRemove(context);
			break;
		default:
			break;
		}
	}

	private void handleInit(ECPModelContainer context) {
		if (context instanceof InternalProject) {
			final InternalProject project = (InternalProject) context;
			EditingDomain editingDomain = project.getEditingDomain();
			editingDomain.getResourceSet().eAdapters().add(new WorkspaceProjectObserver(project));
		}

	}

	private void handleRemove(ECPModelContainer context) {
		// TODO Auto-generated method stub

	}

	private void handleCreate(ECPModelContainer context) {
		// TODO Auto-generated method stub

	}

	private void handelDispose(ECPModelContainer context) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doDispose() {
		try {
			// WORKSPACE.removeResourceChangeListener(this);
			super.doDispose();
		} finally {
			INSTANCE = null;
		}
	}

	@Override
	public boolean canAddRepositories() {
		return false;
	}

	@Override
	public void fillChildren(ECPModelContainer context, Object parent, InternalChildrenList childrenList) {
		if (parent instanceof ECPRepository) {
		} else if (parent instanceof ECPProject) {
			ECPProject project = (ECPProject) parent;
			String rootURI = project.getProperties().getValue(PROP_ROOT_URI);

			ResourceSet resourceSet = project.getEditingDomain().getResourceSet();

			URI uri = URI.createURI(rootURI);
			if (uri.hasFragment()) {
				EObject eObject = resourceSet.getEObject(uri, true);
				super.fillChildren(context, eObject, childrenList);
			} else {
				Resource resource = resourceSet.getResource(uri, true);
				childrenList.addChildren(resource.getContents());
			}

		} else {
			super.fillChildren(context, parent, childrenList);
		}
	}

	/** {@inheritDoc} */
	public EList<? extends Object> getElements(InternalProject project) {
		ResourceSet resourceSet = project.getEditingDomain().getResourceSet();
		return ECollections.unmodifiableEList(resourceSet.getResource(
			URI.createURI(project.getProperties().getValue(PROP_ROOT_URI)), true).getContents());
		// TODO: implement WorkspaceProvider.addRootElement(project, rootElement)
	}

	@Override
	public boolean contains(InternalProject project, Object object) {
		// TODO: optimize
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			EObject root = EcoreUtil.getRootContainer(eObject);
			if (root == null || root.eResource() == null) {
				return false;
			}

			return root.eResource().equals(getRoot(project));
		}
		return false;
	}

	/**
	 * Reloads the project.
	 * 
	 * @param project the project to be reloaded.
	 */
	public void reload(InternalProject project) {
		List<Resource> resources = project.getEditingDomain().getResourceSet().getResources();
		for (Resource resource : resources) {
			if (resource.equals(getRoot(project)) && resource.isLoaded()) {
				resource.unload();

				try {
					resource.load(Collections.EMPTY_MAP);
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}
	}

	/** {@inheritDoc} */
	public void delete(InternalProject project, Collection<Object> objects) {
		project.getEditingDomain().getCommandStack().execute(DeleteCommand.create(project.getEditingDomain(), objects));
	}

	/** {@inheritDoc} */
	public void cloneProject(final InternalProject projectToClone, InternalProject targetProject) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	// FIXME
	public Notifier getRoot(InternalProject project) {
		return project.getEditingDomain().getResourceSet()
			.getResource(URI.createURI(project.getProperties().getValue(PROP_ROOT_URI)), true);
	}

	@Override
	public void doSave(InternalProject project) {
		try {
			final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
			saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			List<Resource> resources = project.getEditingDomain().getResourceSet().getResources();
			for (Resource resource : resources) {
				resource.save(saveOptions);
			}
			((BasicCommandStack) project.getEditingDomain().getCommandStack()).saveIsDone();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		super.doSave(project);
	}

	@Override
	public boolean isDirty(InternalProject project) {
		return ((BasicCommandStack) project.getEditingDomain().getCommandStack()).isSaveNeeded();
	}

	@Override
	public EditingDomain createEditingDomain(final InternalProject project) {

		CommandStack commandStack = new BasicCommandStack();
		EditingDomain editingDomain = new AdapterFactoryEditingDomain(InternalProvider.EMF_ADAPTER_FACTORY,
			commandStack);

		editingDomain.getResourceSet().eAdapters().add(new ECPModelContextAdapter(project));
		URI uri = URI.createURI(project.getProperties().getValue(PROP_ROOT_URI));
		try {
			editingDomain.getResourceSet().getResource(uri, true);
		} catch (WrappedException we) {
			project.close();
		}

		return editingDomain;
	}

	@Override
	public ECPModelContainer getModelContext(Object element) {
		return super.getModelContext(element);
	}

	@Override
	public boolean canAddOfflineProjects() {
		return true;
	}

	/**
	 * Observes changes in a projects resource and notifies the project.
	 */
	private static class WorkspaceProjectObserver extends EContentAdapter {

		private InternalProject project;

		public WorkspaceProjectObserver(InternalProject project) {
			this.project = project;
		}

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);

			if (notification.getNotifier() instanceof EObject) {
				EObject eObject = (EObject) notification.getNotifier();
				project.notifyObjectsChanged((Collection) Collections.singleton(eObject), false);

				Object feature = notification.getFeature();
				if (feature instanceof EReference) {
					EReference eReference = (EReference) feature;

					if (eReference.isContainment() && notification.getNewValue() instanceof EObject) {
						project.notifyObjectsChanged(Collections.singleton(notification.getNewValue()), true);
					}

				}

			}
		}
	}
}
