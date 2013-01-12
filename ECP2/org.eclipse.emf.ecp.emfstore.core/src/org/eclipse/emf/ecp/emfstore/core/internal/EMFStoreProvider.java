/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.ecp.spi.core.util.ModelWrapper;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.emfstore.client.model.Configuration;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.util.EMFStoreClientUtil;
import org.eclipse.emf.emfstore.common.model.IdEObjectCollection;
import org.eclipse.emf.emfstore.common.model.Project;
import org.eclipse.emf.emfstore.common.model.util.IdEObjectCollectionChangeObserver;
import org.eclipse.emf.emfstore.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

import org.eclipse.core.runtime.IStatus;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This is the EMFStore Provider for ECP.
 * 
 * @author Eugen Neufeld
 */
public final class EMFStoreProvider extends DefaultProvider {
	/**
	 * This is the name of the EMFStore Provider.
	 */
	public static final String NAME = "org.eclipse.emf.ecp.emfstore.provider";

	/**
	 * EMFStore Provider Singleton.
	 */
	public static EMFStoreProvider INSTANCE;

	/**
	 * Property constant for Repository URL.
	 */
	public static final String PROP_REPOSITORY_URL = "repositoryUrl";
	/**
	 * Property constant for Repository Port.
	 */
	public static final String PROP_PORT = "port";
	/**
	 * Property constant for Repository Certificate.
	 */
	public static final String PROP_CERTIFICATE = "certificate";
	/**
	 * Property constant for ProjectSpaceID.
	 */
	public static final String PROP_PROJECTSPACEID = "projectSpaceID";
	/**
	 * Property constant for ServerInfoID.
	 */
	public static final String PROP_SERVERINFOID = "serverInfoID";

	private AdapterImpl adapter;

	public EMFStoreProvider() {
		super(NAME);
		configureEMFStore();
		INSTANCE = this;
	}

	/**
   * 
   */
	private void configureEMFStore() {
		Configuration.setAutoSave(false);
	}

	/** {@inheritDoc} */
	@Override
	public EditingDomain createEditingDomain(InternalProject project) {
		return WorkspaceManager.getInstance().getCurrentWorkspace().getEditingDomain();
	}

	/** {@inheritDoc} */
	@Override
	public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList) {
		if (parent instanceof InternalProject) {
			ProjectSpace projectSpace = getProjectSpace((InternalProject) parent);
			if (projectSpace != null) {
				childrenList.addChildren(projectSpace.getProject().getModelElements());
			}
		} else if (parent instanceof InternalRepository) {
			ServerInfo serverInfo = getServerInfo((InternalRepository) parent);
			if (serverInfo.getLastUsersession() != null && serverInfo.getLastUsersession().isLoggedIn()) {
				EList<ProjectInfo> projectInfos = serverInfo.getProjectInfos();
				for (ProjectInfo projectInfo : projectInfos) {
					childrenList.addChild(new EMFStoreProjectWrapper((InternalRepository) parent,
						new EMFStoreCheckoutData(serverInfo, projectInfo)));
				}

			}
		}
		super.fillChildren(context, parent, childrenList);
	}

	@Override
	public boolean hasUnsharedProjectSupport() {
		return true;
	}

	/** {@inheritDoc} */
	public EList<? extends Object> getElements(InternalProject project) {
		return getProjectSpace(project).getProject().getModelElements();
	}

	/** {@inheritDoc} */
	@Override
	public void handleLifecycle(ECPModelContext context, LifecycleEvent event) {
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
		String providerClass = getClass().getSimpleName();
		String contextClass = context.getClass().getSimpleName();
		Activator.log(IStatus.INFO, providerClass + " received " + event + " for " + contextClass + " " + context);
	}

	/**
	 * Called to handle the remove operation on an {@link ECPModelContext}.
	 * 
	 * @param context the {@link ECPModelContext} to remove
	 */
	private void handleRemove(ECPModelContext context) {
		if (context instanceof InternalProject) {
			InternalProject project = (InternalProject) context;
			ProjectSpace ps = (ProjectSpace) project.getProviderSpecificData();
			try {
				WorkspaceManager.getInstance().getCurrentWorkspace().deleteProjectSpace(ps);
			} catch (IOException ex) {
				Activator.log(ex);
			}
		}

	}

	/**
	 * @param context
	 */
	private void handleCreate(final ECPModelContext context) {
		if (context instanceof InternalRepository) {
			ServerInfo serverInfo = getServerInfo((InternalRepository) context);
			if (serverInfo.getLastUsersession() != null && !serverInfo.getLastUsersession().isLoggedIn()) {
				try {
					serverInfo.getLastUsersession().logIn();
				} catch (AccessControlException ex) {
					Activator.log(ex);
				} catch (EmfStoreException ex) {
					Activator.log(ex);
				}
			}
		} else if (context instanceof InternalProject) {
			getProjectSpace((InternalProject) context);
		}
	}

	/**
	 * @param context
	 */
	private void handelDispose(ECPModelContext context) {
		if (context instanceof InternalProject) {
			ProjectSpace projectSpace = getProjectSpace((InternalProject) context);

			projectSpace.getProject().eAdapters().remove(adapter);

		}

	}

	/**
	 * @param context
	 */
	private void handleInit(final ECPModelContext context) {
		if (context instanceof InternalProject) {
			ProjectSpace projectSpace = getProjectSpace((InternalProject) context);
			if (projectSpace == null) {
				return;
			}
			projectSpace.getProject().addIdEObjectCollectionChangeObserver(new IdEObjectCollectionChangeObserver() {
				// 2
				public void notify(Notification notification, IdEObjectCollection collection, EObject modelElement) {

					((InternalProject) context).notifyObjectsChanged(new Object[] { modelElement }, false);
				}

				// 3
				public void modelElementRemoved(IdEObjectCollection collection, EObject modelElement) {
					if (modelElement.eContainer() == null) {
						((InternalProject) context).notifyObjectsChanged(new Object[] { modelElement, context }, true);
					}
				}

				// 1
				public void modelElementAdded(IdEObjectCollection collection, EObject modelElement) {
					if (Project.class.isInstance(modelElement.eContainer())) {
						((InternalProject) context).notifyObjectsChanged(new Object[] { context, modelElement }, true);
					}
					((InternalProject) context).notifyObjectsChanged(new Object[] { modelElement }, true);
				}

				public void collectionDeleted(IdEObjectCollection collection) {
					// project delete
					((InternalProject) context).notifyObjectsChanged(new Object[] { context }, true);
				}
			});
		}

	}

	/** {@inheritDoc} */
	@Override
	public Iterator<EObject> getLinkElements(InternalProject project, EObject modelElement, EReference eReference) {
		Collection<EObject> result = new HashSet<EObject>();
		ItemPropertyDescriptor.collectReachableObjectsOfType(new HashSet<EObject>(), result, getProjectSpace(project)
			.getProject(), eReference.getEType());
		return result.iterator();
	}

	/**
	 * @param info
	 * @param ecpProperties
	 * @return
	 */
	private boolean isSameServerInfo(ServerInfo info, String url, int port, String certificate) {
		return info.getUrl().equalsIgnoreCase(url) && info.getPort() == port
			&& info.getCertificateAlias().equalsIgnoreCase(certificate);
	}

	/** {@inheritDoc} */
	@Override
	public void doSave(InternalProject project) {
		getProjectSpace(project).save();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isDirty(InternalProject project) {
		return getProjectSpace(project).hasUnsavedChanges();
	}

	/** {@inheritDoc} */
	public void delete(InternalProject project, Collection<EObject> eObjects) {
		ProjectSpace projectSpace = getProjectSpace(project);
		for (EObject eObject : eObjects) {
			projectSpace.getProject().deleteModelElement(eObject);
		}
	}

	/** {@inheritDoc} */
	public void cloneProject(final InternalProject projectToClone, InternalProject targetProject) {
		ProjectSpace toClone = getProjectSpace(projectToClone);
		ProjectSpace target = getProjectSpace(targetProject);
		target.setProject(EcoreUtil.copy(toClone.getProject()));
	}

	/** {@inheritDoc} */
	@Override
	public boolean modelExists(InternalProject project) {
		return getProjectSpace(project, false) != null;
	}

	/** {@inheritDoc} */
	public Notifier getRoot(InternalProject project) {
		return getProjectSpace(project).getProject();
	}

	public boolean contains(InternalProject project, Object object) {
		if (!EObject.class.isInstance(object)) {
			return false;
		}
		ProjectSpace projectSpace = getProjectSpace(project);
		return projectSpace.getProject().containsInstance((EObject) object);
	}

	@Override
	public ECPModelContext getModelContext(Object element) {
		if (element instanceof ECPModelContext) {
			return (ECPModelContext) element;
		}

		if (element instanceof ECPModelContextProvider) {
			return ((ECPModelContextProvider) element).getModelContext(element);
		}

		if (element instanceof ModelWrapper) {
			return ((ModelWrapper<?, ?>) element).getContext();
		}

		if (element instanceof EObject) {
			EObject eObject = (EObject) element;
			ProjectSpace ps = null;
			try {
				ps = WorkspaceManager.getProjectSpace(eObject);
			} catch (IllegalArgumentException iae) {
				return null;
			}
			if (ps != null) {
				ECPModelContext context = getModelContextFromAdapter(ps.getProject());
				if (context != null) {
					return context;
				}
			}
			element = eObject.eResource();
		}

		if (element instanceof Resource) {
			Resource resource = (Resource) element;
			ECPModelContext context = getModelContextFromAdapter(resource);
			if (context != null) {
				return context;
			}

			element = resource.getResourceSet();
		}

		if (element instanceof ResourceSet) {
			ResourceSet resourceSet = (ResourceSet) element;
			ECPModelContext context = getModelContextFromAdapter(resourceSet);
			if (context != null) {
				return context;
			}
		}

		return null;
	}

	/**
	 * This retrieves the {@link ProjectSpace} from an {@link InternalProject}.
	 * First it checks whether the {@link InternalProject} has a ProjectSpaceID attached.
	 * If an ID is attached, a ProjectSpace is searched with this ID.
	 * If no ID is attached or now ProjectSpace was found a LocalProject is created.
	 * 
	 * @param internalProject the project to get the ProjectSpace for
	 * @return the corresponding ProjectSpace
	 */
	public ProjectSpace getProjectSpace(InternalProject internalProject) {
		return getProjectSpace(internalProject, true);
	}

	private ProjectSpace getProjectSpace(InternalProject internalProject, boolean createNewIfNeeded) {
		ProjectSpace projectSpace = (ProjectSpace) internalProject.getProviderSpecificData();
		if (projectSpace == null) {
			boolean found = false;
			EList<ProjectSpace> projectSpaces = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces();
			for (ProjectSpace ps : projectSpaces) {
				String projectSpaceID = internalProject.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
				if (ps.getIdentifier().equals(projectSpaceID)) {
					found = true;
					projectSpace = ps;
					break;
				}
			}
			if (!found && createNewIfNeeded) {
				projectSpace = WorkspaceManager.getInstance().getCurrentWorkspace()
					.createLocalProject(internalProject.getName(), "");
				internalProject.getProperties().addProperty(EMFStoreProvider.PROP_PROJECTSPACEID,
					projectSpace.getIdentifier());

			}
			internalProject.setProviderSpecificData(projectSpace);
		}
		return projectSpace;
	}

	/**
	 * This retrieves the {@link ServerInfo} from an {@link InternalRepository}.
	 * First it checks whether the {@link InternalRepository} has a ServerInfoID attached.
	 * If an ID is attached, a ServerInfo is searched with this ID.
	 * If no ID is attached or now ServerInfo was found a default ServerInfo is created.
	 * 
	 * @param internalRepository the repository to get the ServerInfo for
	 * @return the corresponding ServerInfo
	 */
	public ServerInfo getServerInfo(InternalRepository internalRepository) {
		ServerInfo serverInfo = (ServerInfo) internalRepository.getProviderSpecificData();
		if (serverInfo == null) {
			Workspace workspace = WorkspaceManager.getInstance().getCurrentWorkspace();

			boolean foundExisting = false;
			for (ServerInfo info : workspace.getServerInfos()) {
				if (internalRepository.getProperties().hasProperties()
					&& isSameServerInfo(info,
						internalRepository.getProperties().getValue(EMFStoreProvider.PROP_REPOSITORY_URL),
						Integer.parseInt(internalRepository.getProperties().getValue(EMFStoreProvider.PROP_PORT)),
						internalRepository.getProperties().getValue(EMFStoreProvider.PROP_CERTIFICATE))) {
					serverInfo = info;
					foundExisting = true;
					break;
				}
			}
			if (!foundExisting && internalRepository.getProperties().hasProperties()) {
				serverInfo = EMFStoreClientUtil.createServerInfo(
					internalRepository.getProperties().getValue(EMFStoreProvider.PROP_REPOSITORY_URL),
					Integer.parseInt(internalRepository.getProperties().getValue(EMFStoreProvider.PROP_PORT)),
					internalRepository.getProperties().getValue(EMFStoreProvider.PROP_CERTIFICATE));
				workspace.addServerInfo(serverInfo);
				workspace.save();
			} else if (!foundExisting && !internalRepository.getProperties().hasProperties()) {
				serverInfo = EMFStoreClientUtil.giveServerInfo("localhost", 8080);
			}
			internalRepository.setProviderSpecificData(serverInfo);
		}
		return serverInfo;
	}

	/**
	 * This gets the ECPProject based on a ProjectSpace.
	 * 
	 * @param projectSpace the {@link ProjectSpace} to get the {@link ECPProject} for
	 * @return the {@link ECPProject} corresponding to this ProjectSpace or null if none found
	 */
	public ECPProject getProject(ProjectSpace projectSpace) {
		for (InternalProject project : getOpenProjects()) {
			ProjectSpace localProjectSpace = (ProjectSpace) project.getProviderSpecificData();
			if (localProjectSpace.equals(projectSpace)) {
				return project;
			}
		}
		return null;
	}

	/**
	 * This gets the ECPRepository based on a ServerInfo.
	 * 
	 * @param serverInfo the {@link ServerInfo} to get the {@link ECPRepository} for
	 * @return the {@link ECPRepository} corresponding to this ServerInfo or null if none found
	 */
	public ECPRepository getRepository(ServerInfo serverInfo) {
		if (serverInfo != null) {
			for (InternalRepository internalRepository : getRepositories()) {
				if (internalRepository.getProvider().equals(this)) {
					if (serverInfo.equals(internalRepository.getProviderSpecificData())) {
						return internalRepository;
					}
				}
			}
		}
		return null;
	}

}
