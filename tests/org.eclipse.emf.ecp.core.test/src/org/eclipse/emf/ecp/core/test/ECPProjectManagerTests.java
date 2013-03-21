package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.junit.Test;

public class ECPProjectManagerTests extends AbstractTest {

	private String projectName = "name";

	@Test
	public void createOfflineProjectTest() {
		try {
			ECPProject project = getProjectManager().createProject(getProvider(), projectName);
			assertTrue(project != null);
			assertEquals(project.getProvider(), getProvider());
			assertEquals(project.getName(), projectName);
		} catch (ProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ProjectWithNameExistsException.class)
	public void createOfflineProjectWithExistingNameTest() throws ProjectWithNameExistsException {

		ECPProject project = getProjectManager().createProject(getProvider(), projectName);
		assertTrue(project != null);
		assertEquals(project.getProvider(), getProvider());
		assertEquals(project.getName(), projectName);

		getProjectManager().createProject(getProvider(), projectName);
	}

	@Test
	public void createOfflineProjectWithPropertiesTest() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(getProvider(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getProperties(), getProvider());
			assertEquals(project.getProperties(), properties);
		} catch (ProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ProjectWithNameExistsException.class)
	public void createOfflineProjectWithPropertiesAndExistingNameOffline() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(getProvider(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getProperties(), getProvider());
			assertEquals(project.getProperties(), properties);

			getProjectManager().createProject(getProvider(), projectName);
		} catch (ProjectWithNameExistsException e) {
		}
	}

	@Test
	public void createSharedProject() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(getRepository(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getRepository(), getRepository());
			assertEquals(project.getProperties(), properties);
		} catch (ProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ProjectWithNameExistsException.class)
	public void createSharedWithExistingNameProject() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(getRepository(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getRepository(), getRepository());
			assertEquals(project.getProperties(), properties);

			getProjectManager().createProject(getRepository(), projectName, properties);
		} catch (ProjectWithNameExistsException e) {
		}
	}

	@Test
	public void cloneProjectTest() {
		ECPProject project = getProject();
		ECPProject clonedProject = getProjectManager().cloneProject(project);
		assertTrue(project.equals(clonedProject));
	}

	@Test
	public void getProjectWithAdaptableNonECPProjectAwareTest() {

	}

	@Test
	public void getProjectWithAdaptableECPProjectAwareTest() {

	}

	@Test
	public void getProjectWithNameTest() {
		try {
			ECPProject project = getProjectManager().createProject(getProvider(), projectName);
			ECPProject project2 = getProjectManager().getProject(projectName);
			assertTrue(project == project2);
		} catch (ProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test
	public void getProjectWithNameNonExistingTest() {
		try {
			ECPProject project = getProjectManager().createProject(getProvider(), projectName);
			ECPProject project2 = getProjectManager().getProject(projectName);
			assertTrue(project == project2);
		} catch (ProjectWithNameExistsException e) {
			fail();
		}
	}
}
