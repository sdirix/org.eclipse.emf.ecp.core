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
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESRemoteProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.internal.client.model.Configuration;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.impl.ProjectSpaceImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESWorkspaceImpl;
import org.eclipse.emf.emfstore.internal.client.model.util.EMFStoreClientUtil;
import org.eclipse.emf.emfstore.internal.client.model.util.EMFStoreCommand;
import org.eclipse.emf.emfstore.internal.client.observers.OperationObserver;
import org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.impl.ProjectImpl;
import org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AbstractOperation;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
		Configuration.getClientBehavior().setAutoSave(false);
	}

	/** {@inheritDoc} */
	@Override
	public EditingDomain createEditingDomain(final InternalProject project) {

		EditingDomain domain = ((ESWorkspaceImpl) ESWorkspaceProvider.INSTANCE.getWorkspace()).toInternalAPI()
			.getEditingDomain();
		return domain;
	}

	/** {@inheritDoc} */
	@Override
	public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList) {
		if (parent instanceof InternalProject) {
			ESLocalProject projectSpace = getProjectSpace((InternalProject) parent);
			if (projectSpace != null) {
				childrenList.addChildren(projectSpace.getModelElements());
			}
		} else if (parent instanceof InternalRepository) {
			ESServer serverInfo = getServerInfo((InternalRepository) parent);
			if (serverInfo.getLastUsersession() != null && serverInfo.getLastUsersession().isLoggedIn()) {
				try {
					List<ESRemoteProject> projectInfos = serverInfo.getRemoteProjects(serverInfo.getLastUsersession());
					for (ESRemoteProject projectInfo : projectInfos) {
						childrenList.addChild(new EMFStoreProjectWrapper((InternalRepository) parent, projectInfo));
					}
				} catch (ESException e) {
					Activator.log(e);
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
		return getProjectSpace(project).getModelElements();
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
			ESLocalProject ps = (ESLocalProject) project.getProviderSpecificData();
			try {
				ps.delete(new NullProgressMonitor());
			} catch (ESException ex) {
				Activator.log(ex);
			} catch (IOException ex) {
				Activator.log(ex);
			}
			project.setProviderSpecificData(null);
		}

	}

	/**
	 * @param context
	 */
	private void handleCreate(final ECPModelContext context) {
		if (context instanceof InternalRepository) {
			ESServer serverInfo = getServerInfo((InternalRepository) context);
			// TODO autologin?
			// if (serverInfo.getLastUsersession() != null && !serverInfo.getLastUsersession().isLoggedIn()) {
			// try {
			// serverInfo.login(serverInfo.getLastUsersession().getUsername(), serverInfo.getLastUsersession()
			// .getPassword());
			// } catch (AccessControlException ex) {
			// Activator.log(ex);
			// } catch (ESException ex) {
			// Activator.log(ex);
			// }
			// }
		} else if (context instanceof InternalProject) {
			getProjectSpace((InternalProject) context);
		}
	}

	/**
	 * @param context
	 */
	private void handelDispose(ECPModelContext context) {
		if (context instanceof InternalProject) {
			ESLocalProject projectSpace = getProjectSpace((InternalProject) context);

			((ESLocalProjectImpl) projectSpace).toInternalAPI().getProject().eAdapters().remove(adapter);

		}

	}

	private boolean isAutosave() {
		IPreferencesService service = Platform.getPreferencesService();
		return service.getBoolean("org.eclipse.emf.ecp", "AUTOSAVE", true, null);

	}

	/**
	 * @param context
	 */
	private void handleInit(final ECPModelContext context) {
		if (context instanceof InternalProject) {
			ESLocalProject localProject = getProjectSpace((InternalProject) context, true);
			if (localProject == null) {
				return;
			}
			ProjectSpace projectSpace = ((ESLocalProjectImpl) localProject).toInternalAPI();

			if (isAutosave()) {
				// TODO EMFStore how to listen to operations?
				projectSpace.getOperationManager().addOperationObserver(new OperationObserver() {

					public void operationUndone(AbstractOperation operation) {
						doSave((InternalProject) context);
					}

					public void operationExecuted(AbstractOperation operation) {
						doSave((InternalProject) context);
					}
				});
			}
			// TODO EMFStore how to add IdEObjectCollection Observer?
			projectSpace.getProject().addIdEObjectCollectionChangeObserver(new IdEObjectCollectionChangeObserver() {
				// 2
				public void notify(Notification notification, IdEObjectCollection collection, EObject modelElement) {
					if (modelElement instanceof ProjectImpl) {
						ProjectSpaceImpl projectSpace = (ProjectSpaceImpl) modelElement.eContainer();
						ECPProject ecpProject = getProject(projectSpace.toAPI());

						((InternalProject) context).notifyObjectsChanged(new Object[] { ecpProject }, true);

					} else {
						((InternalProject) context).notifyObjectsChanged(new Object[] { modelElement }, true);
					}
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
		// TODO EMFStore does it work with ESLocalProject?
		ProjectSpace projectSpace = ((ESLocalProjectImpl) getProjectSpace(project)).toInternalAPI();
		ItemPropertyDescriptor.collectReachableObjectsOfType(new HashSet<EObject>(), result, projectSpace.getProject(),
			eReference.getEType());
		return result.iterator();
	}

	/**
	 * @param info
	 * @param ecpProperties
	 * @return
	 */
	private boolean isSameServerInfo(ESServer info, String url, int port, String certificate) {
		return info.getURL().equalsIgnoreCase(url) && info.getPort() == port
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
	public void delete(InternalProject project, final Collection<EObject> eObjects) {
		final ProjectSpace projectSpace = ((ESLocalProjectImpl) getProjectSpace(project)).toInternalAPI();
		// TODO EMFStore how to delete eObject?
		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				for (EObject eObject : eObjects) {
					projectSpace.getProject().deleteModelElement(eObject);
				}
			}
		}.run(false);

	}

	/** {@inheritDoc} */
	public void cloneProject(final InternalProject projectToClone, InternalProject targetProject) {
		// TODO EMFStore how to clone local project?
		ProjectSpace toClone = ((ESLocalProjectImpl) getProjectSpace(projectToClone)).toInternalAPI();
		ProjectSpace target = ((ESLocalProjectImpl) getProjectSpace(targetProject)).toInternalAPI();
		target.setProject(EcoreUtil.copy(toClone.getProject()));
	}

	/** {@inheritDoc} */
	@Override
	public boolean modelExists(InternalProject project) {
		return getProjectSpace(project, false) != null;
	}

	/** {@inheritDoc} */
	public Notifier getRoot(InternalProject project) {
		// TODO EMFStore other way to get root of localproject?
		return ((ESLocalProjectImpl) getProjectSpace(project)).toInternalAPI().getProject();
	}

	@Override
	public boolean contains(InternalProject project, Object object) {
		if (!EObject.class.isInstance(object)) {
			return false;
		}
		ESLocalProject projectSpace = getProjectSpace(project);
		return projectSpace.contains((EObject) object);
	}

	@Override
	public ECPModelContext getModelContext(Object element) {
		if (element instanceof ECPModelContext) {
			return (ECPModelContext) element;
		}

		if (element instanceof ECPModelContextProvider) {
			return ((ECPModelContextProvider) element).getModelContext(element);
		}

		if (element instanceof EObject) {
			EObject eObject = (EObject) element;
			ProjectSpace ps = null;
			try {
				ps = ESWorkspaceProviderImpl.getProjectSpace(eObject);
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
	public ESLocalProject getProjectSpace(InternalProject internalProject) {
		return getProjectSpace(internalProject, false);
	}

	private ESLocalProject getProjectSpace(InternalProject internalProject, boolean createNewIfNeeded) {

		ESLocalProject projectSpace = (ESLocalProject) internalProject.getProviderSpecificData();

		if (projectSpace == null) {
			boolean found = false;
			List<ESLocalProject> localProjects = ESWorkspaceProvider.INSTANCE.getWorkspace().getLocalProjects();
			for (ESLocalProject localProject : localProjects) {
				String projectSpaceID = internalProject.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
				if (localProject.getLocalProjectId().getId().equals(projectSpaceID)) {
					found = true;
					projectSpace = localProject;
					break;
				}
			}

			if (!found && createNewIfNeeded) {
				projectSpace = ESWorkspaceProvider.INSTANCE.getWorkspace()
					.createLocalProject(internalProject.getName());
				internalProject.getProperties().addProperty(EMFStoreProvider.PROP_PROJECTSPACEID,
					projectSpace.getLocalProjectId().getId());

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
	public ESServer getServerInfo(InternalRepository internalRepository) {
		ESServer serverInfo = (ESServer) internalRepository.getProviderSpecificData();

		if (serverInfo == null) {

			ESWorkspace workspace = ESWorkspaceProvider.INSTANCE.getWorkspace();
			boolean foundExisting = false;

			for (ESServer info : workspace.getServers()) {
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
					internalRepository.getProperties().getValue(EMFStoreProvider.PROP_CERTIFICATE)).toAPI();
				workspace.addServer(serverInfo);
			} else if (!foundExisting && !internalRepository.getProperties().hasProperties()) {
				serverInfo = EMFStoreClientUtil.giveServerInfo("localhost", 8080).toAPI();
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
	public ECPProject getProject(ESLocalProject projectSpace) {
		for (InternalProject project : getOpenProjects()) {
			ESLocalProject localProjectSpace = (ESLocalProject) project.getProviderSpecificData();
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
