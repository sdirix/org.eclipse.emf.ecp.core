package org.eclipse.emf.ecp.ui.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;


public class CanAddRepositoriesTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders())
	    {
	      if (provider.canAddRepositories())
	      {
	        return expectedValue.equals(true);
	      }
	    }
		return expectedValue.equals(false);
	}

}
