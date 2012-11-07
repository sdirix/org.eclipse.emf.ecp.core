/**
 * 
 */
package org.eclipse.emf.ecp.core.util.observer;

import org.eclipse.emf.ecp.core.ECPProvider;

/**
 * @author Edgar
 * 
 */
public interface IECPProvidersChangedObserver extends IECPObserver {

	void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception;

}
