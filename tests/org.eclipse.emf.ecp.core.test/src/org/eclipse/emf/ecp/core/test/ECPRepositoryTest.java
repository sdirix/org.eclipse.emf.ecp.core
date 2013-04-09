package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.internal.core.ECPRepositoryImpl;
import org.junit.Test;

public class ECPRepositoryTest extends AbstractTest {

	private ECPRepositoryImpl repository = (ECPRepositoryImpl) getRepository();

	@Test
	public void testGetOpenProjects() {
		try {
			assertEquals(0, repository.getOpenProjects().length);
			ECPProject project = getProjectManager().createProject(repository, "Project", getNewProperties());
			assertEquals(1, repository.getOpenProjects().length);
			ECPProject project2 = getProjectManager().createProject(repository, "Project2", getNewProperties());
			assertEquals(2, repository.getOpenProjects().length);
			project2.close();
			assertEquals(1, repository.getOpenProjects().length);
			project.delete();
			project2.delete();
			assertEquals(0, repository.getOpenProjects().length);
		} catch (ProjectWithNameExistsException e) {
		}
	}
}
