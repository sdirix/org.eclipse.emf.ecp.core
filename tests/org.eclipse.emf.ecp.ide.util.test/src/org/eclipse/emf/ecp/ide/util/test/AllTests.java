package org.eclipse.emf.ecp.ide.util.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	EcoreHelperNoDependencies_PTest.class,
	EcoreHelperOneDependency_PTest.class,
	EcoreHelperTwoDependencies_PTest.class,
	EcoreHelperCyclicDependencies_PTest.class,
	EcoreHelperDefaultPackageRegistryContents_PTest.class
})
public class AllTests {

}
