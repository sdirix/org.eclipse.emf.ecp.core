package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.util.AdapterProvider;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.After;
import org.junit.Test;

/**
 * ECPProvider Tests
 * 
 * @author jfaltermeier
 * 
 */
public class ECPProviderTests extends AbstractTest {

	private ECPRepository repository;

	@After
	public void tearDown() {
		if (repository != null) {
			repository.delete();
		}
	}

	@Test
	public void getLabelTest() {
		// TODO any programmatic way to get label-string of EMFStore provider?
		assertEquals("EMFStore", getProvider().getLabel());
	}

	@Test
	public void getRepositoriesTest() {
		Set<ECPRepository> repositories = getProvider().getRepositories();
		assertTrue(repositories.contains(getRepository()));
	}

	@Test
	public void canAddRepositoriesTest() {
		boolean canAddRepositories = getProvider().canAddRepositories();

		int countReposBefore = getProvider().getRepositories().size();
		repository = ECPRepositoryManager.INSTANCE.addRepository(getProvider(), "repository4Name", "repository4Label",
			"description", getNewProperties());
		int countReposAfterAdd = getProvider().getRepositories().size();

		if (countReposAfterAdd - countReposBefore == 1) {
			assertTrue(canAddRepositories);
		} else if (countReposAfterAdd - countReposBefore == 0) {
			assertFalse(canAddRepositories);
		} else {
			fail("More than one repository was added or deleted.");
		}

	}

	@Test
	public void hasUnsharedProjectSupportTest() {
		boolean hasUnsharedProjectSupport = getProvider().canAddOfflineProjects();
		boolean isActuallyPossible = false;

		try {
			// try to create an offline project;
			ECPProject project = getProjectManager().createProject(getProvider(), "test");
			if (project == null && !Arrays.asList(getProjectManager().getProjects()).contains(project)) {
				isActuallyPossible = false;
			} else {
				isActuallyPossible = true;
			}
		} catch (ProjectWithNameExistsException e) {
			fail("Project with name already existing. Fix test setup.");
		} catch (Exception e) {
			isActuallyPossible = false;
		}

		assertEquals(hasUnsharedProjectSupport, isActuallyPossible);
	}
	
	@Test
	public void getUIProviderTest(){
		InternalProvider provider=(InternalProvider) getProvider();
		AdapterProvider uiProvider=provider.getUIProvider();
		assertNotNull(uiProvider);
	}
	@Test
	public void setUIProviderTest(){
		fail("Not yet implemented");
	}
	@Test
	public void isSlowTest(){
		InternalProvider provider=(InternalProvider) getProvider();
		boolean isSlow=provider.isSlow(null);
		fail("what should be passed?");
	}
	@Test
	public void createEditingDomainTest(){
		InternalProvider provider=(InternalProvider) getProvider();
		InternalProject project=null;
		try {
			project = (InternalProject) getProjectManager().createProject(
					getProvider(), "test");
		} catch (ProjectWithNameExistsException e) {
			fail(e.getMessage());
		}
		EditingDomain editingDomain=provider.createEditingDomain(project);
		assertNotNull(editingDomain);
	}
	@Test
	public void getOpenProjectsTest(){
		InternalProvider provider=(InternalProvider) getProvider();
		assertEquals(0,provider.getOpenProjects().size());
		InternalProject project=null;
		try {
			project = (InternalProject)getProjectManager().createProject(
					getProvider(), "test");
		} catch (ProjectWithNameExistsException e) {
			fail(e.getMessage());
		}
		assertEquals(1,provider.getOpenProjects().size());
		project.close();
		assertEquals(0,provider.getOpenProjects().size());
		project.open();
		assertEquals(1,provider.getOpenProjects().size());
	}
}