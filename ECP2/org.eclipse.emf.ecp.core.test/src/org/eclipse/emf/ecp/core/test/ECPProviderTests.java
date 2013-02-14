package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
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
		List<ECPRepository> repositories = Arrays.asList(getProvider().getRepositories());
		assertTrue(repositories.contains(getRepository()));
	}

	@Test
	public void canAddRepositoriesTest() {
		boolean canAddRepositories = getProvider().canAddRepositories();

		int countReposBefore = getProvider().getRepositories().length;
		repository = ECPRepositoryManager.INSTANCE.addRepository(getProvider(), "repository4Name", "repository4Label",
			"description", getNewProperties());
		int countReposAfterAdd = getProvider().getRepositories().length;

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
		boolean hasUnsharedProjectSupport = getProvider().hasUnsharedProjectSupport();
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
}