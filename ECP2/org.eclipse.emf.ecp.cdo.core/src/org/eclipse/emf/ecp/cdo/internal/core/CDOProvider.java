/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.cdo.internal.core;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.server.db.CDODBUtil;
import org.eclipse.emf.cdo.server.db.IDBStore;
import org.eclipse.emf.cdo.server.db.mapping.IMappingStrategy;
import org.eclipse.emf.cdo.session.CDOSessionConfigurationFactory;
import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceBase;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceConfiguration;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceUtil;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.eclipse.net4j.db.IDBConnectionProvider;
import org.eclipse.net4j.db.h2.H2Adapter;
import org.eclipse.net4j.util.UUIDGenerator;
import org.eclipse.net4j.util.io.IOUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class CDOProvider extends DefaultProvider {
	public static final String NAME = "org.eclipse.emf.ecp.cdo.provider";

	public static final String PROP_CONNECTOR_TYPE = "connectorType";

	public static final String PROP_CONNECTOR_DESCRIPTION = "connectorDescription";

	public static final String PROP_REPOSITORY_NAME = "repositoryName";

	public static final String PROP_BRANCH_PATH = "branchPath";

	public static final String PROP_TIME_STAMP = "timeStamp";

	public static final String PROP_WORKSPACE_ID = "workspaceID";

	public static CDOProvider INSTANCE;

	public CDOProvider() {
		super(NAME);
		INSTANCE = this;
		CDOUtil.setLegacyModeDefault(true);
	}

	@Override
	protected void doDispose() {
		INSTANCE = null;
		super.doDispose();
	}

	@Override
	public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		T adapter = ECPProjectAdapterFactory.adapt(adaptable, adapterType);
		if (adapter != null) {
			return adapter;
		}

		return super.getAdapter(adaptable, adapterType);
	}

	@Override
	public boolean isSlow(Object parent) {
		if (parent instanceof CDOBranchWrapper) {
			return true;
		}

		if (parent instanceof EObject) {
			return true;
		}

		return super.isSlow(parent);
	}

	@Override
	public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList) {
		if (parent instanceof InternalProject) {
			InternalProject project = (InternalProject) parent;
			CDOProjectData projectData = getProjectData(project);
			childrenList.addChildren(projectData.getRootResource().getContents());
		} else if (parent instanceof InternalRepository) {
			InternalRepository repository = (InternalRepository) parent;
			CDOBranchWrapper wrapper = new CDOBranchWrapper(repository, CDOBranch.MAIN_BRANCH_NAME);
			childrenList.addChild(wrapper);
		} else if (parent instanceof CDOBranchWrapper) {
			CDOBranchWrapper parentWrapper = (CDOBranchWrapper) parent;
			InternalRepository repository = parentWrapper.getRepository();
			String branchPath = parentWrapper.getBranchPath();

			CDONet4jSession session = null;

			try {
				CDORepositoryData repositoryData = getRepositoryData(repository);
				CDONet4jSessionConfiguration sessionConfiguration = repositoryData.createSessionConfiguration();
				session = sessionConfiguration.openNet4jSession();

				CDOBranch branch = session.getBranchManager().getBranch(branchPath);
				for (CDOBranch child : branch.getBranches()) {
					CDOBranchWrapper wrapper = new CDOBranchWrapper(repository, child.getPathName());
					childrenList.addChild(wrapper);
				}
			} finally {
				IOUtil.close(session);
			}
		} else {
			super.fillChildren(context, parent, childrenList);
		}
	}

	public EList<EObject> getElements(InternalProject project) {
		// TODO: implement CDOProvider.addRootElement(project, rootElement)
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleLifecycle(ECPModelContext context, LifecycleEvent event) {
		super.handleLifecycle(context, event);

		if (context instanceof InternalProject) {
			InternalProject project = (InternalProject) context;
			switch (event) {
			case CREATE:
				createProject(project);
				break;

			case INIT:
				break;

			case DISPOSE:
				disposeProject(project);
				break;

			case REMOVE:
				removeProject(project);
				break;

			default:
				break;
			}
		}
	}

	protected void createProject(InternalProject project) {
		String workspaceID = UUIDGenerator.DEFAULT.generate();
		project.getProperties().addProperty(PROP_WORKSPACE_ID, workspaceID);

		CDOProjectData projectData = getProjectData(project);
		projectData.checkoutWorkspace();

		File folder = getProjectFolder(project);
		PrintStream stream = null;

		try {
			stream = new PrintStream(new File(folder, "ecp.properties"));
			stream.println("project.name = " + project.getName());
		} catch (Exception ex) {
			Activator.log(ex);
		} finally {
			IOUtil.close(stream);
		}
	}

	protected CDOWorkspaceConfiguration createWorkspaceConfiguration(InternalProject project) {
		File folder = getProjectFolder(project);
		folder.mkdirs();

		IDBStore localStore = createLocalStore(project, folder);
		CDOWorkspaceBase base = createWorkspaceBase(project, folder);
		CDOSessionConfigurationFactory remote = getRepositoryData(project.getRepository());

		ECPProperties properties = project.getProperties();
		String branchPath = properties.getValue(PROP_BRANCH_PATH);
		String timeStamp = properties.getValue(PROP_TIME_STAMP);

		CDOWorkspaceConfiguration config = CDOWorkspaceUtil.createWorkspaceConfiguration();
		config.setLocalRepositoryName(folder.getName()); // workspaceID
		config.setStore(localStore);
		config.setBase(base);
		config.setRemote(remote);
		config.setBranchPath(branchPath == null ? CDOBranch.MAIN_BRANCH_NAME : branchPath);
		config.setTimeStamp(timeStamp == null ? CDOBranchPoint.UNSPECIFIED_DATE : Long.parseLong(timeStamp));

		return config;
	}

	protected CDOWorkspaceBase createWorkspaceBase(InternalProject project, File folder) {
		File base = new File(folder, "base");
		base.mkdirs();

		return CDOWorkspaceUtil.createFolderWorkspaceBase(base);
	}

	protected IDBStore createLocalStore(InternalProject project, File folder) {
		File local = new File(folder, "local");
		local.mkdirs();

		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:" + new File(local, "local").getAbsolutePath());

		IMappingStrategy mappingStrategy = CDODBUtil.createHorizontalMappingStrategy(false, false);
		IDBAdapter dbAdapter = new H2Adapter();
		IDBConnectionProvider dbConnectionProvider = DBUtil.createConnectionProvider(dataSource);

		return CDODBUtil.createStore(mappingStrategy, dbAdapter, dbConnectionProvider);
	}

	protected void disposeProject(InternalProject project) {
		CDOProjectData data = (CDOProjectData) project.getProviderSpecificData();
		data.dispose();
	}

	protected void removeProject(InternalProject project) {
		File folder = getProjectFolder(project);
		if (folder.exists()) {
			if (!folder.delete()) {
				folder.deleteOnExit();
			}
		}
	}

	public static CDORepositoryData getRepositoryData(InternalRepository repository) {
		synchronized (repository) {
			CDORepositoryData data = (CDORepositoryData) repository.getProviderSpecificData();
			if (data == null) {
				data = new CDORepositoryData(repository);
				repository.setProviderSpecificData(data);
			}

			return data;
		}
	}

	public static CDOProjectData getProjectData(InternalProject project) {
		synchronized (project) {
			CDOProjectData data = (CDOProjectData) project.getProviderSpecificData();
			if (data == null) {
				data = new CDOProjectData(project);
				project.setProviderSpecificData(data);
			}

			return data;
		}
	}

	public static File getProjectFolder(InternalProject project) {
		String workspaceID = project.getProperties().getValue(PROP_WORKSPACE_ID);
		return new File(Activator.getInstance().getStateLocation().toFile(), workspaceID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#delete(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * java.util.Collection)
	 */
	public void delete(InternalProject project, Collection<EObject> eObjects) {
		// CDOResource cdoResource = getProjectData(project).getRootResource();

		// cdoResource.eContents().removeAll(eObjects);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#cloneProject(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * org.eclipse.emf.ecp.spi.core.InternalProject)
	 */
	public void cloneProject(final InternalProject projectToClone, InternalProject targetProject) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#contains(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * org.eclipse.emf.ecore.EObject)
	 */
	public boolean contains(InternalProject project, EObject eObject) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#getRoot(org.eclipse.emf.ecp.spi.core.InternalProject)
	 */
	public Object getRoot(InternalProject project) {
		throw new UnsupportedOperationException();
	}
}
