package org.eclipse.emf.ecp.ide.util.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	EcoreHelperNoDependenciesTest.class,
	EcoreHelperOneDependencyTest.class,
	EcoreHelperTwoDependenciesTest.class,
	EcoreHelperCyclicDependenciesTest.class,
	EcoreHelperDefaultPackageRegistryContentsTest.class
})
public class AllTests {

}
