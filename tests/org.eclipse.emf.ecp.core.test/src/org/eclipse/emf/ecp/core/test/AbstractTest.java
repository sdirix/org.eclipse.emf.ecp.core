package org.eclipse.emf.ecp.core.test;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractTest {
	private static ECPProject project;
	private static ECPProjectManager projectManager;
	private static ECPProvider provider;
	private static ECPRepository repository;
	@BeforeClass
	public static void init() throws ProjectWithNameExistsException {
		provider = ECPProviderRegistry.INSTANCE.getProvider(EMFStoreProvider.NAME);
		repository = ECPRepositoryManager.INSTANCE.addRepository(provider, "repositoryName", "repositoryLabel",
			"description", getNewProperties());
		projectManager = ECPProjectManager.INSTANCE;
		
	}

	@Before
	public void cleanup() throws ProjectWithNameExistsException {
		for(ECPProject existingProject:projectManager.getProjects()){
			existingProject.delete();
		}
		project = getProjectManager().createProject(getProvider(), "projectName");
	}

	public ECPProject getProject() {
		return project;
	}

	public static ECPProjectManager getProjectManager() {
		return projectManager;
	}

	public static ECPProvider getProvider() {
		return provider;
	}

	public ECPRepository getRepository() {
		return repository;
	}

	public static ECPProperties getNewProperties() {
		ECPProperties properties = ECPUtil.createProperties();
		properties.addProperty(EMFStoreProvider.PROP_REPOSITORY_URL, "localhost");
		properties.addProperty(EMFStoreProvider.PROP_PORT, "8080");
		properties.addProperty(EMFStoreProvider.PROP_CERTIFICATE, "foo");
		return properties;
	}
}
