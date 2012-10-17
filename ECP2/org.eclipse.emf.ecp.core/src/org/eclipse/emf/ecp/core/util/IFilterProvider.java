package org.eclipse.emf.ecp.core.util;

import java.util.Collection;

/**
 * Interface to provide a collection of nsUris that should not be available
 * 
 * @author Eugen Neufeld
 * 
 */
public interface IFilterProvider {

	Collection<String> getFilteredPackages();
}
