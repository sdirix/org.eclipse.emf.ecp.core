/**
 * 
 */
package org.eclipse.emf.ecp.ide.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.internal.ide.util.EcoreHelper;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Alexandra Buzila
 * 
 */
@SuppressWarnings("restriction")
public class EcoreHelperOneDependencyTest {
	private Registry packageRegistry = EPackage.Registry.INSTANCE;
	private static String bEcorePath = "/TestEcoreHelperProjectResources/B.ecore";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("TestEcoreHelperProjectResources");
		// create resources to register and unregister
		if (!project.exists())
			installResourcesProject();
	}

	private static void installResourcesProject() throws Exception {
		ProjectInstallerWizard wiz = new ProjectInstallerWizard();
		wiz.installExample(new NullProgressMonitor());
	}

	@Test
	public void testRegister() throws IOException {
		assertFalse("Package A is already in the registry!",
				packageRegistry.containsKey("a.nsuri"));
		assertFalse("Package B is already in the registry!",
				packageRegistry.containsKey("b.nsuri"));
		EcoreHelper.registerEcore(bEcorePath);
		assertTrue("Package A not in the registry!",
				packageRegistry.containsKey("a.nsuri"));
		assertTrue("Package B not in the registry!",
				packageRegistry.containsKey("b.nsuri"));
	}
	
	@Test
	public void testUnregister() throws IOException{
		EcoreHelper.registerEcore(bEcorePath);
		assertTrue("Package A not in the registry!",
				packageRegistry.containsKey("a.nsuri"));
		assertTrue("Package B not in the registry!",
				packageRegistry.containsKey("b.nsuri"));


		EcoreHelper.unregisterEcore(bEcorePath);
		assertFalse("Package A is already in the registry!",
				packageRegistry.containsKey("a.nsuri"));
		assertFalse("Package B is already in the registry!",
				packageRegistry.containsKey("b.nsuri"));
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		EcoreHelper.unregisterEcore(bEcorePath);
	}

}
