package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.junit.Test;

public class ECPProjectManagerTests extends AbstractTest {

	private String projectName = "name";

	@Test
	public void createOfflineProjectTest() {
		try {
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName);
			assertTrue(project != null);
			assertEquals(project.getProvider(), getProvider());
			assertEquals(project.getName(), projectName);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ECPProjectWithNameExistsException.class)
	public void createOfflineProjectWithExistingNameTest()
			throws ECPProjectWithNameExistsException {

		ECPProject project = getProjectManager().createProject(getProvider(),
				projectName);
		assertTrue(project != null);
		assertEquals(project.getProvider(), getProvider());
		assertEquals(project.getName(), projectName);

		getProjectManager().createProject(getProvider(), projectName);
	}

	@Test
	public void createOfflineProjectWithPropertiesTest() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getProvider(), getProvider());
			assertEquals(project.getProperties(), properties);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ECPProjectWithNameExistsException.class)
	public void createOfflineProjectWithPropertiesAndExistingNameOffline()
			throws ECPProjectWithNameExistsException {
		ECPProperties properties = getNewProperties();
		ECPProject project = getProjectManager().createProject(getProvider(),
				projectName, properties);
		assertTrue(project != null);
		assertEquals(project.getName(), projectName);
		assertEquals(project.getProvider(), getProvider());
		assertEquals(project.getProperties(), properties);

		getProjectManager().createProject(getProvider(), projectName);
	}

	@Test
	public void createSharedProject() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(
					getRepository(), projectName, properties);
			assertTrue(project != null);
			assertEquals(project.getName(), projectName);
			assertEquals(project.getRepository(), getRepository());
			assertEquals(project.getProperties(), properties);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void createSharedProjectWithoutRepository() {
		ECPProperties properties = getNewProperties();
		try {
			ECPProject project = getProjectManager().createProject(
					(ECPRepository) null, projectName, properties);
			fail("Null Repository not allowed.");
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test(expected = ECPProjectWithNameExistsException.class)
	public void createSharedWithExistingNameProject()
			throws ECPProjectWithNameExistsException {
		ECPProperties properties = getNewProperties();
		ECPProject project = getProjectManager().createProject(getRepository(),
				projectName, properties);
		assertTrue(project != null);
		assertEquals(project.getName(), projectName);
		assertEquals(project.getRepository(), getRepository());
		assertEquals(project.getProperties(), properties);

		getProjectManager().createProject(getRepository(), projectName,
				properties);
	}

	@Test
	public void cloneProjectTest() {
		ECPProject project=null;
		try {
			project = getProjectManager().createProject(
					getProvider(), projectName);
		} catch (ECPProjectWithNameExistsException e) {
			fail(e.getMessage());
		}
		ECPProject clonedProject = getProjectManager().createProject(project,project.getName()+"Copy");
		assertTrue(project.equals(clonedProject));
	}

	@Test
	public void getProjectWithAdaptableNonECPProjectAwareTest() {

	}

	@Test
	public void getProjectWithAdaptableECPProjectAwareTest() {

	}

	@Test
	public void getProjectByNameTest() {
		try {
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName);
			ECPProject project2 = getProjectManager().getProject(projectName);
			assertTrue(project == project2);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}
	@Test
	public void getProjectByItselfTest() {
		try {
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName);
			ECPProject project2 = getProjectManager().getProject(project);
			assertTrue(project == project2);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}
	@Test
	public void getProjectByModelElementTest() {
		try {
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName);
			EObject object=EcoreFactory.eINSTANCE.createEObject();
			project.getContents().add(object);
			
			ECPProject project2 = getProjectManager().getProject(object);
			assertTrue(project == project2);
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}
	@Test
	public void getProjectsTest() {
		try {
			assertEquals(0, getProjectManager().getProjects().size());
			ECPProject project = getProjectManager().createProject(
					getProvider(), projectName);
			assertEquals(1, getProjectManager().getProjects().size());
			ECPProject project2 = getProjectManager().createProject(
					getProvider(), projectName+"2");
			assertEquals(2, getProjectManager().getProjects().size());
		} catch (ECPProjectWithNameExistsException e) {
			fail();
		}
	}

	@Test
	public void getProjectByNameNonExistingTest() {
		ECPProject project = getProjectManager().getProject(projectName);
		assertTrue(null == project);
	}
}
